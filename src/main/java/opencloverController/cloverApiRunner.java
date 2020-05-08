package opencloverController;

import com.atlassian.clover.reporters.html.HtmlReporter;
import logger.PluginLogger;
import mavenRunner.CustomRunner;
import org.xml.sax.SAXException;
import pluginResources.PluginSingleton;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class cloverApiRunner implements CustomRunner {

    final static PluginLogger logger = new PluginLogger(cloverApiRunner.class);

    public static void runHtmlReporter() {
        String clover_db_path = PluginSingleton.getInstance().getClover_db_path();
        String report_path = PluginSingleton.getInstance().getClover_html_report_path();
        String[] cliArgs = {"-i", clover_db_path, "-o", report_path};
        int response_code = HtmlReporter.runReport(cliArgs);
        if (response_code != 0) {
            logger.log(PluginLogger.Level.ERROR, "Error during HTML reporter run");
        } else {
            logger.log(PluginLogger.Level.OK, "Error during HTML reporter run");
        }
    }

}
