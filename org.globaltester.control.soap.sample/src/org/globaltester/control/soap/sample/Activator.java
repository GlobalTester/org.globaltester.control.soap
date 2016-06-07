package org.globaltester.control.soap.sample;

import org.globaltester.control.RemoteControlHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private ServiceRegistration<RemoteControlHandler> serviceRegistration;

	@Override
	public void start(BundleContext context) throws Exception {
		RemoteControlHandler dummyService = new SampleSoapService();
		serviceRegistration = context.registerService(RemoteControlHandler.class, dummyService, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		serviceRegistration.unregister();
	}

}
