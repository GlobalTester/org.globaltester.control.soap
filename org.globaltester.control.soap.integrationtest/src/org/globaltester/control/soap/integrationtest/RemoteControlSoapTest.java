package org.globaltester.control.soap.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.globaltester.PlatformHelper;
import org.globaltester.base.PreferenceHelper;
import org.globaltester.control.soap.sample.client.SampleSoapClient;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class RemoteControlSoapTest {
	
	private static URL serviceUrl;
	
	@Test
	public void testSoapSample_present() throws Exception {
		BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		
		PlatformHelper.stopBundle(org.globaltester.control.soap.Activator.class.getPackage().getName(), bc);
		PreferenceHelper.setPreferenceValue("org.globaltester.control.soap", org.globaltester.control.soap.preferences.PreferenceConstants.P_SOAP_PORT, 0 + "");
		PlatformHelper.startBundle(org.globaltester.control.soap.Activator.class.getPackage().getName(), bc);
		int controlEndpointPort = Integer.parseInt(PreferenceHelper.getPreferenceValue("org.globaltester.control.soap", org.globaltester.control.soap.preferences.PreferenceConstants.P_SOAP_PORT));
		
		PlatformHelper.startBundle(org.globaltester.control.soap.sample.service.Activator.class.getPackage().getName(), bc);
		
		serviceUrl = new URL("http://localhost:" + controlEndpointPort + "/globaltester/RemoteControl");
		
		SampleSoapClient client = new SampleSoapClient(serviceUrl);
		
		//check presence of SampleService
		assertTrue("SoapSample not available", client.getAvailableHandlers().contains("SoapSample"));

		serviceUrl = new URL("http://localhost:" + controlEndpointPort + "/globaltester/SoapSample");
		
		//call HelloWorld on the SoapSample
		String helloWorld = SampleSoapClient.soapSampleHelloWorld(serviceUrl);
		assertEquals("Hello World", helloWorld);
	}
}
