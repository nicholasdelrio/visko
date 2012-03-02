package edu.utep.trustlab.visko.knowledge.github;

import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.github.ghostscript.GhostScriptToolkits;
import edu.utep.trustlab.visko.knowledge.github.ghostscript.GhostScriptTransformers;
import edu.utep.trustlab.visko.knowledge.github.gmt.GMTToolkits;
import edu.utep.trustlab.visko.knowledge.github.gmt.GMTTransformers;
import edu.utep.trustlab.visko.knowledge.github.ncl.NCLToolkits;
import edu.utep.trustlab.visko.knowledge.github.ncl.NCLTransformers;
import edu.utep.trustlab.visko.knowledge.github.universal.ViewerSets;
import edu.utep.trustlab.visko.knowledge.github.universal.Views;
import edu.utep.trustlab.visko.knowledge.github.vtk.VTKToolkits;
import edu.utep.trustlab.visko.knowledge.github.vtk.VTKTransformers;

public class Generator1 {
	public static void main(String[] args){
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		
		//create views and viewersets
		Views.create();
		ViewerSets.create();
		
		//create toolkits
		GhostScriptToolkits.create();		
		GMTToolkits.create();
		NCLToolkits.create();
		VTKToolkits.create();

		//create transformers
		NCLTransformers.create();
		GhostScriptTransformers.create();
		GMTTransformers.create();
		VTKTransformers.create();
	}
}
