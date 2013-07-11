package org.openvisko.module;

import javax.jws.WebParam;


public class ModuleService {

	public String moleculeRendering(@WebParam(name="url") String url){
		MoleculeRendering service = new MoleculeRendering(url, MoleculeRendering.RENDERING_DEFAULT);
		return service.transform();
	}
	
	public String moleculeRenderingCartoon(@WebParam(name="url") String url){
		MoleculeRendering service = new MoleculeRendering(url, MoleculeRendering.RENDERING_CARTOON);
		return service.transform();
	}
	
	public String moleculeRenderingRibbon(@WebParam(name="url") String url){
		MoleculeRendering service = new MoleculeRendering(url, MoleculeRendering.RENDERING_RIBBON);
		return service.transform();
	}
	

}
