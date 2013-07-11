package org.openvisko.module;

import javax.jws.WebParam;

public class ModuleService {

  // TODO: add your own methods here
  // use the @WebParam(name="xxx") annotation to declare parameters
  // for your service methods
	
	public String lifeMapperSimulation(
			@WebParam(name="url") String url,
			@WebParam(name="username") String username, 
			@WebParam(name="password") String password,
			@WebParam(name="units") String units, 
			@WebParam(name="algorithm") String algorithm){
		LifeMapperClient service = new LifeMapperClient(url);
		return service.transform(username, password, units, algorithm);
	}	
}
