package edu.utep.trustlab.visko.knowledge.github.vtk;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class Transformers {
	public static void main(String[] args) {
		String name;

		Repository.setRepository(NickConfigurations.getCIServer());
		/*
		 * TransformerWriter wtr = new
		 * TransformerWriter("floatToShortThrOperator"); wtr.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/BINARYFLOATARRAY.owl#BINARYFLOATARRAY"
		 * ); wtr.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/BINARYSHORTINTARRAY.owl#BINARYSHORTINTARRAY"
		 * ); name = "Float Array To Shorts"; wtr.setLabel(name);
		 * wtr.setName(name); wtr.dumpViskoDocumentOnCIServer();
		 * 
		 * TransformerWriter wtr1 = new
		 * TransformerWriter("shortIntToImageDataOperator");
		 * wtr1.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/BINARYSHORTINTARRAY.owl#BINARYSHORTINTARRAY"
		 * ); wtr1.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKIMAGEDATA.owl#VTKIMAGEDATA"
		 * ); name = "Short Int to VTK Image Data"; wtr1.setLabel(name);
		 * wtr1.setName(name); wtr1.dumpViskoDocumentOnCIServer();
		 * 
		 * TransformerWriter wtr2 = new
		 * TransformerWriter("imageDataToContoursPolyDataOperator");
		 * wtr2.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKIMAGEDATA.owl#VTKIMAGEDATA"
		 * ); wtr2.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKPOLYDATA.owl#VTKPOLYDATA"
		 * ); name = "VTK ImageData to Contours PolyData"; wtr2.setLabel(name);
		 * wtr2.setName(name); wtr2.setMappedToView(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/iso-surfaces1.owl#iso-surfaces1"
		 * ); wtr2.dumpViskoDocumentOnCIServer();
		 * 
		 * TransformerWriter wtr3 = new
		 * TransformerWriter("contoursPolyDataToImageOperator");
		 * wtr3.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKPOLYDATA.owl#VTKPOLYDATA"
		 * ); wtr3.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		 * name = "VTK Contours PolyData to Image"; wtr3.setLabel(name);
		 * wtr3.setName(name); wtr3.dumpViskoDocumentOnCIServer();
		 * 
		 * TransformerWriter wtr4 = new
		 * TransformerWriter("imageDataToVolumeOperator"); wtr4.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/VTKIMAGEDATA.owl#VTKIMAGEDATA"
		 * ); wtr4.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		 * name = "VTK ImageData to Volume Image JPEG"; wtr4.setLabel(name);
		 * wtr4.setName(name); wtr4.setMappedToView(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/volume1.owl#volume1"
		 * ); wtr4.dumpViskoDocumentOnCIServer();
		 */

		TransformerWriter wtr = new TransformerWriter("intToShortIntOperator");
		wtr.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/BINARYINTARRAY.owl#BINARYINTARRAY");
		wtr.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/BINARYSHORTINTARRAY.owl#BINARYSHORTINTARRAY");
		name = "Int Array To Short Ints";
		wtr.setLabel(name);
		wtr.setName(name);
		wtr.saveDocument();
	}
}
