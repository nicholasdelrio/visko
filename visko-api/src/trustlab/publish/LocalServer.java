package trustlab.publish;

import trustlab.visko.util.FileUtilities;

public class LocalServer extends Server{
	
	private String url;
	private String path;
	
	public LocalServer(String serverURL, String serverPath) {
		url = serverURL;
		path = serverPath;
	}
	
	public LocalServer(String serverURL){
		url = serverURL;
	}
	
	public String getBaseURL(){
		return url;
	}
	public String saveDocument(String fileContents, String fileName) {
		FileUtilities.writeTextFile(fileContents, path, fileName);
		return url + fileName;
	}
}