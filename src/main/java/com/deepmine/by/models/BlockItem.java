package com.deepmine.by.models;

public class BlockItem {
	
    public String title = "";
    public String id = "0";
    public String bg = "";
    public String url = "";
    
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage() {
		return bg;
	}
	public void setImage(String image) {
		this.bg = image;
	}

    
}
