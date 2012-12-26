package com.prettybit.quickjar;

import com.prettybit.quickjar.web.console.Console;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Pavel Mikhalchuk
 */
public class JarTest {

    private Jar jar = new Jar();

    @Test
    public void testPkgName() {
        assertEquals("com.prettybit.quickjar", jar.pkgName(testCode()));
    }

    @Test
    public void testClassName() {
        assertEquals("Test", jar.className(testCode()));
    }

    @Test
    public void testCook() throws IOException {
        Console.open(System.out);
        jar.make(testCode());
        Console.close();

        assertTrue(new File(jar.baseDir(), "src").exists());
        assertTrue(new File(new File(jar.baseDir(), "src"), "com").exists());
        assertTrue(new File(new File(new File(jar.baseDir(), "src"), "com"), "prettybit").exists());
        assertTrue(new File(new File(new File(new File(jar.baseDir(), "src"), "com"), "prettybit"), "quickjar").exists());
        assertTrue(new File(new File(new File(new File(new File(jar.baseDir(), "src"), "com"), "prettybit"), "quickjar"), "Test.java").exists());

        assertTrue(new File(jar.baseDir(), "classes").exists());
        assertTrue(new File(new File(jar.baseDir(), "classes"), "com").exists());
        assertTrue(new File(new File(new File(jar.baseDir(), "classes"), "com"), "prettybit").exists());
        assertTrue(new File(new File(new File(new File(jar.baseDir(), "classes"), "com"), "prettybit"), "quickjar").exists());
        assertTrue(new File(new File(new File(new File(new File(jar.baseDir(), "classes"), "com"), "prettybit"), "quickjar"), "Test.class").exists());

        assertEquals("Main-Class: com.prettybit.quickjar.Test\n", readFileToString(new File(jar.baseDir(), "MANIFEST.txt")));

        assertTrue(new File(jar.baseDir(), "Test.jar").exists());
    }

    private String testCode() {
        return "package com.prettybit.quickjar;\n" +
                "\n" +
                "/**\n" +
                " * @author Pavel Mikhalchuk\n" +
                " */\n" +
                "public class Test {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"First Quick Jar\");\n" +
                "    }\n" +
                "\n" +
                "}\n";
    }

}