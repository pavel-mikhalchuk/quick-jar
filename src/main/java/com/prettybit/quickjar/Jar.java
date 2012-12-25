package com.prettybit.quickjar;

import com.prettybit.quickjar.web.console.Console;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.apache.commons.io.FileUtils.writeStringToFile;

/**
 * @author Pavel Mikhalchuk
 */
public class Jar {

    private File base = initBaseDir();

    private final File JAR = new File(base, "JAR.jar");

    public File baseDir() {
        return base;
    }

    public File make(String pkgName, String className, String code) throws IOException {
        cleanDirectory(base);
        compile(theClass(pkgName, className, code));
        make(classesDir(), createManifest(pkgName, className));
        Console.close();
        return JAR;
    }

    public void run(File file) throws IOException {
        Console.write(Process.run("java", "-jar", file.getAbsolutePath()));
        Console.close();
    }

    private void compile(File clazz) throws IOException {
        Console.write(Process.run("javac", "-verbose", clazz.getAbsolutePath(), "-d", classesDir().getAbsolutePath()));
    }

    private File createManifest(String pkgName, String className) throws IOException {
        File result = new File(base, "MANIFEST.txt");
        writeStringToFile(result, "Main-Class: " + pkgName + "." + className + "\n");
        return result;
    }

    private void make(File classes, File manifest) throws IOException {
        Console.write(Process.run("jar", "cvfm", JAR.getAbsolutePath(), manifest.getAbsolutePath(), "-C", classes.getAbsolutePath(), "."));
    }

    private File theClass(String pkgName, String className, String code) throws IOException {
        File pkg = new File(new File(base, "src"), pkgName.replaceAll("\\.", File.separator));
        pkg.mkdirs();
        File clazz = new File(pkg, className + ".java");
        writeStringToFile(clazz, code);
        return clazz;
    }

    private File classesDir() {
        File result = new File(base, "classes");
        result.mkdirs();
        return result;
    }

    private File initBaseDir() {
        try {
            File base = new File(Storage.BASE, "kitchen");
            base.mkdirs();
            cleanDirectory(base);
            return base;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}