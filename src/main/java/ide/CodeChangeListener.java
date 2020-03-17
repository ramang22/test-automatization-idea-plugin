package ide;

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
import pluginResources.TestSingleton;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

// https://raw.githubusercontent.com/rastocny/mock-management/master/src/mock/manager/logic/eventlistener/CodeChangeListener.java?token=AHEYZANLMANBAWXXLFGKBEK6L5MSE

public class CodeChangeListener implements PsiTreeChangeListener {
    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void childReplaced(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildAddition(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        handleCodeChange(psiTreeChangeEvent);
    }

    public void handleCodeChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
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


}
