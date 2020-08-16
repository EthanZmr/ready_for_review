package com.ethan.sync;

import java.lang.invoke.VarHandle;
import java.util.concurrent.Semaphore;

public class Semaphore01 {
    public static void main(String[] args) {
        // 允许一个线程同时执行
        Semaphore s = new Semaphore(1);

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("T1 running...");
                Thread.sleep(200);
                System.out.println("T1 running...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                s.release();
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("T2 running...");
                Thread.sleep(200);
                System.out.println("T2 running...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                s.release();
            }
        }).start();
    }
}