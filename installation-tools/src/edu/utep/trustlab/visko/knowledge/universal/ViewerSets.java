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


package edu.utep.trustlab.visko.knowledge.universal;

import edu.utep.trustlab.visko.ontology.operator.writer.ViewerSetWriter;

public class ViewerSets {

	public static String probeit;
	public static String firefox;
	public static String internetExplorer;
	public static String divaGraphics;
	
	public static void create() {	
		ViewerSetWriter wtr = new ViewerSetWriter("probeit");
		wtr.setLabel("Probe-It!");
		System.out.println(wtr.saveDocument());
		probeit = wtr.getURI();

		ViewerSetWriter wtr1 = new ViewerSetWriter("mozilla-firefox");
		wtr1.setLabel("Mozilla Firefox");
		System.out.println(wtr1.saveDocument());
		firefox = wtr1.getURI();

		ViewerSetWriter wtr2 = new ViewerSetWriter("internet-explorer");
		wtr2.setLabel("Microsoft Internet Explorer");
		System.out.println(wtr2.saveDocument());
		internetExplorer = wtr2.getURI();

		ViewerSetWriter wtr3 = new ViewerSetWriter("diva-graphics");
		wtr3.setLabel("Diva Graphics Java Package");
		System.out.println(wtr3.saveDocument());
		divaGraphics = wtr3.getURI();
	}
}
