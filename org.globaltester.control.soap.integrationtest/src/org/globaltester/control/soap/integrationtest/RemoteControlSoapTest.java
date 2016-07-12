package org.globaltester.control.soap.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.globaltester.PlatformHelper;
import org.globaltester.control.soap.sample.client.SampleSoapClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class RemoteControlSoapTest {
	
	private static URL serviceUrl;

	@BeforeClass
	public static void setUpSuite() throws Exception {
		String wsdlLocation = "http://localhost:8888/globaltester/RemoteControl?wsdl";
		
		serviceUrl = new URL(adjustPortInWsdlLocation(wsdlLocation));
	}

	private static String adjustPortInWsdlLocation(String wsdlLocation) {
		String portProperty = System.getProperty("org.globaltester.control.soap.port");
		try { 
			Integer.parseInt(portProperty);
			wsdlLocation = wsdlLocation.replaceAll("8888", portProperty);
		} catch (NumberFormatException e) {
			//ignore intentionally, don't use unparseable values from properties   
		}
		return wsdlLocation;
	}
	
	@Test
	public void testSoapSample_present() throws Exception {
		
		BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		PlatformHelper.startBundle(org.globaltester.control.soap.sample.service.Activator.class.getPackage().getName(), bc);
		
		SampleSoapClient client = new SampleSoapClient(serviceUrl);
		
		//check presence of SampleService
		assertTrue("SoapSample not available", client.getAvailableHandlers().contains("SoapSample"));

		
		//call HelloWorld on the SoapSample
		String wsdlLocation = "http://localhost:8888/globaltester/SoapSample?wsdl";
		String helloWorld = client.soapSampleHelloWorld(new URL(adjustPortInWsdlLocation(wsdlLocation)));
		assertEquals("Hello World", helloWorld);
	}
	
	@Test
	public void testSoapSample_absent() throws Exception {
		
		//ensure SoapSample bundle is absent
		FrameworkUtil.getBundle(org.globaltester.control.soap.sample.service.Activator.class).stop();
		
		SampleSoapClient client = new SampleSoapClient(serviceUrl);
		
		//check presence of SampleService
		assertFalse("SoapSample unexpectedly available", client.getAvailableHandlers().contains("SoapSample"));
	
	}
}
