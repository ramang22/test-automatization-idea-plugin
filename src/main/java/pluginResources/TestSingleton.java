package pluginResources;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiTreeChangeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSingleton {
    private static TestSingleton INSTANCE = null;

    private HashMap<String, List<String>> testMap;
    private List<PsiTreeChangeEvent> events;

    private TestSingleton() {
        testMap = new HashMap<>();
        events = new ArrayList<>();
    }

    public static TestSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestSingleton();
        }
        return INSTANCE;
    }

    public HashMap<String, List<String>> getTestMap() {
        return testMap;
    }

    public void setTestMap(HashMap<String, List<String>> testMap) {
        this.testMap = testMap;
    }

    public List<PsiTreeChangeEvent> getEvents() {
        return events;
    }

    public void setEvents(List<PsiTreeChangeEvent> events) {
        this.events = events;
    }

}

