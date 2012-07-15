package edu.utep.trustlab.visko.execution;

public class JobStatus {
	
	private int totalServiceCount;
	private int currentService;
	private boolean isComplete;
	
	public void setTotalServiceCount(int count){
		totalServiceCount = count;
	}
	
	public int getTotalServiceCount(){
		return totalServiceCount;
	}
	
	public void setCurrentServiceIndex(int index){
		currentService = index;
	}
	
	public int getCurrentServiceIndex(){
		return currentService;
	}
	
	public void setIsComplete(boolean complete){
		isComplete = complete;
	}
	
	public boolean isComplete(){
		return isComplete;
	}
	
	public String getExecutionMessage(){
		return "Executing service " + getCurrentServiceIndex() + " of " + getTotalServiceCount();
	}
}