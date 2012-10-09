/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.web.html;

import java.net.URI;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class SelectionOptionsHTML {
	public static final String DEFAULT = "default";
	private ViskoTripleStore viskoStore;

	public SelectionOptionsHTML() {
		viskoStore = new ViskoTripleStore();
	}

	private static String getURIFragment(String uri) {
		try {
			return new URI(uri).getFragment();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getFormats() {
		ResultSet formats = viskoStore.getInputFormats();

		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose Format --</option>";
		String formatURI;
		String option = "";
		while (formats.hasNext()) {
			formatURI = formats.nextSolution().get("?inputFormat").toString();
			option = "<option title=\"" + formatURI + "\" value=\"" + formatURI
					+ "\">" + getURIFragment(formatURI) + "</option>";
			options += option;
		}

		return options;
	}

	public String getViewerSets() {
		ResultSet viewerSets = viskoStore.getViewerSets();

		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose ViewerSet --</option>";
		String viewerSetURI;
		String option = "";
		while (viewerSets.hasNext()) {
			viewerSetURI = viewerSets.nextSolution().get("?viewerSet").toString();
			option = "<option title=\"" + viewerSetURI + "\" value=\""
					+ viewerSetURI + "\">" + getURIFragment(viewerSetURI)
					+ "</option>";
			options += option;
		}

		return options;
	}

	public String getTypes() {
		ResultSet types = viskoStore.getInputDataTypes();
		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose Type --</option>";
		String typeURI;
		String label;
		String option = "";
		while (types.hasNext()) {
			QuerySolution solution = types.nextSolution();
			typeURI = solution.get("?dataType").toString();
			
			if(solution.get("?label") != null){
				label = solution.get("?label").toString();
				option = "<option title=\"" + typeURI + "\" value=\"" + typeURI + "\">" + label + "</option>";
			}
			else
				option = "<option title=\"" + typeURI + "\" value=\"" + typeURI + "\">" + typeURI + "</option>";
			
			options += option;
		}

		return options;
	}

	public String getParameters() {
		ResultSet parameters = viskoStore.getAllParameters();
		String paramURI;
		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose Parameter --</option>";
		String option = "";
		while (parameters.hasNext()) {
			paramURI = parameters.nextSolution().get("?param").toString();

			if (!(paramURI.contains("url") || paramURI.contains("datasetURL"))) {
				option = "<option title=\"" + paramURI + "\" value=\""
						+ paramURI + "\">" + getURIFragment(paramURI)
						+ "</option>";
				options += option;
			}
		}

		return options;
	}

	public String getViskoViews() {
		ResultSet views = viskoStore.getVisualizationAbstractions();
		String viewURI;
		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose View --</option>";
		
		String option = "<option title=\"*\" value=\"*\">*</option>";
		options += option;
		
		while (views.hasNext()) {
			viewURI = views.nextSolution().get("?visualizationAbstraction").toString();
			option = "<option title=\"" + viewURI + "\" value=\"" + viewURI
					+ "\">" + getURIFragment(viewURI) + "</option>";
			options += option;
		}

		return options;
	}
}
