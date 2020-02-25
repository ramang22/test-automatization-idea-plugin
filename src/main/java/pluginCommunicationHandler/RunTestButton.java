package pluginCommunicationHandler;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import testController.MainTestController;

public class RunTestButton extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog(e.getProject(), "Ahoj", "HMM",Messages.getQuestionIcon());
        MainTestController main = new MainTestController();
        try {
            main.runAllTests("",e.getProject());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
