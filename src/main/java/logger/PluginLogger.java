package logger;

import pluginResources.PluginSingleton;

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

    public void logInfoFile(Level level, String date_string, String msg) throws IOException {
        String loggerUrl = PluginSingleton.getInstance().getProjectRootFolderPath() + "TestPlugin/logger/log.txt";
        File f = new File(loggerUrl);
        if (!f.exists()) {
            new File(PluginSingleton.getInstance().getProjectRootFolderPath() + "TestPlugin/logger").mkdirs();
            f.createNewFile();
            FileWriter myWriter = new FileWriter(loggerUrl);
            myWriter.write(date_string+" : "+this.class_name+" "+level+ " -> "+msg);
            myWriter.close();
        } else {
            FileWriter myWriter = new FileWriter(loggerUrl);
            myWriter.write(date_string+" : "+this.class_name+" "+level+ " -> "+msg);
            myWriter.close();
        }
    }
    public static enum Level {
        INFO,
        ERROR,
        OK
    }

    public void handleLog(Level level,String msg) throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date_string = formatter.format(date);
        if (this.consoleLog){
            System.out.println(date_string+" : "+this.class_name+" "+level+ " -> "+msg);
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
