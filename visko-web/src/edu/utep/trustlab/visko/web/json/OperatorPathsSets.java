package edu.utep.trustlab.visko.web.json;

import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.visko.planning.OperatorPaths;
import edu.utep.trustlab.visko.planning.PipelineSetBuilder;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class OperatorPathsSets {
	
	private static Vector<OperatorPaths> pathsSets;
	private static ViskoTripleStore ts;
	
	public static Vector<OperatorPaths> getOperatorPathsSets(){
		if(pathsSets != null && pathsSets.size() > 0)
			return pathsSets;
		
		pathsSets = new Vector<OperatorPaths>();
		ts= new ViskoTripleStore();
		
		ResultSet formatsAndDataTypes = ts.getInputFormatsAndDataTypes();
		String formatURI;
		String dataTypeURI;
		QuerySolution solution;
		Query query;
		PipelineSetBuilder builder;
		Vector<String> viewerSetURIs = ResultSetToVector.getVectorFromResultSet(ts.getViewerSets(), "viewerSet");
		
		while(formatsAndDataTypes.hasNext()){
			solution = formatsAndDataTypes.next();
			formatURI = solution.get("format").toString();
			dataTypeURI = solution.get("dataType").toString();
			
			query = new Query(null, formatURI, null);
			query.setTypeURI(dataTypeURI);
			
			builder = new PipelineSetBuilder(query);
			pathsSets.add(builder.getAllOperatorPaths(viewerSetURIs));
		}
		return pathsSets;
	}
}
