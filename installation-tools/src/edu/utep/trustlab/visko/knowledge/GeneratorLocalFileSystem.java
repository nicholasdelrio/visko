package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;


public class GeneratorLocalFileSystem {
	public static void main(String[] args){
		
		ContentManager.setContentManager(getContentManager(args));
		String wsdlURL = args[2];
		
		Generator1.gen();
		System.out.println("gen1 done");
		System.out.println("Push to GitHub now!");
		System.out.println("*****************************************");
		System.out.println("cd visko");
		System.out.println("git add *");
		System.out.println("git commit -am 'adding new RDF'");
		System.out.println("git push <<your forked remote visko repo>>");
		System.console().readLine("Press <<enter>> after your have pushed to GitHub.");
		
		Generator2.gen();
		System.out.println("gen2 done");
		System.out.println("Push to GitHub now!");
		System.out.println("*****************************************");
		System.out.println("cd visko");
		System.out.println("git add *");
		System.out.println("git commit -am 'adding new RDF'");
		System.out.println("git push <<your forked remote visko repo>>");
		System.console().readLine("Press <<enter>> after your have pushed to GitHub.");
			
		Generator3.gen(wsdlURL);
		System.out.println("gen3 done");
		System.out.println("Push to GitHub now!");
		System.out.println("*****************************************");
		System.out.println("cd visko");
		System.out.println("git add rdf/*");
		System.out.println("git commit -am 'adding new RDF'");
		System.out.println("git push <<your forked remote visko repo>>");
		System.console().readLine("Press <<enter>> after your have pushed to GitHub.");
			
		Generator4.gen();
		System.out.println("gen4 done");
		System.out.println("Push to GitHub now!");
		System.out.println("*****************************************");
		System.out.println("cd visko");
		System.out.println("git add *");
		System.out.println("git commit -am 'adding new RDF'");
		System.out.println("git push <<your forked remote visko repo>>");
		System.console().readLine("Press <<enter>> after your have pushed to GitHub.");
		
		System.out.println("RDF Generation Complete!");

	}
	
	public static ContentManager getContentManager(String[] args){
		String serverURL = args[0];
		String rdfFilePath = args[1];

		LocalFileSystem client =  new LocalFileSystem(serverURL, rdfFilePath);
		return client;		
	}
}
