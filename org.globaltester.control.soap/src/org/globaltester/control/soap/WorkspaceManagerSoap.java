package org.globaltester.control.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.globaltester.control.AbstractRemoteControlHandler;

@WebService
@SOAPBinding(style = Style.RPC)
public class WorkspaceManagerSoap extends AbstractRemoteControlHandler implements JaxWsSoapAdapter {

	public WorkspaceManagerSoap() {
	}
	
	@Override
	public String getIdentifier() {
		return "WorkspaceManager";
	}

	@Override
	public <T> T getAdapter(Class<T> c) {
		if (c == JaxWsSoapAdapter.class) {
			return c.cast(this);
		}
		return null;
	}

    @WebMethod
    public void refreshProject(String name) {
        try {
            ResourcesPlugin.getWorkspace().getRoot().getProject(name).refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
