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


package edu.utep.trustlab.visko.ontology.vocabulary;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ViskoO {
	public static final String ONTOLOGY_VISKO_O_URI = Visko.VISKO_O;

	// Concepts
	public static final String CLASS_URI_OPERATOR = ONTOLOGY_VISKO_O_URI + "#Operator";
	public static final String CLASS_URI_VIEWMAPPER = ONTOLOGY_VISKO_O_URI + "#ViewMapper";
	public static final String CLASS_URI_FORMATCONVERTER = ONTOLOGY_VISKO_O_URI + "#FormatConverter";
	public static final String CLASS_URI_DATATRANSFORMER = ONTOLOGY_VISKO_O_URI + "#DataTransformer";
	public static final String CLASS_URI_DATAFILTER = ONTOLOGY_VISKO_O_URI + "#DataFilter";

	public static final String CLASS_URI_VIEWER = ONTOLOGY_VISKO_O_URI + "#Viewer";
	public static final String CLASS_URI_VIEWERSET = ONTOLOGY_VISKO_O_URI + "#ViewerSet";

	/*****************************************************************************************/
	// Properties
	public static final String PROPERTY_URI_HASINPUTFORMAT = ONTOLOGY_VISKO_O_URI + "#hasInputFormat";
	public static final String PROPERTY_URI_HASOUTPUTFORMAT = ONTOLOGY_VISKO_O_URI + "#hasOutputFormat";
	
	public static final String PROPERTY_URI_HASINPUTDATATYPE = ONTOLOGY_VISKO_O_URI + "#hasInputDataType";
	public static final String PROPERTY_URI_HASOUTPUTDATATYPE = ONTOLOGY_VISKO_O_URI + "#hasOutputDataType";	
	
	public static final String PROPERTY_URI_MAPS_TO = ONTOLOGY_VISKO_O_URI + "#mapsToView";
	public static final String PROPERTY_URI_PART_OF_VIEWERSET = ONTOLOGY_VISKO_O_URI + "#partOfViewerSet";
	
	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel();
		model.read(ONTOLOGY_VISKO_O_URI);
		ontology = model.getOntology(ONTOLOGY_VISKO_O_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
