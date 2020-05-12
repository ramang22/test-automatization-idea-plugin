package pluginResources;


import com.intellij.psi.PsiElement;
import highlighter.HighlightedLane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HighlightSingleton {

    private static HighlightSingleton INSTANCE = null;
    /**
     * list of Highlighted Lanes
     */
    private List<HighlightedLane> highlighted_lanes;
    /**
     * list of Highlighted Lanes with gutter
     */
    private List<HighlightedLane> highlighted_lanes_with_gutter;
    /**
     * list of Highlighted methods headers
     */
    private List<PsiElement> methodsHeaders;
    /**
     * list of test to highlight
     */
    private HashSet<String> tests_to_highlight;

    private HighlightSingleton() {
        this.highlighted_lanes = new ArrayList<>();
        this.highlighted_lanes_with_gutter = new ArrayList<>();
        this.methodsHeaders = new ArrayList<>();
        this.tests_to_highlight = new HashSet<>();
    }

    public static HighlightSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HighlightSingleton();
        }
        return INSTANCE;
    }

    public List<PsiElement> getMethodsHeaders() {
        return methodsHeaders;
    }

    public void setMethodsHeaders(List<PsiElement> methodsHeaders) {
        this.methodsHeaders = methodsHeaders;
    }

    public List<HighlightedLane> getHighlighted_lanes_with_gutter() {
        return highlighted_lanes_with_gutter;
    }

    public void setHighlighted_lanes_with_gutter(List<HighlightedLane> highlighted_lanes_with_gutter) {
        this.highlighted_lanes_with_gutter = highlighted_lanes_with_gutter;
    }

    public List<HighlightedLane> getHighlighted_lanes() {
        return highlighted_lanes;
    }

    public void setHighlighted_lanes(List<HighlightedLane> highlighted_lanes) {
        this.highlighted_lanes = highlighted_lanes;
    }

    public HashSet<String> getTests_to_highlight() {
        return tests_to_highlight;
    }

    public void setTests_to_highlight(HashSet<String> tests_to_highlight) {
        this.tests_to_highlight = tests_to_highlight;
    }
}
