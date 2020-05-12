package ide;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.NotNull;
import pluginResources.TestSingleton;

import java.util.*;

public class PsiHandler {
    /**
     * get All classes in project
     *
     * @param project project file
     * @return List<PsiClass> of all classes
     */
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

    /**
     * Check if method is test
     *
     * @param psiMethod psi method
     * @return if method is test return true else false
     */
    public boolean isTest(@NotNull PsiMethod psiMethod) {
        PsiModifierList psiModifierList = psiMethod.getModifierList();
        PsiAnnotation[] annotations = psiModifierList.getAnnotations();

        for (PsiAnnotation annotation : annotations) {
            if ("@Test".equals(annotation.getText()))
                return true;
        }

        return false;
    }

    /**
     * Get all tests method
     *
     * @param project project file
     * @return List<PsiMethod>
     */
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

    /**
     * Find all usage for method
     *
     * @param methodChild method to traverse
     * @return HashSet of Psi methods
     */
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

    /**
     * finding all methods in class
     *
     * @param c                  PSI class
     * @param integerListHashMap Test names in list
     */
    public void mapLinesToTests(PsiClass c, HashMap<Integer, HashSet<String>> integerListHashMap) {
        PsiMethod[] methods = c.getAllMethods();
        for (PsiMethod method : methods) {
            for (PsiElement elem : method.getChildren()) {
                this.mapElementToTest(elem, integerListHashMap);
            }
        }
    }

    /**
     * Add parent element to map
     *
     * @param elem    element
     * @param map     Map
     * @param lineNum Parent element line number
     */
    private void addParentElement(PsiElement elem, HashMap<Integer, HashSet<String>> map, Integer lineNum) {
        PsiElement elemParent = elem.getParent();
        if (elemParent != null) {
            if (TestSingleton.getInstance().getPsiElementToTests().containsKey(elemParent)) {
                TestSingleton.getInstance().getPsiElementToTests().get(elemParent).addAll(map.get(lineNum + 1));
            } else {
                HashSet<String> newList = new HashSet<String>(map.get(lineNum + 1));
                TestSingleton.getInstance().getPsiElementToTests().put(elemParent, newList);
            }
        }

    }

    /**
     * Recursive finding of all elements and mapping them to line number
     *
     * @param elem Element
     * @param map  Map to save
     */
    public void mapElementToTest(PsiElement elem, HashMap<Integer, HashSet<String>> map) {
        if (elem == null) {
            return;
        }
        for (PsiElement child : elem.getChildren()) {
            this.mapElementToTest(child, map);
            Document document = FileDocumentManager.getInstance().getDocument(elem.getContainingFile().getVirtualFile());
            if (elem.getTextOffset() == -1) {
                continue;
            }
            int lineNum = document.getLineNumber(elem.getTextOffset());
            if (map.containsKey(lineNum + 1)) {
                if (TestSingleton.getInstance().getPsiElementToTests().containsKey(elem)) {
                    TestSingleton.getInstance().getPsiElementToTests().get(elem).addAll(map.get(lineNum + 1));
                    addParentElement(elem, map, lineNum);
                } else {
                    HashSet<String> newList = new HashSet<String>(map.get(lineNum + 1));
                    TestSingleton.getInstance().getPsiElementToTests().put(elem, newList);
                    addParentElement(elem, map, lineNum);
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
