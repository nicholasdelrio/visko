package edu.utep.trustlab.visko.planning.pipelines;

import java.util.Vector;

import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.operatorPaths.OperatorPath;
import edu.utep.trustlab.visko.planning.operatorPaths.OperatorPaths;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.CartesianProduct;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class OperatorPathsToPipelines {
	private PipelineSet pipelines;
	private ViskoTripleStore ts;
	private OperatorPaths opPaths;
	
	public OperatorPathsToPipelines(Query query, OperatorPaths operatorPaths){
		pipelines = new PipelineSet(query);
		ts = new ViskoTripleStore();
		opPaths = operatorPaths;
	}
	
	public PipelineSet getPipelines(){
		if(pipelines.size() == 0)
			setPipelines();
		return pipelines;
	}
	
	private void setPipelines(){
		Vector<Vector<String>> operatorImplSets;
		Pipeline pipe;

		for (OperatorPath operatorPath : opPaths) {
			pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getVisualizationAbstractionGenerated(), pipelines);

			if (operatorPath.isEmpty())
				pipelines.add(pipe);

			else {
				operatorImplSets = new Vector<Vector<String>>();
				for (String operatorURI : operatorPath){
					
					ResultSet operatorImplURIs = ts.getOWLSServiceImplementationsURIs(operatorURI);
					Vector<String> operatorImplURIsVector = ResultSetToVector.getVectorFromResultSet(operatorImplURIs, "opImpl");

					if (operatorImplURIsVector.size() > 0)
						operatorImplSets.add(operatorImplURIsVector);
				}

				if (operatorImplSets.size() == operatorPath.size()) {
					Vector<Vector<String>> cartesianOperatorImpl = CartesianProduct.cartesianProduct(operatorImplSets, 0);

					for (Vector<String> cartesianPath : cartesianOperatorImpl) {
						pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getVisualizationAbstractionGenerated(), pipelines);
						pipe.setServiceURIs(cartesianPath);
						pipelines.add(pipe);
					}
				}
			}
		}
	}
}
