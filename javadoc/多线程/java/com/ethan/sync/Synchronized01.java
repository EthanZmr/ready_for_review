package com.ethan.sync;

public class Synchronized01 implements Runnable{
    private int count = 100;

    public static void main(String[] args) {
        Synchronized01 t = new Synchronized01();
        for (int i = 0; i < 100; i++) {
            new Thread(t, "THREAD " + i).start();
        }
    }

    @Override
    public /*synchronized*/ void run() {
        count--;
        System.out.println(Thread.currentThread().getName() + " count=" + count);
    }
}
