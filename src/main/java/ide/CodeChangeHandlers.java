package ide;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.util.PsiTreeUtil;
import logger.PluginLogger;
import org.jetbrains.annotations.NotNull;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import test.Event;
import testController.MainTestController;

import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class CodeChangeHandlers {

    /**
     * Instance of MainTestController
     */
    public static MainTestController testStater = new MainTestController();
    /**
     * Instance of PluginLogger.
     */
    private final PluginLogger logger = new PluginLogger(CodeChangeHandlers.class);

    /**
     * Prints elements of psiTreeChangeEvent
     *
     * @param psiTreeChangeEvent element
     */
    public void printEventElements(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

        PsiElement oldChild = psiTreeChangeEvent.getOldChild();
        PsiElement oldParent = psiTreeChangeEvent.getOldParent();
        PsiElement newChild = psiTreeChangeEvent.getNewChild();
        PsiElement newParent = psiTreeChangeEvent.getNewParent();
        PsiElement element = psiTreeChangeEvent.getElement();
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        PsiElement element_child = psiTreeChangeEvent.getChild();
        if (oldChild != null) {
            System.out.println("Old child Element : " + oldChild.getText() + " ,hashCode : " + oldChild.hashCode());
        }
        if (oldParent != null) {
            System.out.println("Old parent Element : " + oldParent.getText() + " ,hashCode : " + oldParent.hashCode());
        }
        if (newChild != null) {
            System.out.println("New child Element : " + newChild.getText() + " ,hashCode : " + newChild.hashCode());
        }
        if (newParent != null) {
            System.out.println("New parent Element : " + newParent.getText() + " ,hashCode" + newParent.hashCode());
        }
        if (element != null) {
            System.out.println("Element : " + element.getText() + " ,hashCode : " + element.hashCode());
        }
        if (element_parent != null) {
            System.out.println("Element parent : " + element_parent.getText() + " ,hashCode : " + element_parent.hashCode());
        }
        if (element_child != null) {
            System.out.println("Element child : " + element_child.getText() + " ,hashCode : " + element_child.hashCode());
        }

    }

    /**
     * Handle code change.
     *
     * @param psiTreeChangeEvent event of change
     * @param element            PSI element
     * @param inMap              PSI element map
     */
    public void handleCodeChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent, PsiElement element, PsiElement inMap) {
        if (this.checkIfEventIsInMethods(psiTreeChangeEvent) &&
                this.checkIfEventIsInJava(Objects.requireNonNull(psiTreeChangeEvent.getFile()).getName()) &&
                this.checkIfEventIsNotTest(psiTreeChangeEvent)
        ) {
            if (TestSingleton.getInstance().getPsiElementToTests().containsKey(inMap)) {
                HashSet<String> testNames = new HashSet<>(TestSingleton.getInstance().getPsiElementToTests().get(inMap));
                TestSingleton.getInstance().getTestsForExecution().addAll(testNames);
                for (String name : testNames) {
                    addCustomElement(element, inMap, name);
                }

                if (!TestSingleton.getInstance().getPsiElementToTests().containsKey(element)) {
                    TestSingleton.getInstance().getPsiElementToTests().put(element, testNames);
                }
            }
        }
    }

    /**
     * Check if element is test method
     *
     * @param psiTreeChangeEvent change element
     * @return true if element is not method false if it is
     */
    private boolean checkIfEventIsNotTest(PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ?
                (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
        if (parentMethod == null) {
            return false;
        } else {
            PsiHandler checkTest = new PsiHandler();
            return !checkTest.isTest(parentMethod);
        }
    }

    /**
     * check if PSI file is .jva
     *
     * @param name file name
     * @return true if java , false if not
     */
    private boolean checkIfEventIsInJava(String name) {
        return name.contains(".java");
    }

    private void addCustomElement(PsiElement element, PsiElement parentElement, String test_name) {
        PsiMethod parentMethod = element instanceof PsiMethod ?
                (PsiMethod) element : PsiTreeUtil.getTopmostParentOfType(element, PsiMethod.class);
        Document document = FileDocumentManager.getInstance().getDocument(parentElement.getContainingFile().getVirtualFile());
        int lineNum = document.getLineNumber(element.getTextOffset());
        Event newEvent = new Event(element, parentElement, parentMethod, document, lineNum);

        if (TestSingleton.getInstance().getTestMethod_CustomEvent().containsKey(test_name)) {
            TestSingleton.getInstance().getTestMethod_CustomEvent().get(test_name).add(newEvent);
        } else {
            List<Event> newList = new ArrayList<>();
            newList.add(newEvent);
            TestSingleton.getInstance().getTestMethod_CustomEvent().put(test_name, newList);
        }
    }

    /**
     * check if change element is in method body
     *
     * @param psiTreeChangeEvent change element
     * @return true if it is body false if not
     */
    private Boolean checkIfEventIsInMethods(PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ?
                (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
        return parentMethod != null;
    }

    /**
     * handle timer existence
     *
     * @param psiTreeChangeEvent event change
     */
    private void checkTimer(PsiTreeChangeEvent psiTreeChangeEvent) {
        if (this.checkIfEventIsInMethods(psiTreeChangeEvent) &&
                this.checkIfEventIsInJava(Objects.requireNonNull(psiTreeChangeEvent.getFile()).getName()) &&
                this.checkIfEventIsNotTest(psiTreeChangeEvent)
        ) {
            if (!PluginSingleton.getInstance().isTestExecution()) {
                if (PluginSingleton.getInstance().isTimerWorking()) {
                    PluginSingleton.getInstance().getTimer().cancel();
                    PluginSingleton.getInstance().setTimer(new Timer());
                } else {
                    PluginSingleton.getInstance().setTimerWorking(true);
                }
                ScheduleNewTimer();
            }
        }
    }

    /**
     * save all project files
     */
    private void safeAllFiles() {
        ApplicationManager.getApplication().invokeAndWait(() -> ApplicationManager.getApplication()
                .runWriteAction(() -> FileDocumentManager.getInstance().saveAllDocuments()));
    }

    /**
     * schedule new timer
     */
    private void ScheduleNewTimer() {
        PluginSingleton.getInstance().getTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                logger.log(PluginLogger.Level.INFO, "Starting test sequence.");
                PluginSingleton.getInstance().setTestExecution(true);
                try {
                    safeAllFiles();
                    testStater.runAllTests();
                } catch (InterruptedException e) {
                    logger.log(PluginLogger.Level.ERROR, e.getMessage());
                }
                SwingUtilities.invokeLater(() -> {
                    testStater.runHighlighter();
                    PluginSingleton.getInstance().setTimerWorking(false);
                    PluginSingleton.getInstance().setTestExecution(false);
                    logger.log(PluginLogger.Level.INFO, "Plug-in work done.");
                });

            }
        }, PluginSingleton.TIMER_DELAY * 1000);
    }

    /**
     * handlerBeforeChildReplacement
     *
     * @param psiTreeChangeEvent change event
     */
    public void handlerBeforeChildReplacement(PsiTreeChangeEvent psiTreeChangeEvent) {
        this.checkTimer(psiTreeChangeEvent);
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent, element_parent, element_parent);
    }

    /**
     * handlerBeforeChildRemoval
     *
     * @param psiTreeChangeEvent change event
     */
    public void handlerBeforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        // TODO handle element parent
        this.checkTimer(psiTreeChangeEvent);
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent, element_parent, element_parent);
    }

    /**
     * handlerChildAdded
     *
     * @param psiTreeChangeEvent change event
     */
    public void handlerChildAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        this.checkTimer(psiTreeChangeEvent);
        PsiElement element_child = psiTreeChangeEvent.getChild();
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent, element_child, element_parent);
    }
}
