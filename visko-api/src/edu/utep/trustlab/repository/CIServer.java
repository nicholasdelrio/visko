package edu.utep.trustlab.repository;

import edu.utep.cybershare.ciclient.CIPut;
import edu.utep.cybershare.ciclient.CIReturnObject;
import edu.utep.cybershare.ciclient.ciconnect.CIClient;
import edu.utep.cybershare.ciclient.ciconnect.CIKnownServerTable;

public class CIServer extends Repository {
	
	private String uname;
	private String pword;
	private String url;
	private String pName;
	private CIClient ciClient;
	private static String INFIX = "ciprojects/";

	public CIServer(String serverURL, String projectName, String username, String password) {
		url = serverURL;
		pName = projectName;
		uname = username;
		pword = password;
		
		connect();
	}
	
	public CIServer(String serverURL){
		url = serverURL;
	}
	
	public String getBaseURL() {
		String viskoCIServerURL;
		if (url.endsWith("/")) {
			viskoCIServerURL = url + INFIX + pName + "/";
		} else {
			viskoCIServerURL = url + "/" + INFIX + pName + "/";
		}
		return viskoCIServerURL;

	}

	private void connect() {
		try {
			int rioServerId = CIKnownServerTable.getInstance().ciGetServerEntryFromURL(url);
			CIKnownServerTable.getInstance().ciSetServerPassword(rioServerId, pword);
			CIKnownServerTable.getInstance().ciSetServerUsername(rioServerId, uname);
			ciClient = new CIClient(rioServerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String saveDocument(String fileContents, String fileName) {
		try {
			CIReturnObject ro = null;
			ro = CIPut.ciUploadFile(ciClient, pName, fileName, fileContents, CIClient.VISKO_TYPE, true, false);
			System.out.println("The result is: error code: " + ro.gStatus + " with message: " + ro.gMessage);

			if (ro.gStatus.equals("0")) {
				System.out.println("The url of the new file is " + ro.gFileURL);
				System.out.println("Successfully Loaded Transformer file: "	+ ro.gFileURL);
				return ro.gFileURL;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Cannot upload VisKo document...");
			return null;
		}
	}
}