package com.ethan.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicInteger01 {
    AtomicInteger count = new AtomicInteger(0);
    void m() {
        for (int i = 0; i < 1000; i++) {
            count.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        AtomicInteger01 t = new AtomicInteger01();
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threadList.add(new Thread(t::m, "Thread-" + i));
        }

        threadList.forEach(Thread::start);

        threadList.forEach((o) -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(t.count);
    }
}
