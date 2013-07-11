package org.openvisko.module;

import javax.jws.WebParam;

public class ModuleService {

	public String fits2png(@WebParam(name="url") String url){
		FITSToPNG service = new FITSToPNG(url);
		return service.transform();
	}
	
	public String spherize(@WebParam(name="url") String url){
		Spherize service = new Spherize(url);
		return service.transform();
	}
	

}
