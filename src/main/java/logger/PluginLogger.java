package logger;

import pluginResources.PluginSingleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PluginLogger {
    /**
     * if console log is enabled
     */
    public final boolean consoleLog = true;
    /**
     * class name for logging
     */
    private String class_name;

    public PluginLogger(Class name) {
        this.class_name = name.getName();
    }

    /**
     * level of logger info
     */
    public enum Level {
        INFO,
        ERROR,
        OK,
        WARN,
        FAILED,
        PASSED
    }

    /**
     * building logger msg
     *
     * @param date  date of log
     * @param level level of log
     * @param msg   log msg
     * @return Log msg in String
     */
    public String getLogString(String date, Level level, String msg) {
        StringBuilder returnString = new StringBuilder();
        returnString.append(date);
        returnString.append(" [ ");
        returnString.append(level);
        returnString.append(" ] : ");
        returnString.append(this.class_name);
        returnString.append(" -> ");
        returnString.append(msg);
        return returnString.toString();
    }

    /**
     * Write log into log file
     *
     * @param loggerUrl   log file url
     * @param date_string date of log
     * @param level       log level
     * @param msg         message of log
     */
    public void writeLogIntoFile(String loggerUrl, String date_string, Level level, String msg) throws IOException {
        FileWriter myWriter = new FileWriter(loggerUrl, true);
        BufferedWriter bw = new BufferedWriter(myWriter);
        bw.write(this.getLogString(date_string, level, msg));
        bw.newLine();
        bw.close();
        myWriter.close();
    }

    /**
     * init log into file
     *
     * @param level       log level
     * @param date_string log date
     * @param msg         message of log
     */
    public void logInfoFile(Level level, String date_string, String msg) throws IOException {
        String loggerUrl = PluginSingleton.getInstance().getProjectRootFolderPath() + "TestPlugin/logger/log.txt";
        File f = new File(loggerUrl);
        if (!f.exists()) {
            boolean dirCreation = new File(PluginSingleton.getInstance().getProjectRootFolderPath() + "TestPlugin/logger").mkdirs();
            boolean fileCreation = f.createNewFile();
            writeLogIntoFile(loggerUrl, date_string, level, msg);
        } else {
            writeLogIntoFile(loggerUrl, date_string, level, msg);
        }
    }

    /**
     * handle log registration
     *
     * @param level log level
     * @param msg   message
     */
    public void handleLog(Level level, String msg) throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date_string = formatter.format(date);
        if (this.consoleLog) {
            System.out.println(this.getLogString(date_string, level, msg));
        }
        this.logInfoFile(level, date_string, msg);
        PluginSingleton.getInstance().getLogMessages().add(this.getLogString(date_string, level, msg));
        if (PluginSingleton.getInstance().getToolWindow() != null){
            PluginSingleton.getInstance().getToolWindow().getLogs();

        }
    }

    /**
     * method for call logger outside of logger class
     *
     * @param level level
     * @param msg   message
     */
    public void log(Level level, String msg) {
        try {
            this.handleLog(level, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
