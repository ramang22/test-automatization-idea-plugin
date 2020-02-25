package test;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.HashSet;
import java.util.List;

public class Test {
    private String name;
    private PsiMethod method;
    private PsiClass test_class;
    private String test_class_name;
    private HashSet<PsiMethod> containedMethods;
    private List<PsiMethod> containedMethods_names;

    Test(PsiMethod method, PsiClass test_class, HashSet<PsiMethod> methods){
        this.method = method;
        this.test_class = test_class;
        this.containedMethods = methods;
        this.name = method.getName();
        this.test_class_name = test_class.getName();
        //this.containedMethods_names = new List<PsiMethod>();
        for (PsiMethod x : this.containedMethods){

        }
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

    public List<PsiMethod> getContainedMethods_names() {
        return containedMethods_names;
    }

    public void setContainedMethods_names(List<PsiMethod> containedMethods_names) {
        this.containedMethods_names = containedMethods_names;
    }


}
