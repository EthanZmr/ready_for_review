package com.ethan.sync;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Phaser01 {
    static Random r = new Random();
    static MarriagePhaser phaser = new MarriagePhaser();

    static void milliSleep(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        phaser.bulkRegister(7);

        for (int i = 0; i < 5; i++) {
            new Thread(new Person("p" + i)).start();
        }

        new Thread(new Person("新郎")).start();
        new Thread(new Person("新娘")).start();
    }

    private static class Person implements Runnable {
        String name;
        public Person(String s) {
            name = s;
        }

        public void arrive() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 到达现场!\n", name);
            phaser.arriveAndAwaitAdvance();
        }

        public void eat() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 吃完！\n", name);
            phaser.arriveAndAwaitAdvance();
        }

        public void leave() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 离开!\n", name);
            phaser.arriveAndAwaitAdvance();
        }

        private void hug() {
            if (name.equals("新娘") || name.equals("新郎")) {
                milliSleep(r.nextInt(1000));
                System.out.printf("%s 洞房！\n", name);
                phaser.arriveAndAwaitAdvance();
            } else {
                phaser.arriveAndDeregister();
            }
        }

        @Override
        public void run() {
            arrive();
            eat();
            leave();
            hug();
        }
    }
}

class MarriagePhaser extends Phaser {
    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case 0:
                System.out.println("所有人都到齐了!" + registeredParties);
                System.out.println();
                return false;
            case 1:
                System.out.println("所有人都吃完了!" + registeredParties);
                System.out.println();
                return false;
            case 2:
                System.out.println("所有人都离开了!" + registeredParties);
                System.out.println();
                return false;
            case 3:
                System.out.println("婚礼结束！新娘新郎抱抱!" + registeredParties);
                return true;
            default:
                return true;
        }
    }
}
