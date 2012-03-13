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
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/GMT-services/services/GMTGravityTransformersUsingParameters.GMTGravityTransformersUsingParametersPort?wsdl";

	public static void create() {
		String operationName = "PSToPDF";
		
		/*
		ServiceWriter wtr1 = new ServiceWriter(operationName);
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setOperationName(operationName);
		wtr1.setLabel("Adobe PostScript to Portable Document Format");
		wtr1.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ps-to-pdf.owl#ps-to-pdf");
		wtr1.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ghostscript.owl#ghostscript");
		wtr1.saveDocument();

		operationName = "PSFToPNG";
		ServiceWriter wtr2 = new ServiceWriter(operationName);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setOperationName(operationName);
		wtr2.setLabel("Adobe PostScript to Portable Network Graphic");
		wtr2.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ps-to-png.owl#ps-to-png");
		wtr2.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ghostscript.owl#ghostscript");
		wtr2.saveDocument();*/

		operationName = "PDFToPNG";
		ServiceWriter wtr3 = new ServiceWriter(operationName);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setOperationName(operationName);
		wtr3.setLabel("Adobe Portable Document Format to Portable Network Graphic");
		wtr3.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/pdf-to-png.owl#pdf-to-png");
		wtr3.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ghostscript.owl#ghostscript");
		System.out.println(wtr3.saveDocument());
	}
}
