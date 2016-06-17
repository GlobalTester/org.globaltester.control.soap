package org.globaltester.control.soap.integrationtest;

import static org.junit.Assert.*;

import org.globaltester.PlatformHelper;
import org.globaltester.control.soap.sample.client.SampleSoapClient;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class RemoteControlSoapTest {
	
	
	@Test
	public void testSoapSample_present() throws Exception {
		
		BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		PlatformHelper.startBundle(org.globaltester.control.soap.sample.service.Activator.class.getPackage().getName(), bc);
		
		SampleSoapClient client = new SampleSoapClient();
		
		//check presence of SampleService
		assertTrue("SoapSample not available", client.getAvailableHandlers().contains("SoapSample"));

		
		//call HelloWorld on the SoapSample
		String helloWorld = client.soapSampleHelloWorld();
		assertEquals("Hello World", helloWorld);
	}
	
	@Test
	public void testSoapSample_absent() throws Exception {
		
		//ensure SoapSample bundle is absent
		FrameworkUtil.getBundle(org.globaltester.control.soap.sample.service.Activator.class).stop();
		
		SampleSoapClient client = new SampleSoapClient();
		
		//check presence of SampleService
		assertFalse("SoapSample unexpectedly available", client.getAvailableHandlers().contains("SoapSample"));
	
	}
}
