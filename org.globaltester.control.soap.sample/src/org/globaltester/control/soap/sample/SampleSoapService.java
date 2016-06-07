package org.globaltester.control.soap.sample;

import java.util.HashMap;

import org.globaltester.control.AbstractRemoteControlHandler;

import com.hjp.simulator.SimulatorControl;

/**
 * This implements a dummy control service that can be remotely published via
 * SOAP and is used together with the {@link SampleSoapClient} application to
 * demonstrate usage of the SOAP interface implementation.
 * 
 * @author mboonk
 *
 */
public class SampleSoapService extends AbstractRemoteControlHandler implements
		SimulatorControl {
	private String error = "";
	private boolean configurationLoaded = false;
	private boolean simulatorRunning = false;
	private HashMap<String, String[]> state = new HashMap<>();

	@Override
	public String getIdentifier() {
		return "simulatorSampleService";
	}

	@Override
	public boolean startSimulator() {
		if (!error.isEmpty()) {
			return false;
		}
		if (!configurationLoaded) {
			error = "A configuration needs to be loaded before starting the simulator";
			reset();
			return false;
		}
		simulatorRunning = true;
		return true;
	}

	@Override
	public boolean stopSimulator() {
		if (!error.isEmpty()) {
			return false;
		}
		if (!simulatorRunning) {
			error = "The simulator needs to be running before stopping it";
			reset();
			return false;
		}
		if (!configurationLoaded) {
			error = "A configuration needs to be loaded before starting the simulator";
			reset();
			return false;
		}
		simulatorRunning = false;
		return true;
	}

	@Override
	public boolean loadConfiguration(String configurationIdentifier) {
		if (simulatorRunning) {
			error = "The simulator can not be running when a configuration should be loaded";
			reset();
			return false;
		}
		error = "";
		if (configurationIdentifier.equals("sampleConfig")) {
			configurationLoaded = true;
			return true;
		}
		reset();
		return false;
	}

	@Override
	public boolean updateState(String updateType, String[] updateParameters) {
		if (!error.isEmpty()) {
			return false;
		}
		if (simulatorRunning) {
			error = "The state can not be updated while the simulator is running";
			reset();
			return false;
		}
		if (!configurationLoaded) {
			error = "A configuration needs to be loaded before updating the state";
			reset();
			return false;
		}
		state.put(updateType, updateParameters);
		return true;
	}

	@Override
	public String[] getState(String stateType, String[] stateParameters) {
		if (!error.isEmpty()) {
			return new String[0];
		}
		if (!configurationLoaded) {
			error = "A configuration needs to be loaded before reading the state";
			reset();
			return new String[0];
		}
		if (stateParameters.length > 0) {
			error = "State parameters are not supported";
			reset();
			return new String[0];
		}
		return state.get(stateType) != null ? state.get(stateType) : new String [0];
	}

	@Override
	public String getError() {
		return error;
	}

	private void reset() {
		state = new HashMap<>();
		configurationLoaded = false;
		simulatorRunning = false;
	}

}
