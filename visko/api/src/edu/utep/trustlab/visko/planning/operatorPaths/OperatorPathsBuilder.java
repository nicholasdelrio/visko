package edu.utep.trustlab.visko.planning.operatorPaths;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public abstract class OperatorPathsBuilder {
	protected ViskoTripleStore ts;
	protected OperatorPaths operatorPaths;

	public OperatorPathsBuilder(){
		ts = new ViskoTripleStore();
		operatorPaths = new OperatorPaths();
	}
	
	public OperatorPaths getOperatorPaths(){
		if(operatorPaths.size() == 0)
			setOperatorPaths();
		return operatorPaths;
	}
	protected abstract void setOperatorPaths();	
}
