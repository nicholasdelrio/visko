package org.openvisko.module;

import javax.jws.WebParam;

public class ModuleService 
{
	public String html2pdf(@WebParam(name="url") String url)
	{
		PDFConverter converter=new PDFConverter(url);
		return converter.transform();
	}
  // TODO: add your own methods here
  // use the @WebParam(name="xxx") annotation to declare parameters
  // for your service methods	
}
