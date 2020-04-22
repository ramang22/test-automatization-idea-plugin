package pluginResources;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTreeChangeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TestSingleton {
    private static TestSingleton INSTANCE = null;

    private HashMap<String, List<String>> testMap;
    private List<PsiTreeChangeEvent> events;
    private HashMap<String, List<PsiTreeChangeEvent>> eventsForMethod;
    private HashMap<String, String> testClasses;

    // key = test method name, value = lists of changes
    private HashMap<String, List<PsiTreeChangeEvent>> testMethod_event;


    private HashMap<String, HashMap<Integer, HashSet<String>>> coverageByClass;

    // line PsiElement : Array of test names strings
    private HashMap<PsiElement, HashSet<String>> psiElementToTests;
    private HashMap<String, HashSet<PsiElement>> testToPsiElements;

    private TestSingleton() {
        testMap = new HashMap<>();
        events = new ArrayList<>();
        eventsForMethod = new HashMap<>();
        testClasses = new HashMap<>();
        testMethod_event = new HashMap<>();
        coverageByClass = new HashMap<>();
        psiElementToTests = new HashMap<>();
        testToPsiElements = new HashMap<>();
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

    public HashMap<String, HashMap<Integer, HashSet<String>>> getCoverageByClass() {
        return coverageByClass;
    }

    public void setCoverageByClass(HashMap<String, HashMap<Integer, HashSet<String>>> coverageByClass) {
        this.coverageByClass = coverageByClass;
    }

    public HashMap<PsiElement, HashSet<String>> getPsiElementToTests() {
        return psiElementToTests;
    }

    public void setPsiElementToTests(HashMap<PsiElement, HashSet<String>> psiElementToTests) {
        this.psiElementToTests = psiElementToTests;
    }

    public HashMap<String, HashSet<PsiElement>> getTestToPsiElements() {
        return testToPsiElements;
    }

    public void setTestToPsiElements(HashMap<String, HashSet<PsiElement>> testToPsiElements) {
        this.testToPsiElements = testToPsiElements;
    }
}

