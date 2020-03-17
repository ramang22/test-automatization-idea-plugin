package pluginCommunicationHandler;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;
import testController.MainTestController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingWrapper extends DialogWrapper {

    JPanel panel = new JPanel(new GridBagLayout());
    JTextField input = new JTextField();
    JButton fdButton = new JButton("Set");
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

        fdButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                FileChooserDescriptor fd = new FileChooserDescriptor(
                        false,
                        true,
                        false,
                        false,
                        false,
                        false);
                fd.setTitle("Select Test Folder");
                fd.setDescription("Selector for Tests Folder");
                FileChooser.chooseFile(fd,project,null, virtualFile -> {
                    MainTestController main = new MainTestController();
                    try {
                        main.runAllTests();
                    } catch (InterruptedException | NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                    Messages.showMessageDialog(project,virtualFile.getPath(),"Path",Messages.getInformationIcon());
                });
            }
        });
        GridBag bag = new GridBag()
                .setDefaultInsets(JBUI.insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400,200));
        panel.add(label("mode"),bag.nextLine().next().weightx(0.2));
        panel.add(input,bag.nextLine().next().weightx(0.2));
        panel.add(fdButton,bag.nextLine().next().weightx(0.2));
        return panel;
    }

    private JComponent label(String text){
        JComponent x = new JLabel(text);
        return x;
    }
}
