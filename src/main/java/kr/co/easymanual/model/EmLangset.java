package kr.co.easymanual.model;

public class EmLangset {
    private Integer id;

    private String langset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLangset() {
        return langset;
    }

    public void setLangset(String langset) {
        this.langset = langset == null ? null : langset.trim();
    }
}