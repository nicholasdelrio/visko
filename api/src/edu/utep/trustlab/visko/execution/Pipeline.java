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


package edu.utep.trustlab.visko.execution;

import java.util.HashMap;
import java.util.Vector;
import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.Viewer;
import edu.utep.trustlab.visko.ontology.service.OWLSService;

public class Pipeline extends Vector<OWLSService> {
	private Viewer viewer;
	private OWLSModel owlsLoadingModel;
	private ViskoModel viskoLoadingModel;
	private PipelineSet parentContainer;

	public Pipeline(String viewerURI, PipelineSet parent) {
		super();
		viskoLoadingModel = new ViskoModel();
		owlsLoadingModel = new OWLSModel();
		parentContainer = parent;
		viewer = new Viewer(viewerURI, viskoLoadingModel);
	}

	public Viewer getViewer() {
		return viewer;
	}

	public HashMap<String, String> getParameterBindings() {
		return parentContainer.getParameterBindings();
	}

	public String getArtfifactURL() {
		return parentContainer.getArtifactURL();
	}

	public void addOWLSServiceURI(String serviceURI) {
		add(new OWLSService(serviceURI, owlsLoadingModel));
	}

	public void setOWLSServiceURIs(Vector<String> serviceURIs) {
		for (String serviceImplURI : serviceURIs) {
			addOWLSServiceURI(serviceImplURI);
		}
	}

	public String executePath(boolean provenance) {
		PipelineExecutor executor = new PipelineExecutor(provenance);

		String artifactURL = parentContainer.getArtifactURL();

		String visualizationURL = "NULL: Artifact to be visualized was never specified!!!";

		if (artifactURL != null) {
			// null object behavior
			if (size() == 0) {
				visualizationURL = parentContainer.getArtifactURL();
			}

			try {
				visualizationURL = executor.executeServiceChain(this,
						parentContainer.getArtifactURL());
			} catch (Exception e) {
				e.printStackTrace();
				visualizationURL = e.getMessage();
			}
		}

		return visualizationURL;
	}
}
