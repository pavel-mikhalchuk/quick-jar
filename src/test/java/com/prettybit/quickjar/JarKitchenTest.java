package com.prettybit.quickjar;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Pavel Mikhalchuk
 */
public class JarKitchenTest {

    private JarKitchen kitchen = new JarKitchen();

    @Test
    public void testCook() throws IOException {
        String pkgName = "com.prettybit.quickjar";
        String className = "Test";
        String code = testCode();

        kitchen.cook(pkgName, className, code);

        assertTrue(new File(kitchen.baseDir(), "src").exists());
        assertTrue(new File(new File(kitchen.baseDir(), "src"), "com").exists());
        assertTrue(new File(new File(new File(kitchen.baseDir(), "src"), "com"), "prettybit").exists());
        assertTrue(new File(new File(new File(new File(kitchen.baseDir(), "src"), "com"), "prettybit"), "quickjar").exists());
        assertTrue(new File(new File(new File(new File(new File(kitchen.baseDir(), "src"), "com"), "prettybit"), "quickjar"), "Test.java").exists());

        assertTrue(new File(kitchen.baseDir(), "classes").exists());
        assertTrue(new File(new File(kitchen.baseDir(), "classes"), "com").exists());
        assertTrue(new File(new File(new File(kitchen.baseDir(), "classes"), "com"), "prettybit").exists());
        assertTrue(new File(new File(new File(new File(kitchen.baseDir(), "classes"), "com"), "prettybit"), "quickjar").exists());
        assertTrue(new File(new File(new File(new File(new File(kitchen.baseDir(), "classes"), "com"), "prettybit"), "quickjar"), "Test.class").exists());

        assertEquals("Main-Class: com.prettybit.quickjar.Test\n", readFileToString(new File(kitchen.baseDir(), "MANIFEST.txt")));

        assertTrue(new File(kitchen.baseDir(), "JAR.jar").exists());
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