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
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ViskoS {
	
	// Classes
	public static final String CLASS_URI_Module = Visko.CORE_VISKO_S	+ "#Module";
	public static final String CLASS_URI_Service = Visko.CORE_VISKO_S	+ "#Service";
	public static final String CLASS_URI_Toolkit = Visko.CORE_VISKO_S	+ "#Toolkit";
	public static final String CLASS_URI_Extractor = Visko.CORE_VISKO_S + "#Extractor";
	public static final String CLASS_URI_InputParameterBindings = Visko.CORE_VISKO_S	+ "#InputParameterBindings";

	// Object Properties
	public static final String PROPERTY_URI_wrapsToolkit = Visko.CORE_VISKO_S + "#wrapsToolkit";	
	public static final String PROPERTY_URI_implementsOperator = Visko.CORE_VISKO_S + "#implementsOperator";
	public static final String PROPERTY_URI_supportedByToolkit = Visko.CORE_VISKO_S + "#supportedByToolkit";
	public static final String PROPERTY_URI_supportedByOWLSService = Visko.CORE_VISKO_S + "#supportedByOWLSService";
	public static final String PROPERTY_URI_declaresBindings = Visko.CORE_VISKO_S + "#declaresBindings";
	public static final String PROPERTY_URI_profiles = Visko.CORE_VISKO_S + "#profiles";

	//Datatype properties
	public static final String DATATYPE_PROPERTY_URI_hasSourceCode = Visko.CORE_VISKO_S + "#hasSourceCode";
	public static final String DATATYPE_PROPERTY_URI_hasDocumentation = Visko.CORE_VISKO_S + "#hasDocumentation";

	
	private static OntModel model;

	static {
		model = ModelFactory.createOntologyModel();
		model.read(Visko.CORE_VISKO_S);
	}

	public static OntModel getModel() {
		return model;
	}
}
