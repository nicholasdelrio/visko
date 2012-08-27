package edu.utep.trustlab.visko.installation.packages.rdf;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.visko.ontology.operator.Viewer;
import edu.utep.trustlab.visko.ontology.service.Toolkit;

public class PackageWriterTest {
	
	public static void main(String[] args){
		
		ContentManager.setWorkspacePath("C:/Users/Public/git/visko/installation-tools/test-output-rdf/");
		LocalFileSystem local = new LocalFileSystem("http://iw.cs.utep.edu/visko/", "C:/Users/Public/git/visko/installation-tools/test-output-rdf/");
		local.setProjectName("visko-rdf");
		
		PackageWriter writer = new PackageWriter(local.getBaseURL(), "visko-package.owl");
		
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
		
		PackageViewerSet ieExplorer = writer.createNewViewerSet("iexplorer");
		ieExplorer.setLabel("internet explorere");
		ieExplorer.setComment("some thing");
		
		Viewer viewer2 = ieExplorer.createNewViewer("some-gosh-darn-viewer");
		viewer2.setComment("some gosh darn");
		viewer2.setLabel("some gosh darn");
		viewer2.addOperatesOnFormat(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/resources/formats/PDF.owl#PDF"));
		
		Toolkit tk = writer.createNewToolkit("gmt");
		tk.setComment("generic mapping tools");
		tk.setLabel("gen map tool");
		
		String wsdlURL = "http://iw.cs.utep.edu:8080/toolkits/services/ToolkitServices?wsdl";
		PackageOperatorService opService = writer.createNewOperatorService("psxy");
		opService.setComment("copy some colors");
		opService.setLabel("copy color");
		opService.setView(PackageWriter.getView("https://raw.github.com/nicholasdelrio/visko/master/resources/views/contour-lines.owl#contour-lines"));
		opService.setInputFormat(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/NETCDF.owl#NETCDF"));
		opService.setOutputFormat(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/POSTSCRIPT.owl#POSTSCRIPT"));
		opService.setWSDLURL(wsdlURL);

		PackageOperatorService opService1 = writer.createNewOperatorService("grdcontour");
		opService1.setComment("copy some colors");
		opService1.setLabel("copy color");
		opService1.setView(PackageWriter.getView("https://raw.github.com/nicholasdelrio/visko/master/resources/views/contour-lines.owl#contour-lines"));
		opService1.setInputFormat(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/NETCDF.owl#NETCDF"));
		opService1.setOutputFormat(PackageWriter.getFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/POSTSCRIPT.owl#POSTSCRIPT"));
		opService1.setWSDLURL(wsdlURL);
		
		
		PackageInputParameterBindings bindings = writer.createNewInputParameterBindings();
		bindings.addDataType("http://iw.cs.utep.edu/data1");
		bindings.addDataType("http://iw.cs.utep.edu/data2");
		bindings.addInputBinding("grdcontour", "A", "red");
		bindings.addInputBinding("grdcontour", "J", "jedi");
		bindings.addInputBinding("grdcontour", "B", "goodness");
		bindings.addInputBinding("psxy", "B", "bodificition");
		
		PackageInputParameterBindings bindings1 = writer.createNewInputParameterBindings();
		bindings1.addDataType("http://iw.cs.utep.edu/data12");
		bindings1.addDataType("http://iw.cs.utep.edu/data13");
		bindings1.addInputBinding("grdcontour", "A", "arod");
		bindings1.addInputBinding("grdcontour", "J", "jrod");
		bindings1.addInputBinding("grdcontour", "B", "brod");
		
		writer.dumpPackageRDF(local);
	}
}