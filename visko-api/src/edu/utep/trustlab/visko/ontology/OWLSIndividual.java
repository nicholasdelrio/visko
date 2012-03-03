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


package edu.utep.trustlab.visko.ontology;

import java.io.StringWriter;

import org.mindswap.owl.OWLIndividual;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.util.OWLSRDFCleanup;

public abstract class OWLSIndividual implements ViskoIndividual {
	private String name;
	protected String uri;
	private String fullURL;
	private String fileName;
	private String baseURL;

	protected boolean isForReading;

	protected OWLSModel model;

	public OWLSIndividual(String baseURL, String name, OWLSModel owlsModel) {
		System.out
				.println("asssuming that the object will be used for writing...");

		setURI(baseURL, name);

		model = owlsModel;
		model.createOntology(fullURL);
		model.addImportsToOntology();

		setProperties();
	}

	public OWLSIndividual(String individualURI, OWLSModel owlsModel) {
		System.out
				.println("asssuming that the object will be used for reading...");
		isForReading = true;
		model = owlsModel;
		uri = individualURI;

		setProperties();

		getIndividual();
	}

	public String toString() {
		StringWriter wtr = new StringWriter();
		model.getOntology().write(wtr, model.getOntology().getURI());
		String rdfString = wtr.toString();
		rdfString = OWLSRDFCleanup.fixURIForImplementsOperator(rdfString, Repository.getRepository().getBaseURL());
		rdfString = OWLSRDFCleanup.fixURIForSupportedByToolkit(rdfString, Repository.getRepository().getBaseURL());
		return rdfString;
	}

	public String getFileName() {
		return fileName;
	}

	public String getURI() {
		return uri;
	}

	public abstract OWLIndividual getIndividual();

	protected abstract void setProperties();

	protected abstract boolean allFieldsPopulated();

	protected abstract void initializeFields();

	/********************* PRIVATE METHODS *****************************/
	private void setURI(String base, String individualName) {
		baseURL = base;
		name = individualName;
		fileName = name + ".owl";
		fullURL = baseURL + fileName;
		uri = baseURL + fileName + "#" + name;
	}
}
