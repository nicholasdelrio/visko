package trustlab.visko.ontology.service;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.service.Service;
import trustlab.visko.ontology.OWLSIndividual;
import trustlab.visko.ontology.model.OWLSModel;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.operator.Operator;
import trustlab.visko.ontology.vocabulary.ViskoS;
import trustlab.visko.util.wsdl.WSDLTranslatorBuilder;

public class OWLSService extends OWLSIndividual {

	private String wsdlURL;
	private String operationName;
	private String label;

	private Operator implementedOperator;
	private Extractor implementedExtractor;

	private Toolkit supportingToolkit;

	private OWLObjectProperty implementsOperatorProperty;
	private OWLObjectProperty supportingToolkitProperty;
	private OWLObjectProperty implementsExtractorProperty;

	private boolean isOperatorService;
	private boolean isExtractorService;

	public OWLSService(String uri, OWLSModel owlsModel) {// for reading
		super(uri, owlsModel);
	}

	public OWLSService(String baseURI, String name, OWLSModel owlsModel) {// for
																			// writing
		super(baseURI, name, owlsModel);
	}

	public void setLabel(String lbl) {
		label = lbl;
	}

	public void setConceptualOperator(Operator op) {
		implementedOperator = op;
	}

	public Operator getConceptualOperator() {
		return implementedOperator;
	}

	public void setConceptualExtractor(Extractor ex) {
		implementedExtractor = ex;
	}

	public Extractor getConceptualExtractor() {
		return implementedExtractor;
	}

	public void setSupportingToolkit(Toolkit tk) {
		supportingToolkit = tk;
	}

	public Toolkit getSupportingToolkit() {
		return supportingToolkit;
	}

	public void setWSDLURL(String url) {
		wsdlURL = url;
	}

	public void setOperationName(String opName) {
		operationName = opName;
	}

	private OWLIndividual getToolkitAsOWLIndividual() {
		return model.convertJenaToOWLIndividual(supportingToolkit);
	}

	private OWLIndividual getOperatorAsOWLIndividual() {
		return model.convertJenaToOWLIndividual(implementedOperator);
	}

	private OWLIndividual getExtractorAsOWLIndividual() {
		return model.convertJenaToOWLIndividual(implementedExtractor);
	}

	@Override
	public Service getIndividual() {
		Service service = null;
		if (allFieldsPopulated() && !isForReading) {// create service
			WSDLTranslatorBuilder builder = new WSDLTranslatorBuilder(
					model.getOntology(), operationName, wsdlURL);

			service = builder.getTranslator().getService();

			service.setLabel(label, null);

			if (isOperatorService) {
				service.addProperty(supportingToolkitProperty,
						getToolkitAsOWLIndividual());
				service.addProperty(implementsOperatorProperty,
						getOperatorAsOWLIndividual());
			} else
				service.addProperty(implementsExtractorProperty,
						getExtractorAsOWLIndividual());
		} else if (!allFieldsPopulated() && !isForReading) {
			System.out.println("all fields not populated....");
		} else {// read service and populate service object
			service = model.readService(uri);

			OWLIndividual operator = service
					.getProperty(implementsOperatorProperty);
			OWLIndividual toolkit = service
					.getProperty(supportingToolkitProperty);
			OWLIndividual extractor = service
					.getProperty(implementsExtractorProperty);

			ViskoModel viskoModel = new ViskoModel();

			if (operator != null && toolkit != null) {
				supportingToolkit = new Toolkit(toolkit.getURI()
						.toASCIIString(), viskoModel);
				implementedOperator = new Operator(operator.getURI()
						.toASCIIString(), viskoModel);
			}

			if (extractor != null)
				implementedExtractor = new Extractor(extractor.getURI()
						.toASCIIString(), viskoModel);
		}

		return service;
	}

	@Override
	protected void setProperties() {
		implementsOperatorProperty = model
				.getObjectProperty(ViskoS.PROPERTY_URI_IMPLEMENTS_OPERATOR);
		implementsExtractorProperty = model
				.getObjectProperty(ViskoS.PROPERTY_URI_IMPLEMENTS_EXTRACTOR);
		supportingToolkitProperty = model
				.getObjectProperty(ViskoS.PROPERTY_URI_SUPPORTED_BY);
	}

	@Override
	protected boolean allFieldsPopulated() {
		isOperatorService = wsdlURL != null && operationName != null
				&& implementedOperator != null
				&& this.supportingToolkit != null && label != null;
		isExtractorService = wsdlURL != null && operationName != null
				&& implementedExtractor != null && label != null;

		return isOperatorService || isExtractorService;
	}

	@Override
	protected void initializeFields() {
		// TODO Auto-generated method stub

	}
}