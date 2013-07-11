package edu.utep.trustlab.visko.web.context;

import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.planning.QueryEngine;

public class ViskoWebSession {
	
	public static final String SESSION_ID = "visko-web-session";
	
	private QueryEngine queryEngine;
	private PipelineExecutor pipelineExecutor;
	
	public void setQueryEngine(QueryEngine engine){
		queryEngine = engine;
	}
	
	public QueryEngine getQueryEngine(){
		return queryEngine;
	}
	
	public void setPipelineExecutor(PipelineExecutor executor){
		pipelineExecutor = executor;
	}
	
	public PipelineExecutor getPipelineExecutor(){
		return pipelineExecutor;
	}
	
	public boolean hasPipelineExecutor(){
		if(pipelineExecutor == null)
			return false;
		return true;
	}
	
	public void removePipelineExecutor(){
		pipelineExecutor = null;
	}
}
