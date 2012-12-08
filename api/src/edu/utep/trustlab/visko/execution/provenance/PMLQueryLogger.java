package edu.utep.trustlab.visko.execution.provenance;
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

import java.io.StringWriter;

import edu.utep.trustlab.visko.util.FileUtils;
import org.inference_web.pml.v2.pmlj.IWQuery;
import org.inference_web.pml.v2.pmlp.IWInformation;
import org.inference_web.pml.v2.util.PMLObjectManager;
import org.inference_web.pml.v2.vocabulary.PMLJ;
import org.inference_web.pml.v2.vocabulary.PMLP;

import edu.utep.trustlab.contentManagement.ContentManager;

public class PMLQueryLogger {

	private String queryName;
	private IWQuery query;
	
	public PMLQueryLogger(){
		String baseQueryName = "visko-query";
		queryName = baseQueryName + "-" + FileUtils.getRandomFileName() + ".owl";
		String baseURL = ContentManager.getProvenanceContentManager().getBaseURL();
		String url = baseURL + queryName;
		query = (IWQuery)PMLObjectManager.createPMLObject(PMLJ.Query_lname);
		query.setIdentifier(PMLObjectManager.getObjectID(url + "#" + "query"));
	}
	
	public String dumpPMLQuery(){
		StringWriter rdfStringWriter = new StringWriter();
  		PMLObjectManager.getOntModel(query).write(rdfStringWriter, "RDF/XML-ABBREV");
  		PMLObjectManager.getOntModel(query).close();
  			
  		ContentManager.getProvenanceContentManager().saveDocument(rdfStringWriter.toString(), queryName);
  		
  		return query.getIdentifier().getURIString();
	}
	
	public void addAnswer(String nodesetURI){
		query.addHasAnswer(nodesetURI);
	}
	
	public void setViskoQuery(String viskoQuery){
		// create Information instance as conclusion
  		IWInformation content = (IWInformation)PMLObjectManager.createPMLObject(PMLP.Information_lname);
  		content.setHasRawString(viskoQuery);
  		query.setHasContent(content);
	}
}