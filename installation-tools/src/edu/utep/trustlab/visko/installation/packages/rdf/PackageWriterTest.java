package edu.utep.trustlab.visko.installation.packages.rdf;

import edu.utep.trustlab.visko.ontology.operator.Viewer;
import edu.utep.trustlab.visko.ontology.service.Toolkit;

public class PackageWriterTest {
	
	public static void main(String[] args){
		PackageWriter writer = new PackageWriter("http://somedomain/test/", "visko-package.owl");
		
		Toolkit toolkit = writer.createNewToolkit("gmt");
		toolkit.setComment("just a stinkin gmt toolkit");
		toolkit.setLabel("generic mapping tools");
		
		PackageViewerSet viewerSet = writer.createNewViewerSet("mozilla");
		viewerSet.setLabel("mozilla firefox");
		viewerSet.setComment("just a test mozilla firefox");
		
		Viewer viewer = viewerSet.createNewViewer("broswer-image-viewer");
		viewer.setComment("views jpegs and such");
		viewer.setLabel("browser image viewer");
		viewer.addOperatesOnFormat(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/resources/formats/AMRWB.owl#AMRWB"));
		viewer.addOperatesOnFormat(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/resources/formats/AMRWB.owl#AMRWB"));
	
		Viewer viewer1 = viewerSet.createNewViewer("pdf-viewer");
		viewer1.setComment("views pdfs");
		viewer1.setLabel("pdf viewer");
		viewer1.addOperatesOnFormat(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/resources/formats/PDF.owl#PDF"));
		
		viewerSet.addToModel();
		
		Toolkit tk = writer.createNewToolkit("gmt");
		tk.setComment("generic mapping tools");
		tk.setLabel("gen map tool");
		
		String wsdlURL = "http://geobrain.laits.gmu.edu/axis/services/Raster_ChangeColortable_Copy?wsdl";
		PackageOperatorService opService = writer.createNewOperatorService("copyColor");
		opService.setComment("copy some colors");
		opService.setLabel("copy color");
		opService.setViewURI("https://raw.github.com/nicholasdelrio/visko/master/resources/views/contour-lines.owl#contour-lines");
		opService.setInputFormatURI(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/NETCDF.owl#NETCDF"));
		opService.setOutputFormatURI(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/POSTSCRIPT.owl#POSTSCRIPT"));
		opService.setWSDLURL(wsdlURL);
		
		opService.addToModel();
		
		String viskoRDF = writer.getRDFString()[0];
		String owlsRDF = writer.getRDFString()[1];
		
		System.out.println(viskoRDF);
		System.out.println(owlsRDF);
	}
}
