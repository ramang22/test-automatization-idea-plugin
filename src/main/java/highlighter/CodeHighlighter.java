package highlighter;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.DocumentUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginResources.HighlightSingleton;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class CodeHighlighter {

    CodeHighlighter code_highlighter = new CodeHighlighter();

    public static void addLineHighlight(Document document, int lineNumber,
                                        String text) {
        //setupLineStyle(type);
        Icon highlightIcon = IconLoader.getIcon("/META-INF/fail.png");
        addGutterIcon(getRangeHighlighter(document, lineNumber), highlightIcon, text);
    }

    @NotNull
    private static RangeHighlighter getRangeHighlighter(Document document, int lineNumber) {
        MarkupModel markupModel = getMarkupModel(document);
        TextAttributes textAttributes = getTextAttributes();
        RangeHighlighter highlighter;
        highlighter = markupModel.addLineHighlighter(lineNumber, 66, textAttributes);
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

            @Override
            @Nullable
            public String getTooltipText() {
                return text;
            }
        });

    }

    private static MarkupModel getMarkupModel(Document document) {
        return DocumentMarkupModel.forDocument(document, PluginSingleton.getInstance().getProject(), true);
    }

    @NotNull
    private static TextAttributes getTextAttributes() {
        TextAttributes textAttributes =  new TextAttributes();
        textAttributes.setBackgroundColor(JBColor.RED);
        textAttributes.setErrorStripeColor(JBColor.BLUE);
        return textAttributes;
    }

    private static void saveHighlight(Document document, int line_num, Boolean withGutter) {
        if (withGutter) {
            HighlightSingleton.getInstance().getHighlighted_lanes_with_gutter().add(new HighlightedLane(document, line_num));
        } else {
            HighlightSingleton.getInstance().getHighlighted_lanes().add(new HighlightedLane(document, line_num));
        }
    }

    private static void highLightLine(@NotNull PsiElement psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent;
        Document document = FileDocumentManager.getInstance().getDocument(psiTreeElement.getContainingFile().getVirtualFile());
        int lineNum = document.getLineNumber(psiTreeChangeEvent.getTextOffset());
        saveHighlight(document, lineNum, false);

        MarkupModel markupModel = getMarkupModel(document);
        TextAttributes textAttributes = getTextAttributes();
        RangeHighlighter highlighter = markupModel.addLineHighlighter(lineNum, 66, textAttributes);
    }

    private static void highLightLineWithGutter(@NotNull PsiElement element) {
        PsiFile containingFile = element.getContainingFile();
        FileViewProvider fileViewProvider = containingFile.getViewProvider();
        Document document = fileViewProvider.getDocument();
        int textOffset = element.getTextOffset();
        int lineNumber = document.getLineNumber(textOffset);

        saveHighlight(document, lineNumber, true);

        addLineHighlight(document, lineNumber, "TEXT PLACE HOLDER");
    }

    private static void removeLineHighlight(Document document, int lineNumber) {
        MarkupModel markupModel = getMarkupModel(document);
        TextRange lineTextRange = DocumentUtil.getLineTextRange(document, lineNumber);
        for (RangeHighlighter highlighter : markupModel.getAllHighlighters()) {
            //  if (intersectsAndMatchLayer(highlighter, lineTextRange)) {
            markupModel.removeHighlighter(highlighter);
            //}
        }
    }

    public static void removeOldHighlights() {

        if (!HighlightSingleton.getInstance().getHighlighted_lanes().isEmpty()) {
            for (HighlightedLane h : HighlightSingleton.getInstance().getHighlighted_lanes()) {
                int lim_num = h.getLine_num();
                removeLineHighlight(h.getDocument(), lim_num);
            }
        }
        if (!HighlightSingleton.getInstance().getHighlighted_lanes_with_gutter().isEmpty()) {
            for (HighlightedLane h : HighlightSingleton.getInstance().getHighlighted_lanes_with_gutter()) {
                removeLineHighlight(h.getDocument(), h.getLine_num());
            }
        }
        HighlightSingleton.getInstance().getHighlighted_lanes_with_gutter().clear();
        HighlightSingleton.getInstance().getHighlighted_lanes().clear();
    }

    private static boolean intersectsAndMatchLayer(@NotNull RangeHighlighter highlighter, @NotNull TextRange lineTextRange) {
        return !(highlighter.getEndOffset() < lineTextRange.getStartOffset()
                || highlighter.getStartOffset() > lineTextRange.getEndOffset())
                && highlighter.getLayer() == 66;
    }

    public static void highlightTest(String test_name, Boolean test_passed) {
        // get all events for tests
        List<PsiElement> events = TestSingleton.getInstance().getTestMethod_event().get(test_name);
        HashSet<PsiElement> methods = new HashSet<>();
        // for every event
        for (PsiElement event : events) {

            // highlight
            highLightLine(event);

            // get parent method
            PsiElement psiTreeElement = event.getParent();
            PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ? (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
            methods.add(parentMethod);
        }

        // hightlight parent methods with gutter
        for (PsiElement method : methods) {
            highLightLineWithGutter(method);
        }
    }

}
