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


package edu.utep.trustlab.visko.knowledge.ncl;

import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class NCLServices {

	public static String netCDFGridContour;
	public static String netCDFGridRaster;
	public static String netCDFTimeSeries;
	
	public static void create(String wsdlURL) {
		
		String operationName = "gsn_csm_contour_map";
		ServiceWriter wtr2 = new ServiceWriter(operationName);
		wtr2.setLabel(operationName);
		wtr2.setOperationName(operationName);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setConceptualOperator(NCLTransformers.netCDFContourer);
		wtr2.setSupportingToolkit(NCLToolkits.ncl);
		wtr2.saveDocument();
		System.out.println(wtr2.getURI());
		netCDFGridContour = wtr2.getURI();
		
		operationName = "gsn_csm_contour_map_raster";
		ServiceWriter wtr3 = new ServiceWriter(operationName);
		wtr3.setLabel(operationName);
		wtr3.setOperationName(operationName);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setConceptualOperator(NCLTransformers.netCDFRasterer);
		wtr3.setSupportingToolkit(NCLToolkits.ncl);
		wtr3.saveDocument();
		System.out.println(wtr3.getURI());
		netCDFGridRaster = wtr3.getURI();

		operationName = "gsn_csm_xy2_time_series";
		ServiceWriter wtr4 = new ServiceWriter(operationName);
		wtr4.setLabel(operationName);
		wtr4.setOperationName(operationName);
		wtr4.setWSDLURL(wsdlURL);
		wtr4.setConceptualOperator(NCLTransformers.netCDFTimeSeriesPlotter);
		wtr4.setSupportingToolkit(NCLToolkits.ncl);
		wtr4.saveDocument();
		System.out.println(wtr4.getURI());
		netCDFTimeSeries = wtr4.getURI();
	}
}
