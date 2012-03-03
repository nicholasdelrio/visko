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


package edu.utep.trustlab.visko.ontology.operator;

import java.util.Vector;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

public class OperatorSet extends JenaIndividual {
	private Vector<Operator> operators;
	private Toolkit toolkit;

	// properties
	private ObjectProperty containsOperatorProperty;
	private ObjectProperty supportedByProperty;

	public OperatorSet(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_OPERATORSET, baseURL, name, viskoModel);
	}

	public OperatorSet(String conceptURI, ViskoModel viskoModel) {
		super(conceptURI, viskoModel);
	}

	public void setSupportedByToolkit(Toolkit tk) {
		toolkit = tk;
	}

	public Toolkit getSupportedToolkit() {
		return toolkit;
	}

	public void setOperators(Vector<Operator> ops) {
		operators = ops;
	}

	public void addOperator(Operator operator) {
		operators.add(operator);
	}

	public Vector<Operator> getOperators() {
		return operators;
	}

	private void addContainsOperatorProperties(Individual subjectInd) {
		for (Operator operator : operators) {
			subjectInd.addProperty(containsOperatorProperty,
					operator.getIndividual());
		}
	}

	private void addSupportedByToolkitProperty(Individual subjectInd) {
		if (toolkit != null) {
			subjectInd
					.addProperty(supportedByProperty, toolkit.getIndividual());
		}
	}

	@Override
	protected boolean allFieldsPopulated() {
		return operators.size() > 0;
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		NodeIterator ops = ind.listPropertyValues(containsOperatorProperty);
		Operator op;
		while (ops.hasNext()) {
			op = new Operator(ops.next().as(Individual.class).getURI(), model);
			operators.add(op);
		}

		RDFNode supportingTK = ind.getPropertyValue(supportedByProperty);
		toolkit = new Toolkit(supportingTK.as(Individual.class).getURI(), model);
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addContainsOperatorProperties(ind);
		this.addSupportedByToolkitProperty(ind);
		return ind;
	}

	@Override
	protected void setProperties() {
		containsOperatorProperty = model
				.getObjectProperty(ViskoO.PROPERTY_URI_CONTAINS_OPERATOR);
		supportedByProperty = model
				.getObjectProperty(ViskoO.PROPERTY_URI_SUPPORTEDBY);
	}

	@Override
	protected void initializeFields() {
		operators = new Vector<Operator>();
	}
}
