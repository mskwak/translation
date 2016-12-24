package kr.co.easymanual.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import kr.co.easymanual.model.solr.TbxSolrBean;
import kr.co.easymanual.utils.SpringBean;
import kr.co.easymanual.utils.TbxUtils;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Index implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Index.class);

	private final File file;
	private String termEntryId;
	private String charset;
	private String descrip;
	private String term;
	private final HttpSolrClient httpSolrClient = SpringBean.getBean("solrServer", HttpSolrClient.class);

	public File getFile() {
		return this.file;
	}

	public String getTermEntryId() {
		return this.termEntryId;
	}

	public String getCharset() {
		return this.charset;
	}

	public String getDescrip() {
		return this.descrip;
	}

	public String getTerm() {
		return this.term;
	}

	public Index(File file) {
		this.file = file;
	}

	public Index(String file) {
		this.file = new File(file);
	}

	@Override
	public void run() {
		try {
			File fileForIndex = this.getFileForIndex(this.file);
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileForIndex);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//termEntry";
			NodeList termEntryNodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

	        for(int i = 0, termEntryCount = termEntryNodeList.getLength(); i < termEntryCount; i++ ) {
	        	this.termEntryId = termEntryNodeList.item(i).getAttributes().getNamedItem("id").getTextContent();
	        	NodeList langSetNodeList = termEntryNodeList.item(i).getChildNodes();
	        	this.getLangSetNode(langSetNodeList);
	        }

	        FileUtils.deleteQuietly(fileForIndex);
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (SAXException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} catch (ParserConfigurationException e) {
			logger.error("", e);
		} catch (XPathExpressionException e) {
			logger.error("", e);
		} catch (SolrServerException e) {
			logger.error("", e);
		} finally {
			try {
				this.httpSolrClient.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	private File getFileForIndex(File file) throws IOException {
		String suffix = ".index";

		if(file.toString().toLowerCase().endsWith(suffix)) {
			return file;
		}

		File fileForIndex = new File(file.toString() + suffix);
		FileUtils.copyFile(file, fileForIndex);

		return fileForIndex;
	}

	private void getLangSetNode(NodeList langSetNodeList) throws IOException, SolrServerException {
    	for(int i = 0, langSetCount = langSetNodeList.getLength(); i < langSetCount; i++) {
    		Node langSetNode = langSetNodeList.item(i);

    		this.charset = null;
    		this.descrip = null;
    		this.term = null;

    		if(langSetNode != null) {
    			if("langSet".equals(langSetNode.getNodeName())) {
    				this.charset = langSetNode.getAttributes().getNamedItem("xml:lang").getNodeValue();
    				this.getChildNodes(langSetNode);

    				TbxSolrBean tbxSolrBean = new TbxSolrBean();
    				//tbxSolrBean.setId(UUID.randomUUID().toString());
    				tbxSolrBean.setId(this.termEntryId + "-" + this.charset);
    				tbxSolrBean.setTermEntryId(this.termEntryId);
    				tbxSolrBean.setLangSet(this.charset);
    				tbxSolrBean.setCountry(TbxUtils.getCountryName(this.charset));
    				tbxSolrBean.setDescrip(this.descrip);
    				tbxSolrBean.setTerm(this.term);

    				this.doIndex(tbxSolrBean);
    			}
    		}
    	}
	}

	private void doIndex(TbxSolrBean tbxSolrBean) throws IOException, SolrServerException {
			UpdateResponse updateResponse = this.httpSolrClient.addBean(tbxSolrBean);

			if(updateResponse.getStatus() != 0) {
				throw new IOException();
			}

			this.httpSolrClient.commit();
	}

	private void getChildNodes(Node parentNode) {
		NodeList nodeList = parentNode.getChildNodes();

		for(int i = 0, nodeListCount = nodeList.getLength(); i < nodeListCount; i++) {
			Node node = nodeList.item(i);

			if(node != null) {
				switch(node.getNodeName()) {
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
}
