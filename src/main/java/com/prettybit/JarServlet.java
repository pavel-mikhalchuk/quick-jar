package com.prettybit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Pavel Mikhalchuk
 */
public class JarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String className = request.getParameter("class");
        String code = request.getParameter("code");

        File clazz = store(className, code);
        File byteCode = compile(clazz);
        File jar = makeJar(byteCode);

        write(jar, response);

        FileUtils.forceDelete(clazz);
        FileUtils.forceDelete(byteCode);
        FileUtils.forceDelete(jar);
    }

    private File store(String className, String code) throws IOException {
        File file = new File(FileUtils.getTempDirectory(), className + ".java");
        FileUtils.write(file, code);
        return file;
    }

    private File compile(File clazz) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("/usr/bin/javac", "-verbose", clazz.getAbsolutePath());
        builder.redirectErrorStream(true);
        Process process = builder.start();
        printProcessOutput(process.getInputStream());
        return new File(FilenameUtils.removeExtension(clazz.getAbsolutePath()) + ".class");
    }

    private File makeJar(File byteCode) throws IOException {
        File jar = new File(FileUtils.getTempDirectory(), "JAR.jar");
        ProcessBuilder builder = new ProcessBuilder("/usr/bin/jar", "cvf", jar.getAbsolutePath(), byteCode.getAbsolutePath());
        builder.redirectErrorStream(true);
        Process process = builder.start();
        printProcessOutput(process.getInputStream());
        return jar;
    }

    private void write(File jar, HttpServletResponse response) throws IOException {
        response.setContentLength((int) jar.length());
        response.setContentType("application/java-archive");
        response.setHeader("Content-Disposition", "attachment; filename=\"JAR.jar\"");
        response.getOutputStream().write(FileUtils.readFileToByteArray(jar));
    }

    private void printProcessOutput(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

}