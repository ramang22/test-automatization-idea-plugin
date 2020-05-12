package ide;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;


public class CodeChangeListener implements PsiTreeChangeListener {

    /**
     * Object CodeChangeHandlers for handling changes
     */
    private CodeChangeHandlers codeChangeHanlder = new CodeChangeHandlers();

    /**
     * childAdded
     * @param psiTreeChangeEvent change event
     */
    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        codeChangeHanlder.handlerChildAdded(psiTreeChangeEvent);
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
        //codeChangeHanlder.handlerBeforeChildAddition(psiTreeChangeEvent);
    }

    /**
     * beforeChildRemoval
     * @param psiTreeChangeEvent change event
     */
    @Override
    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        codeChangeHanlder.handlerBeforeChildRemoval(psiTreeChangeEvent);
    }

    /**
     * beforeChildReplacement
     * @param psiTreeChangeEvent change event
     */
    @Override
    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        codeChangeHanlder.handlerBeforeChildReplacement(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        //codeChangeHanlder.handlerBeforeChildMovement(psiTreeChangeEvent);
    }

    @Override
    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        //codeChangeHanlder.handlerBeforeChildrenChange(psiTreeChangeEvent);
    }

    @Override
    public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        //codeChangeHanlder.handlerBeforePropertyChange(psiTreeChangeEvent);
    }

    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        //  handleCodeChange(psiTreeChangeEvent);
    }






}
