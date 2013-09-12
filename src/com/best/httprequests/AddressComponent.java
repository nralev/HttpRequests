package com.best.httprequests;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressComponent {
	private String long_name;
	private String short_name;

	public String getLong_name() {
		return long_name;
	}
	
	public void setLong_name(String long_name) {
		this.long_name = long_name;
	}
	
	public String getShort_name() {
		return short_name;
	}
	
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
}
