package edu.utep.trustlab.visko.util;

import java.util.HashMap;

import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.query.ValueMap;

public class OWLSParameterBinder {
	
	public static ValueMap<Input, OWLValue> buildInputValueMap(Process process, String datasetURL, HashMap<String, String> bindings, OWLKnowledgeBase kb) {
		ValueMap<Input, OWLValue> inputs = new ValueMap<Input, OWLValue>();

		String value;
		String uri;
		boolean error = false;
		for (Input input : process.getInputs()) {
			uri = input.getURI().toASCIIString();

			if (uri.contains("url") || uri.contains("URL") || uri.contains("fileLoc")){
				inputs.setValue(input, kb.createDataValue(datasetURL));
				System.out.println("found binding for data: " + datasetURL);
			}
			else {
				value = bindings.get(input.getURI().toASCIIString());
				
				if (value != null){					
					inputs.setValue(input, kb.createDataValue(value));
				}
				
				else{
					System.out.println("can't find binding for " + input.getURI().toASCIIString());
					error = true;
				}
			}
		}

		if (error){
			System.out.println("not all parameters could be bound!  Can't execute service!");
			return null;
		}
		else
			return inputs;
	}
}
