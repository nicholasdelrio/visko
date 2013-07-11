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


package edu.utep.trustlab.visko.ontology.model;

import java.io.StringWriter;
import java.net.URI;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.service.Service;

import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.util.GetURLContents;

public class OWLSModel {
	private OWLKnowledgeBase kb;
	private OWLOntology ontology;
	
	public OWLSModel() {
		loadKB();
	}
		public OWLKnowledgeBase getOWLKnowledgeBase() {
		return kb;
	}

	public Service readService(String serviceURI) {
		Service service = null;
		
		URI uri = GetURLContents.getURI(serviceURI);
		try {
			service = kb.readService(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return service;
	}

	public void createOntology(String uri) {
		URI ontologyURI = GetURLContents.getURI(uri);
		ontology = kb.createOntology(ontologyURI);
	}

	public OWLObjectProperty getObjectProperty(String uri) {
		URI propertyURI = GetURLContents.getURI(uri);
		try {
			kb.read(propertyURI);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kb.getObjectProperty(propertyURI);
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public OWLIndividual getOWLIndividual(String uri){
		URI uriObject = GetURLContents.getURI(uri);
		return kb.getIndividual(uriObject);
	}
	
	public String getModelAsRDFString(){
		StringWriter wtr = new StringWriter();
		getOntology().write(wtr, getOntology().getURI());
		String rdfString = wtr.toString();
		return rdfString;
	}
	
	public OWLIndividual convertJenaToOWLIndividual(JenaIndividual ind) {
		URI uri = GetURLContents.getURI(ind.getURI());
		try {
			kb.read(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kb.getIndividual(uri);
	}

	private void loadKB() {
		kb = OWLFactory.createKB();
	}
}
