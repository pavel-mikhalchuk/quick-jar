package com.prettybit.quickjar;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pavel Mikhalchuk
 */
public class Process {

    public static InputStream run(String... args) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.redirectErrorStream(true);
        return builder.start().getInputStream();
    }

}