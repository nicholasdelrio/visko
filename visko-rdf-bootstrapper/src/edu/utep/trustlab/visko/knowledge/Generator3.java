package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.ghostscript.GSServices;
import edu.utep.trustlab.visko.knowledge.gmt.GMTServices;
import edu.utep.trustlab.visko.knowledge.ncl.NCLServices;
import edu.utep.trustlab.visko.knowledge.vtk.VTKServices;

public class Generator3 {
	public static void main(String[] args){
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		
		//create services that depend on Transformers created in Generator2
		GSServices.create();
		GMTServices.create();
		NCLServices.create();
		VTKServices.create();
	}
}
