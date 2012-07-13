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
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class OperatorSetWriter extends ViskoWriter {
	private Vector<Operator> operators;
	private Toolkit supportingToolkit;
	private ViskoModel loadingModel;

	private Format nullFormat;
	private OperatorSet operatorSet;

	public OperatorSetWriter(String name) {
		loadingModel = new ViskoModel();
		operators = new Vector<Operator>();

		nullFormat = new Format("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/UNKNOWN.owl#UNKNOWN", loadingModel);
		
		operatorSet = new OperatorSet(ContentManager.getViskoRDFContentManager().getBaseURL(JenaIndividual.makeFileName(name)), name, viskoModel);
	}

	public void setToolkit(String toolkitName, String label) {
		supportingToolkit = new Toolkit(operatorSet.getFullURL(), toolkitName, viskoModel);
		supportingToolkit.setLabel("visualizationToolkit");
	}

	public void addOperator(String operatorName, Vector<String> inputFormats,
			String descriptionText) {
		Operator operator = new Operator(ViskoO.CLASS_URI_OPERATOR, operatorSet.getFullURL(), operatorName, viskoModel);
		operator.setLabel(operatorName);
		operator.setName(operatorName);

		if (inputFormats == null)
			operator.addOperatesOnFormat(nullFormat);
		else {
			for (String formatURI : inputFormats) {
				Format format = new Format(formatURI, loadingModel);
				operator.addOperatesOnFormat(format);
			}
		}

		if (descriptionText != null)
			operator.setComment(descriptionText);

		operators.add(operator);
	}

	public String toRDFString() {
		operatorSet.setOperators(operators);
		operatorSet.setSupportedByToolkit(supportingToolkit);
		operatorSet.getIndividual();
		this.viskoIndividual = operatorSet;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
