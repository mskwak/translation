package kr.co.easymanual.solr;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

public interface Handler {
	public SolrQuery getSolrQuery(String query);

	public Map<String, List<SolrDocument>> getSolrResponse(SolrQuery solrQuery) throws SolrServerException, IOException;
}
