package edu.utep.trustlab.repository.aggregator;
	
import java.io.File;
import java.io.FileInputStream;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.tdb.*;

	public class TripleStore {
	
	private static String MODELNAME = "TDB-VISKO";
	private String tsDirectory;
	private String rdfDirectory;
	
	public TripleStore(String inputRDFDirectory, String tripleStoreDirectory){
		rdfDirectory = inputRDFDirectory;
		tsDirectory = tripleStoreDirectory;	
		
		if(!tsDirectory.endsWith("/"))
			tsDirectory = tsDirectory + "/";
	}

	public String create(){
		File storesDirectory = new File(tsDirectory + MODELNAME);
		
		// If it exists then clean it, else create it.
		if (storesDirectory.exists()){
			for(File aFile : storesDirectory.listFiles()){
				aFile.delete();
			}
		}
		else
			storesDirectory.mkdir();

		Model model = TDBFactory.createModel(MODELNAME);

		// Iterate through all files and load any pml data found
		File rdf = new File(rdfDirectory);
		for(File aFile : rdf.listFiles()){
			try{
			model.read(new FileInputStream(aFile), null);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		System.out.println("Model now has " + model.size() + " statements.");
		model.close();
		
		return storesDirectory.getAbsolutePath();
	}
	
	public static void main(String[] args){
		if(args.length != 2){
			System.err.print("Need to specify the directory where your RDF is stored and the directory where the triple store will be created.");
		}
		
		TripleStore ts = new TripleStore(args[0], args[1]);
		System.out.println(ts.create());
	}
}
