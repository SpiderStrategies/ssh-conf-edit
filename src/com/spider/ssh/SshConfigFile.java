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
	
	public void save() throws IOException {
		FileWriter writer = new FileWriter(source);
		
		save(writer);
		
		writer.flush();
		writer.close();
	}
	
	protected void save(Writer writer) throws IOException {
		for(Host h : hosts) {
			writer.write(String.format(HOST_FORMAT, h.getName()) + lineTerminator);
			for(Entry<String, String> prop : h.getProperties().entrySet()) {
				writer.write(formatProperty(prop.getKey(), prop.getValue()) + lineTerminator);
			}
		}
	}
	
	protected String formatProperty(String name, String value) {
		if(value == null) {
			value = "";
		}
		return String.format(PROPERTY_FORMAT, name, value);
	}
	
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
