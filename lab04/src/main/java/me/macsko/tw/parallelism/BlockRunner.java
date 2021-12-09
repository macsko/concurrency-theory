package me.macsko.tw.parallelism;

import me.macsko.tw.production.IProduction;

public interface BlockRunner {

    //starts all threads
    public void startAll();

    //adds a thread to poll
    public void addThread(IProduction pThread);
}
