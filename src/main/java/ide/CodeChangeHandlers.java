package ide;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import pluginResources.TestSingleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CodeChangeHandlers {


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
        if (this.checkIfEventIsInMethods(psiTreeChangeEvent)) {
            if (TestSingleton.getInstance().getPsiElementToTests().containsKey(inMap)) {
                //add tests to queue
                HashSet<String> testNames = new HashSet<>(TestSingleton.getInstance().getPsiElementToTests().get(inMap));
                // add event to events for highlights
                for (String name : testNames) {
                    TestSingleton.getInstance().getTestsForExecution().add(name);
                    if (TestSingleton.getInstance().getTestMethod_event().containsKey(name)) {
                        TestSingleton.getInstance().getTestMethod_event().get(name).add(element);
                    } else {
                        List<PsiElement> newList = new ArrayList<>();
                        newList.add(element);
                        TestSingleton.getInstance().getTestMethod_event().put(name, newList);
                    }
                }

            }
        }
    }

    private Boolean checkIfEventIsInMethods(PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ?
                (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
        return parentMethod != null;
    }

    public void handlerBeforeChildReplacement(PsiTreeChangeEvent psiTreeChangeEvent) {
//        Old child Element : xy ,hashCode : 31266
//        New child Element : x-y ,hashCode : 34460
//        Element parent : return xy; ,hashCode : 565900040
        // TODO Extract element parent
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent,element_parent,element_parent);
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
        this.printEventElements(psiTreeChangeEvent);
    }

    public void handlerBeforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        // TODO handle element parent
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent,element_parent,element_parent);
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
        this.handleCodeChange(psiTreeChangeEvent,element_parent,element_parent);
    }

    public void handlerChildAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement element_child = psiTreeChangeEvent.getChild();
        PsiElement element_parent = psiTreeChangeEvent.getParent();
        this.handleCodeChange(psiTreeChangeEvent,element_child,element_parent);
    }
}
