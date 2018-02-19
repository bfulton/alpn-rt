package com.github.bfulton.alpnrt.helper;

public class SomeClass implements Runnable {
    @Override
    public void run() {
        GlobalCounter.val.incrementAndGet();
    }
}
