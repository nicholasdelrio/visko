/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.contentManagement;

import java.io.File;

import edu.utep.cybershare.ciclient.CIPut;
import edu.utep.cybershare.ciclient.CIReturnObject;
import edu.utep.cybershare.ciclient.ciconnect.CIClient;
import edu.utep.cybershare.ciclient.ciconnect.CIKnownServerTable;
import edu.utep.trustlab.contentManagement.util.FileUtilities;

public class CIServer extends ContentManager {
	
	private String uname;
	private String pword;
	private String url;
	private CIClient ciClient;
	private static String INFIX = "ciprojects/";

	public CIServer(String serverURL, String username, String password) {
		url = serverURL;
		uname = username;
		pword = password;
		
		connect();
	}
	
	public CIServer(String serverURL){
		url = serverURL;
	}
	
	public String getBaseURL(String fileName) {
		String viskoCIServerURL;
		
		if (url.endsWith("/")) {
			viskoCIServerURL = url + INFIX + projectName + "/";
		} else {
			viskoCIServerURL = url + "/" + INFIX + projectName + "/";
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
	
	@Override
	public String saveDocument(String fileContents, String fileName) {
		try {
			CIReturnObject ro = null;
			ro = CIPut.ciUploadFile(ciClient, projectName, fileName, fileContents, CIClient.VISKO_TYPE, true, false);
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

	@Override
	public String saveDocument(File file) {
		String fileContents = FileUtilities.readTextFile(file.getAbsolutePath());
		return saveDocument(fileContents, file.getName());
	}
}
