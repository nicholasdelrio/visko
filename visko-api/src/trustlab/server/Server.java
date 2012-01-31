package trustlab.server;

public abstract class Server {

	private static Server SERVER;
	
	public static void setServer(Server server){
		SERVER = server;
	}
	
	public static Server getServer(){
		return SERVER;
	}
	
	public abstract String saveDocument(String fileContents, String fileName);
	public abstract String getBaseURL();
}
