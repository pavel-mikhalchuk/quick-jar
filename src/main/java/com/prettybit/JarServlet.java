package com.prettybit;

import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author Pavel Mikhalchuk
 */
public class JarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        File clazz = store(code);
        File byteCode = compile(clazz);
        File jar = makeJar(byteCode);

        write(clazz, response);

        clazz.delete();
        //byteCode.delete();
        //jar.delete();
    }

    private File store(String code) throws IOException {
        File file = File.createTempFile("code", ".java");
        FileUtils.write(file, code);
        return file;
    }

    private File compile(File clazz) {
        return null;
    }

    private File makeJar(File byteCode) {
        return null;
    }

    private void write(File jar, HttpServletResponse response) throws IOException {
        response.setContentLength((int) jar.length());
        response.setContentType("application/java-archive");
        response.setHeader("Content-Disposition", "attachment; filename=\"jar.jar\"");
        response.getOutputStream().write(FileUtils.readFileToByteArray(jar));
    }

}