package opencloverController;

import com.atlassian.clover.reporters.html.HtmlReporter;
import mavenRunner.CustomRunner;
import org.xml.sax.SAXException;
import pluginResources.PluginSingleton;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class cloverApiRunner implements CustomRunner {


    public static void runHtmlReporter(){
        String clover_db_path = PluginSingleton.getInstance().getClover_db_path();
        String report_path = PluginSingleton.getInstance().getClover_html_report_path();
        String [] cliArgs = { "-i", clover_db_path, "-o", report_path };
        int response_code = HtmlReporter.runReport(cliArgs);
        if (response_code != 0){
            System.out.println("Error during html reporter");
        }else {
            System.out.println("HTML reporter done");
        }
    }

}
