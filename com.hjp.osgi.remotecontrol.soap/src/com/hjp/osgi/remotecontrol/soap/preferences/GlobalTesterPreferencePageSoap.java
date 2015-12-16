package com.hjp.osgi.remotecontrol.soap.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hjp.globaltester.control.soap.Activator;

public class GlobalTesterPreferencePageSoap extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public GlobalTesterPreferencePageSoap() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("GlobalTester Settings for the SOAP interface");
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		Composite comp = new Composite(this.getFieldEditorParent(), SWT.NONE);
		GridData containerData = new GridData(GridData.FILL, GridData.FILL, true, false);
		containerData.horizontalSpan = 1;

		comp.setLayoutData(containerData);
		GridLayout layout = new GridLayout(1, false);
		comp.setLayout(layout);
		Group soapInterface = new Group(comp, SWT.NONE);

		GridData gd5 = new GridData(GridData.FILL, GridData.FILL, true, false);
		gd5.horizontalSpan = 2;
		soapInterface.setLayoutData(gd5);
		soapInterface.setLayout(new GridLayout(0, false));

		soapInterface.setText("SOAP interface");
		StringFieldEditor soapInterfaceHost = new StringFieldEditor(PreferenceConstants.P_SOAP_HOST, "Host:",
				soapInterface){
			@Override
			protected boolean doCheckState() {
				return super.doCheckState() && !getStringValue().equals("0.0.0.0") && !getStringValue().equals("0:0:0:0:0:0:0:0") && !getStringValue().equals("::");
			}
		};
		addField(soapInterfaceHost);

		IntegerFieldEditor soapInterfacePort = new IntegerFieldEditor(PreferenceConstants.P_SOAP_PORT, "Port:",
				soapInterface);
		addField(soapInterfacePort);
		
		BooleanFieldEditor soapDeactivate = new BooleanFieldEditor(PreferenceConstants.P_SOAP_DEACTIVATED, "Deactivate SOAP", soapInterface);
		addField(soapDeactivate);
	}

}
