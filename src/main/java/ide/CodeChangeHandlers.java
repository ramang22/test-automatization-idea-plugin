package ide;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import pluginResources.TestSingleton;

public class CodeChangeHandlers {


    public void printEventElements(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

        PsiElement oldChild = psiTreeChangeEvent.getOldChild();
        PsiElement oldParent = psiTreeChangeEvent.getOldParent();
        PsiElement newChild = psiTreeChangeEvent.getNewChild();
        PsiElement newParent = psiTreeChangeEvent.getNewParent();
        PsiElement element = psiTreeChangeEvent.getElement();
        PsiElement element_parent = psiTreeChangeEvent.getParent();

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

    }

    public void handleCodeChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        this.printEventElements(psiTreeChangeEvent);
        // TODO MAKE HANDLER FOR EVERY BEFORE AND ADD
        // TODO Ignore changes in tests
        PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ?
                (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
        if (parentMethod == null) {
            PsiClass parentClass = psiTreeElement instanceof PsiClass ?
                    (PsiClass) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiClass.class);
            if (parentClass != null) {
                PsiElement[] classChildren = parentClass.getChildren();
                for (PsiElement classChild : classChildren) {
                    if (classChild instanceof PsiMethod) {
                        PsiMethod psiMethod = (PsiMethod) classChild;
                        System.out.println("PSI METHOD NAME: " + psiMethod.getName());
                    }
                }
            }
        } else {
            TestSingleton.getInstance().getEvents().add(psiTreeChangeEvent);
        }
    }

    private Boolean checkIfEventIsInMethods(PsiTreeChangeEvent psiTreeChangeEvent){
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
        if (this.checkIfEventIsInMethods(psiTreeChangeEvent)){
            PsiElement element_parent = psiTreeChangeEvent.getParent();
            if (TestSingleton.getInstance().getPsiElementToTests().containsKey(element_parent)){
                // add event to events for highlights
                System.out.println(TestSingleton.getInstance().getPsiElementToTests().get(element_parent));
                // add tests for that event to test queue
            }
        }
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

    public void handlerBeforeChildRemoval(PsiTreeChangeEvent psiTreeChangeEvent) {
        // TODO handle element parent
        this.printEventElements(psiTreeChangeEvent);
    }

    public void handlerBeforeChildAddition(PsiTreeChangeEvent psiTreeChangeEvent) {
        //Element parent : {
        //            x++;
        //            return x-y;
        //        } ,hashCode : 1371802751
        //Element parent : {
        //            x++;
        //            xreturn x-y;
        //        } ,hashCode : 1371802751
        // TODO handle parent
        this.printEventElements(psiTreeChangeEvent);
    }
}
