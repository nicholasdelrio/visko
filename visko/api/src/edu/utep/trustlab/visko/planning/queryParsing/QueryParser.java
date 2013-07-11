package edu.utep.trustlab.visko.planning.queryParsing;

import java.util.HashMap;

public interface QueryParser {

	public String getContentURL();
	public String getFormatURI();
	public String getViewerSetURI();
	public String getViewURI();
	public String getSemanticTypeURI();
	public String getTargetFormatURI();
	public String getTargetTypeURI();
	public HashMap<String, String> getParameterBindings();
	public HashMap<String, String> getPrefixes();
	public String getNodesetURI();
	public String[] getTokens();
	public void parse();
	public boolean isValidQuery();
	public boolean dataIsFiltered();
	
}
