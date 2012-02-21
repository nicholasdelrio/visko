package edu.utep.trustlab.visko.knowledge.github.scenarios;



import edu.utep.trustlab.repository.CIServer;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;
import edu.utep.trustlab.visko.ontology.operator.writer.ViewerSetWriter;
import edu.utep.trustlab.visko.ontology.operator.writer.ViewerWriter;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;
import edu.utep.trustlab.visko.ontology.view.writer.ViewContourWriter;
import edu.utep.trustlab.visko.ontology.view.writer.ViewPointsWriter;
import edu.utep.trustlab.visko.ontology.view.writer.ViewRasterWriter;

public class GravityMapScenario {

	/**
	 * This program generates all the VisKo Visualization Knowledge (i.e., RDF
	 * docs) needed to support the answering of Visualization Queries related to
	 * the "Gravity Map" scenario. The Gravity Map Scenario is the generation of
	 * contours, raster, and 2D Plot visualizations of gravity data encoded in
	 * tabular ASCII files.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/*****************************************************************************************************************
		 * Set CI-Server Context. This will configure VisKo to dump our RDF docs
		 * to the CI-Server designated in the code The VisKo API assumes that if
		 * you are dumping to a CI-Server, then then three projects have been
		 * created on the server: visko-view, visko-operator, and visko-service
		 *****************************************************************************************************************/
		String serverURL = null; // replace, example: http://rio.cs.utep.edu/ciserver
		String serverProject = null; // replace, example: visko
		String userName = null; // replace
		String password = null; // replace

		Repository ciServer = new CIServer(serverURL, serverProject, userName, password);

		Repository.setRepository(NickConfigurations.getCIServer());
		/****************************************************************************************************************
		 * We need to generate knowledge about the different kinds of views that
		 * our gravity data can be visualized as. This will include isolines
		 * (i.e., contour maps), raster maps, and 2D Plots
		 ****************************************************************************************************************/
		String contourViewURI;
		String pointsViewURI;
		String rasterViewURI;

		// ViewContourWriter generates an instance of Contour Map concept
		// http://www.ordnancesurvey.co.uk/ontology/Datatypes.owl#Contour
		ViewContourWriter wtr = new ViewContourWriter("contour-lines");
		wtr.setLabel("Contour Lines (Isolines)");
		System.out.println(wtr.saveDocument());
		contourViewURI = wtr.getURI();

		// ViewPointsWriter generates an instance of Points View Geometry
		// Concept http://www.ordnancesurvey.co.uk/ontology/Datatypes.owl#Point
		ViewPointsWriter wtr1 = new ViewPointsWriter("plot-2D");
		wtr1.setLabel("2D Dimensional Plot");
		System.out.println(wtr1.saveDocument());
		pointsViewURI = wtr1.getURI();

		// ViewRasterWriter generates an instance of Raster Map View Concept
		// http://trust.utep.edu/visko/ontology/visko-view-v3.owl#Raster
		ViewRasterWriter wtr2 = new ViewRasterWriter("raster");
		wtr2.setLabel("Raster");
		System.out.println(wtr2.saveDocument());
		rasterViewURI = wtr2.getURI();

		/***************************************************************************************************************
		 * We now will create some simple ViewerSets that we know support
		 * viewers that can display images of our visualized data. For example,
		 * Mozilla Firefox is a Web browser that has many viewers for images and
		 * PDF docs
		 ***************************************************************************************************************/
		// ViewerSetWriter generates instances of ViewerSets
		// http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#ViewerSet
		String firefoxViewerSetURI;
		ViewerSetWriter wtr3 = new ViewerSetWriter("mozilla-firefox");
		wtr3.setLabel("Mozilla Firefox");
		System.out.println(wtr3.saveDocument());
		firefoxViewerSetURI = wtr3.getURI();

