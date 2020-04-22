package ide;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.JBColor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginResources.HighlightSingleton;
import pluginResources.TestSingleton;

import javax.swing.*;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.util.Optional;

// https://raw.githubusercontent.com/rastocny/mock-management/master/src/mock/manager/logic/eventlistener/CodeChangeListener.java?token=AHEYZANLMANBAWXXLFGKBEK6L5MSE

public class CodeChangeListener implements PsiTreeChangeListener {
    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

        //handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

        //handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void childReplaced(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

        //handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
       // handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

        //handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildAddition(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
     //   handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
      //  handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
      //  handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
      //  handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
     //   handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
      //  handleCodeChange(psiTreeChangeEvent);
    }

    public void printParents(@NotNull PsiTreeChangeEvent psiTreeChangeEvent){

        PsiElement oldChild = psiTreeChangeEvent.getOldChild();
        PsiElement oldParent = psiTreeChangeEvent.getOldParent();
        PsiElement newChild = psiTreeChangeEvent.getNewChild();
        PsiElement newParent = psiTreeChangeEvent.getNewParent();
        if (oldChild != null) {
            System.out.println("Old child Element " + oldChild.getText() + " not in hash map : " + psiTreeChangeEvent.getOldChild().hashCode());
           // System.out.println("Old child Element " + oldChild.getParent().getText() + " not in hash map : " + oldChild.getParent().hashCode());
        }
        if (oldParent != null ) {
            System.out.println("Old parent Element " + oldParent.getText() + " not in hash map : " + psiTreeChangeEvent.getOldParent().hashCode());
        }
        if (newChild != null ) {
            System.out.println("New child parent Element " + newChild.getText() + " not in hash map : " + psiTreeChangeEvent.getNewChild().hashCode());
          //  System.out.println("New child parent Element " + newChild.getParent().getText() + " not in hash map : " + newChild.getParent().hashCode());
        }
        if (newParent != null ) {
            System.out.println("New parent Element " + newParent.getText() + " not in hash map : " + psiTreeChangeEvent.getNewParent().hashCode());
        }
        if (psiTreeChangeEvent.getElement() != null){
            System.out.println("Element "+psiTreeChangeEvent.getElement().getText()+" not in hash map : " + psiTreeChangeEvent.getElement().hashCode());
        }
        if (psiTreeChangeEvent.getParent() != null){
            System.out.println("Element parent "+psiTreeChangeEvent.getParent().getText()+" not in hash map : " + psiTreeChangeEvent.getParent().hashCode());
        }

    }

    public void handleCodeChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        this.printParents(psiTreeChangeEvent);
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
            //HighlightSingleton.getInstance().getMethodsHeaders().add(parentMethod);
        }
    }


}
