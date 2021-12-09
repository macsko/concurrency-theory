package me.macsko.tw;

// Semafor ogólny (licznikowy)
public class Semaphore {
    private int state;
    private final BinarySemaphore canBeDecremented;
    private final BinarySemaphore criticalEnter;

    public Semaphore(int initialState) {
        this.state = Math.max(initialState, 0);
        canBeDecremented = new BinarySemaphore(initialState > 0);
        criticalEnter = new BinarySemaphore(true);
    }

    public void P() { // decrement
        canBeDecremented.P();
        criticalEnter.P();
        this.state--;
        if(this.state > 0) {
            canBeDecremented.V();
        }
        criticalEnter.V();
    }

    public void V() { // increment
        criticalEnter.P();
        this.state++;
        if(this.state == 1) {
            canBeDecremented.V();
        }
        criticalEnter.V();
    }
}
