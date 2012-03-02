package edu.utep.trustlab.visko.knowledge.github;

import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.github.ghostscript.GSServices;
import edu.utep.trustlab.visko.knowledge.github.gmt.GMTServices;
import edu.utep.trustlab.visko.knowledge.github.ncl.NCLServices;
import edu.utep.trustlab.visko.knowledge.github.universal.Viewers;
import edu.utep.trustlab.visko.knowledge.github.vtk.VTKServices;

public class Generator2 {
	public static void main(String[] args){
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		
		//create viewers that depend on ViewerSets created in Generator1
		Viewers.create();
		
		//create services that depend on Transformers created in Generator1
		GSServices.create();
		GMTServices.create();
		NCLServices.create();
		VTKServices.create();
	}
}
