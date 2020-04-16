package pluginCommunicationHandler;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.xml.sax.SAXException;
import testController.MainTestController;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class RunCoverageButton extends AnAction {
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog(e.getProject(), "Re-running whole coverage with openclover and maven", "Coverage run",Messages.getQuestionIcon());
        MainTestController main = new MainTestController();
        main.runCoverage();
    }
}
