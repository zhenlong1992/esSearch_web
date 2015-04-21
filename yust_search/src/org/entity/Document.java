package org.entity;


public class Document {

    private Integer id;
    private String url;
    private String content;
    
    public Document() {
        super();
    }

    public Document(Integer id, String url, String content) {
        super();
        this.id = id;
        this.url = url;
        this.content = content;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
    //getter and  setter ()
    
}