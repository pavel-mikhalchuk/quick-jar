package com.prettybit.quickjar;

/**
 * @author Pavel Mikhalchuk
 */
public class Console {

    private static ConsoleWriter WRITER;

    public static ConsoleWriter getWriter() {
        return WRITER;
    }

    public static void setWriter(ConsoleWriter writer) {
        WRITER = writer;
    }

    public static boolean isOpen() {
        return WRITER.isOpen();
    }

}