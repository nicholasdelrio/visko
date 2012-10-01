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


package edu.utep.trustlab.visko.ontology.viskoOperator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.NodeIterator;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

import java.util.Vector;

public class Viewer extends Operator {

	private Vector<ViewerSet> viewerSets;
	private ObjectProperty partOfViewerSet;
	
	public Viewer(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_Viewer, baseURL, name, viskoModel);
	}

	public Viewer(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setViewerSets(Vector<ViewerSet> someViewerSets) {
		viewerSets = someViewerSets;
	}

	public void addBelongsToViewerSet(ViewerSet viewerSet) {
		viewerSets.add(viewerSet);
	}

	public Vector<ViewerSet> getViewerSets() {
		return viewerSets;
	}
	
	private void addPartOfViewerSet(Individual subjectInd) {
		for (ViewerSet set : viewerSets)
			subjectInd.addProperty(partOfViewerSet, set.getIndividual());
	}

	@Override
	protected boolean allFieldsPopulated() {
		return super.allFieldsPopulated() && viewerSets.size() > 0;		
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addPartOfViewerSet(ind);
		return ind;
	}

	@Override
	protected void setProperties() {partOfViewerSet = model.getObjectProperty(ViskoO.PROPERTY_URI_partOfViewerSet);}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		// populate viewer sets
		NodeIterator vSets = ind.listPropertyValues(partOfViewerSet);
		while (vSets.hasNext())
			viewerSets.add(new ViewerSet(vSets.next().as(Individual.class).getURI(), model));		
	}

	@Override
	protected void initializeFields() {
		viewerSets = new Vector<ViewerSet>();
	}
}