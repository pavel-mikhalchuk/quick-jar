package com.prettybit.quickjar.web;

import com.prettybit.quickjar.DB;
import com.prettybit.quickjar.JarKitchen;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * @author Pavel Mikhalchuk
 */
@Path("jar")
public class JarApi {

    private JarKitchen kitchen = new JarKitchen();

    @POST
    public Response cookJar(@FormParam("pkg") String pkg, @FormParam("class") String className, @FormParam("code") String code) throws IOException {
        DB.add(className, kitchen.cook(pkg, className, code));
        return Response.status(302).location(URI.create("")).build();
    }

    private void write(File jar, HttpServletResponse response) throws IOException {
        response.setContentLength((int) jar.length());
        response.setContentType("application/java-archive");
        response.setHeader("Content-Disposition", "attachment; filename=\"JAR.jar\"");
        response.getOutputStream().write(readFileToByteArray(jar));
    }

}