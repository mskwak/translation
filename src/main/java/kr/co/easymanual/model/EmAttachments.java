package kr.co.easymanual.model;

import java.util.Date;

import lombok.Data;

@Data
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
}