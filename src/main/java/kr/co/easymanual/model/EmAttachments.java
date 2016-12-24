package kr.co.easymanual.model;

import java.util.Date;

public class EmAttachments {
    private Integer id;
    private String langset;
    private String country;
    private String name;
    private String hashName;
    private String path;
    private String extension;
    private Long size;
    private String uploader;
    private Date createdTime;
    private Date updatedTime;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLangset() {
		return this.langset;
	}

	public void setLangset(String langset) {
		this.langset = langset;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHashName() {
		return this.hashName;
	}

	public void setHashName(String hashName) {
		this.hashName = hashName;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUploader() {
		return this.uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "EmAttachments [id=" + this.id + ", langset=" + this.langset
				+ ", country=" + this.country + ", name=" + this.name + ", hashName="
				+ this.hashName + ", path=" + this.path + ", extension=" + this.extension
				+ ", size=" + this.size + ", uploader=" + this.uploader
				+ ", createdTime=" + this.createdTime + ", updatedTime="
				+ this.updatedTime + "]";
	}
}