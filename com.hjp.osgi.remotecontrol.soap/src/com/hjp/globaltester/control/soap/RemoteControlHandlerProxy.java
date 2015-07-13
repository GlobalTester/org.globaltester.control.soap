package com.hjp.globaltester.control.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.hjp.globaltester.control.RemoteControlHandler;

@WebService
@SOAPBinding(style = Style.RPC)
public class RemoteControlHandlerProxy extends AbstractProxy<RemoteControlHandler> implements RemoteControlHandler{

	public RemoteControlHandlerProxy(RemoteControlHandler handler) {
		super(handler);
	}

	@Override
	@WebMethod
	public String getIdentifier() {
		return handler.getIdentifier();
	}

	@Override
	@WebMethod
	public String getType() {
		return handler.getType();
	}

}
