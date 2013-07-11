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
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;

public class Module extends JenaIndividual {
	
	private Toolkit toolkit;
	private URL moduleDocURL;
	private URL sourceCodeURL;
	
	private ObjectProperty wrapsToolkit;
	private DatatypeProperty hasDocumentation;	
	private DatatypeProperty hasSourceCode;
	
	public Module(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_Module, baseURL, name, viskoModel);
	}

	public Module(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}	
	
	public void setDocumentationURL(URL documentationURL){
		moduleDocURL = documentationURL;
	}
	
	public URL getDocumentationURL(){
		return moduleDocURL;
	}

	public void setToolkit(Toolkit tk) {
		toolkit = tk;
	}
	
	public Toolkit getToolkit() {
		return toolkit;
	}
	
	public void setSourceCodeURL(URL sourceURL){
		sourceCodeURL = sourceURL;
	}
	
	public URL getSourceCodeURL(){
		return sourceCodeURL;
	}
	
	private void addWrapsToolkit(Individual subjectInd) {
		if (toolkit != null)
			subjectInd.addProperty(wrapsToolkit, toolkit.getIndividual());
	}

	private void addDocumentationURL(Individual subjectInd) {
		if(moduleDocURL == null){
			try{moduleDocURL = new URL("http://null.org");}
			catch(Exception e){e.printStackTrace();}
		}
		Literal urlLiteral = model.createLiteral(moduleDocURL.toString());
		subjectInd.addProperty(hasDocumentation, urlLiteral);
	}
	
	private void addSourceCodeURL(Individual subjectInd) {
		if(sourceCodeURL == null){
			try{sourceCodeURL = new URL("http://null.org");}
			catch(Exception e){e.printStackTrace();}
		}
		
		Literal urlLiteral = model.createLiteral(sourceCodeURL.toString());
		subjectInd.addProperty(hasSourceCode, urlLiteral);
	}

	@Override
	protected boolean allFieldsPopulated() {
		return toolkit != null;
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		
		this.addDocumentationURL(ind);
		this.addWrapsToolkit(ind);
		this.addSourceCodeURL(ind);
		return ind;
	}

	@Override
	protected void setProperties() {
		wrapsToolkit = model.getObjectProperty(ViskoS.PROPERTY_URI_wrapsToolkit);
		hasDocumentation = model.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_hasDocumentation);
		hasSourceCode = model.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_hasSourceCode);
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		
		// populate toolkit
		RDFNode toolkitNode = ind.getPropertyValue(wrapsToolkit);
		toolkit = new Toolkit(toolkitNode.as(Individual.class).getURI(), model);
		
		// populate documentationURL
		String docURL = ind.getPropertyValue(hasDocumentation).as(Literal.class).getString();
		try{moduleDocURL = new URL(docURL);}
		catch(Exception e){e.printStackTrace();}
		
		// populate sourceCodeURL
		String sourceURL = ind.getPropertyValue(hasSourceCode).as(Literal.class).getString();
		try{sourceCodeURL = new URL(sourceURL);}
		catch(Exception e){e.printStackTrace();}

	}

	@Override
	protected void initializeFields() {

	}
}