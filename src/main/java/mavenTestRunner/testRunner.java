package mavenTestRunner;

import highlighter.CodeHighlighter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.stream.Collectors;

public class testRunner {


    private static String getStdInput(Process process) {
        BufferedReader stdInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        return stdInput.lines().collect(Collectors.joining("\n"));
    }

    public static void runTest(String className, String test_name) throws InterruptedException {
        String pomPath = "/Users/ramang/Documents/Developer/tests-project-for-plugin/pom.xml";
        String testToRun = "-Dtest="+ className+"#"+test_name;
        Process process;
        System.out.println(String.format("Running test : %s", test_name));
        try {
            String[] exec_cmd = new String[]{"mvn", "-f", pomPath, "test", testToRun};
            process = Runtime.getRuntime().exec(exec_cmd);
        } catch (IOException e) {
            e.printStackTrace();
            process = null;
        }
        int result = process.waitFor();
        String output = getStdInput(process);
        if (output.contains("BUILD FAILURE")) {
            CodeHighlighter.highlightTest(test_name, false);
            System.out.println("Test failure");
        } else {
            System.out.println("Test success");
        }
    }
    public static void runClover() throws InterruptedException, TransformerException, IOException, SAXException, ParserConfigurationException {
        String pomPath = "/Users/ramang/Documents/Developer/tests-project-for-plugin/pom.xml";
        Process process;
        try {
            String[] exec_cmd = new String[]{"mvn", "-f", pomPath, "test", "clover:setup", "clover:aggregate", "clover:clover"};
            process = Runtime.getRuntime().exec(exec_cmd);
        } catch (IOException e) {
            e.printStackTrace();
            process = null;
        }
        int result = process.waitFor();
        String output = getStdInput(process);
        if (output.contains("BUILD FAILURE")) {
            System.out.println("Test failure");
        } else {
            // TODO ok
            System.out.println("Test success");
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        File fXmlFile = new File("/Users/ramang/Documents/Developer/tests-project-for-plugin/target/site/clover/clover.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        NodeList testproject = doc.getDocumentElement().getElementsByTagName("file");

        for (int temp = 0; temp < testproject.getLength(); temp++)
        {
            Node node = testproject.item(temp);
            System.out.println(node.getNodeName());
        }
//        transformer = tf.newTransformer();
//
//        // Uncomment if you do not require XML declaration
//        // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//
//        //A character stream that collects its output in a string buffer,
//        //which can then be used to construct a string.
//        StringWriter writer = new StringWriter();
//
//        //transform document to string
//        transformer.transform(new DOMSource(doc), new StreamResult(writer));
//
//        String xmlString = writer.getBuffer().toString();
//        System.out.println(xmlString);
    }
}
