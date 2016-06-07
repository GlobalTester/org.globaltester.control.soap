package org.globaltester.control.soap;

import java.util.Hashtable;

import javax.xml.ws.Endpoint;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.globaltester.control.soap.preferences.PreferenceConstants;
import org.globaltester.service.GtService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * This manages the services needed to supply the SOAP {@link Endpoint}
 * @author mboonk
 *
 */
public class Activator extends AbstractUIPlugin {

	private static BundleContext context;
	private static Activator plugin;
	
	public static BundleContext getContext() {
		return context;
	}

	public static Activator getDefault() {
		return plugin;
	}

	private SoapControlEndpointManager endpointManager;
	private ServiceRegistration<GtService> gtServiceRegistration;
	
	

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.plugin = this;

		//register endpointManager as GtService
		endpointManager = new SoapControlEndpointManager();
		gtServiceRegistration = context.registerService(GtService.class, endpointManager, new Hashtable<String, String>());
		
		//handle autostart
		//FIXME refactor preferences accordingly
		boolean autostart = getPreferenceStore().getBoolean(PreferenceConstants.P_SOAP_AUTOSTART);
		if (autostart) {
			endpointManager.start();
		} 

	}

	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		gtServiceRegistration.unregister();
		context = null;
	}

}
