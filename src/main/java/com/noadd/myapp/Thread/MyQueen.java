package com.noadd.myapp.Thread;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 **/
public class MyQueen {


    private final LinkedList<Object> list = new LinkedList<>();
    private final AtomicInteger conn = new AtomicInteger(0);
    private static final int minSize = 0;
    private static final int maxSize = 99;
    private final Object lock = new Object();

    public int put(String obj) {
        synchronized (lock) {
            if (conn.get() == maxSize) {
                return 0;
            }
            list.add(obj);
            conn.incrementAndGet();
            lock.notify();
            return 1;
        }
    }

    public Object get() {
        synchronized (lock) {
            if (conn.get() == minSize) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Object retObj = list.getFirst();
            list.removeFirst();
            conn.decrementAndGet();
            lock.notify();
            return retObj;
        }
    }

}
