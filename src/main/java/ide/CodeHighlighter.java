package ide;

import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;

import javax.swing.*;
import java.awt.*;

public class CodeHighlighter {
    public static void addLineHighlight(Document document, int lineNumber,
                                        String text) {

        //setupLineStyle(type);
        Icon highlightIcon = IconLoader.getIcon("META-INF/fail.png");

        addGutterIcon(getRangeHighlighter(document, lineNumber), highlightIcon, text);
    }


    @NotNull
    private static RangeHighlighter getRangeHighlighter(Document document, int lineNumber) {
        MarkupModel markupModel = getMarkupModel(document);
        TextAttributes textAttributes = getTextAttributes();

        RangeHighlighter highlighter;
        highlighter = markupModel.addLineHighlighter(lineNumber, 66 , textAttributes);
        return highlighter;
    }

    private static void addGutterIcon(@NotNull RangeHighlighter highlighter, Icon icon, String text) {

        highlighter.setGutterIconRenderer(new GutterIconRenderer() {
            @Override
            public boolean equals(Object obj) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @NotNull
            @Override
            public Icon getIcon() {
                return icon;
            }
        });

    }

    private static MarkupModel getMarkupModel(Document document) {
        return DocumentMarkupModel.forDocument(document, PluginSingleton.getInstance().getProject(), true);
    }

    @NotNull
    private static TextAttributes getTextAttributes() {
        TextAttributes textAttributes = null;
        textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(JBColor.RED);
        textAttributes.setErrorStripeColor(JBColor.RED);
        return textAttributes;
    }

    private void highLightLine(@NotNull PsiTreeChangeEvent psiTreeChangeEvent){

//        ApplicationManager.getApplication().invokeAndWait(() -> ApplicationManager.getApplication()
//                .runWriteAction(() -> addLineHighlight(FileDocumentManager.getInstance().
//                                getDocument(psiTreeElement.getContainingFile().getVirtualFile())
//                        , lineNum
//                        ,                             "ahoj")));

        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        Document document = FileDocumentManager.getInstance().getDocument(psiTreeElement.getContainingFile().getVirtualFile());
        int lineNum = document.getLineNumber(psiTreeChangeEvent.getChild().getTextOffset());
        final TextAttributes textattributes = new TextAttributes(null, Color.BLUE, null, EffectType.LINE_UNDERSCORE, Font.PLAIN);
        final Project project = psiTreeElement.getProject();
        final FileEditorManager editorManager = FileEditorManager.getInstance(project);
        final Editor editor = editorManager.getSelectedTextEditor();
        editor.getMarkupModel().addLineHighlighter(lineNum, HighlighterLayer.CARET_ROW, textattributes);
    }
}
