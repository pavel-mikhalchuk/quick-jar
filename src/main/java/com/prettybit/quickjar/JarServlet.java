package com.prettybit.quickjar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * @author Pavel Mikhalchuk
 */
public class JarServlet extends HttpServlet {

    private Jar jar = new Jar();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pkgName = request.getParameter("pkg");
        String className = request.getParameter("class");
        String code = request.getParameter("code");

        write(jar.make(pkgName, className, code), response);
    }

    private void write(File jar, HttpServletResponse response) throws IOException {
        response.setContentLength((int) jar.length());
        response.setContentType("application/java-archive");
        response.setHeader("Content-Disposition", "attachment; filename=\"JAR.jar\"");
        response.getOutputStream().write(readFileToByteArray(jar));
    }

}