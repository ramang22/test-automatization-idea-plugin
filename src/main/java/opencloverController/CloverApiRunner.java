package opencloverController;

import com.atlassian.clover.reporters.html.HtmlReporter;
import logger.PluginLogger;
import mavenRunner.CustomRunner;
import pluginResources.PluginSingleton;


public class CloverApiRunner implements CustomRunner {
    /**
     * Instance of plugin logger
     */
    final static PluginLogger logger = new PluginLogger(CloverApiRunner.class);

    /**
     * run html reporter from OpenClover api
     */
    public static void runHtmlReporter() {
        String clover_db_path = PluginSingleton.getInstance().getClover_db_path();
        String report_path = PluginSingleton.getInstance().getClover_html_report_path();
        String[] cliArgs = {"-i", clover_db_path, "-o", report_path};
        int response_code = HtmlReporter.runReport(cliArgs);
        if (response_code != 0) {
            logger.log(PluginLogger.Level.ERROR, "Error during HTML reporter run");
        } else {
            logger.log(PluginLogger.Level.OK, "HTML reporter run OK");
        }
    }

}
