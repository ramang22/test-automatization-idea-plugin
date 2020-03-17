package pluginResources;

import com.intellij.openapi.project.Project;
import ide.Tester;

import java.util.HashMap;
import java.util.List;

public class PluginSingleton {
    private static PluginSingleton INSTANCE = null;
    private Project project;
    Tester tester;


    private PluginSingleton()
    {
        tester = new Tester();
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
}
