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


package edu.utep.trustlab.visko.knowledge.ghostscript;

import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;
public class GSServices {
	
	public static String ps2pdf;
	public static String ps2png;
	public static String pdf2png;

	public static void create(String wsdlURL) {
		String operationName = "ps2pdf";
		
		ServiceWriter wtr = new ServiceWriter(operationName);
		wtr.setWSDLURL(wsdlURL);
		wtr.setOperationName(operationName);
		wtr.setLabel("Adobe PostScript to Portable Document Format");
		wtr.setConceptualOperator(GSTransformers.ps2pdf);
		wtr.setSupportingToolkit(GSToolkits.ghostscript);
		System.out.println(wtr.saveDocument());
		ps2pdf = wtr.getURI();
		
		operationName = "ps2png";
		ServiceWriter wtr1 = new ServiceWriter(operationName);
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setOperationName(operationName);
		wtr1.setLabel("Adobe PostScript to Portable Network Graphic");
		wtr1.setConceptualOperator(GSTransformers.ps2png);
		wtr1.setSupportingToolkit(GSToolkits.ghostscript);
		System.out.println(wtr1.saveDocument());
		ps2png = wtr1.getURI();

		operationName = "pdf2png";
		ServiceWriter wtr2 = new ServiceWriter(operationName);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setOperationName(operationName);
		wtr2.setLabel("Adobe Portable Document Format to Portable Network Graphic");
		wtr2.setConceptualOperator(GSTransformers.pdf2png);
		wtr2.setSupportingToolkit(GSToolkits.ghostscript);
		System.out.println(wtr2.saveDocument());
		pdf2png = wtr2.getURI();
	}
}
