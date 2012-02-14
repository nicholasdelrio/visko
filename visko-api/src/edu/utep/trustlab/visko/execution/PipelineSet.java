package edu.utep.trustlab.visko.execution;

import java.util.HashMap;
import java.util.Vector;

public class PipelineSet extends Vector<Pipeline> {
	private HashMap<String, String> variableBindings;
	private String artifURL;

	public PipelineSet() {
		super();
	}

	public void setParameterBindings(HashMap<String, String> bindings) {
		variableBindings = bindings;
	}

	public void setArtifactURL(String artifactURL) {
		artifURL = artifactURL;
	}

	public String getArtifactURL() {
		return artifURL;
	}

	public HashMap<String, String> getParameterBindings() {
		return variableBindings;
	}
}
