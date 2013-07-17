package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class MoleculeRendering extends ToolkitOperator{

	public final static int RENDERING_DEFAULT = 0; 
	public final static int RENDERING_CARTOON = 1; 
	public final static int RENDERING_RIBBON = 2; 
	
	private static final String SCRIPT_MOLECULE_DEFAULT = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "jmol-default.sh";
	private static final String SCRIPT_MOLECULE_CARTOON = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "jmol-cartoon.sh";
	private static final String SCRIPT_MOLECULE_RIBBON = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "jmol-ribbon.sh";

	private String selected_script;
	
	public MoleculeRendering(String pdbFileURL, int renderingOption){	
			super(pdbFileURL, "mol-struct.pdb", false, false, "mol-struct.jpg");
			selected_script = SCRIPT_MOLECULE_DEFAULT;
			if (renderingOption == RENDERING_CARTOON)
				selected_script = SCRIPT_MOLECULE_CARTOON;
			else if (renderingOption == RENDERING_RIBBON)
				selected_script = SCRIPT_MOLECULE_RIBBON;
	}
			

	public String transform(){
			String command;
			
			//TODO: move this command to the init() of the module and make sure it runs for all SCRIPT_MOLECULE_*
			command = "chmod 755 " + selected_script;
			CommandRunner.run(command);
			
			command = selected_script + " " + inputPath + " " + outputPath + " " + 
					ServerProperties.getInstance().getWebappDir().getAbsolutePath();
			CommandRunner.run(command);
			
			return outputURL;
	}
} 
