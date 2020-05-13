package mavenRunner;

import logger.PluginLogger;
import pluginResources.PluginSingleton;

import java.io.IOException;

public class CloverRunner {
    /**
     * instance of PluginLogger
     */
    final static PluginLogger logger = new PluginLogger(CloverRunner.class);

    /**
     * Run clover code instrumentation
     */
    public static void runClover() throws InterruptedException {
        String pomPath = PluginSingleton.getInstance().getPomPath();
        Process process;
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                String[] exec_cmd = new String[]{"mvn", "-f", pomPath, "clean", "clover:setup", "test", "clover:aggregate", "clover:clover"};
                process = Runtime.getRuntime().exec(exec_cmd);
            } catch (IOException e) {
                logger.log(PluginLogger.Level.ERROR, "UNIX : " + e.getMessage());
                process = null;
            }
            int result = process.waitFor();
            String output = CustomRunner.getStdInput(process);
            if (output.contains("BUILD FAILURE")) {
                logger.log(PluginLogger.Level.ERROR, "Clover run FAIL");
            } else {
                logger.log(PluginLogger.Level.OK, "Clover run OK");
            }
        } else {
            try {
                String[] exec_cmd = new String[]{"cmd.exe", "/c", "mvn", "-f", pomPath, "clean", "clover:setup", "test", "clover:aggregate", "clover:clover"};
                process = Runtime.getRuntime().exec(exec_cmd);
            } catch (IOException e) {
                logger.log(PluginLogger.Level.ERROR, "Windows : " + e.getMessage());
                process = null;
            }
//            int result = process.waitFor();
            String output = CustomRunner.getStdInput(process);
            if (output.contains("BUILD FAILURE")) {
                logger.log(PluginLogger.Level.ERROR, "Clover run FAIL");
            } else {
                logger.log(PluginLogger.Level.OK, "Clover run OK");
            }
        }
    }
}
