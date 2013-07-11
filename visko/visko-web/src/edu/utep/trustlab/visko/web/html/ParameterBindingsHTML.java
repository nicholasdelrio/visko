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

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.variable.Input;

import edu.utep.trustlab.visko.ontology.viskoService.OWLSService;
import edu.utep.trustlab.visko.ontology.viskoService.Service;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;

public class ParameterBindingsHTML {

	public static String getParameterBindingsList(Pipeline pipe) {
		String html = "<ol>";

		HashMap<String, String> bindings = pipe.getParameterBindings();

		for(int i = 0; i < pipe.size(); i ++){
			Service viskoService = pipe.getService(i);
			OWLSService service = viskoService.getOWLSService();
			String uri = service.getURI();
			
			html += "<li><b>Service Name:</b> <a href=\"" + uri + "\">" + uri + "</a></li>";
			html += "<ul>";
			html += "<li><b>Supporting Toolkit:</b> <a href=\"" + viskoService.getSupportingToolkit().getURI() + "\">" + viskoService.getSupportingToolkit().getLabel() + "</a></li>";
			html += "</ul>";
			
			OWLIndividualList<Input> paramList = service.getIndividual().getProfile().getInputs();
			String parameterURI;
			String parameterValue;
			if ((paramList.size() - 1) > 0) {
				html += "<table>";
				for (Input inputParameter : paramList) {
					parameterURI = inputParameter.getURI().toASCIIString();
					parameterValue = bindings.get(parameterURI);
					
					if (!parameterURI.contains("url") && !parameterURI.contains("datasetURL")) {
						if(parameterValue == null)
							html += "<tr><td><a href=\"" + parameterURI + "\">" + PipelineHTML.getURIFragment(parameterURI) + " </a></td><td><b>=</b> <input style=\"width: 100px; background-color: #AFEEEE\" name=\"" + parameterURI + "\" /></td></tr>";
						else
							html += "<tr><td><a href=\"" + parameterURI + "\">" + PipelineHTML.getURIFragment(parameterURI) + " </a></td><td><b>=</b> <input style=\"width: 100px; background-color: #AFEEEE\" value=\"" + parameterValue + "\" name=\"" + parameterURI + "\" /></td></tr>";
					}
				}				
				html += "</table>";
			}
			else
				html += "<b>No Parameters associated with service.</b>";
			
			
			html += "<hr>";
		}
		
		html += "</ol>";
		return html;
	}
}
