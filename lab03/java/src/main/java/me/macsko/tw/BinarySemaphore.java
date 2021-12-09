package me.macsko.tw;

// Semafor binarny
public class BinarySemaphore {
    private boolean state;

    public BinarySemaphore(boolean initialState) {
        this.state = initialState;
    }

    public synchronized void P() { // decrement
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

    public synchronized boolean getState() {
        return state;
    }
}