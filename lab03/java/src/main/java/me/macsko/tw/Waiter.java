package me.macsko.tw;

// Kelner zarządzajacy używanymi sztućcami
public class Waiter {
    private final int N;
    private final BinarySemaphore[] forks;
    private final Semaphore freeSeats;
    private final BinarySemaphore table;

    Waiter(int N, BinarySemaphore[] forks, BinarySemaphore table) {
        this.N = N;
        this.forks = forks;
        this.freeSeats = new Semaphore(2);
        this.table = table;
    }

    public void get(int id) {
        freeSeats.P();
        table.P();
        while(!forks[id - 1].getState() || !forks[id%N].getState()) {
            freeSeats.V();
            table.V();
            freeSeats.P();
            table.P();
        }
    }

    public void release() {
        freeSeats.V();
    }
}
