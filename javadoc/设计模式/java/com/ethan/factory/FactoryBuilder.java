package com.ethan.factory;

public class FactoryBuilder {
    public static SimpleFactory getInstance(Class className) {
        try {
            return (SimpleFactory) className.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
