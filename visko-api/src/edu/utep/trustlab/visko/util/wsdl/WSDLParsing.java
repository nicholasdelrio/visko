package edu.utep.trustlab.visko.util.wsdl;

import java.util.List;

import org.mindswap.wsdl.WSDLOperation;
import org.mindswap.wsdl.WSDLService;

public class WSDLParsing {
	public static WSDLOperation getWSDLOperationFromName(WSDLService s,
			String opName) {
		List<WSDLOperation> ops = s.getOperations();
		for (WSDLOperation op : ops) {
			if (op.getName().equals(opName)) {
				return op;
			}
		}
		return null;
	}

	public static WSDLService getWSDLService(String wsdlURL) {
		WSDLService service = null;
		try {
			service = WSDLService.createService(wsdlURL);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return service;
	}
}
