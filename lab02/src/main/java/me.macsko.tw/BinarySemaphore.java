package me.macsko.tw;

// Semafor binarny
public class BinarySemaphore implements ISemaphore {
    private boolean state;

    public BinarySemaphore(boolean initialState) {
        this.state = initialState;
    }

    public synchronized void P() { // decrement
        // Jeżeli kilka wątków będzie czekało by zdekrementować semafor,
        // to wtedy mógłby być wybudzony jeden z nich, w chwili gdy wcześniejszy ustawił semafor na false.
        while(!this.state) {
            try {
                wait();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        state = false;
        notify();
    }

    public synchronized void V() { // increment
        while(this.state) {
            try {
                wait();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        state = true;
        notify();
    }
}
