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

public class ViskoV {

	// Classes
	public static final String CLASS_URI_View = Visko.CORE_VISKO_V + "#View";
	
	public static final String CLASS_URI_1D_Geometric_View = Visko.CORE_VISKO_V + "#1D_Geometric_View";
	public static final String CLASS_URI_2D_Geometric_View = Visko.CORE_VISKO_V + "#2D_Geometric_View";
	public static final String CLASS_URI_3D_Geometric_View = Visko.CORE_VISKO_V + "#3D_Geometric_View";
	public static final String CLASS_URI_4D_Geometric_View = Visko.CORE_VISKO_V + "#4D_Geometric_View";

	public static final String CLASS_URI_1D_DataStructure_View = Visko.CORE_VISKO_V + "#1D_DataStructure_View";
	public static final String CLASS_URI_2D_DataStructure_View = Visko.CORE_VISKO_V + "#2D_DataStructure_View";
	public static final String CLASS_URI_3D_DataStructure_View = Visko.CORE_VISKO_V + "#3D_DataStructure_View";
	public static final String CLASS_URI_4D_DataStructure_View = Visko.CORE_VISKO_V + "#4D_DataStructure_View";
	
	// Individuals
	public static final String INDIVIDUAL_URI_Timeline = Visko.CORE_VISKO_V + "#Timeline";
	
	public static final String INDIVIDUAL_URI_SurfacePlot = Visko.CORE_VISKO_V + "#SurfacePlot";
	public static final String INDIVIDUAL_URI_ContourMap = Visko.CORE_VISKO_V + "#ContourMap";
	public static final String INDIVIDUAL_URI_PointPlot = Visko.CORE_VISKO_V + "#PointPlot";
	public static final String INDIVIDUAL_URI_RasterMap = Visko.CORE_VISKO_V + "#RasterMap";
	public static final String INDIVIDUAL_URI_TimeSeriesPlot = Visko.CORE_VISKO_V + "#TimeSeriesPlot";
	
	public static final String INDIVIDUAL_URI_IsoSurfaceRendering = Visko.CORE_VISKO_V + "#IsoSurfacesRendering";
	public static final String INDIVIDUAL_URI_MeshPlot = Visko.CORE_VISKO_V + "#MeshPlot";
	public static final String INDIVIDUAL_URI_VolumeRendering = Visko.CORE_VISKO_V + "#VolumeRendering";
	public static final String INDIVIDUAL_URI_RasterCube = Visko.CORE_VISKO_V + "#RasterCube";

	private static OntModel model;

	static {
		model = ModelFactory.createOntologyModel();
		model.read(Visko.CORE_VISKO_V);
	}

	public static OntModel getModel() {
		return model;
	}
}