package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.ghostscript.GSTransformers;
import edu.utep.trustlab.visko.knowledge.gmt.GMTTransformers;
import edu.utep.trustlab.visko.knowledge.ncl.NCLTransformers;
import edu.utep.trustlab.visko.knowledge.universal.Viewers;
import edu.utep.trustlab.visko.knowledge.vtk.VTKTransformers;

public class Generator2 {

	public static void main(String[] args){
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		
		//create viewers that depend on ViewerSets created in Generator1
		Viewers.create();		
		
		//create transformers
		NCLTransformers.create();
		GSTransformers.create();
		GMTTransformers.create();
		VTKTransformers.create();
	}
	
}
