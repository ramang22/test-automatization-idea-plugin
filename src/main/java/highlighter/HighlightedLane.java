package highlighter;

import com.intellij.openapi.editor.Document;

public class HighlightedLane {
    /**
     * Document where highlight is
     */
    private Document document;
    /**
     * Line number of highlight
     */
    private Integer line_num;

    HighlightedLane(Document doc, Integer line_num){
        this.document = doc;
        this.line_num = line_num;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Integer getLine_num() {
        return line_num;
    }

    public void setLine_num(Integer line_num) {
        this.line_num = line_num;
    }
}
