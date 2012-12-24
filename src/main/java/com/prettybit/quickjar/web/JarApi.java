package com.prettybit.quickjar.web;

import com.prettybit.quickjar.Console;
import com.prettybit.quickjar.ConsoleWriter;
import com.prettybit.quickjar.DB;
import com.prettybit.quickjar.Jar;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
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

    private Jar jar = new Jar();

    @POST
    @Path("make")
    public void make(@FormParam("pkg") String pkg, @FormParam("class") String className, @FormParam("code") String code) throws IOException {
        DB.add(className, jar.make(pkg, className, code));
    }

    @GET
    @Path("delete/{name}")
    public Response delete(@PathParam("name") String name) {
        DB.delete(name);
        return redirect();
    }

    @GET
    @Path("run/{name}")
    public void run(@PathParam("name") String name) throws IOException {
        jar.run(DB.get(name));
    }

    @GET
    @Path("console")
    public void console(@Context HttpServletResponse response) throws IOException {
        Console.setWriter(new ConsoleWriter(response));
        while (Console.isOpen()) {
        }
    }

    private Response redirect() {
        return Response.status(302).location(URI.create("")).build();
    }

    private void write(File jar, HttpServletResponse response) throws IOException {
        response.setContentLength((int) jar.length());
        response.setContentType("application/java-archive");
        response.setHeader("Content-Disposition", "attachment; filename=\"JAR.jar\"");
        response.getOutputStream().write(readFileToByteArray(jar));
    }

}