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

import java.util.HashMap;
import java.util.Vector;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.variable.Input;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;
import edu.utep.trustlab.visko.execution.Pipeline;
import edu.utep.trustlab.visko.ontology.operator.Viewer;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.service.OWLSService;

public class PipelineHTML {

	public static String getPipelineHTML(Pipeline pipe) {
		String html = "<ol>";

		HashMap<String, String> bindings = pipe.getParameterBindings();

		for(int i = 0; i < pipe.size(); i ++){
			OWLSService service = pipe.getService(i);
			String uri = service.getURI();
			html += "<li><b>Service Name:</b> <a href=\"" + uri + "\">" + uri
					+ "</a></li>";
			html += "<ul>";

			html += "<li><b>Operator Type:</b> ";

			String operatorURI = service.getConceptualOperator().getURI();
			ViskoTripleStore ts = new ViskoTripleStore();

			if (!ts.isMapper(operatorURI)) {
				html += "<a href=\"http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Transformer\">Transformer</a>";
			}

			else {
				Vector<String> viewURIs = ResultSetToVector
						.getVectorFromResultSet(
								ts.getViewsGeneratedFrom(operatorURI), "view");
				html += "<a href=\"http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Mapper\">Mapper</a>";
				html += "<ul><li>Generates View: <a href=\""
						+ viewURIs.firstElement() + "\">"
						+ viewURIs.firstElement() + "</a></li></ul>";
			}

			OWLIndividualList<Input> paramList = service.getIndividual()
					.getProfile().getInputs();
			String parameterURI;
			String parameterValue;
			if (paramList.size() > 0) {
				for (Input inputParameter : paramList) {
					parameterURI = inputParameter.getURI().toASCIIString();
					parameterValue = bindings.get(parameterURI);

					if (!parameterURI.contains("url")
							&& !parameterURI.contains("datasetURL")) {
						html += "<li><b>Parameter:</b> <a href=\""
								+ parameterURI + "\">" + parameterURI
								+ " </a><b>=</b> " + parameterValue;
					}
				}
			}

			else {
				html += "<li><b>No Parameters</b></li>";
			}

			html += "</ul>";

			html += "<ul>";
			html += "<li><b>Supporting Toolkit:</b> <a href=\""
					+ service.getSupportingToolkit().getURI() + "\">"
					+ service.getSupportingToolkit().getLabel() + "</a></li>";
			html += "<li><b>Implements Operator:</b> <a href=\""
					+ service.getConceptualOperator().getURI() + "\">"
					+ service.getConceptualOperator().getURI() + "</a></li>";
			html += "<li><b>Input Format(s):</b>";
			html += getInputFormatList(service.getConceptualOperator()
					.getOperatesOnFormats()) + "</li>";
			html += "</ul>";
			html += "<p>---------------------------------------------------------------------------------------------------</p>";
		}
		html += "</ol>";

		return html;
	}

	private static String getInputFormatList(Vector<Format> formats) {
		String html = "<ul>";
		for (Format format : formats) {
			html += "<li><a href=\"" + format.getURI() + "\">"
					+ format.getURI() + "</a>";
		}
		html += "</ul>";

		return html;
	}

	public static String getViewerHTML(Pipeline pipe) {
		String html;
		Viewer viewer = pipe.getViewer();
		if (viewer != null) {
			html = "<ul>";
			html += "<li>" + viewer.getLabel() + "</li>";
			html += "<li><a href=\"" + viewer.getURI() + "\">"
					+ viewer.getURI() + "</a></li>";

			html += "<li><b>Input Format(s):</b>";
			html += getInputFormatList(viewer.getOperatesOnFormats()) + "</li>";

			html += "<ul>";
		} else
			html = "<p>Viewer was not specified.</p>";

		return html;
	}

}
