package com.github.bfulton.alpnrt;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

public class AlpnClassloader extends ClassLoader {
    private static final String ALPN_BOOT_JAR_RESOURCE_PATH = "com/github/bfulton/alpnrt/alpn-boot-8.1.12.v20180117.jar";

    private final Set<String> alpnClassNames;
    private final URLClassLoader alpnBootClassloader;

    public AlpnClassloader(ClassLoader parent) {
        this(parent, null);
    }

    public AlpnClassloader(ClassLoader parent, File tempDir) {
        super(parent);
        alpnClassNames = new HashSet<String>();
        alpnBootClassloader = initAlpnClassLoader(tempDir);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if ("org.eclipse.jetty.alpn.ALPN".equals(name)) {
            return alpnBootClassloader.loadClass(name);
        }
        return null;
    }

    private URLClassLoader initAlpnClassLoader(File tempDir) {
        URL alpnBootJarUrl = getParent().getResource(ALPN_BOOT_JAR_RESOURCE_PATH);
        if (alpnBootJarUrl == null) {
            throw new IllegalStateException("unable to locate alpn-boot JAR: " + ALPN_BOOT_JAR_RESOURCE_PATH);
        }
        try {
            File tmpfile = AlpnRtUtil.writeTempFile(alpnBootJarUrl.openStream(), alpnBootJarUrl.getFile(), tempDir);
            tmpfile.deleteOnExit();
            JarFile jarFile = new JarFile(tmpfile);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.');
                    alpnClassNames.add(className);
                }
            }
            return new URLClassLoader(new URL[] { tmpfile.toURI().toURL() }, this);
        } catch (IOException e) {
            throw new RuntimeException("unable to create alpn-boot classloader: " + alpnBootJarUrl, e);
        }
    }
}
