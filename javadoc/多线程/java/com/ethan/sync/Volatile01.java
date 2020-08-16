package com.ethan.sync;

import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;

public class Volatile01 {
    /*volatile*/ boolean running = true;

    void m() {
        System.out.println("m start");
        while (running) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        Volatile01 t = new Volatile01();
        new Thread(t::m, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.running = false;

        Unsafe unsafe = Unsafe.getUnsafe();
        System.out.println(unsafe.allocateMemory(100000L));
    }
}
