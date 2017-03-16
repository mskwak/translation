package kr.co.easymanual.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.easymanual.solr.Handler;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchTerminologyManager {
	private static final Logger logger = LoggerFactory.getLogger(SearchTerminologyManager.class);

	@Autowired
	private Map<String, Handler> handler;

	public Map<String, List<SolrDocument>> searchTerminology(String query, String charset, String h) throws SolrServerException, IOException {
		Handler handler = this.handler.get(h);
		Map<String, List<SolrDocument>> map = new HashMap<String, List<SolrDocument>>();

		if(handler == null) {
			return map;
		}

		return handler.getSolrResponse(handler.getSolrQuery(query, charset));
	}
}