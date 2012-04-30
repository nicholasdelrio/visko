/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.contentManagement.aggregator;
	
import java.io.File;
import java.io.FileInputStream;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.tdb.*;

	public class TripleStore {

	private String tsDirectory;
	private String owlDirectory;
	private String rdfDirectory;
	private String formatsDirectory;
	private String formatRestrictionsDirectory;
	
	public TripleStore(String inputRDFDirectory, String inputOntologyDirectory, String inputFormatsDirectory, String inputFormatRestrictionsDirectory, String tripleStoreDirectory){
		rdfDirectory = inputRDFDirectory;
		tsDirectory = tripleStoreDirectory;	
		owlDirectory = inputOntologyDirectory;
		formatsDirectory = inputFormatsDirectory;
		formatRestrictionsDirectory = inputFormatRestrictionsDirectory;
		if(!tsDirectory.endsWith("/"))
			tsDirectory = tsDirectory + "/";
	}

	public String create(){

		File storesDirectory = new File(tsDirectory);

		TripleStore.clean(storesDirectory);
		Model model = TDBFactory.createModel(tsDirectory);

		TripleStore.aggregate(rdfDirectory, model);
		TripleStore.aggregate(owlDirectory, model);
		TripleStore.aggregate(formatsDirectory, model);
		TripleStore.aggregate(formatRestrictionsDirectory, model);
		System.out.println("Model now has " + model.size() + " statements.");
		model.close();
		
		return storesDirectory.getAbsolutePath();
	}
	
	public static void clean(File storesDirectory){
		// If it exists then clean it, else create it.
		if (storesDirectory.exists()){
			for(File aFile : storesDirectory.listFiles()){
				aFile.delete();
			}
		}
		else
			storesDirectory.mkdir();
	}
	
	public static void aggregate(String directory, Model model){
		// Iterate through all ontology files and load any pml data found
		File owl = new File(directory);
		for(File aFile : owl.listFiles()){
			try{
			model.read(new FileInputStream(aFile), null);
			}catch(Exception e){
				System.out.println("error: " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args){
		TripleStore ts = new TripleStore(args[0], args[1], args[2], args[3], args[4]);
		System.out.println(ts.create());
	}
}
