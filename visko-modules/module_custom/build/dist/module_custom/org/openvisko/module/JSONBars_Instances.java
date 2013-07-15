package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.GetURLContents;

public class JSONBars_Instances extends ToolkitOperator
{
	public JSONBars_Instances(){	
		super(null, null, true, true, "viskoInstances.json");
	}

	public String transform(){
		String webServerURL = FileUtils.getServerBaseURL().toString();
		String json = GetURLContents.downloadText(webServerURL + "/visko-web/ViskoServletManager?requestType=knowledge-base-info&info=rdfInstances");
		FileUtils.writeTextFile(json, FileUtils.getOutputDir().getAbsolutePath(), outputFileName);
		return outputURL;
	}
}
