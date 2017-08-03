package org.grizz.model;

public class Counter {
    private int counter;

    private Counter(int counter) {
        this.counter = counter;
    }

    public static Counter of(int counter) {
        return new Counter(counter);
    }
}
