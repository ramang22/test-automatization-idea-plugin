package pluginCommunicationHandler;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class PluginSettings extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        SettingWrapper settings = new SettingWrapper(e.getProject(),true);
        if(settings.showAndGet()){
            //TODO SETTINGS WORK
        }
    }
}
