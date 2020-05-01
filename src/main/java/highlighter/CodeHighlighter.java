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
import test.Event;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;

public class CodeHighlighter {

    public static int HIGHLIGHT_LAYER = 66;

    public static void addLineHighlight(Document document, int lineNumber,
                                        String text) {
        //setupLineStyle(type);
        addGutterIcon(getRangeHighlighter(document, lineNumber), CustomIcons.FAIL_GUTTER, text);
    }

    @NotNull
    private static RangeHighlighter getRangeHighlighter(Document document, int lineNumber) {

        MarkupModel markupModel = getMarkupModel(document);
        TextAttributes textAttributes = getTextAttributes();
        RangeHighlighter highlighter;
        highlighter = markupModel.addLineHighlighter(lineNumber, HIGHLIGHT_LAYER, textAttributes);
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
        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(CustomColors.ERROR);
        textAttributes.setErrorStripeColor(CustomColors.ERROR);
        return textAttributes;
    }

    private static void saveHighlight(Document document, int line_num, Boolean withGutter) {
        if (withGutter) {
            HighlightSingleton.getInstance().getHighlighted_lanes_with_gutter().add(new HighlightedLane(document, line_num));
        } else {
            HighlightSingleton.getInstance().getHighlighted_lanes().add(new HighlightedLane(document, line_num));
        }
    }

    private static void highLightLine(@NotNull Event event) {
        Document document = event.getDocument();
        int lineNum = event.getLineNumber();
        saveHighlight(document, lineNum, false);

        MarkupModel markupModel = getMarkupModel(document);
        TextAttributes textAttributes = getTextAttributes();
        RangeHighlighter highlighter = markupModel.addLineHighlighter(lineNum, HIGHLIGHT_LAYER, textAttributes);

    }

    private static void highLightLineWithGutter(@NotNull Event element, String toolTipText) {
        saveHighlight(element.getDocument(), element.getParentMethodLineNumber(), true);
        addLineHighlight(element.getDocument(), element.getParentMethodLineNumber(), toolTipText);
    }

    private static void removeLineHighlight(Document document, int lineNumber) {
        MarkupModel markupModel = getMarkupModel(document);
        TextRange lineTextRange = DocumentUtil.getLineTextRange(document, lineNumber);
        for (RangeHighlighter highlighter : markupModel.getAllHighlighters()) {
            if (intersectsAndMatchLayer(highlighter, lineTextRange)) {
                markupModel.removeHighlighter(highlighter);
            }
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
                && highlighter.getLayer() == HIGHLIGHT_LAYER;
    }

    public static void highlightTest(String test_name, Boolean test_passed, String tooltip) {
        List<Event> events = TestSingleton.getInstance().getTestMethod_CustomEvent_forExecution().get(test_name);
        HashSet<String> methods = new HashSet<>();
        for (Event event : events) {
            highLightLine(event);
        }

        // hightlight parent methods with gutter
        for (Event method : events) {
            if (!methods.contains(method.getParentMethod().getName())){
                highLightLineWithGutter(method, tooltip);
                methods.add(method.getParentMethod().getName());
            }
        }

    }

}
