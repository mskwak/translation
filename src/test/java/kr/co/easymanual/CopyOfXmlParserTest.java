package kr.co.easymanual;

import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CopyOfXmlParserTest {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		InputSource is = new InputSource(new FileReader("/MicrosoftTermCollection.tbx"));
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		XPath xpath = XPathFactory.newInstance().newXPath();

		// http://egloos.zum.com/skymong9/v/1386892

		// http://pandorica.tistory.com/34

		// http://viralpatel.net/blogs/java-xml-xpath-tutorial-parse-xml/ -> expression 에 대하여 설명한 페이지

		//NodeList cols = (NodeList)xpath.evaluate("//term", document, XPathConstants.NODESET);

		// String expression = "//termEntry";
		//String expression = "//langSet[@xml:lang]";
		String expression = "//martif";
		System.out.println("11111111111");
		//NodeList termEntryNodeList = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);


//		System.out.println(termEntryNodeList.getLength());
//		System.out.println(termEntryNodeList.item(0).getAttributes().getNamedItem("typee").getTextContent());
//		System.out.println(termEntryNodeList.item(0).getAttributes().getNamedItem("xml:lang").getTextContent());

		Node termEntryNodeList = (Node) xpath.compile(expression).evaluate(document, XPathConstants.NODE);

		System.out.println(termEntryNodeList.getAttributes().getNamedItem("type").getTextContent());

		System.out.println(termEntryNodeList);
		if(termEntryNodeList == null) {

			System.out.println("1111111111111111222222222222222222222");
		}

		System.out.println("22222222222");
//		int termEntryCount = termEntryNodeList.getLength();
	//	System.out.println(termEntryCount);

   //     for(int i = 0; i < termEntryCount; i++ ) {
        	//String termEntryId = termEntryNodeList.item(i).getAttributes().getNamedItem("id").getTextContent();


        	//System.out.println(termEntryId);
        	//int langSetCount = termEntryNodeList.item(i).getChildNodes().getLength();

        	//NodeList langSetNodeList = termEntryNodeList.item(i).getChildNodes();

        	//processLangSetNodeList(xpath, document, termEntryId, langSetNodeList);

        	//System.out.println(langSetNodeList.item(i).getAttributes().getNamedItem("xml:lang").getTextContent());
/*
        	String expression1 = "//termEntry[@id='" + termEntryId + "']/langSet/descripGrp/descrip";
        	String x = xpath.compile(expression1).evaluate(document);
        	System.out.println(x);

        	expression1 = "//termEntry[@id='" + termEntryId + "']/langSet/ntig/termGrp/term";
        	x = xpath.compile(expression1).evaluate(document);
        	System.out.println(x);
*/


        	//System.out.println(langSetCount);

        	/*NodeList nodeList = cols.item(i).getChildNodes()

        	for(int j = 0; j < langSetCount; j++) {
        		System.out.println();
        	}*/



/*
        	NodeList nodeList = cols.item(i).getChildNodes();

        	System.out.println(nodeList.item(i).getTextContent());

        	System.out.println(cols.item(i).toString());
            System.out.println(cols.item(i).getTextContent());
            i++;
            break;*/
     //   }
	}

	private static void processLangSetNodeList(XPath xpath, Document document, String termEntryId, NodeList langSetNodeList) throws XPathExpressionException {
		int langSetCount = langSetNodeList.getLength();

		for(int i = 0; i < langSetCount; i++) {
			String id = langSetNodeList.item(i).getAttributes().getNamedItem("xml:lang").getNodeValue();
			int j = i + 1;
			System.out.println(id);
		//	langSetNodeList.item(i).getChildNodes().


			//System.out.println(termEntryId + ":" + id);
			//String expression1 = "//langSet[@xml:lang='" + id + "']/descripGrp/descrip";
			//String expression1 = "//termEntry[@id='" + termEntryId + "']/langSet[@xml:lang='" + id + "']/descripGrp/descrip";
			//String expression1 = "//termEntry[@id='" + termEntryId + "']/langSet/descripGrp/descrip";
			//String expression1 = "//termEntry[@id='" + termEntryId + "']/langSet[" + j + "]/descripGrp/descrip";
			//System.out.println(expression1);
			//String expression1 = "//langSet/@xml:lang";
        	//String x = xpath.compile(expression1).evaluate(document);
        	//System.out.println(termEntryId + ":" + id + ":" + x);


        	//String expression2 = "//termEntry[@id='" + termEntryId + "']/langSet[" + j + "]/ntig/termGrp/term";
			//System.out.println(expression1);
			//String expression1 = "//langSet/@xml:lang";
        	//String y = xpath.compile(expression2).evaluate(document);
        	//System.out.println(termEntryId + ":" + id + ":" + y);
		}
	}
}
