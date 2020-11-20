package org.globaltester.control.soap;

import java.util.Hashtable;

import javax.xml.ws.Endpoint;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.globaltester.control.RemoteControlHandler;
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
	private PreferenceManagerSoap preferenceManager;
	private PropertyManagerSoap propertyManager;
	private WorkspaceManagerSoap workspaceManager;
	private ServiceRegistration<GtService> gtServiceRegistration;
	private ServiceRegistration<RemoteControlHandler> preferenceManagerRegistration;
	private ServiceRegistration<RemoteControlHandler> propertyManagerRegistration;
	private ServiceRegistration<RemoteControlHandler> workspaceManagerRegistration;
	
	

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.plugin = this;

		//register endpointManager as GtService
		endpointManager = new SoapControlEndpointManager();
		gtServiceRegistration = context.registerService(GtService.class, endpointManager, new Hashtable<String, String>());
		
		preferenceManager = new PreferenceManagerSoap();
		preferenceManagerRegistration = context.registerService(RemoteControlHandler.class, preferenceManager, new Hashtable<String, String>());

		propertyManager = new PropertyManagerSoap();
		propertyManagerRegistration = context.registerService(RemoteControlHandler.class, propertyManager, new Hashtable<String, String>());

		workspaceManager = new WorkspaceManagerSoap();
		workspaceManagerRegistration = context.registerService(RemoteControlHandler.class, workspaceManager, new Hashtable<String, String>());
		
		//handle autostart
		boolean autostart = getPreferenceStore().getBoolean(PreferenceConstants.P_SOAP_AUTOSTART);
		if (autostart) {
			endpointManager.start();
		} 

	}

	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (endpointManager != null && endpointManager.isRunning()){
			endpointManager.stop();	
		}
		if (gtServiceRegistration != null){
			gtServiceRegistration.unregister();	
		}
		if (preferenceManagerRegistration != null){
			preferenceManagerRegistration.unregister();	
		}
		if (propertyManagerRegistration != null){
			propertyManagerRegistration.unregister();	
		}
		if (workspaceManagerRegistration != null){
			workspaceManagerRegistration.unregister();	
		}
		context = null;
	}

}
