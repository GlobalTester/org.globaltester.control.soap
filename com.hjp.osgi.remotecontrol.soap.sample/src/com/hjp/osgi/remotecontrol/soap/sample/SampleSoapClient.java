package com.hjp.osgi.remotecontrol.soap.sample;

import net.java.dev.jaxb.array.StringArray;

import com.hjp.globaltester.control.soap.SimulatorControlSoapProxy;
import com.hjp.globaltester.control.soap.SimulatorControlSoapProxyService;
import com.hjp.globaltester.control.soap.SoapServiceProvider;
import com.hjp.globaltester.control.soap.SoapServiceProviderService;

public class SampleSoapClient {
    private static SoapServiceProviderService service = new SoapServiceProviderService();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	SoapServiceProvider port = service.getSoapServiceProviderPort();    	
    	for (String string : port.getAvailableHandlers().getItem()){
    		System.out.println("Handler: " + string);
    		if (string.equals("simulatorSampleService")){
    			useSimulator();
    		}
    	}
    }
    
    private static void useSimulator(){
    	SimulatorControlSoapProxyService service = new SimulatorControlSoapProxyService();
    	SimulatorControlSoapProxy port = service.getSimulatorControlSoapProxyPort();
    	System.out.println("Using simulator SOAP webservice:\n\n----------------------------\n");
    	
    	String config = "notExistingConfig";
    	System.out.println("Try to load configuration: " + config);
    	printStatusAndGetError(port.loadConfiguration(config), port);
    	
    	config = "sampleConfig";
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
    }
    
    private static void printStatusAndGetError(boolean result, SimulatorControlSoapProxy port){
    		System.out.println(result ? "Success" : "Failure");
    		if (!result){
    			System.out.println("\tError message: " + port.getError());
    		}
    		System.out.println();
    		System.out.println();
    }
    
    private static void printStringArray(StringArray array){
    	System.out.print('[');
    	for (String string: array.getItem()){
    		System.out.println("\t\"" + string + "\"");
    	}
    	System.out.println("]");
    }
}