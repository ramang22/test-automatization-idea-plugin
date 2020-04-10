package mavenTestRunner;

import highlighter.CodeHighlighter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class testRunner {


    private static String getStdInput(Process process) {
        BufferedReader stdInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        return stdInput.lines().collect(Collectors.joining("\n"));
    }

    public static void runTest(String className, String test_name) throws InterruptedException {
        String pomPath = "/Users/ramang/Documents/Developer/tests-project-for-plugin/pom.xml";
        String testToRun = "-Dtest="+ className+"#"+test_name;
        Process process;
        System.out.println(String.format("Running test : %s", test_name));
        try {
            String[] exec_cmd = new String[]{"mvn", "-f", pomPath, "test", testToRun};
            process = Runtime.getRuntime().exec(exec_cmd);
        } catch (IOException e) {
            e.printStackTrace();
            process = null;
        }
        int result = process.waitFor();
        String output = getStdInput(process);
        if (output.contains("BUILD FAILURE")) {
            CodeHighlighter.highlightTest(test_name, false);
            System.out.println("Test failure");
        } else {
            // TODO ok
            System.out.println("Test success");
        }
    }
}
