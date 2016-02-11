package com.hjp.globaltester.control.soap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.hjp.globaltester.control.RemoteControlHandler;

public class SoapServiceProviderData {

	private HashSet<RemoteControlHandler> handlers = new HashSet<>();

	public String[] getHandlerIdentifiers() {

		List<String> result = new LinkedList<>();
		
		for (RemoteControlHandler handler : handlers){
			result.add(handler.getIdentifier());
		}
		return result.toArray(new String[0]);
	}

	public void addHandler(RemoteControlHandler handler) {
		handlers.add(handler);
	}

	public void removeHandler(RemoteControlHandler handler) {
		handlers.remove(handler);
	}
}
