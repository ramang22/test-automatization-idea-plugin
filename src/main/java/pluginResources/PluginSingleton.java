package pluginResources;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;

import java.util.HashSet;
import java.util.Timer;

public class PluginSingleton {
    /**
     * timer delay ,default 3sec
     */
    public static int TIMER_DELAY = 3;
    private static PluginSingleton INSTANCE = null;
    /**
     * project instance
     */
    private Project project;
    /**
     * path to project in file system
     */
    private String projectRootFolderPath;
    /**
     * path to pom file
     */
    private String pomPath;
    /**
     * path to clover DB
     */
    private String clover_db_path;
    /**
     * path to html report
     */
    private String clover_html_report_path;
    /**
     * path to package file paths
     */
    private HashSet<String> package_file_paths;
    /**
     * timer
     */
    private Timer timer;
    /**
     * timer is counting down
     */
    private boolean timerWorking;
    /**
     * tests are in execution
     */
    private boolean testExecution;


    private PluginSingleton() {
        this.projectRootFolderPath = "";
        this.pomPath = "";
        this.clover_db_path = "";
        this.clover_html_report_path = "";
        this.package_file_paths = new HashSet<>();
        this.timer = new Timer();
        this.timerWorking = false;
        this.testExecution = false;
    }

    public static PluginSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PluginSingleton();
        }
        return INSTANCE;
    }

    public static void safeAllFiles() {
        new Thread(() -> ApplicationManager.getApplication()
                .runWriteAction(() -> FileDocumentManager.getInstance().saveAllDocuments())).start();
    }


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getProjectRootFolderPath() {
        return projectRootFolderPath;
    }

    public void setProjectRootFolderPath(String projectRootFolderPath) {
        this.setPomPath(projectRootFolderPath + "pom.xml");
        this.setClover_db_path(projectRootFolderPath + "target/clover/clover.db");
        this.setClover_html_report_path(projectRootFolderPath + "TestPlugin/openclover/");
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

    public boolean isTestExecution() {
        return testExecution;
    }

    public void setTestExecution(boolean testExecution) {
        this.testExecution = testExecution;
    }
}
