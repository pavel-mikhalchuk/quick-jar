package com.prettybit.quickjar;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.prettybit.quickjar.Storage.BASE;
import static org.apache.commons.io.FileUtils.copyFile;

/**
 * @author Pavel Mikhalchuk
 */
public class DB {

    public static void add(String className, File jar) throws IOException {
        copyFile(jar, new File(BASE, generateJarName(className)));
    }

    public static List<File> list() {
        File[] files = BASE.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".jar");
            }
        });
        return files != null ? newArrayList(files) : Collections.<File>emptyList();
    }

    public static File get(String name) {
        for (File file : list()) {
            if (file.getName().equals(name)) return file;
        }
        return null;
    }

    public static void delete(String name) {
        if (get(name) != null) get(name).delete();
    }

    private static String generateJarName(String className) {
        return get(className + ".jar") == null ? className + ".jar" : className + " - " + new SimpleDateFormat("MM.dd.yyyy HH:mm:ss").format(new Date()) + ".jar";
    }

}
