package me.macsko.tw;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int N = 5;
        int iters = 10;
        if(args.length > 0) {
            N = Integer.parseInt(args[0]);
            if(args.length > 1) {
                iters = Integer.parseInt(args[1]);
            }
        }

        Thread[] threads = new Thread[N];
        BinarySemaphore[] forks = new BinarySemaphore[N];
        BinarySemaphore table = new BinarySemaphore(true);
        for(int i = 0; i < N; i++) {
            forks[i] = new BinarySemaphore(true);
        }
        Waiter waiter = new Waiter(N, forks, table);
        double[] waitingTimes = new double[N];

        for(int i = 0; i < N; i++) {
            int id = i;
            int NThread = N;
            int itersThread = iters;
            threads[i] = new Thread(() -> {
                Philosopher p = new Philosopher(id + 1, forks[id], forks[(id + 1)%NThread], table);
                if(args.length > 2 && args[2].equals("waiter")) {
                    waitingTimes[id] = p.startWaiter(itersThread, waiter);
                }else {
                    waitingTimes[id] = p.startStarvation(itersThread);
                }
            });
            threads[i].start();
        }

        for(int i = 0; i < N; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Filozofowie czekali średnio:");
        for(var i = 0; i < N; i++) {
            System.out.println("Filozof " + (i + 1) + ": " + waitingTimes[i] + "ms");
        }
        System.out.println(Arrays.toString(waitingTimes));
    }
}
