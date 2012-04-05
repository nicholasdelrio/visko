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


package edu.utep.trustlab.visko.knowledge.universal.profiles;

import edu.utep.trustlab.visko.knowledge.ncl.NCLServices;
import edu.utep.trustlab.visko.knowledge.ncl.NCLToolkits;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class BrightnessTemperatureProfile {
	public static void create() {
		String documentURL;
		/************ profile for brightness data ******************************/
		String dataTypeURI = "http://giovanni.gsfc.nasa.gov/data/brightness.owl#brightness";

		ToolkitProfileWriter wtr = new ToolkitProfileWriter("brightnessDataProfile");
		wtr.addDataType(dataTypeURI);
		
		String netCDFGridContour = NCLServices.netCDFGridContour.substring(0, NCLServices.netCDFGridContour.indexOf("#"));
		String netCDFGridRaster = NCLServices.netCDFGridRaster.substring(0, NCLServices.netCDFGridRaster.indexOf("#"));
		
		// for netCDFGridContour
		wtr.addInputBinding(netCDFGridContour + "#plotVariable", "ch4");
		wtr.addInputBinding(netCDFGridContour + "#lbOrientation", "vertical");
		wtr.addInputBinding(netCDFGridContour + "#cnLevelSpacingF", "10");
		wtr.addInputBinding(netCDFGridContour + "#colorTable", "WhiteBlueGreenYellowRed");
		wtr.addInputBinding(netCDFGridContour + "#font", "helvetica");
		wtr.addInputBinding(netCDFGridContour + "#cnFillOn", "True");
		wtr.addInputBinding(netCDFGridContour + "#cnLinesOn", "False");

		// for netCDFGridRaster
		wtr.addInputBinding(netCDFGridRaster + "#plotVariable", "ch4");
		wtr.addInputBinding(netCDFGridRaster + "#lbOrientation", "vertical");
		wtr.addInputBinding(netCDFGridRaster + "#colorTable", "WhiteBlueGreenYellowRed");
		wtr.addInputBinding(netCDFGridRaster + "#font", "helvetica");

		wtr.setSupportingToolkit(NCLToolkits.ncl);
		
		documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
