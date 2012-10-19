package com.spider.ssh;

import static junit.framework.Assert.*;

import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class SshConfigFileTest {
	
	/**
	 * Tests to ensure that the data is correctly read and written from the file
	 * @throws Exception
	 */
	@Test
	public void testParsing() throws Exception {
		SshConfigFile conf = new SshConfigFile();
		
		String original = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("com/spider/ssh/test1.conf"));
		
		conf.parse(getClass().getClassLoader().getResourceAsStream("com/spider/ssh/test1.conf"));
		
		Host host;
		
		host = conf.getHosts().get(0);
		assertEquals("test1", host.getName());
		assertEquals(3, host.getProperties().size());
		
		assertEquals("ec2-1.compute-1.amazonaws.com", host.getProperties().get("HostName"));
		assertEquals("ec2-user", host.getProperties().get("User"));
		assertEquals("~/.ssh/smctest.pem", host.getProperties().get("IdentityFile"));
		
		assertEquals(3, conf.getHosts().size());
		
		StringWriter writer = new StringWriter();
		conf.save(writer);
		assertEquals(original, writer.toString());
		
	}
	
	
	/**
	 * Ensures that properties with different values and whitespace are correctly read.
	 */
	@Test
	public void testSplitProperty() {
		SshConfigFile conf = new SshConfigFile();
		
		String[] prop = conf.splitProperty("	HostName ec2-2.compute-1.amazonaws.com");
		assertEquals("HostName", prop[0]);
		assertEquals("ec2-2.compute-1.amazonaws.com", prop[1]);
		
		prop = conf.splitProperty("	HostName	ec2-2.co mput	e-1.amazonaws.com");
		assertEquals("HostName", prop[0]);
		assertEquals("ec2-2.co mput	e-1.amazonaws.com", prop[1]);
		
		prop = conf.splitProperty("	HostName 	 ec2-2.co mput	e-1.amazonaws.com");
		assertEquals("HostName", prop[0]);
		assertEquals("ec2-2.co mput	e-1.amazonaws.com", prop[1]);
		
		prop = conf.splitProperty("	HostName 	 ");
		assertEquals("HostName", prop[0]);
		assertNull(prop[1]);


	}
	
	
	/**
	 * Ensures that name value pairs are properly formatted for writing to the output stream.
	 */
	@Test
	public void testCreatePropertyLine() {
		SshConfigFile conf = new SshConfigFile();
		assertEquals("	HostName something here", conf.formatProperty("HostName", "something here"));
		assertEquals("	HostName ", conf.formatProperty("HostName", null));
	}

}
