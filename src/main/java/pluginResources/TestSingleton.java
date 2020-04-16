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
    private HashMap<String, List<PsiTreeChangeEvent>> eventsForMethod;
    private HashMap<String, String> testClasses;

    // key = test method name, value = lists of changes
    private HashMap<String, List<PsiTreeChangeEvent>> testMethod_event;


    private TestSingleton() {
        testMap = new HashMap<>();
        events = new ArrayList<>();
        eventsForMethod = new HashMap<>();
        testClasses = new HashMap<>();
        testMethod_event = new HashMap<>();
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

    public HashMap<String, List<PsiTreeChangeEvent>> getEventsForMethod() {
        return eventsForMethod;
    }

    public void setEventsForMethod(HashMap<String, List<PsiTreeChangeEvent>> eventsForMethod) {
        this.eventsForMethod = eventsForMethod;
    }

    public HashMap<String, String> getTestClasses() {
        return testClasses;
    }

    public void setTestClasses(HashMap<String, String> testClasses) {
        this.testClasses = testClasses;
    }

    public HashMap<String, List<PsiTreeChangeEvent>> getTestMethod_event() {
        return testMethod_event;
    }

    public void setTestMethod_event(HashMap<String, List<PsiTreeChangeEvent>> testMethod_event) {
        this.testMethod_event = testMethod_event;
    }
}

