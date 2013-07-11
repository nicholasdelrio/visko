package edu.utep.trustlab.visko.web.json;

import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.operatorPaths.OperatorPaths;

public class OperatorPathsCache extends Thread {
	
	private OperatorPaths operatorPaths;
							
	public OperatorPaths getOperatorPaths(){
		if(operatorPaths == null)
			constructOperatorPaths();
		return operatorPaths;
	}
	
	private void constructOperatorPaths(){		
		QueryEngine queryEngine = new QueryEngine(null);
		operatorPaths = queryEngine.getOperatorPaths();
	}

	@Override
	public void run() {
		constructOperatorPaths();
	}
}
