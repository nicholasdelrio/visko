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
import java.util.HashMap;
import java.util.Vector;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.variable.Input;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.OWLSService;
import edu.utep.trustlab.visko.ontology.viskoService.Service;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

public class PipelineHTML {

	public static String getPipelineHTML(Pipeline pipe) {
		String html = "<ol>";

		HashMap<String, String> bindings = pipe.getParameterBindings();

		for(int i = 0; i < pipe.size(); i ++){
			Service viskoService = pipe.getService(i);
			OWLSService service = viskoService.getOWLSService();
			String uri = service.getURI();
			html += "<li><b>Service Name:</b> <a href=\"" + uri + "\">" + uri + "</a></li>";
			html += "<ul>";

			html += "<li><b>Operator Type:</b> ";

			String operatorURI = viskoService.getConceptualOperator().getURI();
			ViskoTripleStore ts = new ViskoTripleStore();

			if (!ts.isMapper(operatorURI)) {
				html += "<a href=\""+ ViskoO.CLASS_URI_Operator + "\">Transformer</a></li>";
			}

			else {
				Vector<String> viewURIs = ResultSetToVector.getVectorFromResultSet(ts.getViewsGeneratedFrom(operatorURI), "view");
				html += "<a href=\"" + ViskoO.CLASS_URI_Mapper + "\">Mapper</a></li>";
				html += "<li><b>Generates View:</b> <a href=\""
						+ viewURIs.firstElement() + "\">"
						+ viewURIs.firstElement() + "</a></li>";
			}

			OWLIndividualList<Input> paramList = service.getIndividual().getProfile().getInputs();
			String parameterURI;
			String parameterValue;
			if (paramList.size() > 0) {
				for (Input inputParameter : paramList) {
					parameterURI = inputParameter.getURI().toASCIIString();
					parameterValue = bindings.get(parameterURI);

					if (!parameterURI.contains("url") && !parameterURI.contains("datasetURL")) {
						html += "<li><a href=\"" + parameterURI + "\">" + getURIFragment(parameterURI) + " </a><b>=</b> " + parameterValue + "</li>";
					}
				}
			}

			else {
				html += "<li><b>No Parameters</b></li>";
			}

			html += "</ul>";

			html += "<ul>";
			html += "<li><b>Supporting Toolkit:</b> <a href=\""
					+ viskoService.getSupportingToolkit().getURI() + "\">"
					+ viskoService.getSupportingToolkit().getLabel() + "</a></li>";
			html += "<li><b>Implements Operator:</b> <a href=\""
					+ viskoService.getConceptualOperator().getURI() + "\">"
					+ viskoService.getConceptualOperator().getURI() + "</a></li>";
			html += "<li><b>Input Format: </b>";
			
			String formatURI = viskoService.getConceptualOperator().getInputFormats().firstElement().getURI();
			String formatName = getURIFragment(formatURI);
			
			html += "<a href=\"" + formatURI + "\">" + formatName + "</a></li>";
			html += "</ul>";
			html += "<br/>";
			html += "<hr/>";
			html += "<br/>";
		}
		html += "</ol>";

		return html;
	}

	private static String getInputFormatList(Vector<Format> formats) {
		String html = "<ul>";
		String formatURI;
		String formatName;
		if(formats.size() == 0)
			System.out.println("no formats!");
		for (Format format : formats) {
			System.out.println("formatURI: " + format.getURI());
			formatURI = format.getURI();
			formatName = getURIFragment(formatURI);
			html += "<li><a href=\"" + format.getURI() + "\">" + formatName + "</a>";
		}
		html += "</ul>";

		return html;
	}

	public static String getViewerHTML(Pipeline pipe) {
		String html;
		Viewer viewer = pipe.getViewer();
		if (viewer != null) {
			html = "<ul>";
			html += "<li><a href=\"" + viewer.getURI() + "\">" + viewer.getLabel() + "</a></li>";

			html += "<li><b>Input Format(s):</b>";
			html += getInputFormatList(viewer.getInputFormats());
			html += "</li>";
			html += "<ul>";
		} else
			html = "<p>Viewer was not specified.</p>";

		return html;
	}
	
	public static String getURIFragment(String uri){
		try{
			return new URI(uri).getFragment();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
