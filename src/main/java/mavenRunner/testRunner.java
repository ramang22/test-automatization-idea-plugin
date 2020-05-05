package mavenRunner;

import database.DbController;
import highlighter.CodeHighlighter;
import pluginResources.HighlightSingleton;
import pluginResources.PluginSingleton;

import java.io.*;

public class testRunner {

    public static void runTest(String className, String test_name) throws InterruptedException {
        String pomPath = PluginSingleton.getInstance().getPomPath();
        String testToRun = "-Dtest="+ className+"#"+test_name;
        Process process;
        System.out.println(String.format("Running test : %s", test_name));
        if (System.getProperty("os.name").equals("Mac OS X")){
            try {
                // TODO windows run too
                String[] exec_cmd = new String[]{"mvn", "-f", pomPath, "test", testToRun};
                process = Runtime.getRuntime().exec(exec_cmd);
            } catch (IOException e) {
                e.printStackTrace();
                process = null;
            }
            int result = process.waitFor();
        }else {
            try {
                // TODO windows run too
                String[] exec_cmd = new String[]{"cmd.exe","/c","mvn", "-f", pomPath, "test", testToRun};
                process = Runtime.getRuntime().exec(exec_cmd);
            } catch (IOException e) {
                e.printStackTrace();
                process = null;
            }
//            int result = process.waitFor();
        }
        String output = CustomRunner.getStdInput(process);
        DbController db_controller = new DbController();
        testRunner runner = new testRunner();
        String time = runner.getExecTime(output);
        if (output.contains("BUILD FAILURE")) {
            db_controller.addTestResult(test_name,0,time);
            HighlightSingleton.getInstance().getTests_to_highlight().add(test_name);
            //CodeHighlighter.highlightTest(test_name, false);
            System.out.println("Test failure");
        } else {

            db_controller.addTestResult(test_name,1, time);
            System.out.println("Test success");
        }

    }
    public String getExecTime(String output){
        int indexOfTime = output.indexOf("Time elapsed: ")+"Time elapsed: ".length();
        String timeString = output.substring(indexOfTime);
        StringBuilder time = new StringBuilder();
        for (int i = 0; i < timeString.length(); i++){
            char c = timeString.charAt(i);
            if (Character.isDigit(c) || c == '.'){
                time.append(c);
            }else {
                break;
            }
            //Process char
        }
        //Double.parseDouble(time.toString());
       return time.toString();
    }
}
