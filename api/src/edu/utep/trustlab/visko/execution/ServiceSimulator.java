package edu.utep.trustlab.visko.execution;

import edu.utep.trustlab.visko.util.FileUtils;

public class ServiceSimulator {
	
	protected static String exec(){
	
		String url = "http://dummy-domain.edu/dummy-data-" + FileUtils.getRandomFileName() + ".data";
		return url;
	}
}
