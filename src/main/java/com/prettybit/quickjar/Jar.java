package com.prettybit.quickjar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.apache.commons.io.FileUtils.writeStringToFile;

/**
 * @author Pavel Mikhalchuk
 */
public class Jar {

    private File base = initBaseDir();

    public final File JAR = new File(base, "JAR.jar");

    public File baseDir() {
        return base;
    }

    public File make(String pkgName, String className, String code) throws IOException {
        cleanDirectory(base);
        compile(theClass(pkgName, className, code));
        make(classesDir(), createManifest(pkgName, className));
        return JAR;
    }

    private void compile(File clazz) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("javac", "-verbose", clazz.getAbsolutePath(), "-d", classesDir().getAbsolutePath());
        builder.redirectErrorStream(true);
        Process process = builder.start();
        printProcessOutput(process.getInputStream());
    }

    private File createManifest(String pkgName, String className) throws IOException {
        File result = new File(base, "MANIFEST.txt");
        writeStringToFile(result, "Main-Class: " + pkgName + "." + className + "\n");
        return result;
    }

    private void make(File classes, File manifest) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("jar", "cvfm", JAR.getAbsolutePath(), manifest.getAbsolutePath(), "-C", classes.getAbsolutePath(), ".");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        printProcessOutput(process.getInputStream());
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

    private void printProcessOutput(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    private File initBaseDir() {
        try {
            File base = new File(System.getProperty("java.io.tmpdir"), "quickjar");
            base.mkdirs();
            cleanDirectory(base);
            return base;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}