package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.repository.LocalFileSystem;
import edu.utep.trustlab.repository.Repository;

public class GeneratorManager {
	public static void main(String[] args){
		int generatorID = Integer.valueOf(args[0]);
		String serverURL = args[1];
		String filePath = args[2];
		
		LocalFileSystem fs = new LocalFileSystem(serverURL, filePath);
		Repository.setRepository(fs);
		
		switch(generatorID){
			case 1: 
				Generator1.gen();
				break;
			case 2: 
				Generator2.gen();
				break;
			case 3: 
				Generator3.gen();
				break;
			case 4:
				Generator4.gen();
				break;
			default: 
				Generator1.gen();
		}
	}
}
