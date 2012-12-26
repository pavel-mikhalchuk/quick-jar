package com.prettybit.quickjar.web.console;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Pavel Mikhalchuk
 */
public class Console {

    private static Lock lock = new ReentrantLock();
    private static Condition open = lock.newCondition();

    private static ConsoleWriter WRITER;

    public static void open(HttpServletResponse response) throws IOException {
        try {
            lock.lock();
            WRITER = new ConsoleWriter(response);
            WRITER.open();
            waitUntilClosed();
        } finally {
            lock.unlock();
        }
    }

    public static void open(OutputStream stream) throws IOException {
        try {
            lock.lock();
            WRITER = new ConsoleWriter(stream);
            WRITER.open();
        } finally {
            lock.unlock();
        }
    }

    public static boolean isOpen() {
        return WRITER.isOpen();
    }

    public static void write(String string) throws IOException {
        try {
            lock.lock();
            WRITER.write(string);
        } finally {
            lock.unlock();
        }
    }

    public static void write(InputStream stream) throws IOException {
        try {
            lock.lock();
            WRITER.write(stream);
        } finally {
            lock.unlock();
        }
    }

    public static void close() throws IOException {
        try {
            lock.lock();
            WRITER.close();
        } finally {
            open.signal();
            lock.unlock();
        }
    }

    private static void waitUntilClosed() {
        try {
            while (isOpen()) {
                open.await();
            }
        } catch (InterruptedException e) {
            //just leave
        }
    }

}