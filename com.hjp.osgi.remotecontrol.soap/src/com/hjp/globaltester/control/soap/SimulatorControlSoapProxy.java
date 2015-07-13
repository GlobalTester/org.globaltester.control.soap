package com.hjp.globaltester.control.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.hjp.simulator.SimulatorControl;

/**
 * SOAP proxy for {@link SimulatorControl} objects.
 * @author mboonk
 *
 */
@WebService
@SOAPBinding(style = Style.RPC)
public class SimulatorControlSoapProxy extends AbstractProxy<SimulatorControl> implements SimulatorControl {
	
	public SimulatorControlSoapProxy(SimulatorControl handler) {
		super(handler);
	}
	
	@Override
	@WebMethod
	public boolean startSimulator() {
		return handler.startSimulator();
	}

	@Override
	@WebMethod
	public boolean stopSimulator() {
		return handler.stopSimulator();
	}

	@Override
	@WebMethod
	public boolean loadConfiguration(String configurationIdentifier) {
		return handler.loadConfiguration(configurationIdentifier);
	}

	@Override
	@WebMethod
	public boolean updateState(String updateType, String[] updateParameters) {
		return handler.updateState(updateType, updateParameters);
	}

	@Override
	@WebMethod
	public String[] getState(String stateType, String[] stateParameters) {
		return handler.getState(stateType, stateParameters);
	}

	@Override
	@WebMethod
	public String getError() {
		return handler.getError();
	}

}
