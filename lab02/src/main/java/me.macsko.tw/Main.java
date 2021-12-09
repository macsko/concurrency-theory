package me.macsko.tw;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0 || args[0].equals("race")) {
            // Problem wyścigu z zastosowaniem semaforów binarnych
            new Race(1000000, false).run();
        }else if(args[0].equals("race-wrong")) {
            // Problem wyścigu z zastosowaniem semaforów binarnych niepoprawnych (z ifem zamiast while'a)
            new Race(1000000, true).run();
        }else if(args[0].equals("restaurant")) {
            // Test semaforów licznikowych - restauracja, gdzie chce wejść 25 gości, a wolne jest tylko 5 stolików
            new Restaurant(5, 25).run();
        }
    }
}
