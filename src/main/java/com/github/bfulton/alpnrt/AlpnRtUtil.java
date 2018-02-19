package com.github.bfulton.alpnrt;

import java.io.*;

class AlpnRtUtil {
    static File writeTempFile(InputStream in, String filename) {
        int filenameLastDotIndex = filename.lastIndexOf('.');
        String filenamePrefix;
        String filenameSuffix;
        if (filenameLastDotIndex > 0) {
            filenamePrefix = filename.substring(0, filenameLastDotIndex) + ".";
            filenameSuffix = filename.substring(filenameLastDotIndex);
        } else {
            filenamePrefix = filename;
            filenameSuffix = "";
        }
        File tmpfile;
        try {
            tmpfile = File.createTempFile(filenamePrefix, filenameSuffix);
        } catch (IOException e) {
            throw new RuntimeException("unable to create temp file for filename: " + filename, e);
        }
        writeFile(in, tmpfile);
        return tmpfile;
    }

    static void writeFile(InputStream in, File dest) {
        try {
            OutputStream out = new FileOutputStream(dest);
            try {
                write(in, out);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("unable to write file: " + dest, e);
        }
    }

    static void write(InputStream in, OutputStream out) throws IOException {
        final byte[] buf = new byte[8192];
        while (true) {
            final int readBytes = in.read(buf);
            if (readBytes < 0) {
                break;
            }
            out.write(buf, 0, readBytes);
        }
    }
}
