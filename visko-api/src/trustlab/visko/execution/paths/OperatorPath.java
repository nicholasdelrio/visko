package trustlab.visko.execution.paths;

import java.util.*;

public class OperatorPath extends Vector<String> {
	private String viewerURI;

	public void set(Vector<String> operatorURIs) {
		for (String operatorURI : operatorURIs)
			super.add(operatorURI);
	}

	public boolean isEmptyPath() {
		return size() == 0;
	}

	public OperatorPath(String viewerURI) {
		super();
		this.viewerURI = viewerURI;
	}

	public String getViewerURI() {
		return viewerURI;
	}
}
