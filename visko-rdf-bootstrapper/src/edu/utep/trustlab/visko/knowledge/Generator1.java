package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.ghostscript.GSToolkits;
import edu.utep.trustlab.visko.knowledge.ghostscript.GSTransformers;
import edu.utep.trustlab.visko.knowledge.gmt.GMTToolkits;
import edu.utep.trustlab.visko.knowledge.gmt.GMTTransformers;
import edu.utep.trustlab.visko.knowledge.ncl.NCLToolkits;
import edu.utep.trustlab.visko.knowledge.ncl.NCLTransformers;
import edu.utep.trustlab.visko.knowledge.universal.ViewerSets;
import edu.utep.trustlab.visko.knowledge.universal.Views;
import edu.utep.trustlab.visko.knowledge.vtk.VTKToolkits;
import edu.utep.trustlab.visko.knowledge.vtk.VTKTransformers;

public class Generator1 {
	public static void main(String[] args){
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		
		//create views and viewersets
		Views.create();
		ViewerSets.create();
		
		//create toolkits
		GSToolkits.create();		
		GMTToolkits.create();
		NCLToolkits.create();
		VTKToolkits.create();

		//create transformers
		NCLTransformers.create();
		GSTransformers.create();
		GMTTransformers.create();
		VTKTransformers.create();
	}
}
