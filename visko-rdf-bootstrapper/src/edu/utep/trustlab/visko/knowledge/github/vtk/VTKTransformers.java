package edu.utep.trustlab.visko.knowledge.github.vtk;

import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class VTKTransformers {
	public static void create() {
		String name;
		TransformerWriter wtr = new TransformerWriter("floatArrayToShortIntArray");
		wtr.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/BINARYFLOATARRAYLENDIAN.owl#BINARYFLOATARRAYLENDIAN");
		wtr.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/BINARYSHORTINTARRAYLENDIAN.owl#BINARYSHORTINTARRAYLENDIAN");
		name = "Float Array To Short Int Array";
		wtr.setLabel(name);
		wtr.setName(name);
		wtr.saveDocument();

		TransformerWriter wtr1 = new TransformerWriter("shortIntArrayToGriddedData");
		wtr1.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/BINARYSHORTINTARRAYLENDIAN.owl#BINARYSHORTINTARRAYLENDIAN");
		wtr1.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKIMAGEDATA.owl#VTKIMAGEDATA");
		name = "Short Int to Image Data";
		wtr1.setLabel(name);
		wtr1.setName(name);
		wtr1.saveDocument();

		TransformerWriter wtr2 = new TransformerWriter("contourer3D");
		wtr2.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKIMAGEDATA.owl#VTKIMAGEDATA");
		wtr2.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKPOLYDATA.owl#VTKPOLYDATA");
		name = "ImageData to Contours PolyData";
		wtr2.setLabel(name);
		wtr2.setName(name);
		wtr2.setMappedToView("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/iso-surfaces.owl#iso-surfaces");
		wtr2.saveDocument();

		TransformerWriter wtr3 = new TransformerWriter("contoursPolyDataToImageOperator");
		wtr3.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKPOLYDATA.owl#VTKPOLYDATA");
		wtr3.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		name = "Contours PolyData to Image";
		wtr3.setLabel(name);
		wtr3.setName(name);
		wtr3.saveDocument();

		TransformerWriter wtr4 = new TransformerWriter("volume-generator");
		wtr4.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKIMAGEDATA.owl#VTKIMAGEDATA");
		wtr4.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		name = "ImageData to Volume Image JPEG";
		wtr4.setLabel(name);
		wtr4.setName(name);
		wtr4.setMappedToView("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/volume.owl#volume");
		wtr4.saveDocument();

		TransformerWriter wtr5 = new TransformerWriter("intToShortIntOperator");
		wtr5.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/BINARYINTARRAYLENDIAN.owl#BINARYINTARRAYLENDIAN");
		wtr5.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/BINARYSHORTINTARRAYLENDIAN.owl#BINARYSHORTINTARRAYLENDIAN");
		name = "Int Array To Short Int Array";
		wtr5.setLabel(name);
		wtr5.setName(name);
		wtr5.saveDocument();
		
		TransformerWriter wtr6 = new TransformerWriter("floatArrayToGriddedData");
		wtr6.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/BINARYFLOATARRAYLENDIAN.owl#BINARYFLOATARRAYLENDIAN");
		wtr6.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKIMAGEDATA.owl#VTKIMAGEDATA");
		name = "Floats to Image Data";
		wtr6.setLabel(name);
		wtr6.setName(name);
		wtr6.saveDocument();
	}
}