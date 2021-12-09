package me.macsko.tw.parallelism;

import me.macsko.tw.production.IProduction;

public class ConcurentBlockRunner extends AbstractBlockRunner {

    private final MyLock lock = new MyLock();

    @Override
    void runOne(IProduction _pOne) {
        _pOne.injectRefs(lock);
        _pOne.start();
    }

    @Override
    void wakeAll() {
        lock.unlock();
    }

}
