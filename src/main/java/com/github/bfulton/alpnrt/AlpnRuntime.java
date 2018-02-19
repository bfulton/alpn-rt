package com.github.bfulton.alpnrt;

public class AlpnRuntime {
    public static void run(String runnableClassName) {
        AlpnClassloader cl = new AlpnClassloader(Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(cl);
        Runnable runnable;
        try {
            runnable = (Runnable) cl.loadClass(runnableClassName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("unable to instantiate Runnable class: " + runnableClassName, e);
        }
        runnable.run();
    }
}
