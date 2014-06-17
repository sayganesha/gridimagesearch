package com.gsangle.example.searchimage;

import java.io.Serializable;

public class SearchFilters implements Serializable {
	
	public String getColor() {
		return color;
	}
	public String getType() {
		return type;
	}
	public String getSize() {
		return size;
	}
	public String getSite() {
		return site;
	}
	private static final long serialVersionUID = -1795563480576875102L;
	private String color = "";
	private String type = "";
	private String size = "";
	private String site = "";
	public void setColor(String color) {
		this.color = color;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public void setSite(String site) {
		this.site = site;
	}
   
}
