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


package edu.utep.trustlab.visko.ontology.operator.writer;

import java.util.Vector;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.*;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.view.View;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class TransformerWriter extends ViskoWriter {
	private String label;
	private Vector<Format> inputFormats;
	private Format outputFormat;
	private Transformer trans;
	private String name;
	private Mapper mapper;
	private View view;
	private ViskoModel loadingModel = new ViskoModel();

	public TransformerWriter(String name) {
		trans = new Transformer(ContentManager.getRepository().getBaseURL(JenaIndividual.makeFileName(name)), name, viskoModel);
		mapper = new Mapper(ContentManager.getRepository().getBaseURL(JenaIndividual.makeFileName(name)), name, viskoModel);
		inputFormats = new Vector<Format>();
	}

	public void setName(String aName) {
		name = aName;
	}

	public void addInputFormat(String fmtURI) {
		Format fmt = new Format(fmtURI, loadingModel);
		inputFormats.add(fmt);
	}

	public void setMappedToView(String viewURI) {
		view = new View(viewURI, loadingModel);
	}

	public void setLabel(String lbl) {
		label = lbl;
	}

	public void setOutputFormat(String fmtURI) {
		Format fmt = new Format(fmtURI, loadingModel);
		outputFormat = fmt;
	}

	public String toRDFString() {
		if (view == null) {
			trans.setOperatesOnFormats(inputFormats);
			trans.setTransformsToFormat(outputFormat);
			trans.setLabel(label);
			trans.setName(name);

			// adds the individual to the model and returns a reference to that
			// Individual
			trans.getIndividual();
			this.viskoIndividual = trans;
		} else {
			mapper.setViewToMapTo(view);
			mapper.setOperatesOnFormats(inputFormats);
			mapper.setTransformsToFormat(outputFormat);
			mapper.setLabel(label);
			mapper.setName(name);

			// adds the individual to the model and returns a reference to that
			// Individual
			mapper.getIndividual();
			this.viskoIndividual = mapper;
		}

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
