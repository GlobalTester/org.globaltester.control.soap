package org.globaltester.control.soap.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.globaltester.control.soap.Activator;


/**
 * This class stores the properties of the plugin
 * Class used to initialize default preference values
 * 
 * @version		Release 0.5.0
 * @author 		Jacob Goeke
 *
 */

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * Use this to store plugin preferences
	 * For meaning of each preference look at PreferenceConstants.java
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();

		store.setDefault(PreferenceConstants.P_SOAP_HOST, "localhost");
		store.setDefault(PreferenceConstants.P_SOAP_PORT, 8888);
		store.setDefault(PreferenceConstants.P_SOAP_AUTOSTART, false);
	}

}
