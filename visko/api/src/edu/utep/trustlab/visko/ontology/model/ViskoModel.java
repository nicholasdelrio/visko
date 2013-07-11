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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;

import org.mindswap.owl.OWLIndividual;

import edu.utep.trustlab.visko.ontology.vocabulary.Visko;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWLS_Process;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWLS_Service;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.PMLP;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.RDFS;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

public class ViskoModel{
	private OntModel model;
	private static Vector<ViskoModel> models = new Vector<ViskoModel>();
	
	public ViskoModel() {
		models.add(this);
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		//add namespace mappings
		model.setNsPrefix("viskoV", Visko.CORE_VISKO_V + "#");
		model.setNsPrefix("viskoO", Visko.CORE_VISKO_O + "#");
		model.setNsPrefix("viskoS", Visko.CORE_VISKO_S + "#");
		model.setNsPrefix("pmlp", PMLP.ONTOLOGY_PMLP_URI + "#");
		model.setNsPrefix("owlsService", OWLS_Service.ONTOLOGY_OWLS_SERVICE_URI + "#");
		model.setNsPrefix("owlsProcess", OWLS_Process.ONTOLOGY_OWLS_PROCESS_URI + "#");
	}

	public static void closeModels() {
		for (ViskoModel model : models) {
			model.closeModel();
		}
	}

	public static OntModel getOntModelFromRDFFile(String rdfFileContents) {
		OntModel model = ModelFactory.createOntologyModel();
		StringReader reader = new StringReader(rdfFileContents);
		model.read(reader, null);
		return model;
	}

	private OntModel getRelevantOntModel(String uri) {		
		OntModel ontModel = null;
		
		if (uri.contains(Visko.CORE_VISKO_V))
			ontModel = ViskoV.getModel();
		else if (uri.contains(Visko.CORE_VISKO_O))
			ontModel = ViskoO.getModel();
		else if (uri.contains(Visko.CORE_VISKO_S))
			ontModel = ViskoS.getModel();
		else if (uri.contains(PMLP.ONTOLOGY_PMLP_URI))
			ontModel = ViskoO.getModel();
		else if (uri.contains(OWLS_Service.ONTOLOGY_OWLS_SERVICE_URI))
			ontModel = OWLS_Service.getModel();
		else if (uri.contains(OWLS_Process.ONTOLOGY_OWLS_PROCESS_URI))
			ontModel = OWLS_Process.getModel();
		else if (uri.contains(RDFS.ONTOLOGY_RDFS_URI))
			ontModel = RDFS.getModel();
		else
			System.out.println("Couldn't find OntClass in VisKo ontology collection...");

		return ontModel;
	}
	
	public String getModelAsRDFString() {
		StringWriter wtr = new StringWriter();
		model.write(wtr, "RDF/XML-ABBREV");
		return wtr.toString();
	}

	public Individual getIndividualFromOWLIndividual(OWLIndividual owlIndividual){
		OntModel loadingModel = ModelFactory.createOntologyModel();
		String owlIndividualURI = owlIndividual.getURI().toASCIIString();
		String owlIndividualRDF = owlIndividual.toRDF(true, false);
		StringReader rdr = new StringReader(owlIndividualRDF);
		loadingModel.read(rdr, null);
		return loadingModel.getIndividual(owlIndividualURI);
	}
	
	public Individual getIndividual(String uri) {
		
		if(model.getIndividual(uri) == null)
			model.read(uri);
		
		return model.getIndividual(uri);
	}

	public void addToModel(String rdf){
		StringReader reader = new StringReader(rdf);
		model.read(reader, null);
	}
	
	public OntClass getOntClass(String uri) {
		OntModel relevantModel = getRelevantOntModel(uri);
		return relevantModel.getOntClass(uri);
	}

	public ObjectProperty getObjectProperty(String uri) {
		OntModel relevantModel = getRelevantOntModel(uri);
		return relevantModel.getObjectProperty(uri);
	}

	public Property getProperty(String uri) {
		OntModel relevantModel = getRelevantOntModel(uri);
		return relevantModel.getProperty(uri);
	}

	public Individual createIndividual(String uri, OntClass ontClass) {
		return model.createIndividual(uri, ontClass);
	}

	public Literal createLiteral(String value) {
		return model.createLiteral(value);
	}

	public Literal createTypedLiteral(String value, String typeURI) {
		return model.createTypedLiteral(value, typeURI);
	}

	public DatatypeProperty getDatatypeProperty(String uri) {
		OntModel relevantModel = getRelevantOntModel(uri);
		return relevantModel.getDatatypeProperty(uri);
	}

	public void closeModel() {
		model.close();
	}
}
