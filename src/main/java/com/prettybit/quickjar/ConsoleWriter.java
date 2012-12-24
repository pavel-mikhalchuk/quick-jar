package com.prettybit.quickjar;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author Pavel Mikhalchuk
 */
public class ConsoleWriter {

    private PrintWriter writer;
    private boolean open = true;

    public ConsoleWriter(HttpServletResponse response) throws IOException {
        writer = response.getWriter();
        response.setHeader("Content-Type", "text/event-stream");
        response.setHeader("Cache-Control", "no-cache");
        open();
    }

    public void open() throws IOException {
        writer.print("event: start\n");
        writer.print("data: \n\n");
        writer.flush();
    }

    public void write(String string) throws IOException {
        writer.print("data: " + string + "\n\n");
        writer.flush();
    }

    public void write(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = br.readLine()) != null) {
            write(line);
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void close() throws IOException {
        writer.print("event: end\n");
        writer.print("data: \n\n");
        writer.flush();
        open = false;
    }

}