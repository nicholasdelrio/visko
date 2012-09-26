package edu.utep.trustlab.visko.web.json;

import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.visko.planning.OperatorPaths;
import edu.utep.trustlab.visko.planning.PipelineSetBuilder;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class OperatorPathsSets extends Thread {
	
	private Vector<OperatorPaths> pathsSets;
	private ViskoTripleStore ts;
							
	public Vector<OperatorPaths> getOperatorPathsSets(){
		return pathsSets;
	}
	
	private Vector<OperatorPaths> constructOperatorPathsSets(){		
		pathsSets = new Vector<OperatorPaths>();
		ts= new ViskoTripleStore();
		
		ResultSet formatsAndDataTypes = ts.getInputDataParents();
		String formatURI;
		String dataTypeURI;
		QuerySolution solution;
		Query query;
		PipelineSetBuilder builder;
		
		while(formatsAndDataTypes.hasNext()){
			solution = formatsAndDataTypes.next();
			formatURI = solution.get("format").toString();
			dataTypeURI = solution.get("dataType").toString();

			System.out.println("Finding operator paths for format: " + formatURI + " and dataTypeURI: " + dataTypeURI);
			
			Vector<String> allViewerURIs = ResultSetToVector.getVectorFromResultSet(ts.getViewers(), "viewer");
			
			query = new Query(null, formatURI, null);
			query.setTypeURI(dataTypeURI);
			
			builder = new PipelineSetBuilder(formatURI, dataTypeURI, allViewerURIs, null);
			pathsSets.add(builder.getOperatorPaths());
		}
		return pathsSets;
	}

	@Override
	public void run() {
		constructOperatorPathsSets();
	}
}
