package com.prettybit.quickjar;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * @author Pavel Mikhalchuk
 */
public class JarTest {

    private Jar jar = new Jar();

    @Test
    public void test() throws IOException {
        String pkgName = "com.prettybit.quickjar";
        String className = "Test";
        String code = testCode();

        jar.make(pkgName, className, code);

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