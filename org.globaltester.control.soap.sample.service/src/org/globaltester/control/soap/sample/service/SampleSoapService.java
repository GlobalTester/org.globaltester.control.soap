package org.globaltester.control.soap.sample.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.globaltester.control.AbstractRemoteControlHandler;
import org.globaltester.control.soap.JaxWsSoapAdapter;

/**
 * This implements a dummy control service that can be remotely published via
 * SOAP and is used together with the {@link SampleSoapClient} application to
 * demonstrate usage of the SOAP interface implementation.
 * 
 * @author mboonk
 *
 */
@WebService
@SOAPBinding(style = Style.RPC)
public class SampleSoapService extends AbstractRemoteControlHandler implements JaxWsSoapAdapter {
	@Override
	public String getIdentifier() {
		return "SampleService";
	}

	@WebMethod
	public String helloWorld() {
		return "Hello World";
	}

	@Override
	public <T> T getAdapter(Class<T> c) {
		if (c == JaxWsSoapAdapter.class) {
			return c.cast(this);
		}
		return null;
	}

}
