package com.ethan.sync;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UsingVolatile {
    volatile ArrayList<Integer> c = new ArrayList<>();

    public synchronized void add(Integer num) {
        c.add(num);
    }

    public Integer get(int index) {
        return c.get(index);
    }

    public synchronized int size() {
        return c.size();
    }

    public static void main(String[] args) {
        UsingVolatile c = new UsingVolatile();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                c.add(i);
                System.out.println("add " + i);
//                try {
//                    TimeUnit.MILLISECONDS.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }, "t1").start();

        new Thread(() -> {
            while (true) {
                if (c.size() == 5) {
                    System.out.println("break");
                    break;
                }
            }
        }, "t2").start();
    }

}
