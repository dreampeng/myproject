package com.noadd.myapp.Thread;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 **/
public class MyQueen {


    private final AtomicInteger conn = new AtomicInteger(0);
    private final LinkedList<Object> list = new LinkedList<>();
    private int minSize;
    private int maxSize;
    private final Object lock = new Object();

    public MyQueen(int minSize, int maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public int put(Object obj) {
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

    public String getAllStr() {
        return list.toString();
    }
}
