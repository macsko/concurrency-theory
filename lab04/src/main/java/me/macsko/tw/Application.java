package me.macsko.tw;
// Szkielet programu z: https://github.com/macwozni/1DMeshParallel

import me.macsko.tw.parallelism.ConcurentBlockRunner;

class Application {

    public static void main(String args[]) {
        int N = 5;
        if(args.length > 0) {
            N = Integer.parseInt(args[0]);
        }
        Executor e = new Executor(new ConcurentBlockRunner());
        e.N = N;
        e.start();
    }
}
