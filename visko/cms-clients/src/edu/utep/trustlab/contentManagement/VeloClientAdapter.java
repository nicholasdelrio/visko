/**
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH 
DAMAGE.
 */

package edu.utep.trustlab.contentManagement;

import java.io.File;
import org.velo.java.client.VeloJavaClient;

import edu.utep.trustlab.contentManagement.util.FileUtilities;

public class VeloClientAdapter extends ContentManager {

	private VeloJavaClient client; 

	private String uname;
	private String pword;
	private String url;
	
	public VeloClientAdapter(String serverURL, String username, String password){
		url = serverURL;
		
		if(!url.endsWith("/"))
			url = url + "/";
		
		uname = username;
		pword = password;
		
		client = new VeloJavaClient();
        client.setBaseUrl(url);
        client.setAuthentication(uname, pword);
	}
	
	@Override
	public String saveDocument(File file) {
		client.upload("/Projects/" + projectName, file);
		
		return getBaseURL() + file.getName();
	}

	@Override
	public String saveDocument(String fileContents, String fileName) {
		String filePath = FileUtilities.writeTextFile(fileContents, ContentManager.getWorkspacePath(), fileName);
		client.upload("/Projects/" + projectName, filePath);
		
		return getBaseURL() + fileName;
	}

	@Override
	public String saveDocument(byte[] fileContents, String fileName) {
		String filePath = FileUtilities.writeBinaryFile(fileContents, ContentManager.getWorkspacePath(), fileName);
		client.upload("/Projects/" + projectName, filePath);
		
		return getBaseURL() + fileName;		
	}
	
	@Override
	public String getBaseURL() {
		if(projectName != null)
			return url + "webdav/Projects/" + projectName + "/";
		
		return null;
	}

	@Override
	public String saveDocument(String url) {
		return this.url + "webdav/" + client.uploadUrl("/Projects/" + projectName, url);
	}
}