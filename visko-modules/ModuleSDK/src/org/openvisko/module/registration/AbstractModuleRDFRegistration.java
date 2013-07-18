package org.openvisko.module.registration;

import java.io.File;
import java.net.URL;

import org.openvisko.module.util.ServerProperties;

public abstract class AbstractModuleRDFRegistration {
	private String webappName = "";
	private File wsdlFile;
	private ModuleWriter packageWriter;

	public abstract void populateViewerSets();

	public abstract void populateToolkit();

	public abstract void populateServices();

	public abstract void populateParameterBindings();

	public void setModuleWriter(ModuleWriter writer) {
		packageWriter = writer;
	}

	public ModuleWriter getModuleWriter() {
		return packageWriter;
	}

	public File getWsdlFile() {
		return wsdlFile;
	}

	/**
	 * Generate the wsdl URL from a local file so we don't require the web
	 * service to actually be running in order to generate the rdf
	 * 
	 * @return
	 */
	public String getWSDLURL() {
		String wsdlUrl = "";
		try {
			wsdlUrl = wsdlFile.toURI().toURL().toString();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return wsdlUrl;
	}

	public void setWsdlFile(File wsdlFile) {
		this.wsdlFile = wsdlFile;
	}

	public String getWebappName() {
		return webappName;
	}

	public void setWebappName(String webappName) {
		this.webappName = webappName;
	}

	public URL getWebServerUrl() {
		return ServerProperties.getInstance().getServerBaseURL();
	}
}