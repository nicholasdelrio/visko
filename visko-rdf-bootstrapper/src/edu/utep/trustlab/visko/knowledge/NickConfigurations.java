package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.repository.CIServer;
import edu.utep.trustlab.repository.LocalFileSystem;

public class NickConfigurations {
	
	public static CIServer getCIServer() {		
		return new CIServer("http://rio.cs.utep.edu/ciserver", "visko", "ndel2", "NDel209!");
	}
	
	public static LocalFileSystem getLocalFileSystem(){
		return new LocalFileSystem("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/", LocalFileSystem.VISKO_GITHUB_RDF_PATH);
	}
}
