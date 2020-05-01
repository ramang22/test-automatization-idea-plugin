package pluginResources;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTreeChangeEvent;
import test.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TestSingleton {
    private static TestSingleton INSTANCE = null;

    //
    private HashMap<String, String> testClasses;

    //key = test name, value = list of events
    private HashMap<String, List<Event>> testMethod_CustomEvent;
    //key = test name, value = list of events
    private HashMap<String, List<Event>> testMethod_CustomEvent_forExecution;
//    // key = test method name, value = lists of changes
//    private HashMap<String, List<PsiElement>> testMethod_event;
//    // copy of testMethod_event hash map, but for execution
//    private HashMap<String, List<PsiElement>> testMethod_event_forExecution;

    //list of test names for testing
    private HashSet<String> testsForExecution;


    private HashMap<String, HashMap<Integer, HashSet<String>>> coverageByClass;

    // line PsiElement : Array of test names strings
    private HashMap<PsiElement, HashSet<String>> psiElementToTests;
    private HashMap<String, HashSet<PsiElement>> testToPsiElements;

    private TestSingleton() {

        testClasses = new HashMap<>();
//        testMethod_event = new HashMap<>();
//        testMethod_event_forExecution = new HashMap<>();
        coverageByClass = new HashMap<>();
        psiElementToTests = new HashMap<>();
        testToPsiElements = new HashMap<>();
        testsForExecution = new HashSet<>();
        testMethod_CustomEvent = new HashMap<>();
        testMethod_CustomEvent_forExecution = new HashMap<>();
    }

    public static TestSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestSingleton();
        }
        return INSTANCE;
    }

    public HashMap<String, String> getTestClasses() {
        return testClasses;
    }

    public void setTestClasses(HashMap<String, String> testClasses) {
        this.testClasses = testClasses;
    }

//    public HashMap<String, List<PsiElement>> getTestMethod_event() {
//        return testMethod_event;
//    }
//
//    public void setTestMethod_event(HashMap<String, List<PsiElement>> testMethod_event) {
//        this.testMethod_event = testMethod_event;
//    }

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

    public HashSet<String> getTestsForExecution() {
        return testsForExecution;
    }

    public void setTestsForExecution(HashSet<String> testsForExecution) {
        this.testsForExecution = testsForExecution;
    }

//    public HashMap<String, List<PsiElement>> getTestMethod_event_forExecution() {
//        return testMethod_event_forExecution;
//    }
//
//    public void setTestMethod_event_forExecution(HashMap<String, List<PsiElement>> testMethod_event_forExecution) {
//        this.testMethod_event_forExecution = testMethod_event_forExecution;
//    }

    public HashMap<String, List<Event>> getTestMethod_CustomEvent() {
        return testMethod_CustomEvent;
    }

    public void setTestMethod_CustomEvent(HashMap<String, List<Event>> testMethod_CustomEvent) {
        this.testMethod_CustomEvent = testMethod_CustomEvent;
    }

    public HashMap<String, List<Event>> getTestMethod_CustomEvent_forExecution() {
        return testMethod_CustomEvent_forExecution;
    }

    public void setTestMethod_CustomEvent_forExecution(HashMap<String, List<Event>> testMethod_CustomEvent_forExecution) {
        this.testMethod_CustomEvent_forExecution = testMethod_CustomEvent_forExecution;
    }
}

