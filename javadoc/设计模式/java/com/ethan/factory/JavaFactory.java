package com.ethan.factory;

public class JavaFactory implements SimpleFactory{
    @Override
    public void record() {
        System.out.println("Course...");
    }
}

class Test {
    public static void main(String[] args) {
        SimpleFactory factory = FactoryBuilder.getInstance(JavaFactory.class);
        factory.record();
    }
}
