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

	static final class Resources {
	//formats
	static final Format littleEndianSequence = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/LITTLE-ENDIAN-SEQUENCE.owl#LITTLE-ENDIAN-SEQUENCE");
	static final Format xml = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/XML.owl#XML");
	static final Format jpeg = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/JPEG.owl#JPEG");
	static final Format spaceSeparatedValues = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES");
	
	// data types
	static final OntResource array1DFloat = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#Array1DFloat");
	static final OntResource array1DInteger = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#Array1DInteger");
	static final OntResource array1DUnsignedShortInteger = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#Array1DUnsignedShortInteger");
	
	static final OntResource vtkImageData3D = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData3D");
	static final OntResource vtkImageData3DUnsignedShortIntegers = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData3DUnsignedShortIntegers");
	static final OntResource vtkImageData3DIntegers = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData3DIntegers");
	static final OntResource vtkImageData3DFloats = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData3DFloats");
	
	static final OntResource vtkImageData2D = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData2D");
	static final OntResource vtkImageData2DUnsignedShortIntegers = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData2DUnsignedShortIntegers");
	static final OntResource vtkImageData2DIntegers = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData2DShortIntegers");
	static final OntResource vtkImageData2DFloats = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData2DFloats");
	
	static final OntResource vtkPolyData = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkPolyData");
	
	//views
	static final VisualizationAbstraction surfacePlot3D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_SurfacePlot);
	static final VisualizationAbstraction contourMap2D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_ContourMap);
	static final VisualizationAbstraction isosurfaces3D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_IsoSurfaceRendering);
	static final VisualizationAbstraction rasterCube3D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_RasterCube);
	static final VisualizationAbstraction volume3D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_VolumeRendering);
	static final VisualizationAbstraction rasterMap2D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_RasterMap);
	static final VisualizationAbstraction pointsPlot3D = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_PointPlot);
	
	//type uris
	static final OntResource gravityData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19");
	
	static final OntResource velocityDataURI_1 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d14-0");
	static final OntResource velocityDataURI_2 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d2");
	static final OntResource velocityDataURI_3 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d2-1");
	
	static final OntResource coverageDataURI = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d7-0");
	static final OntResource griddedTimeURI_1 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d4-0");
	static final OntResource griddedTimeURI_2 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d4");
	
	static final OntResource dusumDataURI_1 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d8-0");
	static final OntResource dusumDataURI_2 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d8");
	
	static String vtkPolyDataMapper = "vtkPolyDataMapper";
	static String vtkVolume = "vtkVolume";
	static String vtkDataObjectToDataSetFilter3DGravity = "vtkDataObjectToDataSetFilter3DGravityData";
	static String vtkShepardMethod = "vtkShepardMethod";
	static String vtkSurfaceReconstructionAndContourFilter = "vtkSurfaceReconstructionAndContourFilter";
	
	static String vtkContourFilter = "vtkContourFilter";
	static String vtkContourFilter2D = vtkContourFilter + "2D";
	static String vtkContourFilter3D = vtkContourFilter + "3D";
	
	static String vtkExtractVOI3D = "vtkExtractVOI3D";
	static String vtkExtractVOIXYPlane = "vtkExtractVOIXYPlane";
	static String vtkExtractVOIXZPlane = "vtkExtractVOIXZPlane";
	static String vtkExtractVOIYZPlane = "vtkExtractVOIYZPlane";
	
	static String vtkImageDataReader3DFloats = "vtkImageDataReader3DFloats";
	static String vtkImageDataReader3DIntegers = "vtkImageDataReader3DIntegers";
	static String vtkImageDataReader3DUnsignedShortIntegers = "vtkImageDataReader3DUnsignedShortIntegers";
	
	static String vtkGlyph3DSphereSource = "vtkGlyph3DSphereSource";
	}
	
	@Override
	public void populateServices() {
	
	String wsdlURL = getWSDLURL();
	ModuleOperatorService service3 = getModuleWriter().createNewOperatorService(null, Resources.vtkPolyDataMapper);
	service3.setInputFormat(Resources.xml);
	service3.setOutputFormat(Resources.jpeg);
	service3.setLabel(Resources.vtkPolyDataMapper);
	service3.setComment("Converts vtkPolyData into JPEG images");
	service3.setWSDLURL(wsdlURL);
	service3.setInputDataType(Resources.vtkPolyData);
	
	ModuleOperatorService service4 = getModuleWriter().createNewOperatorService(null, Resources.vtkVolume);
	service4.setInputFormat(Resources.xml);
	service4.setOutputFormat(Resources.jpeg);
	service4.setView(Resources.volume3D);
	service4.setLabel(Resources.vtkVolume);
	service4.setComment("Convert vtkImageData of short integers into a volume JPEG");
	service4.setWSDLURL(wsdlURL);
	service4.setInputDataType(Resources.vtkImageData3DUnsignedShortIntegers);
	
	ModuleOperatorService service5 = getModuleWriter().createNewOperatorService(null, Resources.vtkDataObjectToDataSetFilter3DGravity);
	service5.setInputFormat(Resources.spaceSeparatedValues);
	service5.setOutputFormat(Resources.xml);
	service5.setLabel(Resources.vtkDataObjectToDataSetFilter3DGravity);
	service5.setComment("Convert PACES gravity dataset into vtkPolyData of 3D Points");
	service5.setWSDLURL(wsdlURL);
	service5.setInputDataType(Resources.gravityData);
	service5.setOutputDataType(Resources.vtkPolyData);
	
	ModuleOperatorService service6 = getModuleWriter().createNewOperatorService(null, Resources.vtkShepardMethod);
	service6.setInputFormat(Resources.xml);
	service6.setOutputFormat(Resources.xml);
	service6.setLabel(Resources.vtkShepardMethod);
	service6.setComment("3D interpolation routine that generates vtkImageData from unstructured points");
	service6.setWSDLURL(wsdlURL);
	service6.setInputDataType(Resources.vtkPolyData);
	service6.setOutputDataType(Resources.vtkImageData3D);
	service6.setAsInterpolator();
	
	ModuleOperatorService service7 = getModuleWriter().createNewOperatorService(null, Resources.vtkSurfaceReconstructionAndContourFilter);
	service7.setInputFormat(Resources.xml);
	service7.setOutputFormat(Resources.xml);
	service7.setLabel(Resources.vtkSurfaceReconstructionAndContourFilter);
	service7.setComment("Generates a vktImageData surface that wraps unstructured vtkPolyData points");
	service7.setWSDLURL(wsdlURL);
	service7.setView(Resources.surfacePlot3D);
	service7.setInputDataType(Resources.vtkPolyData);
	service7.setOutputDataType(Resources.vtkPolyData);
	
	ModuleOperatorService service8 = getModuleWriter().createNewOperatorService(null, Resources.vtkGlyph3DSphereSource);
	service8.setInputFormat(Resources.xml);
	service8.setOutputFormat(Resources.xml);
	service8.setLabel(Resources.vtkGlyph3DSphereSource);
	service8.setComment("Generates a plot of glyphs, where the glyphs have been hard coded to be spheres");
	service8.setWSDLURL(wsdlURL);
	service8.setView(Resources.pointsPlot3D);
	service8.setInputDataType(Resources.vtkPolyData);
	service8.setOutputDataType(Resources.vtkPolyData);
	
	VTKContourFilter.populateVTKContourFilters(wsdlURL, getModuleWriter());
	VTKExtractVOI.populateVTKExtractVOIs(wsdlURL, getModuleWriter());
	VTKImageDataReader.populateVTKImageDataReaders(wsdlURL, getModuleWriter());
	}
	
	@Override
	public void populateToolkit() {
	Toolkit toolkit = getModuleWriter().createNewToolkit("vtk");
	toolkit.setComment("Visualization Toolkit (VTK) Library");
	toolkit.setLabel("Visualization Toolkit (VTK)");
	
	String stringURL = "http://www.vtk.org/";
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
	
	public void addCoverageParameterBindings(ModuleInputParameterBindings bindingsSet){
	bindingsSet.addSemanticType(Resources.coverageDataURI);
	
	// for vtkImageReader integers
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DIntegers, "littleEndian","true");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DIntegers, "dataOrigin", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DIntegers, "dataSpacing", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DIntegers, "dataExtent", "0/229/0/24/0/67");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DIntegers, "numScalarComponents", "1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DIntegers, "readLowerLeft","true");
	
	// for vtkImageReader unsigned short integers
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "littleEndian","true");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataOrigin", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataSpacing", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataExtent", "0/229/0/24/0/67");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "numScalarComponents", "1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "readLowerLeft","true");
	
	// for vtkExtractVOI
	bindingsSet.addInputBinding(Resources.vtkExtractVOI3D, "dataExtent", "0/229/0/24/0/67");
	
	// for vtkExtractVOIXY
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXYPlane, "dataExtent", "0/229/0/24/30");
	
	// for vtkExtractVOIXZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXZPlane, "dataExtent", "0/229/15/0/67");
	
	// for vtkExtractVOIYZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIYZPlane, "dataExtent", "100/0/24/0/67");
	
	// for vtkContourFilter
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "numContours", "10");
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "scalarRange", "0.0/40.0");
	
	// for vtkPolyDataMapper
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "scalarRange","0.0/40.0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "xRotation", "105");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "size", "400/300");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "backgroundColor", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "magnification", "3");
	
	// for vtkVolume
	bindingsSet.addInputBinding(Resources.vtkVolume, "xRotation", "105");
	bindingsSet.addInputBinding(Resources.vtkVolume, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "size", "400/300");
	bindingsSet.addInputBinding(Resources.vtkVolume, "backgroundColor", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "magnification", "3");
	bindingsSet.addInputBinding(Resources.vtkVolume, "colorFunction", "20,1.0,0.0,0.3/80,1.0,0.0,0.3");
	bindingsSet.addInputBinding(Resources.vtkVolume, "opacityFunction", "0,0.0/40,1.0");
	}
	
	public void addGriddedTimeParameterBindings(ModuleInputParameterBindings bindingsSet){
	bindingsSet.addSemanticType(Resources.griddedTimeURI_1);
	bindingsSet.addSemanticType(Resources.griddedTimeURI_2);
	
	// for vtkImageReader floats
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "littleEndian", "true");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataOrigin", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataSpacing", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataExtent", "0/230/0/25/0/68");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "numScalarComponents", "1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "readLowerLeft", "true");
	
	// for vtkImageReader unsigned short ints
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "littleEndian", "true");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataOrigin", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataSpacing", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataExtent", "0/230/0/25/0/68");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "numScalarComponents", "1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "readLowerLeft", "true");
	
	// for vtkExtractVOI
	bindingsSet.addInputBinding(Resources.vtkExtractVOI3D, "dataExtent", "0/230/0/25/0/68");
	
	// for vtkExtractVOIXY
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXYPlane, "dataExtent", "0/230/0/25/30");
	
	// for vtkExtractVOIXZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXZPlane, "dataExtent", "0/230/15/0/68");
	
	// for vtkExtractVOIYZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIYZPlane, "dataExtent", "100/0/25/0/68");
	
	// for vtkContourFilter
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "numContours", "30");
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "scalarRange", "0.0/30315.0");
	
	// for vtkPolyDataMapper
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "scalarRange", "0.0/30315.0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "xRotation", "105");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "size","400/300");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "backgroundColor", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "magnification", "3");
	
	// for vtkVolume
	bindingsSet.addInputBinding(Resources.vtkVolume, "xRotation", "105");
	bindingsSet.addInputBinding(Resources.vtkVolume, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "size", "400/300");
	bindingsSet.addInputBinding(Resources.vtkVolume, "backgroundColor", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkVolume, "magnification", "3");
	bindingsSet.addInputBinding(Resources.vtkVolume, "colorFunction", "0.0,0.0,0.0,0.0/1000.0,1.0,0.0,0.0/3000.0,0.0,0.0,1.0/5000.0,0.0,1.0,0.0/7000.0,0.0,0.2,0.0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "opacityFunction", "20,0.0/255,0.2");
	}
	
	public void addDuSumParameterBindings(ModuleInputParameterBindings bindingsSet){
	bindingsSet.addSemanticType(Resources.dusumDataURI_1);
	bindingsSet.addSemanticType(Resources.dusumDataURI_2);
	
	// for vtkImageReader floats
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "littleEndian", "true");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataOrigin", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataSpacing", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataExtent", "0/229/0/24/0/67");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "numScalarComponents", "1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "readLowerLeft", "true");
	
	// for vtkImageReader unsigned short integers
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "littleEndian", "true");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataOrigin", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataSpacing", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataExtent", "0/229/0/24/0/67");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "numScalarComponents", "1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "readLowerLeft", "true");
	
	// for vtkExtractVOI
	bindingsSet.addInputBinding(Resources.vtkExtractVOI3D, "dataExtent", "0/229/0/24/0/67");
	
	// for vtkExtractVOIXY
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXYPlane, "dataExtent", "0/229/0/24/30");
	
	// for vtkExtractVOIXZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXZPlane, "dataExtent", "0/229/15/0/67");
	
	// for vtkExtractVOIYZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIYZPlane, "dataExtent", "100/0/24/0/67");
	
	// for vtkContourFilter
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "numContours", "35");
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "scalarRange", "9997.0/10014.0");
	
	// for vtkPolyDataMapper
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "scalarRange", "9997.0/10014.0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "xRotation", "90");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "size", "400/300");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "backgroundColor", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "magnification", "3");
	
	// for vtkVolume
	bindingsSet.addInputBinding(Resources.vtkVolume, "xRotation", "105");
	bindingsSet.addInputBinding(Resources.vtkVolume, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "size", "400/300");
	bindingsSet.addInputBinding(Resources.vtkVolume, "backgroundColor", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkVolume, "magnification", "3");
	bindingsSet.addInputBinding(Resources.vtkVolume, "colorFunction", "0.0,0.0,0.0,0.0/1000.0,1.0,0.0,0.0/3000.0,0.0,0.0,1.0/5000.0,0.0,1.0,0.0/7000.0,0.0,0.2,0.0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "opacityFunction", "20,0.0/255,0.2");
	}
	
	public void addVelocityParameterBindings(ModuleInputParameterBindings bindingsSet) {
	bindingsSet.addSemanticType(Resources.velocityDataURI_1);
	bindingsSet.addSemanticType(Resources.velocityDataURI_2);
	bindingsSet.addSemanticType(Resources.velocityDataURI_3);
	
	// for vtkImageReader unsigned short integers
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "littleEndian", "true");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataOrigin", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataSpacing", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "dataExtent", "0/230/0/25/0/68");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "numScalarComponents", "1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DUnsignedShortIntegers, "readLowerLeft","true");
	
	// for vtkImageReader floats
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "littleEndian", "true");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataOrigin", "0/0/0");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataSpacing", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "dataExtent", "0/230/0/25/0/68");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "numScalarComponents", "1");
	bindingsSet.addInputBinding(Resources.vtkImageDataReader3DFloats, "readLowerLeft","true");
	
	// for vtkExtractVOI
	bindingsSet.addInputBinding(Resources.vtkExtractVOI3D, "dataExtent", "0/230/0/25/0/68");
	
	// for vtkExtractVOIXY
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXYPlane, "dataExtent", "0/230/0/25/30");
	
	// for vtkExtractVOIXZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXZPlane, "dataExtent", "0/230/15/0/68");
	
	// for vtkExtractVOIYZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIYZPlane, "dataExtent", "100/0/25/0/68");
	
	// for vtkContourFilter
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "numContours", "35");
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "scalarRange", "0.0/9000.0");
	
	// for vtkPolyDataMapper
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "scalarRange", "0.0/9000.0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "xRotation", "105");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "size", "400/300");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "backgroundColor", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "magnification", "3");
	
	// for vtkVolume
	bindingsSet.addInputBinding(Resources.vtkVolume, "xRotation", "105");
	bindingsSet.addInputBinding(Resources.vtkVolume, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "size", "400/300");
	bindingsSet.addInputBinding(Resources.vtkVolume, "backgroundColor", "1/1/1");
	bindingsSet.addInputBinding(Resources.vtkVolume, "magnification", "3");
	bindingsSet.addInputBinding(Resources.vtkVolume, "colorFunction", "3000,1,1,0/5000,0.5,0.95,0/5600,0,0,1/6500,0.28,0.2,0.5/7000,1,0,0");
	bindingsSet.addInputBinding(Resources.vtkVolume, "opacityFunction", "4000,0.2/8000,0.5");
	}
	
	public void addGravityDataParameterBindings(ModuleInputParameterBindings bindingsSet) {
	bindingsSet.addSemanticType(Resources.gravityData);
	
	// for vtkShepardMethod
	bindingsSet.addInputBinding(Resources.vtkShepardMethod, "sampleDimensions", "30/30/10");
	bindingsSet.addInputBinding(Resources.vtkShepardMethod, "maximumDistance", "0.2");
	
	// for vtkExtractVOI
	bindingsSet.addInputBinding(Resources.vtkExtractVOI3D, "dataExtent", "0/29/0/29/0/9");
	
	// for vtkExtractVOIXY
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXYPlane, "dataExtent", "0/29/0/29/3");
	
	// for vtkExtractVOIXZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIXZPlane, "dataExtent", "0/29/15/0/9");
	
	// for vtkExtractVOIYZ
	bindingsSet.addInputBinding(Resources.vtkExtractVOIYZPlane, "dataExtent", "15/0/29/0/9");
	
	
	// for vtkContourFilter 2D
	bindingsSet.addInputBinding(Resources.vtkContourFilter2D, "numContours", "20");
	bindingsSet.addInputBinding(Resources.vtkContourFilter2D, "scalarRange", "-236/-177");
	
	// for vtkContourFilter 3D
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "numContours", "20");
	bindingsSet.addInputBinding(Resources.vtkContourFilter3D, "scalarRange", "-236/-177");
	
	// for vtkPolyDataMapper
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "scalarRange", "-236/-177");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "xRotation", "-30");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "yRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "zRotation", "0");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "size", "400/300");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "backgroundColor", "0.5/0.5/0.5");
	bindingsSet.addInputBinding(Resources.vtkPolyDataMapper, "magnification", "3");
	
	// for vtkGlyph3DShereSource
	bindingsSet.addInputBinding(Resources.vtkGlyph3DSphereSource, "radius", "0.06");
	bindingsSet.addInputBinding(Resources.vtkGlyph3DSphereSource, "scaleFactor", "0.25");
	}
	
	
	@Override
	public void populateParameterBindings() {
	ModuleInputParameterBindings bindings1 = getModuleWriter().createNewInputParameterBindings();
	ModuleInputParameterBindings bindings2 = getModuleWriter().createNewInputParameterBindings();
	ModuleInputParameterBindings bindings3 = getModuleWriter().createNewInputParameterBindings();
	ModuleInputParameterBindings bindings4 = getModuleWriter().createNewInputParameterBindings();
	ModuleInputParameterBindings bindings5 = getModuleWriter().createNewInputParameterBindings();
	
	addCoverageParameterBindings(bindings1);
	addDuSumParameterBindings(bindings2);
	addVelocityParameterBindings(bindings3);
	addGriddedTimeParameterBindings(bindings4);
	addGravityDataParameterBindings(bindings5);
	}	
}