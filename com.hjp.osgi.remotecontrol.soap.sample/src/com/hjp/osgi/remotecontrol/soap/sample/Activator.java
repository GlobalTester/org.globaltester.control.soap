package com.hjp.osgi.remotecontrol.soap.sample;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.hjp.globaltester.control.RemoteControlHandler;

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
