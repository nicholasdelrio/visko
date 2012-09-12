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
/*

package edu.utep.trustlab.visko.planning;

import java.util.Vector;

import edu.utep.trustlab.visko.planning.Pipeline;
import edu.utep.trustlab.visko.planning.PipelineSet;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.paths.FormatPath;
import edu.utep.trustlab.visko.planning.paths.FormatPaths;
import edu.utep.trustlab.visko.planning.paths.OperatorPath;
import edu.utep.trustlab.visko.planning.paths.OperatorPaths;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.CartesianProduct;
import edu.utep.trustlab.visko.util.ResultSetToVector;

import com.hp.hpl.jena.query.*;

public class PipelineSetBuilder {
	private ViskoTripleStore ts;

	private FormatPaths formatPaths;
	private OperatorPaths operatorPaths;
	private PipelineSet pipelines;
	private Query query;

	public PipelineSetBuilder(Query drivingQuery) {
		ts = new ViskoTripleStore();
		query = drivingQuery;
	}

	public PipelineSet getPipelines() {
		return pipelines;
	}

	public ViskoTripleStore getTripleStore() {
		return ts;
	}

	public boolean formatPathExistsForViewerSet(String formatURI, String viewerSetURI) {
		return ts.canBeVisualizedWithViewerSet(formatURI, viewerSetURI);
	}

	public boolean formatPathExistsForTargetFormat(String formatURI, String targetFormatURI) {
		return ts.canBeVisualizedWithTargetFormat(formatURI, targetFormatURI);
	}

	public boolean isAlreadyVisualizableWithViewerSet(String formatURI, String dataTypeURI, String viewerSetURI) {
		boolean formatCheck = ts.isFormatAlreadyVisualizableWithViewerSet(formatURI, viewerSetURI);
		boolean typeCheck = ts.isDataTypeAlreadyVisualizableWithViewerSet(dataTypeURI, viewerSetURI) || ts.isSubClassOfDataTypeAlreadyVisualizableWithViewerSet(dataTypeURI, viewerSetURI);
		
		return formatCheck && typeCheck;
	}

	public void setPipelines(String formatURI, String dataTypeURI, String viewerSetURI, String viewConstraintURI) {
		setFormatPaths(formatURI, viewerSetURI);
		System.out.println("Num format paths: " + formatPaths.size());

		setOperatorPathsForViewers();
		System.out.println("Num operator paths: " + operatorPaths.size());

		filterOperatorPathsByTypeRestriction(dataTypeURI);
		System.out.println("Num operator paths after type restrictions: " + operatorPaths.size());
		
		if (viewConstraintURI != null) {
			filterOperatorPathsWithViewRestriction(viewConstraintURI);
			System.out.println("Num operator paths after view restrictions: " + operatorPaths.size());
		}

		Vector<Vector<String>> operatorImplSets;

		pipelines = new PipelineSet(query);
		Pipeline pipe;

		for (OperatorPath operatorPath : operatorPaths) {
			pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getViewGenerated(), pipelines);

			if (operatorPath.isEmpty()) {
				pipelines.add(pipe);
			}

			else {
				operatorImplSets = new Vector<Vector<String>>();

				for (String operatorURI : operatorPath){
					
					ResultSet operatorImplURIs = ts.getOWLSServiceImplementationsURIs(operatorURI);
					Vector<String> operatorImplURIsVector = ResultSetToVector.getVectorFromResultSet(operatorImplURIs, "opImpl");

					if (operatorImplURIsVector.size() > 0)
						operatorImplSets.add(operatorImplURIsVector);
				}

				if (operatorImplSets.size() == operatorPath.size()) {
					Vector<Vector<String>> cartesianOperatorImpl = CartesianProduct
							.cartesianProduct(operatorImplSets, 0);

					for (Vector<String> cartesianPath : cartesianOperatorImpl) {
						pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getViewGenerated(), pipelines);
						pipe.setServiceURIs(cartesianPath);
						pipelines.add(pipe);
					}
				}
			}
		}
	}

	public void setPipelinesUsingTargetFormat(String formatURI, String dataTypeURI, String targetFormatURI, String viewConstraintURI) {
		setFormatPathsUsingTargetFormat(formatURI, targetFormatURI);
		System.out.println("Num format paths: " + formatPaths.size());

		setOperatorPathsForViewers();
		System.out.println("Num operator paths: " + operatorPaths.size());
		
		filterOperatorPathsByTypeRestriction(dataTypeURI);
		System.out.println("Num operator paths after type restrictions: " + operatorPaths.size());

		if (viewConstraintURI != null) {
			filterOperatorPathsWithViewRestriction(viewConstraintURI);
			System.out.println("Num operator paths after view restrictions: " + operatorPaths.size());
		}

		Vector<Vector<String>> operatorImplSets;

		pipelines = new PipelineSet(query);
		Pipeline pipe;

		for (OperatorPath operatorPath : operatorPaths) {
			pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getViewGenerated(), pipelines);

			if (operatorPath.isEmpty()) {
				pipelines.add(pipe);
			}

			else {
				operatorImplSets = new Vector<Vector<String>>();

				for (String operatorURI : operatorPath) {
					ResultSet operatorImplURIs = ts
							.getOWLSServiceImplementationsURIs(operatorURI);
					Vector<String> operatorImplURIsVector = ResultSetToVector
							.getVectorFromResultSet(operatorImplURIs, "opImpl");

					if (operatorImplURIsVector.size() > 0)
						operatorImplSets.add(operatorImplURIsVector);
				}

				if (operatorImplSets.size() == operatorPath.size()) {
					Vector<Vector<String>> cartesianOperatorImpl = CartesianProduct
							.cartesianProduct(operatorImplSets, 0);

					for (Vector<String> cartesianPath : cartesianOperatorImpl) {
						pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getViewGenerated(), pipelines);
						pipe.setServiceURIs(cartesianPath);
						pipelines.add(pipe);
					}
				}
			}
		}
	}

	private void filterOperatorPathsWithViewRestriction(String requiredViewURI) {
		operatorPaths.filterByView(requiredViewURI, ts);
	}

	private void filterOperatorPathsByTypeRestriction(String inputDataType){
		operatorPaths.filterByType(inputDataType, ts);
	}
	
	private void setOperatorPathsForViewers() {
		operatorPaths = new OperatorPaths();
		OperatorPath operatorPath;
		Vector<Vector<String>> operatorSets;

		for (FormatPath formatPath : formatPaths) {
			operatorPath = new OperatorPath(formatPath.getViewerURI());
			operatorSets = new Vector<Vector<String>>();

			// to support null pipelines (when no transformation is needed to
			// view current format)
			if (formatPath.isEmptyPath()) {
				operatorPaths.add(operatorPath);
			}

			else {
				for (int i = 0; i < formatPath.size() - 1; i++) {
					ResultSet operatorURIs = ts.getTransformers(
							formatPath.get(i), formatPath.get(i + 1));
					Vector<String> operatorURIsVector = ResultSetToVector
							.getVectorFromResultSet(operatorURIs, "transformer");
					operatorSets.add(operatorURIsVector);
				}

				Vector<Vector<String>> productOperatorPaths = CartesianProduct
						.cartesianProduct(operatorSets, 0);

				for (Vector<String> productOperatorPath : productOperatorPaths) {
					operatorPath = new OperatorPath(formatPath.getViewerURI());
					operatorPath.set(productOperatorPath);
					operatorPaths.add(operatorPath);
				}
			}
		}
	}

	private void setFormatPathsUsingTargetFormat(String formatURI,
			String targetFormatURI) {
		formatPaths = new FormatPaths();
		FormatPath formatPath = new FormatPath(null);
		populateFormatPaths(formatPath, formatPaths, formatURI, targetFormatURI);
	}

	private void setFormatPaths(String formatURI, String viewerSetURI) {
		System.out.println("setting format paths...");
		String targetFormatURI;
		String targetViewerURI;

		formatPaths = new FormatPaths();
		FormatPath formatPath;

		if (ts.isFormatAlreadyVisualizableWithViewerSet(formatURI, viewerSetURI)) {
			System.out.println("already visualizable...");
			Vector<String> targetViewer = ResultSetToVector.getVectorFromResultSet(ts.getTargetViewerOfViewerSet(formatURI, viewerSetURI), "viewer");
			formatPath = new FormatPath(targetViewer.get(0));
			formatPath.add(formatURI);
			formatPaths.add(formatPath);
		}

		ResultSet targetFormats = ts.getTargetFormatsFromViewerSet(formatURI,viewerSetURI);
		Vector<String[]> targetFormatsVector = ResultSetToVector.getVectorPairsFromResultSet(targetFormats, "format", "viewer");
		System.out.println(targetFormatsVector.size());

		for (String[] targetFormatAndViewer : targetFormatsVector) {
			targetFormatURI = targetFormatAndViewer[0];
			targetViewerURI = targetFormatAndViewer[1];

			System.out.println("target viewer: " + targetViewerURI);

			formatPath = new FormatPath(targetViewerURI);
			populateFormatPaths(formatPath, formatPaths, formatURI,
					targetFormatURI);
		}
	}

	private void populateFormatPaths(FormatPath formatPath,
			FormatPaths formatPaths, String currentFormatURI,
			String targetFormatURI) {
		formatPath.add(currentFormatURI);

		if (currentFormatURI.equals(targetFormatURI)) {
			formatPaths.add(formatPath);
		}

		else {
			ResultSet nextFormats = ts.getNextFormats(currentFormatURI,
					targetFormatURI);
			Vector<String> nextFormatsVector = ResultSetToVector
					.getVectorFromResultSet(nextFormats, "format");
			for (String nextFormatURI : nextFormatsVector) {
				FormatPath duplicateFormatPath = formatPath.copy();
				populateFormatPaths(duplicateFormatPath, formatPaths,
						nextFormatURI, targetFormatURI);
			}
		}
	}
}
*/