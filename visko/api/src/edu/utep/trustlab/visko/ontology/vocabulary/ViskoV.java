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
	public static final String CLASS_URI_VisualizationAbstraction = Visko.CORE_VISKO_V + "#VisualizationAbstraction";
		
	// Individuals
	public static final String INDIVIDUAL_URI_1D_Timeline = Visko.CORE_VISKO_V + "#1D_Timeline";

	public static final String INDIVIDUAL_URI_2D_SpeciesDistribution_Map = Visko.CORE_VISKO_V + "#2D_SpeciesDistribution_Map";
	public static final String INDIVIDUAL_URI_2D_ContourMap = Visko.CORE_VISKO_V + "#2D_ContourMap";
	public static final String INDIVIDUAL_URI_2D_PointMap = Visko.CORE_VISKO_V + "#2D_PointMap";
	public static final String INDIVIDUAL_URI_2D_RasterMap = Visko.CORE_VISKO_V + "#2D_RasterMap";
	public static final String INDIVIDUAL_URI_2D_TimeSeriesPlot = Visko.CORE_VISKO_V + "#2D_TimeSeriesPlot";

	public static final String INDIVIDUAL_URI_2D_VisKo_Instances_BarChart = Visko.CORE_VISKO_V + "#2D_VisKo_Instances_BarChart";
	public static final String INDIVIDUAL_URI_2D_VisKo_DataTransformations_ForceGraph = Visko.CORE_VISKO_V + "#2D_VisKo_DataTransformations_ForceGraph";	
	public static final String INDIVIDUAL_URI_2D_VisKo_OperatorPaths_ForceGraph = Visko.CORE_VISKO_V + "#2D_VisKo_OperatorPaths_ForceGraph";	
	public static final String INDIVIDUAL_URI_2D_ForceGraph = Visko.CORE_VISKO_V + "#2D_ForceGraph";	

	public static final String INDIVIDUAL_URI_2D_SpherizedRaster = Visko.CORE_VISKO_V + "#2D_SpherizedRaster";
	public static final String INDIVIDUAL_URI_3D_BarChart = Visko.CORE_VISKO_V + "#3D_BarChart";
	
	public static final String INDIVIDUAL_URI_3D_IsoSurfaceRendering = Visko.CORE_VISKO_V + "#3D_IsoSurfacesRendering";
	public static final String INDIVIDUAL_URI_3D_MeshPlot = Visko.CORE_VISKO_V + "#3D_MeshPlot";
	public static final String INDIVIDUAL_URI_3D_VolumeRendering = Visko.CORE_VISKO_V + "#3D_VolumeRendering";
	public static final String INDIVIDUAL_URI_3D_RasterCube = Visko.CORE_VISKO_V + "#3D_RasterCube";
	public static final String INDIVIDUAL_URI_3D_SurfacePlot = Visko.CORE_VISKO_V + "#3D_SurfacePlot";
	public static final String INDIVIDUAL_URI_3D_PointPlot = Visko.CORE_VISKO_V + "#3D_PointPlot";
	public static final String INDIVIDUAL_URI_3D_MolecularStructure = Visko.CORE_VISKO_V + "#3D_MolecularStructure";
	public static final String INDIVIDUAL_URI_3D_MolecularStructure_Cartoon = Visko.CORE_VISKO_V + "#3D_MolecularStructure_Cartoon";
	public static final String INDIVIDUAL_URI_3D_MolecularStructure_Ribbon = Visko.CORE_VISKO_V + "#3D_MolecularStructure_Ribbon";
	
	private static OntModel model;

	static {
		model = ModelFactory.createOntologyModel();
		model.read(Visko.CORE_VISKO_V);
	}

	public static OntModel getModel() {
		return model;
	}
}