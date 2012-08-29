package edu.utep.trustlab.visko.execution;

public class PipelineExecutorJobStatus {
	
	private int totalServiceCount;
	private int currentServiceIndex;
	private String currentServiceURI;

	public enum PipelineState {NODATA, EMPTYPIPELINE, RUNNING, COMPLETE, ERROR, NEW, INTERRUPTED};
	
	private PipelineState state;
	
	public PipelineExecutorJobStatus(){
		setPipelineState(PipelineState.NEW);
		totalServiceCount = 0;
		currentServiceIndex = 0;
		currentServiceURI = "No Service Currently Running...";
	}
		
	public String getCurrentServiceURI(){
		return currentServiceURI;
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
	
	public void setCurrentService(String serviceURI, int index){
		currentServiceIndex = index + 1;
		currentServiceURI = serviceURI;
	}
	
	public int getCurrentServiceIndex(){
		return currentServiceIndex;
	}
	
	public boolean didJobCompletedNormally(){
		return getPipelineState() == PipelineState.COMPLETE;
	}
	
	public boolean isJobCompleted(){
		boolean completed = getPipelineState() == PipelineState.COMPLETE;
		boolean error = getPipelineState() == PipelineState.ERROR;
		boolean nodata = getPipelineState() == PipelineState.NODATA;
		boolean emptyPipe = getPipelineState() == PipelineState.EMPTYPIPELINE;
		boolean interrupted = getPipelineState() == PipelineState.INTERRUPTED;
		
		return completed || error || nodata || emptyPipe || interrupted;
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
				message = "(" + getCurrentServiceIndex() + " of " + getTotalServiceCount() + ") Executing service: " + getCurrentServiceURI();
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
			case INTERRUPTED:
				message = "Execution Interrupted";
				break;
			default:
				message = "State of execution unknown";
		}
		return message;
	}
}