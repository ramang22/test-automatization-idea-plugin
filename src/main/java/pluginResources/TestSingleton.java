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

    //
    private HashMap<String, String> testClasses;

    // key = test method name, value = lists of changes
    private HashMap<String, List<PsiElement>> testMethod_event;

    //list of test names for testing
    private HashSet<String> testsForExecution;


    private HashMap<String, HashMap<Integer, HashSet<String>>> coverageByClass;

    // line PsiElement : Array of test names strings
    private HashMap<PsiElement, HashSet<String>> psiElementToTests;
    private HashMap<String, HashSet<PsiElement>> testToPsiElements;

    private TestSingleton() {

        testClasses = new HashMap<>();
        testMethod_event = new HashMap<>();
        coverageByClass = new HashMap<>();
        psiElementToTests = new HashMap<>();
        testToPsiElements = new HashMap<>();
        testsForExecution = new HashSet<>();
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

    public HashMap<String, List<PsiElement>> getTestMethod_event() {
        return testMethod_event;
    }

    public void setTestMethod_event(HashMap<String, List<PsiElement>> testMethod_event) {
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

    public HashSet<String> getTestsForExecution() {
        return testsForExecution;
    }

    public void setTestsForExecution(HashSet<String> testsForExecution) {
        this.testsForExecution = testsForExecution;
    }
}

