package edu.utep.trustlab.visko.util;

public class RedirectURI {
	
	public static String redirectHack(String uri, String newURIPrefix) {
		String viskoOWLSPrefix = "http://trust.utep.edu/visko/services/";

		if(!newURIPrefix.endsWith("/"))
			newURIPrefix = newURIPrefix + "/";
			
		String redirectedURI = uri;
		if (uri.contains(viskoOWLSPrefix)) {
			String fileName = uri.substring(viskoOWLSPrefix.length());
			redirectedURI = newURIPrefix + fileName;
		}
		return redirectedURI;
	}
}
