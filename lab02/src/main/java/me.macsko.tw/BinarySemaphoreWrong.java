package me.macsko.tw;

// Semafor binarny niepoprawny (z ifami zamiast while'i) do przetestowania zadania 2
public class BinarySemaphoreWrong implements ISemaphore {
    private boolean state;

    public BinarySemaphoreWrong(boolean initialState) {
        this.state = initialState;
    }

    public synchronized void P() { // decrement
        if(!this.state) {
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
        if(this.state) {
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
