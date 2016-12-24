package kr.co.easymanual.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(value = "tbx")
public class TbxSearchHandler implements Handler {
	private final static Logger logger = LoggerFactory.getLogger(TbxSearchHandler.class);

	@Autowired
	@Qualifier("solrServer")
	private HttpSolrClient httpSolrClient;

	@Override
	public SolrQuery getSolrQuery(String query) {
		SolrQuery solrQuery =  new SolrQuery();
		solrQuery.setQuery("{!join from=termEntryId to=termEntryId}term:" + query);
		solrQuery.setParam("group", true);
		solrQuery.setParam("group.field", "termEntryId");
		solrQuery.setParam("group.offset", "0");
		solrQuery.setParam("group.limit", "200");

		//solrQuery.setFilterQueries("langSet", "ko-kr");
		//solrQuery.setSort(properties, ORDER.desc);
		//solrQuery.setStart(pageable.getOffset());
		//solrQuery.setRows(pageable.getPageSize());

// solrQuery.setQuery("{!join from=termEntryId to=termEntryId}term:" + query); 로 인해 하이라이트 기능이 동작하지 않음. 왜 그럴까?
//		solrQuery.setParam("hl:fl", "term descrip");
//		solrQuery.setHighlight(true);
//		solrQuery.setHighlight(true).setHighlightSnippets(1);
//		solrQuery.setHighlightFragsize(300);
//		solrQuery.setHighlightSimplePre("<strong>");
//		solrQuery.setHighlightSimplePost("</strong>");

		return solrQuery;
	}

	@Override
	public Map<String, List<SolrDocument>> getSolrResponse(SolrQuery solrQuery) throws SolrServerException, IOException {
		Map<String, List<SolrDocument>> map = new HashMap<String, List<SolrDocument>>();

        	QueryResponse queryResponse = this.httpSolrClient.query(solrQuery);
			GroupResponse groupResponse = queryResponse.getGroupResponse();

//			Map<String, Map<String, List<String>>> xxx = queryResponse.getHighlighting();
//
//			if(xxx == null) {
//				System.out.println("qqqqqqqqqqqqqqqqqqqqq");
//				System.out.println("qqqqqqqqqqqqqqqqqqqqq");
//				System.out.println("qqqqqqqqqqqqqqqqqqqqq");
//			} else {
//				System.out.println("wwwwwwwwwwwwwwwwwwwww: " + xxx.toString());
//				System.out.println("wwwwwwwwwwwwwwwwwwwww: " + xxx.toString());
//				System.out.println("wwwwwwwwwwwwwwwwwwwww: " + xxx.toString());
//			}

			List<GroupCommand> groupCommandList = groupResponse.getValues();

			for(GroupCommand groupCommand: groupCommandList) {
				// groupName: termEntryId
				// String groupName = groupCommand.getName();
				List<Group> groupList = groupCommand.getValues();

				for(Group group: groupList) {
					String termEntryId = group.getGroupValue();
					SolrDocumentList solrDocumentList = group.getResult();
					List<SolrDocument> list = new ArrayList<SolrDocument>();
					Set<String> tmpSet = new HashSet<String>();

					for(SolrDocument solrDocument: solrDocumentList) {
						String langSet = (String) solrDocument.getFieldValue("langSet");

						if(!tmpSet.contains(langSet)) {
							tmpSet.add(langSet);
							list.add(solrDocument);

							StringBuilder stringBuilder = new StringBuilder();
							stringBuilder.append("TID:");
							stringBuilder.append(termEntryId);
							stringBuilder.append(" ");
							stringBuilder.append("CS:");
							stringBuilder.append(langSet);
							stringBuilder.append(" ");
							stringBuilder.append("DO:");
							stringBuilder.append(solrDocument.toString());
							stringBuilder.append(" ");
							stringBuilder.append("DS:deduplication");

							logger.debug(stringBuilder.toString());
						}
					}

					map.put(termEntryId, list);
				}
			}

        return map;
	}
}