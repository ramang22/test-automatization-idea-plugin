package pluginCommunicationHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;
import pluginResources.PluginSingleton;

import javax.swing.*;

public class MyCustomToolWindow {
    private JPanel myToolWindowContent;
    private JList<String> list1;

    public MyCustomToolWindow(ToolWindow toolWindow) {
        myToolWindowContent.add(new JScrollPane(list1));
        getLogs();

    }

    public void getLogs() {
        list1.clearSelection();
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String log : PluginSingleton.getInstance().getLogMessages()){
            listModel.addElement(log);
        }
        list1.setModel(listModel);
    }
    public JPanel getContent() {
        return myToolWindowContent;
    }

    private void createUIComponents() {
    }


}