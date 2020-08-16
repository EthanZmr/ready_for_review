package com.ethan.sync;

import java.util.concurrent.*;

public class Callable01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> c = () -> "Hello Callable";
        ExecutorService service = Executors.newCachedThreadPool();
        Future<String> future = service.submit(c);
        System.out.println(future.get());
        service.shutdown();
    }
}
