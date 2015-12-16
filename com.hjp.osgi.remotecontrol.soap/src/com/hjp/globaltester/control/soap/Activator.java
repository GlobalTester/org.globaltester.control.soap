package com.hjp.globaltester.control.soap;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import javax.xml.ws.Endpoint;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.hjp.globaltester.control.RemoteControlHandler;
import com.hjp.osgi.remotecontrol.soap.preferences.PreferenceConstants;
import com.hjp.simulator.SimulatorControl;

/**
 * This manages the services needed to supply the SOAP {@link Endpoint}
 * @author mboonk
 *
 */
public class Activator extends AbstractUIPlugin {

	private static BundleContext context;
	private static Activator plugin;
	private Endpoint controlEndpoint;
	private List<Endpoint> additionalEndpoints;
	private SoapServiceProviderData data = new SoapServiceProviderData();
	private ServiceTracker<RemoteControlHandler, RemoteControlHandler> handlerTracker;
	private ServiceTrackerCustomizer<RemoteControlHandler, RemoteControlHandler> handlerCustomizer;

	private String host;
	private int port;
	private boolean soapDeactivated;
	
	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.plugin = this;
		
		host = getPreferenceStore().getString(PreferenceConstants.P_SOAP_HOST);
		port = getPreferenceStore().getInt(PreferenceConstants.P_SOAP_PORT);
		soapDeactivated = getPreferenceStore().getBoolean(PreferenceConstants.P_SOAP_DEACTIVATED); 
		
		// warn the User if Socket is already in use
		if (!isSocketAvailable(host, port) && !soapDeactivated) {
			Display display = new Display();
			Shell shell = new Shell(display);
			MessageDialog.openWarning(shell, "Warning",
					"Socket for SOAP already in use by another service!\n" + "Tried Host: " + host + " with Port "
							+ port + "Please change them in your GlobalTester preferences.\n"
							+ "This is a common issue if multiple GlobalTesters are started.");
		}
		
		try {
			controlEndpoint = Endpoint.publish("http://" + host + ":" + port + "/globaltester/control",
					new SoapServiceProvider(data));
		} catch (Exception e) {
			int errorLevel = IStatus.ERROR;
			String message = "Socket for SOAP is already in use by another Service";
			
			if(soapDeactivated){
				errorLevel = IStatus.WARNING;
				message = "Socket for SOAP is already in use by another Service, but its deactivated anyway";
			}
			
			IStatus status = new Status(errorLevel, "com.hjp.osgi.remotecontrol.soap", message, e);			
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}

		// This will be used to keep track of handlers as they are un/registering
		handlerCustomizer = new ServiceTrackerCustomizer<RemoteControlHandler, RemoteControlHandler>() {
			
			@Override
			public void removedService(
					ServiceReference<RemoteControlHandler> reference,
					RemoteControlHandler service) {
				data.removeHandler(service);
				
				Endpoint toRemove = null;
				for (Endpoint endpoint : additionalEndpoints){
					Object implementor = endpoint.getImplementor();
					if (implementor instanceof AbstractProxy<?> && service == ((AbstractProxy<?>) implementor).getHandler()){
						endpoint.stop();
						toRemove = endpoint;
						break;
					}
				}
				additionalEndpoints.remove(toRemove);
			}
			
			@Override
			public void modifiedService(
					ServiceReference<RemoteControlHandler> reference,
					RemoteControlHandler service) {
				//nothing to do
			}
			
			@Override
			public RemoteControlHandler addingService(
					ServiceReference<RemoteControlHandler> reference) {
				RemoteControlHandler handlerService = context.getService(reference);
				handleRemoteControl(handlerService);
				return handlerService;
			}
		};
		
		additionalEndpoints = new LinkedList<>();
		handlerTracker = new ServiceTracker<>(bundleContext, RemoteControlHandler.class, handlerCustomizer);
		handlerTracker.open();
	}

	public static Activator getDefault() {
		return plugin;
	}

	private void handleRemoteControl(RemoteControlHandler handlerService){
		data.addHandler(handlerService);
		Endpoint newEndpoint;
		try{
			if (handlerService instanceof SimulatorControl){
				newEndpoint = Endpoint.publish("http://" + host + ":" + port + "/globaltester/" + handlerService.getIdentifier(),
						new SimulatorControlSoapProxy((SimulatorControl)handlerService));
			} else {
				newEndpoint = Endpoint.publish("http://" + host + ":" + port + "/globaltester/" + handlerService.getIdentifier(),
					new RemoteControlHandlerProxy(handlerService));
			}
			additionalEndpoints.add(newEndpoint);
		} catch (Exception e) {
			
			int errorLevel = IStatus.ERROR;
			String message = "Socket for SOAP is already in use by another Service";
			
			if(soapDeactivated){
				errorLevel = IStatus.WARNING;
				message = "Socket for SOAP is already in use by another Service, but its deactivated anyway";
			}
			
			IStatus status = new Status(errorLevel, "com.hjp.osgi.remotecontrol.soap", message, e);			
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		handlerTracker.close();
		
		if(controlEndpoint != null){
			controlEndpoint.stop();
			controlEndpoint = null;
		}

		for (Endpoint endpoint : additionalEndpoints){
			endpoint.stop();
		}
		additionalEndpoints.clear();
		
		Activator.context = null;
	}
	
	/**
	 * This method makes a quick check if the given Socket is already in use or
	 * not.
	 * 
	 * @param host as String
	 * @param port number as int
	 * @return true if it already exists or false
	 */
	private static boolean isSocketAvailable(String host, int port) {
		try{
			Socket socketTester = new Socket(host, port);
			socketTester.close();
			return false;
		} catch (ConnectException ce) {
			//gets thrown when host and port should be ok to use
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
	}
}
