package com.prettybit.quickjar.web.console;

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

    private Writer writer;
    private boolean open;

    public ConsoleWriter(HttpServletResponse response) throws IOException {
        writer = new Writer(response.getWriter());
        response.setHeader("Content-Type", "text/event-stream");
        response.setHeader("Cache-Control", "no-cache");
    }

    public void open() throws IOException {
        open = true;
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

    private class Writer {

        private PrintWriter writer;

        private Writer(PrintWriter writer) {
            this.writer = writer;
        }

        public void print(String string) {
            System.out.print(string);
            writer.print(string);
        }

        public void flush() {
            System.out.println("FLUSHED");
            writer.flush();
        }

    }

}