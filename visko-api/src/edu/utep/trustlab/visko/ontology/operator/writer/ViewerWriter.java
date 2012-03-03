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


package edu.utep.trustlab.visko.ontology.operator.writer;

import java.util.Vector;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.*;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ViewerWriter extends ViskoWriter {
	String visualizationParameterComment;
	String visualizationParameterLabel;
	Vector<Format> formats;
	Vector<ViewerSet> partOfSets;
	Viewer viskoViewer;

	ViskoModel readingModel = new ViskoModel();

	public ViewerWriter(String name) {
		viskoViewer = new Viewer(Repository.getRepository().getBaseURL(), name, viskoModel);
		formats = new Vector<Format>();
		partOfSets = new Vector<ViewerSet>();
	}

	public void addPartOfSetURI(String setURI) {
		ViewerSet set = new ViewerSet(setURI, readingModel);
		partOfSets.add(set);
	}

	public void setLabel(String label) {
		this.visualizationParameterLabel = label;
	}

	public void setViewerComment(String comment) {
		this.visualizationParameterComment = comment;
	}

	public void addFormatURI(String formatURI) {
		Format fmt = new Format(formatURI, readingModel);
		formats.add(fmt);
	}

	public String toRDFString() {
		viskoViewer.setBelongsToViewerSets(partOfSets);
		viskoViewer.setLabel(visualizationParameterLabel);
		viskoViewer.setComment(visualizationParameterComment);
		viskoViewer.setOperatesOnFormats(formats);

		// adds the individual to the model and returns a reference to that
		// Individual
		viskoViewer.getIndividual();
		this.viskoIndividual = viskoViewer;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
