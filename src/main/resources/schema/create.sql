ALTER TABLE em_attachments RENAME TO em_attachments_backup;

DROP TABLE em_attachments CASCADE;

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


DROP TABLE em_langset;

CREATE TABLE em_langset (
  id integer NOT NULL,
  langset character varying(16),
  CONSTRAINT id FOREIGN KEY (id)
      REFERENCES em_attachments (id)
      ON UPDATE CASCADE ON DELETE CASCADE
) WITH (OIDS=FALSE);

ALTER TABLE em_langset OWNER TO mailadm;

-- Index: em_langset_langset_idx
DROP INDEX em_langset_langset_idx;

CREATE INDEX em_langset_langset_idx
  ON em_langset
  USING btree
  (langset COLLATE pg_catalog."default");

