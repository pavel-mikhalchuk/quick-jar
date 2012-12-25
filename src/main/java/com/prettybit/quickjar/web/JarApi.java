package com.prettybit.quickjar.web;

import com.prettybit.quickjar.DB;
import com.prettybit.quickjar.Jar;
import com.prettybit.quickjar.web.console.Console;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Pavel Mikhalchuk
 */
@Path("jar")
public class JarApi {

    private Jar jar = new Jar();

    @POST
    @Produces("application/json")
    @Path("list")
    public String list() {
        JSONObject table = new JSONObject();
        table.put("total", DB.list().size());

        List<JSONObject> rows = new LinkedList<JSONObject>();

        for (File jar : DB.list()) {
            JSONObject row = new JSONObject();
            row.put("cell", Arrays.asList(jar.getName(),
                    new SimpleDateFormat("MM/dd/yyyy").format(new Date(jar.lastModified())),
                    "<input type=\"button\" value=\"X\" onclick=\"deleteJar('" + jar.getName() + "', this)\"/>\n" +
                            "<input type=\"button\" value=\"R\" onclick=\"runJar('" + jar.getName() + "', this)\"/>"));
            rows.add(row);
        }

        table.put("rows", rows);

        return table.toJSONString();
    }

    @POST
    @Path("make")
    public void make(@FormParam("pkg") String pkg, @FormParam("class") String className, @FormParam("code") String code) throws IOException {
        DB.add(className, jar.make(pkg, className, code));
    }

    @GET
    @Path("delete/{name}")
    public void delete(@PathParam("name") String name) {
        DB.delete(name);
    }

    @GET
    @Path("run/{name}")
    public void run(@PathParam("name") String name) throws IOException {
        jar.run(DB.get(name));
    }

    @GET
    @Path("console")
    public void console(@Context HttpServletResponse response) throws IOException {
        System.out.println("OPEN CONSOLE");
        Console.open(response);
        System.out.println("CLOSE CONSOLE");
    }

}