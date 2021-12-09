package me.macsko.tw;

public class Philosopher {
    private final int id;
    private final BinarySemaphore fork1, fork2;
    private static final int eatingTime = 1;
    private static final int thinkingTime = 1;
    private final BinarySemaphore table;

    Philosopher(int id, BinarySemaphore fork1, BinarySemaphore fork2, BinarySemaphore table) {
        this.id = id;
        this.fork1 = fork1;
        this.fork2 = fork2;
        this.table = table;
    }

    void eat() {
        fork1.P();
        fork2.P();
        table.V();
        System.out.println("Filozof " + id + " zaczyna jeść");
        try {
            Thread.sleep(eatingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Filozof " + id + " kończy jeść");
        fork1.V();
        fork2.V();
    }

    void think() {
        System.out.println("Filozof " + id + " zaczyna myśleć");
        try {
            Thread.sleep(thinkingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Filozof " + id + " kończy myśleć");
    }

    // Wersja z możliwym zagłodzeniem
    public double startStarvation(int iters) {
        long waitingTimeTotal = 0;
        long waitingStart = System.nanoTime();
        for(int i = 0; i < iters; i++) {
            table.P();
            while(!fork1.getState() || !fork2.getState()) {
                table.V();
                table.P();
            }
            waitingTimeTotal += System.nanoTime() - waitingStart;
            eat();
            think();
            waitingStart = System.nanoTime();
        }
        return waitingTimeTotal / (1000000.0*iters);
    }

    // Wersja z kelnerem
    public double startWaiter(int iters, Waiter waiter) {
        long waitingTimeTotal = 0;
        long waitingStart = System.nanoTime();
        for(int i = 0; i < iters; i++) {
            waiter.get(id);
            waitingTimeTotal += System.nanoTime() - waitingStart;
            eat();
            waiter.release();
            think();
            waitingStart = System.nanoTime();
        }
        return waitingTimeTotal / (1000000.0*iters);
    }
}
