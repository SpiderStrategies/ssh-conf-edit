/*
*   Copyright 2012 Spider Strategies, Inc.
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
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
