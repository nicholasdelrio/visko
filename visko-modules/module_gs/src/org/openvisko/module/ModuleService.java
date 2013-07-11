package org.openvisko.module;

import javax.jws.WebParam;

public class ModuleService {

	public String pdf2png(@WebParam(name="url") String url){
		PDFToPNG service = new PDFToPNG(url);
		return service.transform();
	}
	
	public String ps2pdf(@WebParam(name="url") String url){
		PSToPDF service = new PSToPDF(url);
		return service.transform();
	}
	
	public String ps2png(@WebParam(name = "url") String url){
		PSToPNG service = new PSToPNG(url);
		return service.transform();
	}

}
