package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
	"sync"
)

var start chan bool
var wg sync.WaitGroup

type Matrix [][]float64

// Obtaining input and output filenames from program arguments
func getFilenames() (string, string) {
	inputFilename := "input.txt"
	outputFilename := "output.txt"
	if len(os.Args) > 1 {
		inputFilename = os.Args[1]
		if len(os.Args) > 2 {
			outputFilename = os.Args[2]
		}
	}
	return inputFilename, outputFilename
}

// Creating NxM slice of float64 values
func make2DSlice(N int, M int) [][]float64 {
	result := make([][]float64, N)
	for i := range result {
		result[i] = make([]float64, M)
	}
	return result
}

// Parsing matrix string to Matrix type
func parseMatrix(matrixString string) (Matrix, int) {
	lines := strings.Split(strings.ReplaceAll(matrixString, "\r\n", "\n"), "\n")

	N, _ := strconv.Atoi(lines[0])

	matrix := make(Matrix, N)
	for i := range matrix {
		matrix[i] = make([]float64, N + 1)
	}

	for i, line := range lines[1 : N + 1] {
		values := strings.Split(line, " ")
		for j := 0; j < N; j++ {
			matrix[i][j], _ = strconv.ParseFloat(values[j], 64)
		}
	}
	values := strings.Split(lines[N + 1], " ")
	for i := 0; i < N; i++ {
		matrix[i][N], _ = strconv.ParseFloat(values[i], 64)
	}

	return matrix, N
}

// Parsing Matrix type to string using Stringer interface
func (matrix Matrix) String() string {
	N := len(matrix)
	result := fmt.Sprintln(N)
	for _, l := range matrix {
		for _, number := range l[:N] {
			result += fmt.Sprint(number, " ")
		}
		result += "\n"
	}
	for _, l := range matrix {
		result += fmt.Sprint(l[N], " ")
	}
	result += "\n"
	return result
}

// A_i_k operation - computing multiplier of i-th row to subtract it from k-th row
func (matrix Matrix) operationA(i int, k int, m []float64) {
	defer wg.Done()
	<-start // Waiting to start all goroutines synchronously
	m[k] = matrix[k][i] / matrix[i][i]
}

// B_i_j_k operation - multiplying j-th element of i-th row by A_i_k multiplier to subtract it from k-th row
func (matrix Matrix) operationB(i int, j int, k int, m []float64, d [][]float64) {
	defer wg.Done()
	<-start
	d[k][j] = matrix[i][j] * m[k]
}

// C_i_j_k operation - subtracting j-th element of i-th row from j-th element of k-th row
func (matrix Matrix) operationC(j int, k int, d [][]float64) {
	defer wg.Done()
	<-start
	matrix[k][j] = matrix[k][j] - d[k][j]
}

// Computing concurrent gaussian elimination on matrix
func (matrix Matrix) gaussianElimination() {
	N := len(matrix)
	m := make([]float64, N)
	d := make2DSlice(N, N+1)

	for i := 0; i < N - 1; i++ {
		start = make(chan bool)
		wg.Add(N - i - 1)
		for k := i + 1; k < N; k++ {
			go matrix.operationA(i, k, m)
		}
		close(start) // Running all goroutines
		wg.Wait()    // Waiting for all goroutines to finish

		start = make(chan bool)
		wg.Add((N - i - 1) * (N - i + 1))
		for k := i + 1; k < N; k++ {
			for j := i; j < N + 1; j++ {
				go matrix.operationB(i, j, k, m, d)
			}
		}
		close(start)
		wg.Wait()

		start = make(chan bool)
		wg.Add((N - i - 1) * (N - i + 1))
		for k := i + 1; k < N; k++ {
			for j := i; j < N + 1; j++ {
				go matrix.operationC(j, k, d)
			}
		}
		close(start)
		wg.Wait()
	}
}

// Computing result of gaussian eliminated matrix using backward substitution
func (matrix Matrix) backwardSubstitution() {
	N := len(matrix)
	for i := N - 1; i >= 0; i-- {
		// Zeroing left side of diagonal
		for j := 0; j < i; j++ {
			matrix[i][j] = 0
		}
		// Computing i_th result and zeroing right side of diagonal
		for j := i + 1; j < N; j++ {
			matrix[i][N] -= matrix[i][j] * matrix[j][N]
			matrix[i][j] = 0
		}
		matrix[i][N] /= matrix[i][i]
		matrix[i][i] = 1
	}
}

func main() {
	inputFilename, outputFilename := getFilenames()
	input, _ := os.ReadFile(inputFilename)
	M, _ := parseMatrix(string(input))
	fmt.Println("Input matrix:")
	fmt.Println(M)

	M.gaussianElimination()
	M.backwardSubstitution()

	fmt.Println("\nOutput matrix:")
	fmt.Println(M)
	err := os.WriteFile(outputFilename, []byte(M.String()), 0666)
	if err != nil {
		fmt.Println("Cannot write output matrix to file " + outputFilename)
	}
}
