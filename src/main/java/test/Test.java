package test;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Test {
    /**
     * test name
     */
    private String name;
    /**
     * method for test
     */
    private PsiMethod method;
    /**
     * psi class for test
     */
    private PsiClass test_class;
    /**
     * class name for test
     */
    private String test_class_name;
    /**
     * methods in test
     */
    private HashSet<PsiMethod> containedMethods;
    /**
     * method names in tests
     */
    private List<String> containedMethods_names;

    public Test(PsiMethod method, PsiClass test_class, HashSet<PsiMethod> methods) {
        this.method = method;
        this.test_class = test_class;
        this.containedMethods = methods;
        this.name = method.getName();
        this.test_class_name = test_class.getName();
        this.containedMethods_names = new ArrayList<>();
        for (PsiMethod x : this.containedMethods) {
            if (x == null)
                continue;
            this.containedMethods_names.add(x.getName());
        }
    }

    public void printTestParams() {
        System.out.println(String.format("Test : %s", this.name));
        System.out.println(String.format("Test Class : %s", this.test_class_name));
        System.out.println("Contained Methods:");
        for (String methodName : this.containedMethods_names) {
            System.out.println(String.format("\tMethod : %s", methodName));
        }
        System.out.println("-------");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PsiMethod getMethod() {
        return method;
    }

    public void setMethod(PsiMethod method) {
        this.method = method;
    }

    public PsiClass getTest_class() {
        return test_class;
    }

    public void setTest_class(PsiClass test_class) {
        this.test_class = test_class;
    }

    public String getTest_class_name() {
        return test_class_name;
    }

    public void setTest_class_name(String test_class_name) {
        this.test_class_name = test_class_name;
    }

    public HashSet<PsiMethod> getContainedMethods() {
        return containedMethods;
    }

    public void setContainedMethods(HashSet<PsiMethod> containedMethods) {
        this.containedMethods = containedMethods;
    }

    public List<String> getContainedMethods_names() {
        return containedMethods_names;
    }

    public void setContainedMethods_names(List<String> containedMethods_names) {
        this.containedMethods_names = containedMethods_names;
    }


}
