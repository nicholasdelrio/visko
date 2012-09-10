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
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ViskoV {
	public static final String ONTOLOGY_VISKO_V_URI = Visko.VISKO_V;

	// Concepts
	public static final String CLASS_URI_1D_GEOMETRIC_VIEW = ONTOLOGY_VISKO_V_URI + "#1D_Geometric_View";
	public static final String CLASS_URI_2D_GEOMETRIC_VIEW = ONTOLOGY_VISKO_V_URI + "#2D_Geometric_View";
	public static final String CLASS_URI_3D_GEOMETRIC_VIEW = ONTOLOGY_VISKO_V_URI + "#3D_Geometric_View";
	public static final String CLASS_URI_4D_GEOMETRIC_VIEW = ONTOLOGY_VISKO_V_URI + "#4D_Geometric_View";

	public static final String CLASS_URI_1D_DATASTRUCTURE_VIEW = ONTOLOGY_VISKO_V_URI + "#1D_DataStructure_View";
	public static final String CLASS_URI_2D_DATASTRUCTURE_VIEW = ONTOLOGY_VISKO_V_URI + "#2D_DataStructure_View";
	public static final String CLASS_URI_3D_DATASTRUCTURE_VIEW = ONTOLOGY_VISKO_V_URI + "#3D_DataStructure_View";
	public static final String CLASS_URI_4D_DATASTRUCTURE_VIEW = ONTOLOGY_VISKO_V_URI + "#4D_DataStructure_View";
	
	// Individuals
	
	public static final String INDIVIDUAL_URI_TIMELINE = ONTOLOGY_VISKO_V_URI + "#Timeline";
	
	public static final String INDIVIDUAL_URI_CONTOURMAP = ONTOLOGY_VISKO_V_URI + "#ContourMap";
	public static final String INDIVIDUAL_URI_POINTPLOT = ONTOLOGY_VISKO_V_URI + "#PointPlot";
	public static final String INDIVIDUAL_URI_RASTERMAP = ONTOLOGY_VISKO_V_URI + "#RasterMap";
	public static final String INDIVIDUAL_URI_TIMESERIESPLOT = ONTOLOGY_VISKO_V_URI + "#TimeSeriesPlot";
	
	public static final String INDIVIDUAL_URI_ISOSURFACESRENDERING = ONTOLOGY_VISKO_V_URI + "#IsoSurfacesRendering";
	public static final String INDIVIDUAL_URI_MESHPLOT = ONTOLOGY_VISKO_V_URI + "#MeshPlot";
	public static final String INDIVIDUAL_URI_VOLUMERENDERING = ONTOLOGY_VISKO_V_URI + "#VolumeRendering";

	/*****************************************************************************************/
	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel();
		model.read(ONTOLOGY_VISKO_V_URI);
		ontology = model.getOntology(ONTOLOGY_VISKO_V_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
