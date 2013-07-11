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


package edu.utep.trustlab.visko.ontology.viskoService;

import java.net.URL;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;

public class Toolkit extends JenaIndividual {

	private URL url;
	private DatatypeProperty hasDocumentation;
	
	public Toolkit(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_Toolkit, baseURL, name, viskoModel);
	}

	public Toolkit(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}
	
	public void setDocumentationURL(URL documentationURL){
		url = documentationURL;
	}
	
	public URL getDocumentationURL(){
		return url;
	}

	private void addDocumentationURL(Individual subjectInd) {
		if(url == null){
			try{url = new URL("http://null.org");}
			catch(Exception e){e.printStackTrace();}
		}
		Literal urlLiteral = model.createLiteral(url.toString());
		subjectInd.addProperty(hasDocumentation, urlLiteral);
	}
	
	@Override
	protected void initializeFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		// populate documentationURL
		String docURL = ind.getPropertyValue(hasDocumentation).as(Literal.class).getString();
		try{url = new URL(docURL);}
		catch(Exception e){e.printStackTrace();}
	}

	@Override
	protected void setProperties() {
		hasDocumentation = model.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_hasDocumentation);
	}

	@Override
	protected boolean allFieldsPopulated() {
		return true;
	}
	
	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addDocumentationURL(ind);	
		return ind;
	}
}
