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

    public static MainTestController testStater = new MainTestController();
    private final PluginLogger logger = new PluginLogger(CodeChangeHandlers.class);

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

    public void handleCodeChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent, PsiElement element, PsiElement inMap) {
        // TODO check if is method in test
        // check if event is in method not outside
        if (this.checkIfEventIsInMethods(psiTreeChangeEvent) &&
                this.checkIfEventIsInJava(Objects.requireNonNull(psiTreeChangeEvent.getFile()).getName()) &&
                this.checkIfEventIsNotTest(psiTreeChangeEvent)
        ) {
            //check if parent element in new or element changed element is in nap
            if (TestSingleton.getInstance().getPsiElementToTests().containsKey(inMap)) {
                // get all tests mapped for element
                HashSet<String> testNames = new HashSet<>(TestSingleton.getInstance().getPsiElementToTests().get(inMap));
                // add event to events for highlights
                TestSingleton.getInstance().getTestsForExecution().addAll(testNames);
                //add event to test method
                for (String name : testNames) {
                    addCustomElement(element, inMap, name);
                }

                //if new element add it to map
                if (!TestSingleton.getInstance().getPsiElementToTests().containsKey(element)) {
                    TestSingleton.getInstance().getPsiElementToTests().put(element, testNames);
                }

            }
        }
    }

    private boolean checkIfEventIsNotTest(PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ?
                (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
        if (parentMethod == null){
            return false;
        }else {
            PsiHandler checkTest = new PsiHandler();
            return !checkTest.isTest(parentMethod);
        }
    }

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

    private Boolean checkIfEventIsInMethods(PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ?
                (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
        return parentMethod != null;
    }

    private void checkTimer(PsiTreeChangeEvent psiTreeChangeEvent) {
        if (this.checkIfEventIsInMethods(psiTreeChangeEvent) &&
                this.checkIfEventIsInJava(Objects.requireNonNull(psiTreeChangeEvent.getFile()).getName()) &&
                this.checkIfEventIsNotTest(psiTreeChangeEvent)
        ) {
            if (!PluginSingleton.getInstance().isTestExecution()) {
                if (PluginSingleton.getInstance().isTimerWorking()) {
                    // stop timer and start again
                    PluginSingleton.getInstance().getTimer().cancel();
                    PluginSingleton.getInstance().setTimer(new Timer());
                    ScheduleNewTimer();
                } else {
                    // start timer
                    PluginSingleton.getInstance().setTimerWorking(true);
                    ScheduleNewTimer();
                }
            }
        }
    }

    private void safeAllFiles() {
        ApplicationManager.getApplication().invokeAndWait(() -> ApplicationManager.getApplication()
                .runWriteAction(() -> FileDocumentManager.getInstance().saveAllDocuments()));
    }

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

    public void handlerBeforeChildReplacement(PsiTreeChangeEvent psiTreeChangeEvent) {
//        Old child Element : xy ,hashCode : 31266
//        New child Element : x-y ,hashCode : 34460
//        Element parent : return xy; ,hashCode : 565900040
        // TODO Extract element parent
        this.checkTimer(psiTreeChangeEvent);
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent, element_parent, element_parent);
    }

    public void handlerBeforeChildMovement(PsiTreeChangeEvent psiTreeChangeEvent) {
        // TODO idk what it does
        this.printEventElements(psiTreeChangeEvent);

    }

    public void handlerBeforeChildrenChange(PsiTreeChangeEvent psiTreeChangeEvent) {
        // TODO whole class when someone changes, not usable idk
        this.printEventElements(psiTreeChangeEvent);
    }

    public void handlerBeforePropertyChange(PsiTreeChangeEvent psiTreeChangeEvent) {
        // TODO idk what it does
        this.checkTimer(psiTreeChangeEvent);
        this.printEventElements(psiTreeChangeEvent);
    }

    public void handlerBeforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        // TODO handle element parent
        this.checkTimer(psiTreeChangeEvent);
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent, element_parent, element_parent);
    }

    public void handlerBeforeChildAddition(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        //Element parent : {
        //            x++;
        //            return x-y;
        //        } ,hashCode : 1371802751
        //Element parent : {
        //            x++;
        //            xreturn x-y;
        //        } ,hashCode : 1371802751
        // TODO handle parent
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent, element_parent, element_parent);
    }

    public void handlerChildAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        this.checkTimer(psiTreeChangeEvent);
        PsiElement element_child = psiTreeChangeEvent.getChild();
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent, element_child, element_parent);
    }
}
