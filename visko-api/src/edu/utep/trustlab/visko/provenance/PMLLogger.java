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


package edu.utep.trustlab.visko.provenance;
/*
import java.util.Vector;

import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.query.ValueMap;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import pml.dumping.writer.NodesetWriter;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.util.FileUtilities;

public class PMLLogger {
	private Vector<NodesetWriter> nodesets;
	private String baseURL = "http://rio.cs.utep.edu/ciserver/ciprojects/pmlj/";
	private String uname = "pvisko1";
	private String pword = "faro!Faxi66";
	private String fileName = "visko";
	private String serviceNodesetName = "pipelineStep";
	private String parameterNodesetName = "parameterAssertion";
	private String project = "visko-provenance";

	private static final String PARAMETER_ONTOLOGY_URI = "http://rio.cs.utep.edu/ciserver/ciprojects/wdo/Visualization-Parameters";

	private static int counter;

	private Vector<OntClass> parameterClasses;

	public static void main(String[] args) {
		PMLLogger logger = new PMLLogger();
	}

	public PMLLogger() {
		nodesets = new Vector<NodesetWriter>();
		fileName = fileName + "_" + FileUtilities.getRandomFileName();
		counter = 0;
		parameterClasses = new Vector<OntClass>();
		loadOntModel();
	}

	private void loadOntModel() {
		OntModel model = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(PARAMETER_ONTOLOGY_URI);

		ExtendedIterator<OntClass> classesIterator = model.listClasses();

		while (classesIterator.hasNext()) {
			OntClass aClass = classesIterator.next();
			if (aClass.getURI() != null
					&& aClass.getURI().startsWith(PARAMETER_ONTOLOGY_URI)) {
				parameterClasses.add(aClass);
			}
		}
	}

	private String getURIFragment(String uri) {
		int index = uri.indexOf("#") + 1;
		return uri.substring(index);

	}

	private String getCorrespondingInformationClassURI(String parameterURI) {
		String fragment = getURIFragment(parameterURI);
		String label;

		for (OntClass aClass : parameterClasses) {
			label = aClass.getLabel(null);
			if (fragment.equalsIgnoreCase(label)) {
				return aClass.getURI();
			}
		}

		return null;
	}

	public void captureProcessingStep(OWLSService service,
			String outDatasetURL, ValueMap<Input, OWLValue> inputValueMap) {
		NodesetWriter serviceNodesetWriter = new NodesetWriter();
		serviceNodesetWriter.setBaseURL(baseURL);
		serviceNodesetWriter.setUniqueFileName(fileName);
		serviceNodesetWriter.setNodesetName(serviceNodesetName);
		serviceNodesetWriter.setRule(service.getConceptualOperator().getURI());
		serviceNodesetWriter.setIdentifier();

		String formatURI = service.getConceptualOperator()
				.getOperatesOnFormats().get(0).getURI();
		serviceNodesetWriter.setConclusionAsURL(outDatasetURL, formatURI);

		NodesetWriter parameterServiceWriter;

		for (Input var : inputValueMap.getVariables()) {
			OWLValue value = inputValueMap.getValue(var);
			String wdoGiovanniParameterURI = getCorrespondingInformationClassURI(var
					.getURI().toASCIIString());

			System.out.println("Searching for match for: "
					+ var.getURI().toASCIIString());

			if (wdoGiovanniParameterURI != null)
				System.out.println("WDO Variable Match: "
						+ wdoGiovanniParameterURI);

			else
				System.out.println("Could not find match");

			if (counter == 0
					|| (counter > 0 && !value.toString().startsWith("http://"))) {
				parameterServiceWriter = new NodesetWriter();

				if (wdoGiovanniParameterURI != null)
					parameterServiceWriter
							.setInformation(wdoGiovanniParameterURI);

				parameterServiceWriter.setBaseURL(baseURL);
				parameterServiceWriter.setUniqueFileName(fileName);
				parameterServiceWriter.setNodesetName(parameterNodesetName);
				parameterServiceWriter.setIdentifier();

				if (value.toString().startsWith("http://") && counter == 0) {
					System.out.println("setting as has conclusion: "
							+ value.toString());
					parameterServiceWriter.setConclusionAsURL(value.toString(),
							formatURI);
				} else {
					// String cleanValue = value.toString().replace("-", " ");
					// cleanValue = cleanValue.replace("_", " ");
					// cleanValue = cleanValue.replace(",", " ");
					// System.out.println("embedding conclusion: " +
					// cleanValue);
					parameterServiceWriter
							.setConclusion(value.toString(),
									"http://rio.cs.utep.edu/ciserver/ciprojects/formats/PLAIN.owl#PLAIN");
				}

				serviceNodesetWriter.addAntecedent(parameterServiceWriter);
			}

			counter++;
		}
		nodesets.add(serviceNodesetWriter);
	}

	public String dumpNodesets(String dataProvenancePML) {
		if (dataProvenancePML != null)
			nodesets.firstElement().addAntecedent(dataProvenancePML);

		for (int i = nodesets.size() - 1; i > 0; i--) {
			nodesets.get(i).addAntecedent(nodesets.get(i - 1));
		}

		String rootNodesetURI = nodesets.get(nodesets.size() - 1).writePML(
				baseURL, project, uname, pword);
		nodesets.get(nodesets.size() - 1).close();
		return rootNodesetURI;
	}
}
*/
