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


package edu.utep.trustlab.visko.ontology.service;

import java.util.Vector;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;
import edu.utep.trustlab.visko.ontology.vocabulary.XSD;

public class Extractor extends JenaIndividual {

	private String profileURI; // will be used in future when pipeline executor
								// is described in visko ontology
	private Vector<String> dataTypesToExtractFrom;
	private Format format;

	// datatype properties
	private DatatypeProperty extractsFromDataOfTypeProperty;
	private DatatypeProperty createsProfileProperty;
	private ObjectProperty extractsFromFormatProperty;

	public Extractor(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_EXTRACTOR, baseURL, name, viskoModel);
	}

	public Extractor(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setDataTypesToExtractFrom(Vector<String> dataTypeURIs) {
		if (dataTypeURIs != null)
			dataTypesToExtractFrom = dataTypeURIs;
	}

	public void setProfileURI(String profileURI) {
		this.profileURI = profileURI;
	}

	public String getProfileURI() {
		return profileURI;
	}

	public void setFormat(Format fmt) {
		format = fmt;
	}

	public void addDataTypeToExtractFrom(String dataTypeURI) {
		if (dataTypeURI != null)
			dataTypesToExtractFrom.add(dataTypeURI);
	}

	public Vector<String> getDataTypesToExtractFrom() {
		return dataTypesToExtractFrom;
	}

	private void addExtractsFromDataOfTypeProperty(Individual subjectInd) {
		for (String type : dataTypesToExtractFrom) {
			Literal dataType = model.createTypedLiteral(type,
					XSD.TYPE_URI_ANYURI);
			subjectInd.addProperty(extractsFromDataOfTypeProperty, dataType);
		}
	}

	private void addCreatesProfileProperty(Individual subjectInd) {
		Literal profile = model.createTypedLiteral(profileURI,
				XSD.TYPE_URI_ANYURI);
		subjectInd.addProperty(createsProfileProperty, profile);
	}

	private void addExtractsFromFormatProperty(Individual subjectInd) {
		subjectInd.addProperty(extractsFromFormatProperty,
				format.getIndividual());
	}

	@Override
	public Individual createNewIndividual() {
		Individual subjectInd = super.createNewIndividual();
		this.addExtractsFromDataOfTypeProperty(subjectInd);
		this.addExtractsFromFormatProperty(subjectInd);
		this.addCreatesProfileProperty(subjectInd);

		return subjectInd;
	}

	@Override
	protected void initializeFields() {
		dataTypesToExtractFrom = new Vector<String>();
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		NodeIterator iterator = ind
				.listPropertyValues(extractsFromDataOfTypeProperty);
		String extractedDataType;
		while (iterator.hasNext()) {
			extractedDataType = (String) iterator.next().as(Literal.class)
					.getValue();
			dataTypesToExtractFrom.add(extractedDataType);
		}

		RDFNode profileNode = ind.getPropertyValue(createsProfileProperty);
		profileURI = (String) profileNode.as(Literal.class).getValue();

		RDFNode fmt = ind.getPropertyValue(extractsFromFormatProperty);
		format = new Format(fmt.as(Individual.class).getURI(), model);
	}

	@Override
	protected void setProperties() {
		extractsFromDataOfTypeProperty = model
				.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_EXTRACTS_DATA_OFTYPE);
		extractsFromFormatProperty = model
				.getObjectProperty(ViskoS.PROPERTY_URI_EXTRACTS_FROM_FORMAT);
		createsProfileProperty = model
				.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_CREATES_PROFILE);
	}

	@Override
	protected boolean allFieldsPopulated() {
		return dataTypesToExtractFrom.size() > 0
				&& extractsFromFormatProperty != null && profileURI != null;
	}
}
