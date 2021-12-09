package me.macsko.tw;

// Symulacja restauracji z dużą liczbą gości, a małą liczbą stolików - test semaforów licznikowych
class Restaurant {
    private final Semaphore freeTables;
    private final int guests;

    public Restaurant(int tables, int guests) {
        this.freeTables = new Semaphore(tables);
        this.guests = guests;
    }

    private void enter() {
        freeTables.P();
    }

    private void leave() {
        freeTables.V();
    }

    public void run() {
        System.out.println("Running restaurant...");

        Thread[] threads = new Thread[guests];
        for(int i = 0; i < guests; i++) {
            int guestNr = i;
            threads[i] = new Thread(() -> {
                enter();
                System.out.println("Guest " + guestNr + " entered restaurant, free tables = " + freeTables.getState());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                leave();
                System.out.println("Guest " + guestNr + " leaved restaurant, free tables = " + freeTables.getState());
            });
            threads[i].start();
        }
        for(int i = 0; i < guests; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Restaurant: free tables = " + freeTables.getState());
    }
}
