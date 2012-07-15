package edu.utep.trustlab.visko.execution;

public class JobStatus {
	
	private int totalServiceCount;
	private int currentService;

	public enum PipelineState {NODATA, EMPTYPIPELINE, RUNNING, COMPLETE};
	
	private PipelineState state;
	
	public void setPipelineState(PipelineState pipeState){
		state = pipeState;
	}
	
	public PipelineState getPipelineState(){
		return state;
	}
	
	public void setTotalServiceCount(int count){
		totalServiceCount = count;
	}
	
	public int getTotalServiceCount(){
		return totalServiceCount;
	}
	
	public void setCurrentServiceIndex(int index){
		currentService = index + 1;
	}
	
	public int getCurrentServiceIndex(){
		return currentService;
	}
	
	public String getExecutionMessage(){
		if(getPipelineState() == PipelineState.EMPTYPIPELINE)
			return "Input Data is already visualizable.";
		else if(getPipelineState() == PipelineState.NODATA)
			return "Can't execute pipeline, no input data specified";
		else
			return "Executing service " + getCurrentServiceIndex() + " of " + getTotalServiceCount();
	}
}