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

	private static final class Resources{
	//formats
	private static final Format netcdf = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/NETCDF.owl#NETCDF");
	private static final Format ps = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/POSTSCRIPT.owl#POSTSCRIPT");

	//views
	private static final VisualizationAbstraction timeSeriesPlot2D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_TimeSeriesPlot);
	private static final VisualizationAbstraction contourMap2D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_ContourMap);
	private static final VisualizationAbstraction rasterMap2D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_RasterMap);

	//data types
	private static final OntResource variableWithLatLon = ModuleWriter.getDataType("http://iridl.ldeo.columbia.edu/ontologies/cf-obj.owl#Variable_with_LatLon");
	private static final OntResource variableWithTime = ModuleWriter.getDataType("http://iridl.ldeo.columbia.edu/ontologies/cf-obj.owl#Variable_with_Time");

	//semantic types
	private static final OntResource brightnessTemperature = ModuleWriter.getDataType("http://giovanni.gsfc.nasa.gov/giovanni-data.owl#BrightnessTemperature");
	private static final OntResource giovanniTimeSeries = ModuleWriter.getDataType("http://giovanni.gsfc.nasa.gov/giovanni-data.owl#Giovanni_Time_Series_Data");
	private static final OntResource griddedGravityData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d12");
	private static final OntResource gravityData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19");
	}

	@Override
	public void populateServices() {
	String wsdlURL = getWSDLURL();

	String operationName = "gsn_csm_contour_map";
	ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
	service1.setComment("Generate contour map from 2D gridded netCDF");
	service1.setLabel(operationName);
	service1.setWSDLURL(wsdlURL);
	service1.setInputFormat(Resources.netcdf);
	service1.setOutputFormat(Resources.ps);
	service1.setView(Resources.contourMap2D);
	service1.setInputDataType(Resources.variableWithLatLon);

	operationName = "gsn_csm_contour_map_raster";
	ModuleOperatorService service2 = getModuleWriter().createNewOperatorService(null, operationName);
	service2.setComment("Generate raster map from 2D gridded netCDF");
	service2.setLabel(operationName);
	service2.setWSDLURL(wsdlURL);
	service2.setInputFormat(Resources.netcdf);
	service2.setOutputFormat(Resources.ps);
	service2.setView(Resources.rasterMap2D);
	service2.setInputDataType(Resources.variableWithLatLon);

	operationName = "gsn_csm_xy2_time_series";
	ModuleOperatorService service3 = getModuleWriter().createNewOperatorService(null, operationName);
	service3.setComment("Generate time series plot from set of 1D arrays");
	service3.setLabel(operationName);
	service3.setWSDLURL(wsdlURL);
	service3.setInputFormat(Resources.netcdf);
	service3.setOutputFormat(Resources.ps);
	service3.setView(Resources.timeSeriesPlot2D);
	service3.setInputDataType(Resources.variableWithTime);
	}

	@Override
	public void populateToolkit() {

	Toolkit toolkit = getModuleWriter().createNewToolkit("ncl");
	toolkit.setLabel("NCAR Command Language");
	toolkit.setComment("NCAR Command Language");
	
	String stringURL = "http://www.ncl.ucar.edu/";
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

	addBrightnessTemperatureBindings(bindings1);
	addGravityDataBindings(bindings2);
	}

	public void addGravityDataBindings(ModuleInputParameterBindings bindingsSet){
	bindingsSet.addSemanticType(Resources.gravityData);
	bindingsSet.addSemanticType(Resources.griddedGravityData);

	String operationName = "gsn_csm_contour_map";
	bindingsSet.addInputBinding(operationName, "lbOrientation", "vertical");
	bindingsSet.addInputBinding(operationName, "cnLevelSpacingF", "10");
	bindingsSet.addInputBinding(operationName, "colorTable", "WhiteBlueGreenYellowRed");
	bindingsSet.addInputBinding(operationName, "font", "helvetica");
	bindingsSet.addInputBinding(operationName, "cnFillOn", "True");
	bindingsSet.addInputBinding(operationName, "cnLinesOn", "False");
	bindingsSet.addInputBinding(operationName, "latVariable", "y");
	bindingsSet.addInputBinding(operationName, "lonVariable", "x");
	bindingsSet.addInputBinding(operationName, "plotVariable", "z");
	bindingsSet.addInputBinding(operationName, "indexOfX", "1");
	bindingsSet.addInputBinding(operationName, "indexOfY", "0");
	bindingsSet.addInputBinding(operationName, "indexOfZ", "-1");

	operationName = "gsn_csm_contour_map_raster";
	bindingsSet.addInputBinding(operationName, "lbOrientation", "vertical");
	bindingsSet.addInputBinding(operationName, "colorTable", "WhiteBlueGreenYellowRed");
	bindingsSet.addInputBinding(operationName, "font", "helvetica");
	bindingsSet.addInputBinding(operationName, "latVariable", "y");
	bindingsSet.addInputBinding(operationName, "lonVariable", "x");
	bindingsSet.addInputBinding(operationName, "plotVariable", "z");
	bindingsSet.addInputBinding(operationName, "indexOfX", "1");
	bindingsSet.addInputBinding(operationName, "indexOfY", "0");
	bindingsSet.addInputBinding(operationName, "indexOfZ", "-1");
	}

	public void addBrightnessTemperatureBindings(ModuleInputParameterBindings bindingsSet) {

	bindingsSet.addSemanticType(Resources.brightnessTemperature);

	String operationName = "gsn_csm_contour_map";
	bindingsSet.addInputBinding(operationName, "plotVariable", "ch4");
	bindingsSet.addInputBinding(operationName, "lbOrientation", "vertical");
	bindingsSet.addInputBinding(operationName, "cnLevelSpacingF", "10");
	bindingsSet.addInputBinding(operationName, "colorTable", "WhiteBlueGreenYellowRed");
	bindingsSet.addInputBinding(operationName, "font", "helvetica");
	bindingsSet.addInputBinding(operationName, "cnFillOn", "True");
	bindingsSet.addInputBinding(operationName, "cnLinesOn", "False");
	bindingsSet.addInputBinding(operationName, "latVariable", "latitude");
	bindingsSet.addInputBinding(operationName, "lonVariable", "longitude");
	bindingsSet.addInputBinding(operationName, "indexOfX", "2");
	bindingsSet.addInputBinding(operationName, "indexOfY", "1");
	bindingsSet.addInputBinding(operationName, "indexOfZ", "0");

	operationName = "gsn_csm_contour_map_raster";
	bindingsSet.addInputBinding(operationName, "plotVariable", "ch4");
	bindingsSet.addInputBinding(operationName, "lbOrientation", "vertical");
	bindingsSet.addInputBinding(operationName, "colorTable", "WhiteBlueGreenYellowRed");
	bindingsSet.addInputBinding(operationName, "font", "helvetica");
	bindingsSet.addInputBinding(operationName, "latVariable", "latitude");
	bindingsSet.addInputBinding(operationName, "lonVariable", "longitude");
	bindingsSet.addInputBinding(operationName, "indexOfX", "2");
	bindingsSet.addInputBinding(operationName, "indexOfY", "1");
	bindingsSet.addInputBinding(operationName, "indexOfZ", "0");
	}
	
}
