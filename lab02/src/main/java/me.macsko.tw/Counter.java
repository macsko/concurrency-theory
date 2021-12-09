package me.macsko.tw;

// Prosty licznik inkrementowalny i dekrementowalny
public class Counter {
    private long value = 0;

    public void increment() {
        this.value++;
    }

    public void decrement() {
        this.value--;
    }

    public long getValue() {
        return this.value;
    }
}
