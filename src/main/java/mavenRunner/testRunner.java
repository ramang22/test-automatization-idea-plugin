package mavenRunner;

import database.DbController;
import logger.PluginLogger;
import pluginResources.HighlightSingleton;
import pluginResources.PluginSingleton;

import java.io.*;

public class testRunner {
    /**
     * Instance of PlugginLogger
     */
    private static PluginLogger logger = new PluginLogger(testRunner.class);

    /**
     * Run test with maven
     *
     * @param className class name
     * @param test_name test name
     */
    public static void runTest(String className, String test_name) throws InterruptedException {
        String pomPath = PluginSingleton.getInstance().getPomPath();
        String testToRun = "-Dtest=" + className + "#" + test_name;
        Process process;
        logger.log(PluginLogger.Level.INFO, "Running test : " + test_name);
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                String[] exec_cmd = new String[]{"mvn", "-f", pomPath, "test", testToRun};
                process = Runtime.getRuntime().exec(exec_cmd);
            } catch (IOException e) {
                logger.log(PluginLogger.Level.ERROR, "UNIX : " + e.getMessage());
                process = null;
            }
            int result = process.waitFor();
        } else {
            try {
                String[] exec_cmd = new String[]{"cmd.exe", "/c", "mvn", "-f", pomPath, "test", testToRun};
                process = Runtime.getRuntime().exec(exec_cmd);
            } catch (IOException e) {
                logger.log(PluginLogger.Level.ERROR, "Windows : " + e.getMessage());
                process = null;
            }
//            int result = process.waitFor();
        }
        String output = CustomRunner.getStdInput(process);
        DbController db_controller = new DbController();
        testRunner runner = new testRunner();
        String time = runner.getExecTime(output);

        if (output.contains("BUILD FAILURE")) {
            db_controller.addTestResult(test_name, 0, time);
            HighlightSingleton.getInstance().getTests_to_highlight().add(test_name);
            //CodeHighlighter.highlightTest(test_name, false);
            logger.log(PluginLogger.Level.FAILED, "Test " + test_name + " failed.");
        } else {
            db_controller.addTestResult(test_name, 1, time);
            logger.log(PluginLogger.Level.PASSED, "Test " + test_name + " success.");
        }

    }

    /**
     * Extract time from output
     * @param output String output
     * @return String time
     */
    public String getExecTime(String output) {
        int indexOfTime = output.indexOf("Time elapsed: ") + "Time elapsed: ".length();
        String timeString = output.substring(indexOfTime);
        StringBuilder time = new StringBuilder();
        for (int i = 0; i < timeString.length(); i++) {
            char c = timeString.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                time.append(c);
            } else {
                break;
            }
            //Process char
        }
        //Double.parseDouble(time.toString());
        return time.toString();
    }
}
