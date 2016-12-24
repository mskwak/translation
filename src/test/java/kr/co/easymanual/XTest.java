package kr.co.easymanual;

import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class XTest {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		// TODO Auto-generated method stub
		String hash_name = UUID.randomUUID().toString();
		System.out.println(hash_name);




	//	TbxToSolrManager tbxToSolrManager = new TbxToSolrManager("/MicrosoftTermCollectionHungary.tbx");

		//tbxToSolrManager.getSolr();

/*		System.out.println(System.currentTimeMillis());

		System.out.println(String.valueOf(System.currentTimeMillis()));

		String prefix = StringUtils.substring(String.valueOf(System.currentTimeMillis()), 1, 6);

		System.out.println(prefix);

		System.out.println(String.format("%05d", 1));

		String x = String.valueOf(System.currentTimeMillis()) + String.format("%05d", 1);

		System.out.println(Long.MAX_VALUE);*/

	}
}
