package com.ethan.sync;

import java.util.concurrent.Exchanger;

public class Exchanger01 {
    static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(() -> {
            String t1 = "T1";
            try {
                exchanger.exchange(t1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":" + t1);
        }, "t1").start();

        new Thread(() -> {
            String t1 = "T2";
            try {
                exchanger.exchange(t1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":" + t1);
        }, "t2").start();
    }
}
