package edu.utep.trustlab.visko.execution;

public class PipelineExecutorJobStatus {
	
	private int totalServiceCount;
	private int currentService;

	public enum PipelineState {NODATA, EMPTYPIPELINE, RUNNING, COMPLETE, ERROR, NEW};
	
	private PipelineState state;
	
	public PipelineExecutorJobStatus(){
		setPipelineState(PipelineState.NEW);
	}
	
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
	
	public String toString(){
		String message;
		switch(getPipelineState()){
			case NODATA:
				message = "Input Data is already visualizable.";
				break;
			case EMPTYPIPELINE:
				message = "Can't execute pipeline, no input data specified";
				break;
			case RUNNING:
				message = "Executing service " + getCurrentServiceIndex() + " of " + getTotalServiceCount();
				break;
			case COMPLETE:
				message = "Execution Completed";
				break;
			case ERROR:
				message = "Execution Error";
				break;
			case NEW:
				message = "Pre execution stage";
				break;
			default:
				message = "State of execution unknown";
		}
		return message;
	}
}