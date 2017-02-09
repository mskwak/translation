package kr.co.easymanual.model.solr;

import org.apache.solr.client.solrj.beans.Field;

import lombok.Data;

@Data
public class TbxSolrBean implements SolrBean {
	@Field
	private String id;

	@Field
	private String termEntryId;

	@Field
	private String langSet;

	@Field
	private String country;

	@Field
	private String descrip;

	@Field
	private String term;
}
