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

    private static void saveHighlight(Document document, int line_num, Boolean withGutter){
        if (withGutter){
            HighlightSingleton.getInstance().getHighlighted_lanes_with_gutter().add(new HighlightedLane(document,line_num));
        }else{
            HighlightSingleton.getInstance().getHighlighted_lanes().add(new HighlightedLane(document,line_num));
        }
    }

    private static void highLightLine(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        PsiElement psiTreeElement = psiTreeChangeEvent.getParent();
        Document document = FileDocumentManager.getInstance().getDocument(psiTreeElement.getContainingFile().getVirtualFile());
        int lineNum = document.getLineNumber(psiTreeChangeEvent.getChild().getTextOffset());
        final TextAttributes textattributes = new TextAttributes(null, JBColor.BLUE, null, EffectType.LINE_UNDERSCORE, Font.PLAIN);
        final Project project = psiTreeElement.getProject();
        final FileEditorManager editorManager = FileEditorManager.getInstance(project);
        final Editor editor = editorManager.getSelectedTextEditor();

        saveHighlight(document, lineNum, false);

        editor.getMarkupModel().addLineHighlighter(lineNum, HighlighterLayer.CARET_ROW, textattributes);
    }

    private static void highLightLineWithGutter(@NotNull PsiElement psiTreeChangeEvent) {
        PsiFile containingFile = psiTreeChangeEvent.getContainingFile();
        FileViewProvider fileViewProvider = containingFile.getViewProvider();
        Document document = fileViewProvider.getDocument();
        int textOffset = psiTreeChangeEvent.getTextOffset();
        int lineNumber = document.getLineNumber(textOffset);

        saveHighlight(document, lineNumber, true);

        addLineHighlight(document, lineNumber, "GUtter");
    }

    private static void removeLineHighlight(Document document, int lineNumber) {
        MarkupModel markupModel = getMarkupModel(document);
        TextRange lineTextRange = DocumentUtil.getLineTextRange(document, lineNumber);
        for (RangeHighlighter highlighter : markupModel.getAllHighlighters()) {

            //if (intersectsAndMatchLayer(highlighter, lineTextRange)) {
                System.out.println("tu som");
                markupModel.removeHighlighter(highlighter);
            //}
        }
    }

    public static void removeOldHighlights(){
        if (!HighlightSingleton.getInstance().getHighlighted_lanes().isEmpty()) {
            for (HighlightedLane h : HighlightSingleton.getInstance().getHighlighted_lanes()) {
                System.out.println("Removing "+h.getLine_num());
                removeLineHighlight(h.getDocument(), h.getLine_num());
            }
        }
        if (!HighlightSingleton.getInstance().getHighlighted_lanes_with_gutter().isEmpty()) {
            for (HighlightedLane h : HighlightSingleton.getInstance().getHighlighted_lanes()) {
                System.out.println("Removing "+h.getLine_num());
                removeLineHighlight(h.getDocument(), h.getLine_num());
            }
        }
    }
    private static boolean intersectsAndMatchLayer(@NotNull RangeHighlighter highlighter, @NotNull TextRange lineTextRange) {
        return !(highlighter.getEndOffset() < lineTextRange.getStartOffset()
                || highlighter.getStartOffset() > lineTextRange.getEndOffset())
                && highlighter.getLayer() == 66;
    }

    public static void highlightTest(String test_name, Boolean test_passed) {
        // get all events for tests
        List<PsiTreeChangeEvent> events = TestSingleton.getInstance().getTestMethod_event().get(test_name);
        HashSet<PsiElement> methods = new HashSet<>();
        // for every event
        for (PsiTreeChangeEvent event : events) {
            // TODO : check if highlighted

            // highlight
            highLightLine(event);

            // get parent method
            PsiElement psiTreeElement = event.getParent();
            PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ? (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
            methods.add(parentMethod);
        }
        for (PsiElement method : methods) {
            // hightlight parent methods with gutter
            highLightLineWithGutter(method);
        }
    }

}
