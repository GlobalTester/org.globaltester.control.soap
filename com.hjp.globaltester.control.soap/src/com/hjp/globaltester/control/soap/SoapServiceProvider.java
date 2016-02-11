package com.hjp.globaltester.control.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public class SoapServiceProvider {
	
	private SoapServiceProviderData data;

	public SoapServiceProvider(SoapServiceProviderData data) {
		this.data = data;
	}
	
	@WebMethod
	public String[] getAvailableHandlers() {
		return data.getHandlerIdentifiers();
	}
}
