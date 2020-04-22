package ide;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.vcs.log.Hash;
import org.jetbrains.annotations.NotNull;
import pluginResources.TestSingleton;

import java.util.*;

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

            for (PsiMethod method : methodsFromCache) {
                if (!isTest(method))
                    methods.add(method);
            }
        }
        return methods;
    }

    public boolean isTest(@NotNull PsiMethod psiMethod) {
        PsiModifierList psiModifierList = psiMethod.getModifierList();
        PsiAnnotation[] annotations = psiModifierList.getAnnotations();

        for (PsiAnnotation annotation : annotations) {
            if ("@Test".equals(annotation.getText()))
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

            for (PsiMethod method : methodsFromCache) {
                if (isTest(method))
                    methods.add(method);
            }
        }
        return methods;
    }

    public HashSet<PsiMethod> traverseBodyToFindAllMethodUsages(PsiElement methodChild) {
        HashSet<PsiMethod> methodCalls = new HashSet<>();

        if (methodChild == null)
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

    public void mapLinesToTests(PsiClass c, HashMap<Integer, List<String>> integerListHashMap) {
        PsiMethod[] methods = c.getAllMethods();
        HashSet<PsiElement> elementsToTests = new HashSet<>();
        for (PsiMethod method : methods) {
            for (PsiElement elem : method.getChildren()) {
                this.mapElementToTest(elem, integerListHashMap);
            }
        }
    }

    public void mapElementToTest(PsiElement elem, HashMap<Integer, List<String>> map) {
        if (elem == null) {
            return;
        }
        for (PsiElement child : elem.getChildren()) {
            this.mapElementToTest(child, map);
            Document document = FileDocumentManager.getInstance().getDocument(elem.getContainingFile().getVirtualFile());
            int lineNum = document.getLineNumber(elem.getTextOffset());
            if (map.containsKey(lineNum + 1)) {
                if (TestSingleton.getInstance().getPsiElementToTests().containsKey(elem)) {
                    TestSingleton.getInstance().getPsiElementToTests().get(elem).addAll(map.get(lineNum + 1));
                } else {
                    List<String> newList = new ArrayList<>(map.get(lineNum + 1));
                    TestSingleton.getInstance().getPsiElementToTests().put(elem, newList);
                }
                for (String testName : map.get(lineNum + 1)) {
                    if (TestSingleton.getInstance().getTestToPsiElements().containsKey(testName)) {
                        TestSingleton.getInstance().getTestToPsiElements().get(testName).add(elem);
                    } else {
                        HashSet<PsiElement> newElemList = new HashSet<>();
                        newElemList.add(elem);
                        TestSingleton.getInstance().getTestToPsiElements().put(testName, newElemList);
                    }
                }

            }
        }
    }
}
