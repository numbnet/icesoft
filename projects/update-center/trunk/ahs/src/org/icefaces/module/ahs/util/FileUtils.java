package org.icefaces.module.ahs.util;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

//TODO: use Apache Commons I/O for file manipulation.
//TODO: use Ant APIs (as they are loaded by the Update Center ClassLoader)
//TODO: drill down into the Update Center APIs to see what is exposed from the Glassfish Administration APIs

public class FileUtils {

    public static void extractAndSave(File jarFile, String entryName, File destDir) throws IOException {
        JarFile jar = new JarFile(jarFile);
        JarEntry entry = jar.getJarEntry(entryName);
        saveEntry(destDir, jar, entry);
    }

    private static void saveEntry(File dest, JarFile jar, JarEntry entry) throws IOException {
        File entryFile = new File(dest, entry.getName());

        if (!entryFile.getParentFile().exists()) {
            entryFile.getParentFile().mkdirs();
        }

        // Create streams for reading and writing the entry
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(entryFile));
        BufferedInputStream bis = new BufferedInputStream(jar.getInputStream(entry));

        // Read and write
        int n = 0;
        byte[] buffer = new byte[8092];
        while ((n = bis.read(buffer)) > 0) {
            bos.write(buffer, 0, n);
        }

        bis.close();
        bos.close();
    }

    public static boolean move(File sourceFile, File targetDirectory) {
        return sourceFile.renameTo(new File(targetDirectory, sourceFile.getName()));
    }

}