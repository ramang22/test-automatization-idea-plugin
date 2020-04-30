package pluginCommunicationHandler;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import highlighter.CustomIcons;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.xml.sax.SAXException;
import pluginResources.PluginSingleton;
import testController.MainTestController;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SettingWrapper extends DialogWrapper {

    JPanel panel = new JPanel(new GridBagLayout());
    JTextField input = new JTextField();
    JButton fdButton = new JButton("Select Root Folder");
    Project project = null;

    protected SettingWrapper(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        init();
        this.setTitle("Settings");
        this.project = project;
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        fdButton.setMaximumSize(new Dimension(40, 40));
        fdButton.setIcon(AllIcons.Actions.NewFolder);
        fdButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                FileChooserDescriptor fd = new FileChooserDescriptor(
                        false,
                        true,
                        false,
                        false,
                        false,
                        false);
                fd.setTitle("Select Project Folder");
                fd.setDescription("Please select project folder");
                FileChooser.chooseFile(fd,project,null, virtualFile -> {
                    PluginSingleton.getInstance().setProjectRootFolderPath(virtualFile.getPath()+"/");
                    Messages.showMessageDialog(project,"Root folder path : "+virtualFile.getPath(),"Project Root Folder",Messages.getInformationIcon());
                });
            }
        });
        GridBag bag = new GridBag()
                .setDefaultInsets(JBUI.insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
        JLabel plugText = new JLabel();
        plugText.setText("Plugin environment settings :");
        panel.setPreferredSize(new Dimension(300,150));
        panel.add(plugText,bag.nextLine().next().weightx(0.2));
        panel.add(fdButton,bag.nextLine().next().weightx(0.2));
        return panel;
    }

    private JComponent label(String text){
        JComponent x = new JLabel(text);
        return x;
    }
}
