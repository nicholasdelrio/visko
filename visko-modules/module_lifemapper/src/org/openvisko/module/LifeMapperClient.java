package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.FileUtils;

public class LifeMapperClient extends ToolkitOperator {
	
	private static final String pythonEXE = "python2.7";
	private static final String lifeMapperScript = "\"" + FileUtils.getScriptsDir().getAbsolutePath() + "/" + "client.py\"";
	private static String resultBaseURL = "http://lifemapper.org/services/sdm/experiments/";
	private String inputURL;
	
	public LifeMapperClient(String dataURL){
		super(null, "input.txt", false, false, "experimentID.txt");
		inputURL = dataURL;
	}
	
	public String transform(String username, String password, String units, String algorithm){
		String command =	pythonEXE + " " +
							lifeMapperScript + " " +
							username + " " +
							password + " " +
							units + " " +
							algorithm + " " +
							"\"" + outputPath + "\" " +
							inputURL;
		
		System.out.println("going to dump file at: " + outputPath);
		
		CommandRunner.run(command);
		String experimentID = FileUtils.readTextFile(outputPath).trim();
		return resultBaseURL + experimentID;
	}
}