		/***************************************************************************************************************
		 * We now will create some simple Viewers and add them to our ViewerSet,
		 * in this case, Mozilla Firefox
		 ***************************************************************************************************************/
		// Mozilla Browser certainly render HTML. ViewerWriter Concept
		// http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Viewer
		ViewerWriter wtr4 = new ViewerWriter("html-viewer1");
		wtr4.addPartOfSetURI(firefoxViewerSetURI); // set this view to be part
													// of Mozilla Firefox
													// ViewerSet
		wtr4.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/HTML.owl#HTML"); // operates
																								// on
																								// HTML
																								// format
		wtr4.setLabel("HyperText Markup Language (HTML) Viewer");
		wtr4.setViewerComment("Renders HTML documents.");
		System.out.println(wtr4.saveDocument());

		// PDF Viewer is usually a PlugIn in most browser, so we will add it to
		// our FireFox ViewerSet
		ViewerWriter wtr5 = new ViewerWriter("pdf-viewer1");
		wtr5.addPartOfSetURI(firefoxViewerSetURI);
		wtr5.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PDF.owl#PDF"); // operates
																								// on
																								// PDF
																								// format
		wtr5.setLabel("Adobe Portable Document Format (PDF) Viewer");
		wtr5.setViewerComment("Renders PDF document and allows for zooming.");
		System.out.println(wtr5.saveDocument());

		// Plain Text Viewer
		ViewerWriter wtr6 = new ViewerWriter("plain-text-viewer1");
		wtr6.addPartOfSetURI(firefoxViewerSetURI);
		wtr6.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PLAIN.owl#PLAIN");
		wtr6.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PLAINTEXT.owl#PLAINTEXT");
		wtr6.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VNDLATEXZ.owl#VNDLATEXZ");
		wtr6.setLabel("Plain Text Viewer");
		wtr6.setViewerComment("Does nothing really, just extracts the text from the PML conclusion...");
		System.out.println(wtr6.saveDocument());

		// Image Viewer that most Browser Support
		ViewerWriter wtr7 = new ViewerWriter("browser-image-viewer1");
		wtr7.addPartOfSetURI(firefoxViewerSetURI);
		wtr7.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/GIF.owl#GIF"); // operates
																								// on
																								// GIF
		wtr7.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG"); // operates
																								// on
																								// PNG
		wtr7.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG"); // operates
																								// on
																								// JPEG
		wtr7.setLabel("Web Browser Image Viewer");
		wtr7.setViewerComment("Views a few standard image formats");
		System.out.println(wtr7.saveDocument());

		/*************************************************************************************************************
		 * This portion of code generates the conceptual transformers and
		 * mappers needed to support a visualization pipeline that can visualize
		 * our gravity map data in the required views. Because our gravity data
		 * is non-gridded, we must include operators that perform different
		 * kinds of gridding and Mappers that can extract the view geometries
		 * from the gridded data
		 *************************************************************************************************************/
		String name;

		// view mapper URIs
		String contourMapperURI;
		String plotterMapperURI;
		String rasterMapperURI;

		// general Transformer URIs
		String nearNeighborGridderTransformerURI;
		String surfaceGridderTransformerURI;

		// A contour map operator that is an instance of a Mapper
		// http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Mapper
		TransformerWriter wtr8 = new TransformerWriter("contour1");
		wtr8.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID"); // operates
																											// on
																											// ESRIGRID
																											// format
		wtr8.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT"); // output
																												// PostScript
																												// format
		name = "contour";
		wtr8.setLabel(name);
		wtr8.setName(name);
		wtr8.setMappedToView(contourViewURI); // this mapper generates the
												// contour map view
		System.out.println(wtr8.saveDocument());
		contourMapperURI = wtr8.getURI();

		// A 2D Plotter Mapper
		TransformerWriter wtr9 = new TransformerWriter("plotter1");
		wtr9.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/TABDELIMITEDDATASET.owl#TABDELIMITEDDATASET"); // operates
																																// on
																																// tabular
																																// ascii
																																// data
		wtr9.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT"); // outputs
																												// PostScript
																												// format
		name = "2D plotter";
		wtr9.setLabel(name);
		wtr9.setName(name);
		wtr9.setMappedToView(pointsViewURI); // generates 2DPoints View
		System.out.println(wtr9.saveDocument());
		plotterMapperURI = wtr9.getURI();

