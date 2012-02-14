package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.publish.CIServer;

public class NickCIServer {
	
	public static CIServer getServer() {		
		return new CIServer("http://rio.cs.utep.edu/ciserver", "visko", "ndel2", "NDel209!");
	}
}
