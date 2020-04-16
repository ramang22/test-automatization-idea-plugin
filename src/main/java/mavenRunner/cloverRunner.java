package mavenRunner;

import pluginResources.PluginSingleton;

import java.io.IOException;

public class cloverRunner {
    public static void runClover() throws InterruptedException{
        String pomPath = PluginSingleton.getInstance().getPomPath();
        Process process;
        try {
            String[] exec_cmd = new String[]{"mvn","-f",pomPath, "clean", "clover:setup", "test", "clover:aggregate", "clover:clover"};
            process = Runtime.getRuntime().exec(exec_cmd);
        } catch (IOException e) {
            e.printStackTrace();
            process = null;
        }
        int result = process.waitFor();
        String output = CustomRunner.getStdInput(process);
        if (output.contains("BUILD FAILURE")) {
            System.out.println("Clover run fail");
        } else {
            System.out.println("Clover run ok");
        }
    }
}
