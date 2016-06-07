package org.globaltester.control.soap.sample;

import java.net.MalformedURLException;
import java.net.URL;

import org.globaltester.control.soap.SimulatorControlSoapProxy;
import org.globaltester.control.soap.SimulatorControlSoapProxyService;
import org.globaltester.control.soap.SoapServiceProvider;
import org.globaltester.control.soap.SoapServiceProviderService;

import net.java.dev.jaxb.array.StringArray;

import com.hjp.simulator.SimulatorControl;

/**
 * This sample client is based on JAX-WS generated code using wsimport. The root
 * directory of this project contains an import script that can be executed when
 * wsimport is on the PATH. It generates the needed classes for access and
 * stores them in the gen/ folder. Although this sample code works for all
 * correct implementations of SOAP proxies for the {@link SimulatorControl}
 * interface, it makes some assumptions on the behavior for demonstrational
 * purposes. This behavior is implemented by the {@link SampleSoapService} class
 * in this bundle.
 * 
 * @author mboonk
 *
 */
public class SampleSoapClient {
	private static SoapServiceProviderService service = new SoapServiceProviderService();

	/**
	 * @param args
	 *            the command line arguments
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		SoapServiceProvider port = service.getSoapServiceProviderPort();
		System.out.println("Control handler availiable, check for others");
		for (String string : port.getAvailableHandlers().getItem()) {
			System.out.println("Handler: " + string);
			if (string.equals("SimulatorControl")) {
				useSimulator(new URL("http://localhost:8888/globaltester/" + string));
			}
		}
	}

	private static void useSimulator(URL wsdlLocation) {
		SimulatorControlSoapProxyService service = new SimulatorControlSoapProxyService(wsdlLocation);
		SimulatorControlSoapProxy port = service
				.getSimulatorControlSoapProxyPort();
		System.out
				.println("Using simulator SOAP webservice:\n\n----------------------------\n");

		String config = "notExistingConfig";
		System.out.println("Try to load configuration: " + config);
		printStatusAndGetError(port.loadConfiguration(config), port);

		config = "gt_default_nPA";
		System.out.println("Try to load configuration: " + config);
		printStatusAndGetError(port.loadConfiguration(config), port);

		StringArray params = new StringArray();
		params.getItem().add("param");
		params.getItem().add("param2");
		System.out.println("Change the state: ");
		printStringArray(params);
		printStatusAndGetError(port.updateState("testString", params), port);

		System.out.println("Read the state: ");
		printStringArray(port.getState("testString", new StringArray()));
		System.out.println("\n");

		System.out.println("Read non existing state: ");
		printStringArray(port.getState("notThere", new StringArray()));
		System.out.println("\n");

		System.out.println("Start the simulator: ");
		printStatusAndGetError(port.startSimulator(), port);

		System.out.println("Stop the simulator:");
		printStatusAndGetError(port.stopSimulator(), port);

		System.out.println("Stop the simulator again:");
		printStatusAndGetError(port.stopSimulator(), port);
	}

	/**
	 * This prints the given result {@link boolean}
	 * @param result
	 * @param port
	 */
	private static void printStatusAndGetError(boolean result,
			SimulatorControlSoapProxy port) {
		System.out.println(result ? "Success" : "Failure");
		if (!result) {
			System.out.println("\tError message: " + port.getError());
		}
		System.out.println();
		System.out.println();
	}

	private static void printStringArray(StringArray array) {
		System.out.print('[');
		for (String string : array.getItem()) {
			System.out.println("\t\"" + string + "\"");
		}
		System.out.println("]");
	}
}
