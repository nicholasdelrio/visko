package edu.utep.trustlab.visko.execution.paths;

import java.util.*;

public class FormatPath extends Vector<String> {
	private String viewerURI;

	public FormatPath(String viewerURI) {
		super();
		this.viewerURI = viewerURI;
	}

	public boolean isEmptyPath() {
		return size() == 1;
	}

	public String getViewerURI() {
		return viewerURI;
	}

	public FormatPath copy() {
		FormatPath duplicateFormat = new FormatPath(viewerURI);
		for (String formatURI : this) {
			duplicateFormat.add(formatURI);
		}

		return duplicateFormat;
	}
}
