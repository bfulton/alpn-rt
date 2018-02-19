package com.github.bfulton.alpnrt;

import java.io.File;

public class AlpnRuntime {
    public static void run(String runnableClassName, File tempDir) {
        AlpnClassloader cl = new AlpnClassloader(Thread.currentThread().getContextClassLoader(), tempDir);
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
