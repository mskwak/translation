package kr.co.easymanual.model.solr;

import org.apache.solr.client.solrj.beans.Field;

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

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTermEntryId() {
		return this.termEntryId;
	}

	public void setTermEntryId(String termEntryId) {
		this.termEntryId = termEntryId;
	}

	public String getLangSet() {
		return this.langSet;
	}

	public void setLangSet(String langSet) {
		this.langSet = langSet;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDescrip() {
		return this.descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public String getTerm() {
		return this.term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
}
