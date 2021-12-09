package me.macsko.tw;

// Problem wyścigu rozwiązany semaforami
public class Race {
    private final long iterations;
    private final boolean testWrong;

    public Race(long iterations, boolean testWrong) {
        this.iterations = iterations;
        this.testWrong = testWrong;
    }

    public void run() {
        Counter counter = new Counter();
        ISemaphore criticalEnter;
        if(testWrong) {
            criticalEnter = new BinarySemaphoreWrong(true);
        }else {
            criticalEnter = new BinarySemaphore(true);
        }
        System.out.println("Running race...");

        Thread thread1 = new Thread(() -> {
            for(long i = 0; i < iterations; i++) {
                criticalEnter.P();
                counter.increment();
                criticalEnter.V();
            }
        });
        Thread thread2 = new Thread(() -> {
            for(long i = 0; i < iterations; i++) {
                criticalEnter.P();
                counter.decrement();
                criticalEnter.V();
            }
        });

        long timeBegin = System.currentTimeMillis();
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long finalTime = System.currentTimeMillis() - timeBegin;

        System.out.println("Race: iterations = " + iterations + ", value = " + counter.getValue() + ", time = " + finalTime);
    }
}
