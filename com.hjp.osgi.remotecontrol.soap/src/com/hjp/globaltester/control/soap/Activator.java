package com.hjp.globaltester.control.soap;

import java.util.LinkedList;
import java.util.List;

import javax.xml.ws.Endpoint;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.hjp.globaltester.control.RemoteControlHandler;
import com.hjp.simulator.SimulatorControl;

/**
 * This manages the services needed to supply the SOAP {@link Endpoint}
 * @author mboonk
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;
	private Endpoint controlEndpoint;
	private List<Endpoint> additionalEndpoints;
	private SoapServiceProviderData data = new SoapServiceProviderData();
	private ServiceTracker<RemoteControlHandler, RemoteControlHandler> handlerTracker;
	private ServiceTrackerCustomizer<RemoteControlHandler, RemoteControlHandler> handlerCustomizer;

	static BundleContext getContext() {
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
		try {
			controlEndpoint = Endpoint.publish("http://localhost:8888/globaltester/control",
					new SoapServiceProvider(data));
		} catch (Exception e) {
			e.printStackTrace();
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
		//initial setup for existing services
		Object [] handlerServices = handlerTracker.getServices();
		if (handlerServices != null){
			for (Object handler : handlerServices){
				handleRemoteControl((RemoteControlHandler)handler);
			}	
		}
	}

	private void handleRemoteControl(RemoteControlHandler handlerService){
		data.addHandler(handlerService);
		Endpoint newEndpoint;
		if (handlerService instanceof SimulatorControl){
			newEndpoint = Endpoint.publish("http://localhost:8888/globaltester/" + handlerService.getIdentifier(),
					new SimulatorControlSoapProxy((SimulatorControl)handlerService));
		} else {
			newEndpoint = Endpoint.publish("http://localhost:8888/globaltester/" + handlerService.getIdentifier(),
				new RemoteControlHandlerProxy(handlerService));
		}
		additionalEndpoints.add(newEndpoint);
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
		controlEndpoint.stop();
		Activator.context = null;
	}
	
}
