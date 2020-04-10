package ide;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PsiHandler {

    public List<PsiClass> getAllClasses(Project project) {
        String[] classNames = PsiShortNamesCache.getInstance(project).getAllClassNames();
        PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        List<PsiClass> psiClasses = new ArrayList<>();

        for (String className : classNames) {
            psiClasses.addAll(Arrays.asList(cache.getClassesByName(className, GlobalSearchScope.projectScope(project))));
        }
        return psiClasses;
    }

    public List<PsiMethod> getAllMethods(Project project) {
        PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        String[] methodNames = cache.getAllMethodNames();
        List<PsiMethod> methods = new ArrayList<>();

        for (String methodName : methodNames) {
            List<PsiMethod> methodsFromCache = Arrays.asList(cache.getMethodsByName(methodName, GlobalSearchScope.projectScope(project)));
            if (methodsFromCache.isEmpty() || methodsFromCache.contains(null))
                continue;

            for(PsiMethod method: methodsFromCache) {
                if(!isTest(method))
                    methods.add(method);
            }
        }
        return methods;
    }

    public boolean isTest(@NotNull PsiMethod psiMethod) {
        PsiModifierList psiModifierList = psiMethod.getModifierList();
        PsiAnnotation[] annotations = psiModifierList.getAnnotations();

        for (PsiAnnotation annotation: annotations) {
            if("@Test".equals(annotation.getText()))
                return true;
        }

        return false;
    }

    public List<PsiMethod> getAllTests(Project project) {
        PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        String[] methodNames = cache.getAllMethodNames();
        List<PsiMethod> methods = new ArrayList<>();

        for (String methodName : methodNames) {
            List<PsiMethod> methodsFromCache = Arrays.asList(cache.getMethodsByName(methodName, GlobalSearchScope.projectScope(project)));
            if (methodsFromCache.isEmpty() || methodsFromCache.contains(null))
                continue;

            for(PsiMethod method: methodsFromCache) {
                if(isTest(method))
                    methods.add(method);
            }
        }
        return methods;
    }

    public HashSet<PsiMethod> traverseBodyToFindAllMethodUsages(PsiElement methodChild) {
        HashSet<PsiMethod> methodCalls = new HashSet<>();

        if(methodChild == null)
            return methodCalls;

        for (PsiElement child : methodChild.getChildren()) {
            methodCalls.addAll(traverseBodyToFindAllMethodUsages(child));

            if (child instanceof PsiMethodCallExpressionImpl) {
                PsiMethod method = ((PsiMethodCallExpressionImpl) child).resolveMethod();
                methodCalls.add(method);
            }
        }
        return methodCalls;
    }
}
