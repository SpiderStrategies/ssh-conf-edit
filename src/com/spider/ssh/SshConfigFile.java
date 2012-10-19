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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a SSH Config file with hosts where each host contains a set of properties
 * @author kolz
 *
 */
public class SshConfigFile {

	protected File source;
	protected List<Host> hosts = new ArrayList<Host>();
	protected String lineTerminator = "\n";
	
	protected static final String HOST_FORMAT = 		"Host %s"; 
	protected static final String PROPERTY_FORMAT = 	"	%s %s"; 
	
	/**
	 * Reads a configuration file
	 * @param source
	 * @throws IOException
	 */
	public SshConfigFile(File source) throws IOException {
		this.source = source;
		parse(new FileInputStream(source));
	}
	
	/**
	 * For testing
	 */
	protected SshConfigFile() {
		
	}
	
	/**
	 * Parses the input stream which contains the SSH Config file, adding the data
	 * to the current object.
	 * @param input
	 * @throws IOException
	 */
	protected void parse(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		Host current = null;
		while((line = reader.readLine()) != null) {
			line = line.trim();
			if(StringUtils.isBlank(line)) {
				continue;
			}
			
			if(line.startsWith("Host ")) {
				current = new Host(line.split("\\s")[1]);
				hosts.add(current);
			}
			else {
				// this is a property
				if(current == null) {
					// don't even try to process the property if we don't have a current Host
					continue;
				}
				String[] vals = splitProperty(line);
				current.getProperties().put(vals[0], vals[1]);
			}
		}
	}
	
	/**
	 * Saves Host objects to disk at the original location.
	 * @throws IOException
	 */
	public void save() throws IOException {
		FileWriter writer = new FileWriter(source);
		
		save(writer);
		
		writer.flush();
		writer.close();
	}
	
	/**
	 * Writes the Host objects to the writer opened by the save() method.
	 * @param writer
	 * @throws IOException
	 */
	protected void save(Writer writer) throws IOException {
		for(Host h : hosts) {
			writer.write(String.format(HOST_FORMAT, h.getName()) + lineTerminator);
			for(Entry<String, String> prop : h.getProperties().entrySet()) {
				writer.write(formatProperty(prop.getKey(), prop.getValue()) + lineTerminator);
			}
		}
	}
	
	/**
	 * Formats a name value pair for insertion into the Config file stream
	 * @param name The name of the property
	 * @param value The value of the property
	 * @return
	 */
	protected String formatProperty(String name, String value) {
		if(value == null) {
			value = "";
		}
		return String.format(PROPERTY_FORMAT, name, value);
	}
	
	/**
	 * Splits a property line from the source file into two pieces, a name and a value.
	 * This method is space preserving so that any whitespace in the value is kept
	 * as it was in the source file.
	 * @param s The property line from the config file
	 * @return
	 */
	protected String[] splitProperty(String s) {
		s = s.trim();
		String[] result = new String[2];
		int location = -1;
		
		char[] data = s.toCharArray();
		for(int i = 0; i < data.length; i++) {
			if(Character.isWhitespace(data[i])) {
				location = i;
				break;
			}
		}
		
		if(location > 0) {
			result[0] = s.substring(0, location);
			result[1] = s.substring(location + 1).trim();
			if(StringUtils.isBlank(result[1])) {
				result[1] = null;
			}
		}
		else {
			result[0] = s;
		}
		
		return result;
	}

	/**
	 * Gets the hosts in the config file
	 * @return
	 */
	public List<Host> getHosts() {
		return hosts;
	}

	
}
