package org.globaltester.control.soap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import javax.xml.ws.Endpoint;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.statushandlers.StatusManager;
import org.globaltester.control.RemoteControlHandler;
import org.globaltester.control.soap.preferences.PreferenceConstants;
import org.globaltester.service.AbstractGtService;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * This manages the services needed to supply the SOAP {@link Endpoint}
 * @author mboonk
 *
 */
public class SoapControlEndpointManager extends AbstractGtService {

	private Endpoint controlEndpoint;
	private List<Endpoint> additionalEndpoints;
	private SoapServiceProviderData data = new SoapServiceProviderData();
	private ServiceTracker<RemoteControlHandler, RemoteControlHandler> handlerTracker;
	private ServiceTrackerCustomizer<RemoteControlHandler, RemoteControlHandler> handlerCustomizer;

	private String host;
	private int port;

	@Override
	public String getName() {
		return "SOAP Control";
	}

	@Override
	public boolean isRunning() {
		return controlEndpoint != null;
	}
	
	@Override
	public void start() {

		host = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_SOAP_HOST);
		port = Activator.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_SOAP_PORT);
		
		String portProperty = System.getProperty("org.globaltester.control.soap.port");
		try { 
			port = Integer.parseInt(portProperty);
		} catch (NumberFormatException e) {
			//ignore intentionally, don't use unparseable values from properties   
		}

		// warn the User if Socket is already in use
		if (!isSocketAvailable(host, port)) {
			
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					MessageDialog.openWarning(null, "Warning",
							"Socket for SOAP already in use by another service or unreachable!\n" + "Tried host " + host + " with port "
									+ port + ". Please change them in your GlobalTester preferences and restart the service.\n"
									+ "Alternatively, deactivate SOAP in the preferences to avoid this warning in the future.\n"
									+ "This is also a common issue if multiple GlobalTesters are started.");
				}
			});
			
			logSocketError();
			
		} else {
			
			controlEndpoint = Endpoint.publish("http://" + host + ":" + port + "/globaltester/RemoteControl", new RemoteControlSoap(data));

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
				RemoteControlHandler handlerService = Activator.getContext().getService(reference);
				handleRemoteControl(handlerService);
				return handlerService;
			}
		};
		
		additionalEndpoints = new LinkedList<>();
		handlerTracker = new ServiceTracker<>(Activator.getContext(), RemoteControlHandler.class, handlerCustomizer);
		handlerTracker.open();
		

		//notify GtServiceListeners of new status
		this.notifyStatusChange(true);
	}
	
	@Override
	public void stop() {
		handlerTracker.close();
		
		if(controlEndpoint != null){
			controlEndpoint.stop();
			controlEndpoint = null;
		}

		for (Endpoint endpoint : additionalEndpoints){
			endpoint.stop();
		}
		additionalEndpoints.clear();

		//notify GtServiceListeners of new status
		this.notifyStatusChange(false);
	}

	private void handleRemoteControl(RemoteControlHandler handlerService){
		data.addHandler(handlerService);
		Endpoint newEndpoint;
		try{
			JaxWsSoapAdapter handlerAdapter = handlerService.getAdapter(JaxWsSoapAdapter.class);
			if (handlerAdapter == null){
				return;
			}
			
			newEndpoint = Endpoint.publish("http://" + host + ":" + port + "/globaltester/" + handlerService.getIdentifier(),
						handlerAdapter);
			additionalEndpoints.add(newEndpoint);
		} catch (Exception e) {
			logSocketError();
		}
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
		InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
		if (inetSocketAddress.getAddress() != null && (inetSocketAddress.getAddress().isAnyLocalAddress() || inetSocketAddress.getAddress().isLoopbackAddress())){
			try{
			ServerSocket socketTester = new ServerSocket();
			socketTester.setSoTimeout(180);
			socketTester.bind(inetSocketAddress);
			socketTester.close();
			return true;
			} catch (IOException e) {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Adds an error (about Socket for SOAP already in use) to the eclipse
	 * logging view
	 */
	private void logSocketError() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				IStatus status = new Status(IStatus.ERROR, getClass().getPackage().getName(),
						"Socket for SOAP is already in use by another Service");
				StatusManager.getManager().handle(status, StatusManager.LOG);
			}
		});
	}
}
