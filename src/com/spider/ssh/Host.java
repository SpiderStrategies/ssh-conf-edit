package com.spider.ssh;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a single host and its properties from an SSH Config File
 * @author kolz
 *
 */
public class Host {
	
	String name;
	Map<String, String> properties = new LinkedHashMap<String, String>();
	
	/**
	 * Creates a Host
	 */
	public Host() {}
	
	/**
	 * Creates a Host with a <code>name</code>.
	 * @param name
	 */
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
