package trustlab.visko.ontology.operator.writer;

import java.util.Vector;

import trustlab.server.Server;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.operator.*;
import trustlab.visko.ontology.pmlp.Format;
import trustlab.visko.ontology.vocabulary.ViskoO;
import trustlab.visko.ontology.writer.ViskoWriter;

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
		operatorSet = new OperatorSet(Server.getServer().getBaseURL(), name, viskoModel);
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
