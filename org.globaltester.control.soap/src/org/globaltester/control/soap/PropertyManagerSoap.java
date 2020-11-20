package org.globaltester.control.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.globaltester.control.AbstractRemoteControlHandler;

@WebService
@SOAPBinding(style = Style.RPC)
public class PropertyManagerSoap extends AbstractRemoteControlHandler implements JaxWsSoapAdapter {

	public PropertyManagerSoap() {
	}
	
	@Override
	public String getIdentifier() {
		return "PropertyManager";
	}

	@Override
	public <T> T getAdapter(Class<T> c) {
		if (c == JaxWsSoapAdapter.class) {
			return c.cast(this);
		}
		return null;
	}

    @WebMethod
    public void setProperty(String key, String value) {
        System.setProperty(key, value);
    }
}
