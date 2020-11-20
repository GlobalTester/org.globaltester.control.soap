package org.globaltester.control.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.globaltester.base.PreferenceHelper;
import org.globaltester.control.AbstractRemoteControlHandler;

@WebService
@SOAPBinding(style = Style.RPC)
public class PreferenceManagerSoap extends AbstractRemoteControlHandler implements JaxWsSoapAdapter {

	public PreferenceManagerSoap() {
	}
	
	@Override
	public String getIdentifier() {
		return "PreferenceManager";
	}

	@Override
	public <T> T getAdapter(Class<T> c) {
		if (c == JaxWsSoapAdapter.class) {
			return c.cast(this);
		}
		return null;
	}
	
    @WebMethod
    public void setPreferenceValue(String bundle, String key, String value) {
    	PreferenceHelper.setPreferenceValue(bundle, key, value);
    }
	
    @WebMethod
    public String getPreferenceValue(String bundle, String key) {
    	return PreferenceHelper.getPreferenceValue(bundle, key);
    }
}
