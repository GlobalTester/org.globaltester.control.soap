package org.globaltester.control.soap.sample.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.globaltester.control.soap.RemoteControlSoap;
import org.globaltester.control.soap.RemoteControlSoapService;
import org.globaltester.control.soap.sample.service.SoapSample;
import org.globaltester.control.soap.sample.service.SoapSampleService;

/**
 * This sample client is based on JAX-WS generated code using wsimport. The root
 * directory of this project contains an import script that can be executed when
 * wsimport is on the PATH. 
 * 
 * @author amay
 *
 */
public class SampleSoapClient {
	
	/**
	 * @param args
	 *            the command line arguments
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		
		SampleSoapClient client = new SampleSoapClient();
		System.out.println("RemoteControl handler reachable\n");
		
		//list all available Handlers
		System.out.println("Searching available handlers... ");
		for (String curHandler : client.getAvailableHandlers()) {
			System.out.println("Handler found: " + curHandler);
		}
		
		//call HelloWorld on the SoapSample
		System.out.println();
		System.out.println("Try calling HelloWorld from SoapSample...");
		String helloWorld = client.soapSampleHelloWorld(null);
		System.out.println(helloWorld);
	}

	RemoteControlSoap port;
	
	public SampleSoapClient() {
		RemoteControlSoapService service = new RemoteControlSoapService();
		port = service.getRemoteControlSoapPort();
	}
	
	public SampleSoapClient(URL serviceUrl) {
		RemoteControlSoapService service = new RemoteControlSoapService(serviceUrl);
		port = service.getRemoteControlSoapPort();
	}

	public Collection<String> getAvailableHandlers() {
		return port.getAvailableHandlers().getItem();
	}
	
	public String soapSampleHelloWorld(URL serviceUrl) {
		SoapSampleService soapSampleService = serviceUrl == null ? new SoapSampleService() : new SoapSampleService(serviceUrl);
		SoapSample soapSample = soapSampleService.getSoapSamplePort();
		return soapSample.helloWorld();
	}
	
}
