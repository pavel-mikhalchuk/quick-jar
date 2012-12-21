package com.prettybit.quickjar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Pavel Mikhalchuk
 */
public class RunServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConsoleWriter console = new ConsoleWriter(response);

        ProcessBuilder builder = new ProcessBuilder("java", "-jar", DB.get("Test.jar").getAbsolutePath());
        builder.redirectErrorStream(true);
        Process process = builder.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            console.write(line);
        }

        console.close();
    }

    private class ConsoleWriter {

        private HttpServletResponse response;

        private ConsoleWriter(HttpServletResponse response) {
            this.response = response;
            response.setHeader("Content-Type", "text/event-stream");
            response.setHeader("Cache-Control", "no-cache");
        }

        public void write(String string) throws IOException {
            response.getWriter().print("data: " + string + "\n\n");
            response.getWriter().flush();
        }

        public void close() throws IOException {
            response.getWriter().print("event: end\n");
            response.getWriter().print("data: \n\n");
            response.getWriter().flush();
        }

    }

}
