package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;


public class GeneratorLocalFileSystem {
	public static void main(String[] args){
		
		ContentManager.setContentManager(getContentManager(args));
		
		Generator1.gen();
		System.out.println("gen1 done");
		System.console().writer().print("Push to GitHug now!");
		System.console().writer().print("cd visko");
		System.console().writer().print("git add *");
		System.console().writer().print("git commit -am 'adding new RDF'");
		System.console().writer().print("git push <<your forked remote visko repo>>");
		System.console().readLine();
		
		Generator2.gen();
		System.out.println("gen2 done");
		System.console().writer().print("Push to GitHug now!");
		System.console().writer().print("cd visko");
		System.console().writer().print("git add *");
		System.console().writer().print("git commit -am 'adding new RDF'");
		System.console().writer().print("git push <<your forked remote visko repo>>");
		System.console().readLine();
			
		Generator3.gen();
		System.out.println("gen3 done");
		System.console().writer().print("Push to GitHug now!");
		System.console().writer().print("cd visko");
		System.console().writer().print("git add *");
		System.console().writer().print("git commit -am 'adding new RDF'");
		System.console().writer().print("git push <<your forked remote visko repo>>");
		System.console().readLine();
			
		Generator4.gen();
		System.out.println("gen4 done");
		System.console().writer().print("Push to GitHug now!");
		System.console().writer().print("cd visko");
		System.console().writer().print("git add *");
		System.console().writer().print("git commit -am 'adding new RDF'");
		System.console().writer().print("git push <<your forked remote visko repo>>");
		System.console().readLine();

	}
	
	public static ContentManager getContentManager(String[] args){
		String serverURL = args[0];
		String rdfFilePath = args[1];

		LocalFileSystem client =  new LocalFileSystem(serverURL, rdfFilePath);
		return client;		
	}
}
