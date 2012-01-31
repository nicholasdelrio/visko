package trustlab.visko.execution;

import java.util.Vector;

import trustlab.visko.execution.paths.FormatPath;
import trustlab.visko.execution.paths.FormatPaths;
import trustlab.visko.execution.paths.OperatorPath;
import trustlab.visko.execution.paths.OperatorPaths;
import trustlab.visko.sparql.ViskoTripleStore;
import trustlab.visko.util.CartesianProduct;
import trustlab.visko.util.ResultSetToVector;

import com.hp.hpl.jena.query.*;

public class PipelineSetBuilder {
	private ViskoTripleStore ts;

	private FormatPaths formatPaths;
	private OperatorPaths operatorPaths;
	private PipelineSet pipelines;

	public PipelineSetBuilder(String viskoSPARQLEndpointURL) {
		ts = new ViskoTripleStore(viskoSPARQLEndpointURL);
	}

	public PipelineSet getPipelines() {
		return pipelines;
	}

	public ViskoTripleStore getTripleStore() {
		return ts;
	}

	public boolean formatPathExistsForViewerSet(String formatURI,
			String viewerSetURI) {
		return ts.canBeVisualizedWithViewerSet(formatURI, viewerSetURI);
	}

	public boolean formatPathExistsForTargetFormat(String formatURI,
			String targetFormatURI) {
		return ts.canBeVisualizedWithTargetFormat(formatURI, targetFormatURI);
	}

	public boolean isAlreadyVisualizableWithViewerSet(String formatURI,
			String viewerSetURI) {
		return ts.isAlreadyVisualizableWithViewerSet(formatURI, viewerSetURI);
	}

	public void setPipelines(String formatURI, String viewerSetURI,
			String viewConstraintURI) {
		setFormatPaths(formatURI, viewerSetURI);
		System.out.println("Num format paths: " + formatPaths.size());

		setOperatorPathsForViewers();
		System.out.println("Num operator paths: " + operatorPaths.size());

		if (viewConstraintURI != null) {
			filterOperatorPathsWithViewRestriction(viewConstraintURI);
			System.out.println("Num operator paths after view restrictions: "
					+ operatorPaths.size());
		}

		Vector<Vector<String>> operatorImplSets;

		pipelines = new PipelineSet();
		Pipeline pipe;

		for (OperatorPath operatorPath : operatorPaths) {
			pipe = new Pipeline(operatorPath.getViewerURI(), pipelines);

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
						pipe = new Pipeline(operatorPath.getViewerURI(),
								pipelines);
						pipe.setOWLSServiceURIs(cartesianPath);
						pipelines.add(pipe);
					}
				}
			}
		}
	}

	public void setPipelinesUsingTargetFormat(String formatURI,
			String targetFormatURI, String viewConstraintURI) {
		setFormatPathsUsingTargetFormat(formatURI, targetFormatURI);
		System.out.println("Num format paths: " + formatPaths.size());

		setOperatorPathsForViewers();
		System.out.println("Num operator paths: " + operatorPaths.size());

		if (viewConstraintURI != null) {
			filterOperatorPathsWithViewRestriction(viewConstraintURI);
			System.out.println("Num operator paths after view restrictions: "
					+ operatorPaths.size());
		}

		Vector<Vector<String>> operatorImplSets;

		pipelines = new PipelineSet();
		Pipeline pipe;

		for (OperatorPath operatorPath : operatorPaths) {
			pipe = new Pipeline(operatorPath.getViewerURI(), pipelines);

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
						pipe = new Pipeline(operatorPath.getViewerURI(),
								pipelines);
						pipe.setOWLSServiceURIs(cartesianPath);
						pipelines.add(pipe);
					}
				}
			}
		}
	}

	private void filterOperatorPathsWithViewRestriction(String requiredViewURI) {
		operatorPaths.filterByView(requiredViewURI, ts);
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

		if (ts.isAlreadyVisualizableWithViewerSet(formatURI, viewerSetURI)) {
			System.out.println("already visualizable...");
			Vector<String> targetViewer = ResultSetToVector
					.getVectorFromResultSet(ts.getTargetViewerOfViewerSet(
							formatURI, viewerSetURI), "viewer");
			formatPath = new FormatPath(targetViewer.get(0));
			formatPath.add(formatURI);
			formatPaths.add(formatPath);
		}

		ResultSet targetFormats = ts.getTargetFormatsFromViewerSet(formatURI,
				viewerSetURI);
		System.out.println(targetFormats.hasNext());
		Vector<String[]> targetFormatsVector = ResultSetToVector
				.getVectorPairsFromResultSet(targetFormats, "format", "viewer");
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
