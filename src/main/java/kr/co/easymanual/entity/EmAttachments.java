package kr.co.easymanual.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/*
https://www.postgresql.org/docs/current/static/datatype-numeric.html#DATATYPE-SERIAL: 컬럼을 SERIAL 타입으로 정의했을 때, postgresql이 스키마 생성을 어떻게 하는지에 대한 설명이 있다.

CREATE TABLE tablename (
    colname SERIAL
);

은

CREATE SEQUENCE tablename_colname_seq;
CREATE TABLE tablename (
    colname integer NOT NULL DEFAULT nextval('tablename_colname_seq')
);
ALTER SEQUENCE tablename_colname_seq OWNED BY tablename.colname;

와 같다

1. serial 타입으로 테이블 생성하기
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

위 CREATE TABLE 은 아래의 스키마 및 em_attachments_id_seq 시퀀스를 생성한다. DROP TABLE em_attachments 수행했을 경우, 시퀀스도 동시에 함께 삭제된다.

                                                        Table "public.em_attachments"
    Column    |          Type           |                          Modifiers                          | Storage  | Stats target | Description
--------------+-------------------------+-------------------------------------------------------------+----------+--------------+-------------
 id           | integer                 | not null default nextval('em_attachments_id_seq'::regclass) | plain    |              |
 langset      | character varying(32)   |                                                             | extended |              |
 name         | character varying(1024) | not null                                                    | extended |              |
 hash_name    | character varying(64)   | not null                                                    | extended |              |
 path         | character varying(2048) | not null                                                    | extended |              |
 extension    | character varying(32)   |                                                             | extended |              |
 size         | bigint                  |                                                             | plain    |              |
 uploader     | character varying(64)   |                                                             | extended |              |
 created_time | date                    |                                                             | plain    |              |
 updated_time | date                    |                                                             | plain    |              |
Indexes:
    "em_attachments_pkey" PRIMARY KEY, btree (id)


2. @Entity를 이용하여 serial 타입으로 테이블을 생성하려면?
	2.1 아래와 같이 했을 때 DB에는 bigint로 잡힌다.
	@Id
	@GeneratedValue
	private long id;

	2.2 아래와 같이 설정 후 실행하면 테이블이 정상적으로 생성이 되나 DEFAULT nextval('tablename_colname_seq') 구문이 빠지게 된다.
	@Id
	@SequenceGenerator(name="em_attachments_id_seq", sequenceName="em_attachments_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="em_attachments_id_seq")
	@Column(nullable=false)

	2.3 아래와 같이 설정 후 실행하면 테이블이 정상적으로 생성이 되지 않는다.
	@Id
	@SequenceGenerator(name="em_attachments_id_seq", sequenceName="em_attachments_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="em_attachments_id_seq")
	@Column(nullable=false, columnDefinition="default nextval('em_attachments_id_seq')")
 */

@Entity
@Table(name="em_attachments")
@Data
public class EmAttachments {
	// serial type으로 설정하기 위한 방법
	// http://stackoverflow.com/questions/11825643/configure-jpa-to-let-postgresql-generate-the-primary-key-value
	@Id
	@SequenceGenerator(name="em_attachments_id_seq", sequenceName="em_attachments_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="em_attachments_id_seq")
	@Column(nullable=false)
	private Integer id;

	@Column(name="langset", length = 32)
	private String langSet;

	@Column(nullable=false)
	private String name;

	@Column(name="hash_name", nullable=false, length=1024)
	private String hashName;

	@Column(nullable=false, length=2048)
	private String path;

	@Column(nullable=false, length=32)
	private String extension;

	@Column
	private long size;

	@Column(length=64)
	private String uploader;

	@Column(name="created_time")
	private Date createdTime;

	@Column(name="updated_time")
	private Date updatedTime;
}
