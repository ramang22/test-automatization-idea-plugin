package pluginCommunicationHandler;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import testController.MainTestController;

import java.io.IOException;

public class SelectTestsFolderButton extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FileChooserDescriptor fd = new FileChooserDescriptor(
                false,
                true,
                false,
                false,
                false,
                false);
        fd.setTitle("Select Test Folder");
        fd.setDescription("KAPP");
        FileChooser.chooseFile(fd,e.getProject(),null, virtualFile -> {
            MainTestController main = new MainTestController();
            try {
              main.runAllTests();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Messages.showMessageDialog(e.getProject(),virtualFile.getPath(),"Path",Messages.getInformationIcon());
        });
    }
}