		// A Raster Mapper
		TransformerWriter wtr10 = new TransformerWriter(
				"grid-to-colored-image1");
		wtr10.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID"); // operates
																											// on
																											// ESRIGrid
		wtr10.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT"); // outputs
																												// PostScript
		name = "Color Coded Gridded Dataset";
		wtr10.setLabel(name);
		wtr10.setName(name);
		wtr10.setMappedToView(rasterViewURI); // generates raster map view
		System.out.println(wtr10.saveDocument());
		rasterMapperURI = wtr10.getURI();

		// A Surface Gridder Transformer (i.e., not a mapper, does not generate
		// view)
		// http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Transformer
		TransformerWriter wtr11 = new TransformerWriter("grid-by-surface1");
		wtr11.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/TABDELIMITEDDATASET.owl#TABDELIMITEDDATASET");
		wtr11.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID");
		name = "Grid By Surface Tension";
		wtr11.setLabel(name);
		wtr11.setName(name);
		System.out.println(wtr11.saveDocument());
		surfaceGridderTransformerURI = wtr11.getURI();

		// Another Gridder by way of the NearNeighbor algorithm
		TransformerWriter wtr12 = new TransformerWriter(
				"grid-by-near-neighbor1");
		wtr12.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/TABDELIMITEDDATASET.owl#TABDELIMITEDDATASET");
		wtr12.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID");
		name = "Grid By Nearneighbor";
		wtr12.setLabel(name);
		wtr12.setName(name);
		System.out.println(wtr12.saveDocument());
		nearNeighborGridderTransformerURI = wtr12.getURI();

		/******************************************************************************************************************
		 * This portion of code will create the Visualization Toolkit
		 * Description. In this example, we will generate a description of the
		 * Generic Mapping Tools (GMT) GIS Visualization Toolkit
		 ******************************************************************************************************************/
		String gmtToolkitURI;
		ToolkitWriter wtr13 = new ToolkitWriter("gmt1");
		wtr13.setLabel("Generic Mapping Tools");
		System.out.println(wtr13.saveDocument());
		gmtToolkitURI = wtr13.getURI();

		/******************************************************************************************************************
		 * This portion of the code will generate the executable service
		 * descriptions and associate the executable services to the conceptual
		 * operator descriptions generated in the previous code segment. All the
		 * services are supported by Generic Mapping Tools (GMT) GIS
		 * visualization toolkit
		 ******************************************************************************************************************/
		// URL of the WSDL document describing the interface of the
		// visualization Web services
		String wsdlURL = "http://iw.cs.utep.edu:8080/GMT-services/services/GMTGravityTransformersUsingParameters.GMTGravityTransformersUsingParametersPort?wsdl";

		String operationName = "ESRIGriddedToContourMapPS";
		ServiceWriter wtr14 = new ServiceWriter(operationName + 1);
		wtr14.setLabel(operationName);
		wtr14.setOperationName(operationName);
		wtr14.setWSDLURL(wsdlURL);
		wtr14.setConceptualOperator(contourMapperURI); // set conceptual
														// operator this service
														// is implementing
		wtr14.setSupportingToolkit(gmtToolkitURI); // set supporting Toolkit to
													// GMT
		wtr14.saveDocument();

		operationName = "GravityASCIIPointsTo2DPlotPS";
		ServiceWriter wtr15 = new ServiceWriter(operationName + 1);
		wtr15.setOperationName(operationName);
		wtr15.setWSDLURL(wsdlURL);
		wtr15.setOperationName(operationName);
		wtr15.setLabel(operationName);
		wtr15.setConceptualOperator(plotterMapperURI); // set conceptual
														// operator this service
														// is implementing
		wtr15.setSupportingToolkit(gmtToolkitURI); // set supporting Toolkit to
													// GMT
		wtr15.saveDocument();

