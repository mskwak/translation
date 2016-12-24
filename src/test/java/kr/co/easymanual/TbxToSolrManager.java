package kr.co.easymanual;

import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import kr.co.easymanual.model.solr.TbxSolrBean;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TbxToSolrManager {
	private final String file;
	private final Document document;
	private final XPath xPath;

	private String termEntryId;
	private String locale;
	private String descrip;
	private String term;

	public String getTermEntryId() {
		return this.termEntryId;
	}

	public String getLocale() {
		return this.locale;
	}

	public String getDescrip() {
		return this.descrip;
	}

	public String getTerm() {
		return this.term;
	}

	public TbxToSolrManager(String file) throws SAXException, IOException, ParserConfigurationException {
		this.file = file;
		InputSource inputSource = new InputSource(new FileReader(this.file));
		this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
		this.xPath = XPathFactory.newInstance().newXPath();
	}

	public void getSolr() throws XPathExpressionException {
		String expression = "//termEntry";
		NodeList termEntryNodeList = (NodeList) this.xPath.compile(expression).evaluate(this.document, XPathConstants.NODESET);

        for(int i = 0, termEntryCount = termEntryNodeList.getLength(); i < termEntryCount; i++ ) {
        	this.termEntryId = termEntryNodeList.item(i).getAttributes().getNamedItem("id").getTextContent();
        	NodeList langSetNodeList = termEntryNodeList.item(i).getChildNodes();
        	this.getLangSetNode(langSetNodeList);
        }
	}

	private void getLangSetNode(NodeList langSetNodeList) {
    	for(int i = 0, langSetCount = langSetNodeList.getLength(); i < langSetCount; i++) {
    		Node langSetNode = langSetNodeList.item(i);

    		this.locale = null;
    		this.descrip = null;
    		this.term = null;

    		if(langSetNode != null) {
    			if("langSet".equals(langSetNode.getNodeName())) {
    				this.locale = langSetNode.getAttributes().getNamedItem("xml:lang").getNodeValue();
    				this.getChildNodes(langSetNode);

    				TbxSolrBean tbxSolrBean = new TbxSolrBean();
    				tbxSolrBean.setId(UUID.randomUUID().toString());
    				tbxSolrBean.setTermEntryId(this.termEntryId);
    				tbxSolrBean.setCountry(this.locale);
    				tbxSolrBean.setDescrip(this.descrip);
    				tbxSolrBean.setTerm(this.term);

    				this.doIndex(tbxSolrBean);
    			}
    		}
    	}
	}

	private void doIndex(TbxSolrBean tbxSolrBean) {
		String urlString = "http://172.21.29.187:8983/solr/tbx";
		SolrClient solrClient = new HttpSolrClient(urlString);

		try {
			UpdateResponse updateResponse = solrClient.addBean(tbxSolrBean);
			solrClient.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(solrClient);
		}
	}

/*
	private long getId() {
		String id = String.valueOf(System.currentTimeMillis()) + String.format("%05d", 1);
		return Long.parseLong(id);
	}
*/

	private void getChildNodes(Node parentNode) {
		NodeList nodeList = parentNode.getChildNodes();

		for(int i = 0, nodeListCount = nodeList.getLength(); i < nodeListCount; i++) {
			Node node = nodeList.item(i);

			if(node != null) {
				String nodeName = node.getNodeName();

				switch(nodeName) {
				case "ntig":
					this.getChildNodes(node);
					break;
				case "tig":
					this.getChildNodes(node);
					break;
				case "descripGrp":
					this.getChildNodes(node);
					break;
				case "descrip":
					if(StringUtils.isEmpty(this.descrip)) {
						this.descrip = node.getTextContent();
					}
					break;
				case "termGrp":
					this.getChildNodes(node);
					break;
				case "term":
					this.term = node.getTextContent();
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		return "TbxToSolrManager [termEntryId=" + this.termEntryId + ", locale="
				+ this.locale + ", descrip=" + this.descrip + ", term=" + this.term + "]";
	}


/*

	private void getChildNodes(Node parentNode) {
		NodeList nodeList = parentNode.getChildNodes();

		for(int i = 0, nodeListCount = nodeList.getLength(); i < nodeListCount; i++) {
			Node childNode = nodeList.item(i);

			if(childNode != null) {
				String nodeName = childNode.getNodeName();

				switch(nodeName) {
				case "ntig":
					this.parseNtigOrTigOrDescripGrp(childNode);
					break;
				case "tig":
					this.parseNtigOrTigOrDescripGrp(childNode);
					break;
				case "descripGrp":
					this.parseNtigOrTigOrDescripGrp(childNode);
					break;
				default:
					break;
				}
			}
		}
	}

	private void parseNtigOrTigOrDescripGrp(Node parentNode) {
		NodeList nodeList = parentNode.getChildNodes();

		for(int i = 0, nodeListCount = nodeList.getLength(); i < nodeListCount; i++) {
			Node node = nodeList.item(i);

			if(node != null) {
				String nodeName = node.getNodeName();

				switch(nodeName) {
				case "descrip":
					this.descrip = node.getTextContent();
					break;
				case "termGrp":
					this.parseTerm(node);
					break;
				default:
					break;
				}
			}
		}
	}

	private void parseTerm(Node parentNode) {
		NodeList nodeList = parentNode.getChildNodes();

		for(int i = 0, nodeListCount = nodeList.getLength(); i < nodeListCount; i++) {
			Node node = nodeList.item(i);

			if(node != null) {
				String nodeName = node.getNodeName();

				switch(nodeName) {
				case "term":
					this.term = node.getTextContent();
					break;
				default:
					break;
				}
			}
		}
	}
*/

/*	private void processLangSetNode(Node langSetNode) throws XPathExpressionException {
		for(int i = 0, langSetCount = langSetNodeList.getLength(); i < langSetCount; i++) {
			String xmlLang = langSetNodeList.item(i).getAttributes().getNamedItem("xml:lang").getNodeValue();
			int j = i + 1;


			String expression = "//termEntry[@id='" + termEntryId + "']/langSet[" + j + "]/descripGrp/descrip";
			String value = this.xPath.compile(expression).evaluate(this.document);
			System.out.println(value);

			expression = "//termEntry[@id='" + termEntryId + "']/langSet[" + j + "]/ntig/termGrp/term";
			value = this.xPath.compile(expression).evaluate(this.document);
			System.out.println(value);


		}
	} */
}
