package test;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

public class Event {
    /**
     * line number of event
     */
    private int lineNumber;
    /**
     * document where event occurred
     */
    private Document document;
    /**
     * PSI element to event
     */
    private PsiElement element;
    /**
     * Parent element for event
     */
    private PsiElement parentElement;
    /**
     * parent method
     */
    private PsiMethod parentMethod;
    /**
     * parent method line
     */
    private int parentMethodLineNumber;

    public Event(PsiElement element, PsiElement parentElement, PsiMethod method, Document document, int line_number) {
        this.element = element;
        this.parentElement = parentElement;
        this.parentMethod = method;
        int lineNum = document.getLineNumber(method.getTextOffset());
        this.parentMethodLineNumber = lineNum;
        this.document = document;
        this.lineNumber = line_number;
    }


    public void print() {
        System.out.println("Element : " + element.getText());
        System.out.println("Parent element : " + parentElement.getText());
        System.out.println("Method : " + parentMethod.getName());
        System.out.println("Line number : " + lineNumber);
        System.out.println("----------");
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public PsiElement getElement() {
        return element;
    }

    public void setElement(PsiElement element) {
        this.element = element;
    }

    public PsiElement getParentElement() {
        return parentElement;
    }

    public void setParentElement(PsiElement parentElement) {
        this.parentElement = parentElement;
    }

    public PsiMethod getParentMethod() {
        return parentMethod;
    }

    public void setParentMethod(PsiMethod parentMethod) {
        this.parentMethod = parentMethod;
    }

    public int getParentMethodLineNumber() {
        return parentMethodLineNumber;
    }

    public void setParentMethodLineNumber(int parentMethodLineNumber) {
        this.parentMethodLineNumber = parentMethodLineNumber;
    }
}
