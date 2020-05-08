package mavenRunner;

import logger.PluginLogger;
import pluginCommunicationHandler.StartUpActivity;
import pluginResources.PluginSingleton;

import java.io.IOException;

public class cloverRunner {

    final static PluginLogger logger = new PluginLogger(cloverRunner.class);

    public static void runClover() throws InterruptedException {
        String pomPath = PluginSingleton.getInstance().getPomPath();
        Process process;
        if (System.getProperty("os.name").equals("Mac OS X")) {
            try {
                // TODO windows run too, check if is possible to run from clover api
                String[] exec_cmd = new String[]{"mvn", "-f", pomPath, "clean", "clover:setup", "test", "clover:aggregate", "clover:clover"};
                process = Runtime.getRuntime().exec(exec_cmd);
            } catch (IOException e) {
                logger.log(PluginLogger.Level.ERROR, e.getMessage());
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
                logger.log(PluginLogger.Level.ERROR, e.getMessage());
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
