package com.base.utils;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;



public class SolrSerachVoTest  implements Serializable {
	
	@Field
	private String id;
	@Field
	private String productName;
	@Field
	private String cityName;
	@Field
	private String productDes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getProductDes() {
		return productDes;
	}
	public void setProductDes(String productDes) {
		this.productDes = productDes;
	}
}
