package logger;

import pluginResources.PluginSingleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PluginLogger {
    public final boolean consoleLog = true;
    private String class_name;

    public  PluginLogger(Class name){
        this.class_name = name.getName();
    }

    public enum Level {
        INFO,
        ERROR,
        OK
    }

    public String getLogString(String date, Level level, String msg){
        StringBuilder returnString = new StringBuilder();
        returnString.append(date);
        returnString.append(" : ");
        returnString.append(this.class_name);
        returnString.append(", Logger level : ");
        returnString.append(level);
        returnString.append(" -> ");
        returnString.append(msg);
        return returnString.toString();
    }

    public void writeLogIntoFile(String loggerUrl, String date_string, Level level, String msg) throws IOException {
        FileWriter myWriter = new FileWriter(loggerUrl,true);
        BufferedWriter bw = new BufferedWriter(myWriter);
        bw.write(this.getLogString(date_string,level,msg));
        bw.newLine();
        bw.close();
        myWriter.close();
    }

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

    public void handleLog(Level level,String msg) throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date_string = formatter.format(date);
        if (this.consoleLog){
            System.out.println(this.getLogString(date_string,level,msg));
        }
        this.logInfoFile(level,date_string,msg);
    }

    public void log(Level level,String msg){
        try {
            this.handleLog(level,msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
