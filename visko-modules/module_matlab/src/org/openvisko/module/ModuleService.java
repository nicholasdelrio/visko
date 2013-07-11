package org.openvisko.module;

import javax.jws.WebParam;


public class ModuleService {

  // TODO: add your own methods here
  // use the @WebParam(name="xxx") annotation to declare parameters
  // for your service methods

	public String matlab2DRaster(
			@WebParam(name="url") String url,
			@WebParam(name="outputFile") String outputFile,
			@WebParam(name="selectedColor") String selectedColor  // Gray, Hot
			){
		Matlab2DRaster service = new Matlab2DRaster(url);
		return service.transform(url, outputFile, selectedColor);
	}
	
}
