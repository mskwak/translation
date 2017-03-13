package kr.co.easymanual.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/*
CREATE TABLE em_attachments (
    id serial primary key,
    langset varchar(32),
    name varchar(1024) not null,
    hash_name varchar(64) not null,
    path varchar(2048) not null,
    extension varchar(32),
    size bigint,
    uploader varchar(64),
    created_time date,
    updated_time date
);
 */

@Entity
@Table(name = "em_attachments")
@Data
public class EmAttachments {
	// PostgreSQL에서 serial primary key로 생성이 되는가?
	@Id
	@GeneratedValue
	private long id;

	@Column(name = "langset", length = 32)
	private String langSet;

	@Column(nullable = false)
	private String name;

	@Column(name = "hash_name", nullable = false, length = 1024)
	private String hashName;

	@Column(nullable = false, length = 2048)
	private String path;

	@Column
	private long size;

	@Column(length = 64)
	private String uploader;

	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "updated_time")
	private Date updatedTime;
}
