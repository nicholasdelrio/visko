package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleInputParameterBindings;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;

import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {
		//formats
		private static final Format ps = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/POSTSCRIPT.owl#POSTSCRIPT");
		private static final Format netCDF = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/NETCDF.owl#NETCDF");
		private static final Format spaceSeparatedValues = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES");
		private static final Format esriGridded = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/ESRIGRID.owl#ESRIGRID");

		//semantic type uris
		private static final OntResource gravityData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19");
		private static final OntResource griddedGravityData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d12");

		//views
		private static final VisualizationAbstraction contourMap2D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_ContourMap);
		private static final VisualizationAbstraction rasterMap2D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_RasterMap);
		private static final VisualizationAbstraction pointMap2D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_PointMap);
		private static final VisualizationAbstraction barchart3D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_BarChart);

		//data types
		private static final OntResource xyzData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d18");
		private static final OntResource fieldTrmmedGravityData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#FieldTrimmedGravityData");
		private static final OntResource COARDS_2D_Grid = ModuleWriter.getDataType("http://gmt.soest.hawaii.edu/gmt-data.owl#2D_Grid_COARDS");
		
		private static final OntResource mbsf = ModuleWriter.getDataType("http://data.oceandrilling.org#mbsf");

	}

	@Override
	public void populateServices() {
		String wsdlURL = getWSDLURL();

		String operationName = "grdcontour";
		ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
		service1.setComment("Generates contour map from netCDF 2D gridded dataset");
		service1.setLabel("GMT grdcontour");
		service1.setWSDLURL(wsdlURL);
		service1.setInputFormat(Resources.netCDF);
		service1.setOutputFormat(Resources.ps);
		service1.setView(Resources.contourMap2D);
		service1.setInputDataType(Resources.COARDS_2D_Grid);
		// not output type...system will set to owl:Thing

		operationName = "surface";
		ModuleOperatorService service2 = getModuleWriter().createNewOperatorService(null, operationName);
		service2.setComment("Employ tensioned splines to generate gridded data in netCDF from ascii tabular point data");
		service2.setLabel("GMT surface");
		service2.setWSDLURL(wsdlURL);
		service2.setInputFormat(Resources.spaceSeparatedValues);
		service2.setOutputFormat(Resources.netCDF);
		service2.setInputDataType(Resources.fieldTrmmedGravityData);
		service2.setOutputDataType(Resources.COARDS_2D_Grid);
		service2.setAsInterpolator();

		operationName = "nearneighbor";
		ModuleOperatorService service3 = getModuleWriter().createNewOperatorService(null, operationName);
		service3.setComment("Employ nearest neighbor to generate gridded data in netCDF from ascii tabular point data");
		service3.setLabel("GMT nearneighbor");
		service3.setWSDLURL(wsdlURL);
		service3.setInputFormat(Resources.spaceSeparatedValues);
		service3.setOutputFormat(Resources.netCDF);
		service3.setInputDataType(Resources.fieldTrmmedGravityData);
		service3.setOutputDataType(Resources.COARDS_2D_Grid);
		service3.setAsInterpolator();

		operationName = "psxy";
		ModuleOperatorService service4 = getModuleWriter().createNewOperatorService(null, operationName);
		service4.setComment("Generate 2D Plot of point data");
		service4.setLabel("GMT psxy");
		service4.setWSDLURL(wsdlURL);
		service4.setInputFormat(Resources.spaceSeparatedValues);
		service4.setOutputFormat(Resources.ps);
		service4.setView(Resources.pointMap2D);
		service4.setInputDataType(Resources.fieldTrmmedGravityData);
		// not output type...system will set to owl:Thing		

		operationName = "grdimage";
		ModuleOperatorService service5 = getModuleWriter().createNewOperatorService(null, operationName);
		service5.setComment("Generate raster map of gridded data encoded in netCDF");
		service5.setLabel("GMT grdimage");
		service5.setWSDLURL(wsdlURL);
		service5.setInputFormat(Resources.netCDF);
		service5.setOutputFormat(Resources.ps);
		service5.setView(Resources.rasterMap2D);
		service5.setInputDataType(Resources.COARDS_2D_Grid);
		// not output type...system will set to owl:Thing

		operationName = "grd2xyz";
		ModuleOperatorService service6 = getModuleWriter().createNewOperatorService(null, operationName);
		service6.setComment("Convert NetCDF COARDS compliant dataset to listing of XYZ values");
		service6.setLabel("GMT grd2xyz");
		service6.setWSDLURL(wsdlURL);
		service6.setInputFormat(Resources.netCDF);
		service6.setOutputFormat(Resources.spaceSeparatedValues);
		service6.setInputDataType(Resources.COARDS_2D_Grid);
		service6.setOutputDataType(Resources.xyzData);	

		operationName = "grd2xyz_esri";
		ModuleOperatorService service7 = getModuleWriter().createNewOperatorService(null, operationName);
		service7.setComment("Convert NetCDF COARDS compliant dataset to an ESRI ArcInfo ASCII Grid");
		service7.setLabel("GMT grd2xyz_esri");
		service7.setWSDLURL(wsdlURL);
		service7.setInputFormat(Resources.netCDF);
		service7.setOutputFormat(Resources.esriGridded);
		service7.setInputDataType(Resources.COARDS_2D_Grid);

		operationName = "psxyz";
		ModuleOperatorService service8 = getModuleWriter().createNewOperatorService(null, operationName);
		service8.setComment("Generate 3D bar chart of XYZ data");
		service8.setLabel("GMT psxyz");
		service8.setWSDLURL(wsdlURL);
		service8.setInputFormat(Resources.spaceSeparatedValues);
		service8.setOutputFormat(Resources.ps);
		service8.setView(Resources.barchart3D);
		service8.setInputDataType(Resources.xyzData);
		// not output type...system will set to owl:Thing
	}

	@Override
	public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("gmt");
		toolkit.setComment("Generic Mapping Tools");
		toolkit.setLabel("Generic Mapping Tools");
		
		String stringURL = "http://gmt.soest.hawaii.edu/";
		try
		{
			URL toolkitURL = new URL(stringURL);
			toolkit.setDocumentationURL(toolkitURL);
		}
		catch(Exception e){e.printStackTrace();}	
	}

	@Override
	public void populateViewerSets() {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateParameterBindings() {
		ModuleInputParameterBindings bindings1 = getModuleWriter().createNewInputParameterBindings();
		ModuleInputParameterBindings bindings2 = getModuleWriter().createNewInputParameterBindings();
		ModuleInputParameterBindings bindings3 = getModuleWriter().createNewInputParameterBindings();

		addGravityDataBindings(bindings1);
		addGriddedGravityDataBindings(bindings2);
		addMBSFParameterBindings(bindings3);
	}
	
	private void addMBSFParameterBindings(ModuleInputParameterBindings bindingsSet){
		String region = "50/300/10/100";

		bindingsSet.addSemanticType(Resources.mbsf);

		// for GMT psxy
		bindingsSet.addInputBinding("psxy", "S", "c0.04c");
		bindingsSet.addInputBinding("psxy", "R", region);
		bindingsSet.addInputBinding("psxy", "J", "x4c");
		bindingsSet.addInputBinding("psxy", "G", "blue");
		bindingsSet.addInputBinding("psxy", "B", "1");
		bindingsSet.addInputBinding("psxy", "indexOfX", "0");
		bindingsSet.addInputBinding("psxy", "indexOfY", "1");
	}

	private void addGravityDataBindings(ModuleInputParameterBindings bindingsSet){
		String region = "-109/-107/33/34";

		bindingsSet.addSemanticType(Resources.gravityData);

		// for grd2xyz
		bindingsSet.addInputBinding("grd2xyz", "N", "0");

		// for grd2xyz_esri
		bindingsSet.addInputBinding("grd2xyz_esri", "N", "0");

		// for GMT psxy
		bindingsSet.addInputBinding("psxy", "S", "c0.04c");
		bindingsSet.addInputBinding("psxy", "R", region);
		bindingsSet.addInputBinding("psxy", "J", "x4c");
		bindingsSet.addInputBinding("psxy", "G", "blue");
		bindingsSet.addInputBinding("psxy", "B", "1");
		bindingsSet.addInputBinding("psxy", "indexOfX", "0");
		bindingsSet.addInputBinding("psxy", "indexOfY", "1");

		// for GMT psxyz
		bindingsSet.addInputBinding("psxyz", "B", "1/1/50");
		bindingsSet.addInputBinding("psxyz", "J", "x6c");
		bindingsSet.addInputBinding("psxyz", "JZ", "5c");
		bindingsSet.addInputBinding("psxyz", "R", region + "/-300/-100");
		bindingsSet.addInputBinding("psxyz", "E", "200/30");
		bindingsSet.addInputBinding("psxyz", "S", "o0.1");
		bindingsSet.addInputBinding("psxyz", "W", "thinnest");		
		bindingsSet.addInputBinding("psxyz", "G", "lightgray");

		// for GMT nearneighbor
		bindingsSet.addInputBinding("nearneighbor", "I", "0.02");
		bindingsSet.addInputBinding("nearneighbor", "S", "0.2");
		bindingsSet.addInputBinding("nearneighbor", "R", region);

		// for GMT surface
		bindingsSet.addInputBinding("surface", "I","0.02");
		bindingsSet.addInputBinding("surface", "T","0.25");
		bindingsSet.addInputBinding("surface", "C", "0.1");
		bindingsSet.addInputBinding("surface", "R", region);

		// for GMT grdimage
		bindingsSet.addInputBinding("grdimage", "B", "1");
		bindingsSet.addInputBinding("grdimage", "J", "x4c");
		bindingsSet.addInputBinding("grdimage", "C", "hot");
		bindingsSet.addInputBinding("grdimage", "T", "-200/200/10");
		bindingsSet.addInputBinding("grdimage", "R", region);

		// for GMT grdcontour
		bindingsSet.addInputBinding("grdcontour", "C", "10");
		bindingsSet.addInputBinding("grdcontour", "A", "20");
		bindingsSet.addInputBinding("grdcontour", "B", "0.5");
		bindingsSet.addInputBinding("grdcontour", "S", "5");
		bindingsSet.addInputBinding("grdcontour", "J", "x4c");
		bindingsSet.addInputBinding("grdcontour", "Wc", "thinnest,black");
		bindingsSet.addInputBinding("grdcontour", "Wa", "thinnest,black");
	}

	private void addGriddedGravityDataBindings(ModuleInputParameterBindings bindingsSet){
		String region = "-109/-107/33/34";

		bindingsSet.addSemanticType(Resources.griddedGravityData);

		// for grd2xyz
		bindingsSet.addInputBinding("grd2xyz", "N", "0");

		// for grd2xyz_esri
		bindingsSet.addInputBinding("grd2xyz_esri", "N", "0");

		// for GMT psxyz
		bindingsSet.addInputBinding("psxyz", "B", "1/1/50");
		bindingsSet.addInputBinding("psxyz", "J", "x6c");
		bindingsSet.addInputBinding("psxyz", "JZ", "5c");
		bindingsSet.addInputBinding("psxyz", "R", region + "/-300/-100");
		bindingsSet.addInputBinding("psxyz", "E", "200/30");
		bindingsSet.addInputBinding("psxyz", "S", "o0.1");
		bindingsSet.addInputBinding("psxyz", "W", "thinnest");		
		bindingsSet.addInputBinding("psxyz", "G", "lightgray");

		// for GMT grdcontour
		bindingsSet.addInputBinding("grdcontour", "C", "10");
		bindingsSet.addInputBinding("grdcontour", "A", "20");
		bindingsSet.addInputBinding("grdcontour", "B", "0.5");
		bindingsSet.addInputBinding("grdcontour", "S", "5");
		bindingsSet.addInputBinding("grdcontour", "J", "x4c");
		bindingsSet.addInputBinding("grdcontour", "Wc", "thinnest,black");
		bindingsSet.addInputBinding("grdcontour", "Wa", "thinnest,black");

		// for GMT grdimage
		bindingsSet.addInputBinding("grdimage", "B", "1");
		bindingsSet.addInputBinding("grdimage", "J", "x4c");
		bindingsSet.addInputBinding("grdimage", "C", "hot");
		bindingsSet.addInputBinding("grdimage", "T", "-200/200/10");
		bindingsSet.addInputBinding("grdimage", "R", region);		
	}

}
