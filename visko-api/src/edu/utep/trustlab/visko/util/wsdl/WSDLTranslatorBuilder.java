package edu.utep.trustlab.visko.util.wsdl;

import java.net.URI;
import java.util.Vector;
import javax.xml.namespace.QName;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.OWL;
import org.mindswap.owl.vocabulary.XSD;
import org.mindswap.utils.URIUtils;
import org.mindswap.wsdl.WSDLConsts;
import org.mindswap.wsdl.WSDLOperation;
import org.mindswap.wsdl.WSDLParameter;
import org.mindswap.wsdl.WSDLService;
import org.mindswap.wsdl.WSDLTranslator;

import edu.utep.trustlab.visko.util.GetURLContents;

public class WSDLTranslatorBuilder {
	WSDLTranslator translator;
	String fileName;
	OWLOntology ontology;

	public WSDLTranslatorBuilder(OWLOntology ont, String operationName,
			String wsdlURL) {
		WSDLOperation o = null;
		WSDLService s = null;

		s = WSDLParsing.getWSDLService(wsdlURL);
		o = WSDLParsing.getWSDLOperationFromName(s, operationName);

		if ((s == null)) {
			return;
		}

		translator = new WSDLTranslator(ont, o, operationName);
		translator.setServiceName(operationName);
		translator
				.setTextDescription("This is an owl-s service referenced as by a VisKo OperationImpl.");

		setInputs(o);
		setOutputs(o);
	}

	private void setInputs(WSDLOperation op) {
		Vector<WSDLParameter> params = op.getInputs();
		QName paramType;
		String paramName;
		URI typeURI;

		for (WSDLParameter param : params) {
			paramType = (param.getType() == null) ? new QName(
					WSDLConsts.xsdURI, "any") : param.getType();
			paramName = URIUtils.getLocalName(param.getName());

			// By default use owl:Thing as param type
			String type = OWL.Thing.toString();

			if (paramType.getNamespaceURI().equals(WSDLConsts.soapEnc)
					|| (paramType.getNamespaceURI().equals(WSDLConsts.xsdURI) && !paramType
							.getLocalPart().equals("any"))) {
				type = XSD.ns + paramType.getLocalPart();
			}

			typeURI = GetURLContents.getURI(type);

			translator.addInput(param, paramName, typeURI, null);
		}
	}

	private void setOutputs(WSDLOperation op) {
		Vector<WSDLParameter> params = op.getOutputs();
		QName paramType;
		String paramName;
		URI typeURI;

		for (WSDLParameter param : params) {
			paramType = (param.getType() == null) ? new QName(
					WSDLConsts.xsdURI, "any") : param.getType();
			paramName = URIUtils.getLocalName(param.getName());

			// By default use owl:Thing as param type
			String type = OWL.Thing.toString();

			if (paramType.getNamespaceURI().equals(WSDLConsts.soapEnc)
					|| (paramType.getNamespaceURI().equals(WSDLConsts.xsdURI) && !paramType
							.getLocalPart().equals("any"))) {
				type = XSD.ns + paramType.getLocalPart();
			}

			typeURI = GetURLContents.getURI(type);

			translator.addOutput(param, paramName, typeURI, null);
		}
	}

	public WSDLTranslator getTranslator() {
		return translator;
	}
}