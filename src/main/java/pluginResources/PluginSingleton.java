package pluginResources;

import com.intellij.openapi.project.Project;
import ide.Tester;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;

public class PluginSingleton {
    public static int TIMER_DELAY = 3;
    private static PluginSingleton INSTANCE = null;
    private Project project;
    private String projectRootFolderPath;
    private String pomPath;
    private String clover_db_path;
    private String clover_html_report_path;
    private HashSet<String> package_file_paths;
    private Timer timer;
    private boolean timerWorking;
    Tester tester;


    private PluginSingleton()
    {
        this.tester = new Tester();
        this.projectRootFolderPath  = "";
        this.pomPath = "";
        this.clover_db_path = "";
        this.clover_html_report_path = "";
        this.package_file_paths = new HashSet<>();
        this.timer = new Timer();
        this.timerWorking = false;
    }

    public static PluginSingleton getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new PluginSingleton();
        }
        return INSTANCE;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }

    public String getProjectRootFolderPath() {
        return projectRootFolderPath;
    }

    public void setProjectRootFolderPath(String projectRootFolderPath) {
        this.setPomPath(projectRootFolderPath+"pom.xml");
        this.setClover_db_path(projectRootFolderPath+"target/clover/clover.db");
        this.setClover_html_report_path(projectRootFolderPath+"TestPlugin/openclover/");
        this.projectRootFolderPath = projectRootFolderPath;
    }

    public String getPomPath() {
        return pomPath;
    }

    public void setPomPath(String pomPath) {
        this.pomPath = pomPath;
    }

    public String getClover_db_path() {
        return clover_db_path;
    }

    public void setClover_db_path(String clover_db_path) {
        this.clover_db_path = clover_db_path;
    }

    public String getClover_html_report_path() {
        return clover_html_report_path;
    }

    public void setClover_html_report_path(String clover_html_report_path) {
        this.clover_html_report_path = clover_html_report_path;
    }

    public HashSet<String> getPackage_file_paths() {
        return package_file_paths;
    }

    public void setPackage_file_paths(HashSet<String> package_file_paths) {
        this.package_file_paths = package_file_paths;
    }

    public boolean isTimerWorking() {
        return timerWorking;
    }

    public void setTimerWorking(boolean timerWorking) {
        this.timerWorking = timerWorking;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
