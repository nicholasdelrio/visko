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

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class GriddedGravityDataProfile {
	public static void create() {
		String baseURL = Repository.getRepository().getBaseURL();
		// gridded data
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d12";
		ToolkitProfileWriter wtr1 = new ToolkitProfileWriter(
		"griddedGravityDataProfile");
		wtr1.setSupportingToolkit(baseURL + "gmt.owl#gmt");
		wtr1.addDataType(dataTypeURI);
		// for colored image
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToColoredImagePS.owl#B",
		"1");
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToColoredImagePS.owl#J",
		"x4c");
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToColoredImagePS.owl#C",
		"hot");
		// for contour map
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToContourMapPS.owl#C",
		"10");
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToContourMapPS.owl#A",
		"20");
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToContourMapPS.owl#B",
		"0.5");
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToContourMapPS.owl#S",
		"5");
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToContourMapPS.owl#J",
		"x4c");
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToContourMapPS.owl#Wc",
		"thinnest,black");
		wtr1.addInputBinding(
		baseURL + "ESRIGriddedToContourMapPS.owl#Wa",
		"thinnest,black");
		// dump document
		String documentURL = wtr1.saveDocument();
		System.out.println(documentURL);

		}
}