		operationName = "ESRIGriddedToColoredImagePS";
		ServiceWriter wtr16 = new ServiceWriter(operationName + 1);
		wtr16.setOperationName(operationName);
		wtr16.setWSDLURL(wsdlURL);
		wtr16.setLabel(operationName);
		wtr16.setConceptualOperator(rasterMapperURI); // set conceptual operator
														// this service is
														// implementing
		wtr16.setSupportingToolkit(gmtToolkitURI); // set supporting Toolkit to
													// GMT
		wtr16.saveDocument();

		operationName = "GravityASCIIPointsToMinCurvatureESRIGridded";
		ServiceWriter wtr17 = new ServiceWriter(operationName + 1);
		wtr17.setWSDLURL(wsdlURL);
		wtr17.setLabel(operationName);
		wtr17.setOperationName(operationName);
		wtr17.setConceptualOperator(surfaceGridderTransformerURI); // set
																	// conceptual
																	// operator
																	// this
																	// service
																	// is
																	// implementing
		wtr17.setSupportingToolkit(gmtToolkitURI); // set supporting Toolkit to
													// GMT
		wtr17.saveDocument();

		operationName = "GravityASCIIPointsToNearNeightborESRIGridded";
		ServiceWriter wtr18 = new ServiceWriter(operationName + 1);
		wtr18.setWSDLURL(wsdlURL);
		wtr18.setLabel(operationName);
		wtr18.setOperationName(operationName);
		wtr18.setConceptualOperator(nearNeighborGridderTransformerURI);
		wtr18.setSupportingToolkit(gmtToolkitURI);
		wtr18.saveDocument();

		/*************************************************************************************************************************
		 * This portion of code provides a default set of parameter bindings for
		 * the Gravity Map Scenario, specifically for gravity data. We tag a
		 * specific kind of data with a URI, in the case of gravity data:
		 * http://
		 * rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling
		 * .owl#d19. If users don't specify specific values, such as color, to
		 * be set in the query the system will roll back to using these defaults
		 * when they are visualizing data of type gravity.
		 *************************************************************************************************************************/
		// gravity data profile
		String gravityDataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19";

		// generate an instance of a ToolkitProfile
		// http://trust.utep.edu/visko/ontology/visko-service-v3.owl#ToolkitProfile
		ToolkitProfileWriter wtr19 = new ToolkitProfileWriter(
				"gravityDataProfile");
		wtr19.setSupportingToolkit(gmtToolkitURI);
		wtr19.addDataType(gravityDataTypeURI);

		// for 2D plotter
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS1.owl#S",
				"c0.04c");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS1.owl#J",
				"x4c");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS1.owl#G",
				"blue");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS1.owl#B",
				"1");

		// for nearneightbor gridding
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToNearNeightborESRIGridded1.owl#I",
				"0.02");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToNearNeightborESRIGridded1.owl#S",
				"0.2");

		// for gridding by surface
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded1.owl#I",
				"0.02");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded1.owl#T",
				"0.25");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded1.owl#C",
				"0.1");

		// for grid to colored image
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#B",
				"1");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#J",
				"x4c");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#C",
				"hot");

		// for contour map
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#C",
				"10");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#A",
				"20");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#B",
				"0.5");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#S",
				"5");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#J",
				"x4c");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#Wc",
				"thinnest,black");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#Wa",
				"thinnest,black");

		// for esriGridContour
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridContour.owl#lbOrientation",
				"vertical");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridContour.owl#cnLevelSpacingF",
				"10");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridContour.owl#colorTable",
				"WhiteBlueGreenYellowRed");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridContour.owl#font",
				"helvetica");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridContour.owl#cnFillOn",
				"True");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridContour.owl#cnLinesOn",
				"False");

		// for esriGridRaster
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridRaster.owl#lbOrientation",
				"vertical");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridRaster.owl#colorTable",
				"WhiteBlueGreenYellowRed");
		wtr19.addInputBinding(
				"http://trust.utep.edu/visko/services/esriGridRaster.owl#font",
				"helvetica");

		// dump document
		System.out.println(wtr19.saveDocument());
	}
}