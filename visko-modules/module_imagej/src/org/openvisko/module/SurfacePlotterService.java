package org.openvisko.module;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openvisko.module.operators.ToolkitOperator;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class SurfacePlotterService extends ToolkitOperator{

	public SurfacePlotterService(String netCDFDataURL){	
		super(netCDFDataURL, "image.png", false, false, "surfacePlot.png");
	}
	
	public String transform(
			String plotWidth,
			String polygonMultiplier,			
			
			String showWireframe,
			String showGrayscale,
			String showAxis,
			String whiteBackground,
			String blackFill,
			String smooth
			){
		
		
		ImagePlus image = new ImagePlus(inputPath);
		ImageProcessor processor = image.getProcessor();
		setROI(image, processor);
	
		SurfacePlotter plotter = new SurfacePlotter();
		
		int intPlotWidth = Integer.parseInt(plotWidth);
		int intPolygonMultiplier = Integer.parseInt(polygonMultiplier);
		
		boolean booleanShowWireframe = Boolean.parseBoolean(showWireframe);
		boolean booleanShowGrayscale = Boolean.parseBoolean(showGrayscale);
		boolean booleanShowAxis = Boolean.parseBoolean(showAxis);
		boolean booleanWhiteBackground = Boolean.parseBoolean(whiteBackground);
		boolean booleanBlackFill = Boolean.parseBoolean(blackFill);
		boolean booleanSmooth = Boolean.parseBoolean(smooth);
		
		plotter.setParameters(intPlotWidth, intPolygonMultiplier, booleanShowWireframe, booleanShowGrayscale, booleanShowAxis, booleanWhiteBackground, booleanBlackFill, booleanSmooth);
		plotter.setImage(image);
		
		ImageProcessor newProcessor = plotter.makeSurfacePlot(processor);
	    dumpSurfacePlot(newProcessor);
		
		return outputURL;
	}

	private void dumpSurfacePlot(ImageProcessor processor){
		Image surfacePlot = processor.createImage();
		RenderedImage renderedSurfacePlot = (RenderedImage)surfacePlot;
		
		try {
		    File outputfile = new File(outputPath);
		    ImageIO.write(renderedSurfacePlot, "png", outputfile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setROI(ImagePlus image, ImageProcessor processor){
		int[] dims = image.getDimensions();
		
		Rectangle rectangle = new Rectangle();
		rectangle.setBounds(dims[0], dims[1], dims[2], dims[3]);		
		
		processor.setRoi(rectangle);
	}
	
}//end class 