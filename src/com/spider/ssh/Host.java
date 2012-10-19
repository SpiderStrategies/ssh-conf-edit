package com.spider.ssh;

import java.util.LinkedHashMap;
import java.util.Map;

public class Host {
	
	String name;
	Map<String, String> properties = new LinkedHashMap<String, String>();
	
	public Host() {}
	
	public Host(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name of this host
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the host
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the properties of this Host in insertion order
	 * @return
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
	
	
}
