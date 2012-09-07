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

public class ESIPData {

	public static final String ONTOLOGY_ESIP_URI = "https://raw.github.com/nicholasdelrio/visko/master/resources/ontologies/esip-data.owl";
	public static final String ONTOLOGY_ESIP_NS = "http://www.ordnancesurvey.co.uk/ontology/Datatypes.owl";

	// Concepts
	public static final String CLASS_ESIP_GEOMETRY = ONTOLOGY_ESIP_NS + "#Geometry";
	public static final String CLASS_ESIP_VOLUME = ONTOLOGY_ESIP_NS + "#Volume";
	public static final String CLASS_ESIP_POINT = ONTOLOGY_ESIP_NS + "#Point";
	public static final String CLASS_ESIP_3DSWATH = ONTOLOGY_ESIP_NS + "#3DSwath";
	public static final String CLASS_ESIP_CURVE = ONTOLOGY_ESIP_NS + "#Curve";
	public static final String CLASS_ESIP_CONTOUR = ONTOLOGY_ESIP_NS + "#Contour";
	public static final String CLASS_ESIP_LINE = ONTOLOGY_ESIP_NS + "#Line";
	public static final String CLASS_ESIP_1DSWATH = ONTOLOGY_ESIP_NS + "#1DSwath";
	public static final String CLASS_ESIP_SURFACE = ONTOLOGY_ESIP_NS + "#Surface";
	public static final String CLASS_ESIP_2DSWATH = ONTOLOGY_ESIP_NS + "#2DSwath";

	/*****************************************************************************************/

	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(ONTOLOGY_ESIP_URI);
		ontology = model.getOntology(ONTOLOGY_ESIP_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
