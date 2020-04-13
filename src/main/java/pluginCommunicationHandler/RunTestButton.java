package pluginCommunicationHandler;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;
import testController.MainTestController;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class RunTestButton extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog(e.getProject(), "Ahoj", "HMM",Messages.getQuestionIcon());
        MainTestController main = new MainTestController();
        try {
            main.runAllTests();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
    }
}
