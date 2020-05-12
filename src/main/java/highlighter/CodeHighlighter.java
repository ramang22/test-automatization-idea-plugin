package highlighter;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.DocumentUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginResources.HighlightSingleton;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import test.Event;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CodeHighlighter {

    /**
     * Lavel of highlighted layer
     */
    public static int HIGHLIGHT_LAYER = 66;

    /**
     * Highlight lane with gutter icon
     *
     * @param document   Document where highlight should appeared.
     * @param lineNumber Line number of highlight
     * @param text       Gutter icon text
     */
    public static void addLineHighlight(Document document, int lineNumber,
                                        String text) {
        //setupLineStyle(type);
        addGutterIcon(getRangeHighlighter(document, lineNumber), CustomIcons.FAIL_GUTTER, text);
    }

    /**
     * @param document   Document where highlight should appeared.
     * @param lineNumber Line number of highlight.
     * @return RangeHighlighter
     */
    @NotNull
    private static RangeHighlighter getRangeHighlighter(Document document, int lineNumber) {

        MarkupModel markupModel = getMarkupModel(document);
        TextAttributes textAttributes = getTextAttributes();
        RangeHighlighter highlighter;
        highlighter = markupModel.addLineHighlighter(lineNumber, HIGHLIGHT_LAYER, textAttributes);
        return highlighter;
    }

    /**
     * Gutter icon initialization.
     *
     * @param highlighter RangeHighlighter
     * @param icon        Icon.png
     * @param text        Gutter icon text.
     */
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

    /**
     * @param document Document where highlight should appeared.
     * @return retrieve markup model from document
     */
    private static MarkupModel getMarkupModel(Document document) {
        return DocumentMarkupModel.forDocument(document, PluginSingleton.getInstance().getProject(), true);
    }

    /**
     * Init text attributes
     *
     * @return TextAttributes
     */
    @NotNull
    private static TextAttributes getTextAttributes() {
        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(CustomColors.ERROR);
        textAttributes.setErrorStripeColor(CustomColors.ERROR);
        return textAttributes;
    }

    /**
     * @param document   Document where highlight should appeared.
     * @param line_num   Line number
     * @param withGutter gutter line or not
     */
    private static void saveHighlight(Document document, int line_num, Boolean withGutter) {
        if (withGutter) {
            HighlightSingleton.getInstance().getHighlighted_lanes_with_gutter().add(new HighlightedLane(document, line_num));
        } else {
            HighlightSingleton.getInstance().getHighlighted_lanes().add(new HighlightedLane(document, line_num));
        }
    }

    /**
     * Highlight line from event
     *
     * @param event Change event
     */
    private static void highLightLine(@NotNull Event event) {
        Document document = event.getDocument();
        int lineNum = document.getLineNumber(event.getElement().getTextOffset());
        saveHighlight(document, lineNum, false);

        MarkupModel markupModel = getMarkupModel(document);
        TextAttributes textAttributes = getTextAttributes();
        RangeHighlighter highlighter = markupModel.addLineHighlighter(lineNum, HIGHLIGHT_LAYER, textAttributes);

    }

    /**
     * Highlight line from event with gutter
     *
     * @param element     Change event
     * @param toolTipText Gutter icon text
     */
    private static void highLightLineWithGutter(@NotNull Event element, String toolTipText) {
        saveHighlight(element.getDocument(), element.getParentMethodLineNumber(), true);
        int lineNum = element.getDocument().getLineNumber(element.getParentMethod().getTextOffset());
        addLineHighlight(element.getDocument(), lineNum, toolTipText);
    }

    /**
     * Remove highlight on specific line
     *
     * @param document   Document where highlight should appeared.
     * @param lineNumber Line number
     */
    private static void removeLineHighlight(Document document, int lineNumber) {
        MarkupModel markupModel = getMarkupModel(document);
        TextRange lineTextRange = DocumentUtil.getLineTextRange(document, lineNumber);
        for (RangeHighlighter highlighter : markupModel.getAllHighlighters()) {
            if (intersectsAndMatchLayer(highlighter, lineTextRange)) {
                markupModel.removeHighlighter(highlighter);
            }
        }
    }

    /**
     * Removes all highlights
     */
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

    /**
     * Highlighter main
     *
     * @param test_name   test name to high
     * @param test_passed
     * @param tooltip
     */
    public static void highlightTest(String test_name, Boolean test_passed, String tooltip) {
        List<Event> events = TestSingleton.getInstance().getTestMethod_CustomEvent_forExecution().get(test_name);
        List<Integer> methods = new ArrayList<>();
        if (events != null && !events.isEmpty()) {
            for (Event event : events) {
                highLightLine(event);
            }

            // hightlight parent methods with gutter
            for (Event method : events) {
                if (!methods.contains(method.getLineNumber())) {
                    highLightLineWithGutter(method, tooltip);
                    methods.add(method.getLineNumber());
                }
            }
        }

    }

}
