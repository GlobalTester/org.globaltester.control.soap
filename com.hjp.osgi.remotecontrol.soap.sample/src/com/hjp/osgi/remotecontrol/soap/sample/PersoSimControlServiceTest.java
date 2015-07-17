package com.hjp.osgi.remotecontrol.soap.sample;

import java.net.MalformedURLException;
import java.net.URL;

import net.java.dev.jaxb.array.StringArray;

import com.hjp.globaltester.control.soap.SimulatorControlSoapProxy;
import com.hjp.globaltester.control.soap.SimulatorControlSoapProxyService;
import com.hjp.globaltester.control.soap.SoapServiceProvider;
import com.hjp.globaltester.control.soap.SoapServiceProviderService;
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
public class PersoSimControlServiceTest {
	private static SoapServiceProviderService service = new SoapServiceProviderService();

	/**
	 * @param args
	 *            the command line arguments
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		SoapServiceProvider port = service.getSoapServiceProviderPort();
		for (String string : port.getAvailableHandlers().getItem()) {
			System.out.println("Handler: " + string);
			if (string.equals("persoSimControlService")) {
				useSimulator();
			}
		}
	}

	private static void useSimulator() throws MalformedURLException {
		URL wsdlLocation = new URL ("http://localhost:8888/globaltester/persoSimControlService?wsdl");
		SimulatorControlSoapProxyService service = new SimulatorControlSoapProxyService(wsdlLocation);
		
		SimulatorControlSoapProxy port = service
				.getSimulatorControlSoapProxyPort();
		System.out
				.println("Using simulator SOAP webservice:\n\n----------------------------\n");

		String config = "notExistingConfig";
		System.out.println("Try to load configuration: " + config);
		printStatusAndGetError(port.loadConfiguration(config), port);
		
		config = "C:/develop/GitHub/GT2/de.persosim.simulator/de.persosim.simulator/personalization/profiles/Profile07.xml";
		System.out.println("Try to load configuration: " + config);
		printStatusAndGetError(port.loadConfiguration(config), port);
		
		config = "01";
		System.out.println("Try to load configuration: " + config);
		printStatusAndGetError(port.loadConfiguration(config), port);
		
		StringArray params = new StringArray();
		params.getItem().add("7F218201B67F4E82016E5F290100420E44455445535665494430303030347F4982011D060A04007F000702020202038120A9FB57DBA1EEA9BC3E660A909D838D726E3BF623D52620282013481D1F6E537782207D5A0975FC2C3057EEF67530417AFFE7FB8055C126DC5C6CE94A4B44F330B5D9832026DC5C6CE94A4B44F330B5D9BBD77CBF958416295CF7E1CE6BCCDC18FF8C07B68441048BD2AEB9CB7E57CB2C4B482FFC81B7AFB9DE27E1E3BD23C23A4453BD9ACE3262547EF835C3DAC4FD97F8461A14611DC9C27745132DED8E545C1D54C72F0469978520A9FB57DBA1EEA9BC3E660A909D838D718C397AA3B561A6F7901E0E82974856A786410474FF63AB838C73C303AC003DFEE95CF8BF55F91E8FEBCB7395D942036E47CF1845EC786EC95BB453AAC288AD023B6067913CF9B63F908F49304E5CFC8B3050DD8701015F200E44455445535465494430303030347F4C12060904007F0007030102025305FC0F13FFFF5F25060102000501015F24060105000501015F37408CAC3E842EB053EE10E9D57FB373FF4E9C36D1EDF966D6535978D498309B00D59C51D83965F4B1C75557FA6B6CA03D360A782B9BC172CE391623D6BB48B9B1AA");
		params.getItem().add("7F218201B67F4E82016E5F290100420E44455445535465494430303030347F4982011D060A04007F000702020202038120A9FB57DBA1EEA9BC3E660A909D838D726E3BF623D52620282013481D1F6E537782207D5A0975FC2C3057EEF67530417AFFE7FB8055C126DC5C6CE94A4B44F330B5D9832026DC5C6CE94A4B44F330B5D9BBD77CBF958416295CF7E1CE6BCCDC18FF8C07B68441048BD2AEB9CB7E57CB2C4B482FFC81B7AFB9DE27E1E3BD23C23A4453BD9ACE3262547EF835C3DAC4FD97F8461A14611DC9C27745132DED8E545C1D54C72F0469978520A9FB57DBA1EEA9BC3E660A909D838D718C397AA3B561A6F7901E0E82974856A786410474FF63AB838C73C303AC003DFEE95CF8BF55F91E8FEBCB7395D942036E47CF1845EC786EC95BB453AAC288AD023B6067913CF9B63F908F49304E5CFC8B3050DD8701015F200E44455445535465494430303030347F4C12060904007F0007030102025305FC0F13FFFF5F25060102000501015F24060105000501015F37408CAC3E842EB053EE10E9D57FB373FF4E9C36D1EDF966D6535978D498309B00D59C51D83965F4B1C75557FA6B6CA03D360A782B9BC172CE391623D6BB48B9B1AA");
//		params.getItem().add("7F218201B67F4E82016E5F290100420E44455445535465494430303030347F4982011D");
		System.out.println("Change the state: ");
		printStringArray(params);
		printStatusAndGetError(port.updateState("TrustPoint", params), port);
		
		params = new StringArray();
		params.getItem().add("20140504");
		params.getItem().add("20140604");
		System.out.println("Change the state: ");
		printStringArray(params);
		printStatusAndGetError(port.updateState("Date", params), port);

		System.out.println("Read the state: ");
		printStringArray(port.getState("TrustPoint", new StringArray()));
		System.out.println("\n");

		System.out.println("Read non existing state: ");
		printStringArray(port.getState("notThere", new StringArray()));
		System.out.println("\n");

		System.out.println("Start the simulator: ");
		printStatusAndGetError(port.startSimulator(), port);

		System.out.println("Stop the simulator:");
		printStatusAndGetError(port.stopSimulator(), port);

//		System.out.println("Stop the simulator again:");
//		printStatusAndGetError(port.stopSimulator(), port);
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