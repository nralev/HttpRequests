package com.best.httprequests;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
	
	
	private List<AddressComponent> address_components;
	private String formatted_address;
	
	public List<AddressComponent> getAddress_components() {
		return address_components;
	}
	public void setAddress_components(List<AddressComponent> address_components) {
		this.address_components = address_components;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
}
