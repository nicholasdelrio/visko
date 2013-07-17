package org.openvisko.module;
import java.io.StringWriter;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.ServerProperties;

public class CSVToTabularASCII extends ToolkitOperator
{

	public CSVToTabularASCII(String esriGriddedURL){	
		super(esriGriddedURL, "asciiTable.csv", true, true, "tabularASCII.txt");
	}
		
	public String transform()
	{
		StringWriter tabularASCII = new StringWriter();
		
		stringData = stringData.replace("\"", "");
		String[] rows = stringData.split("/n");
		for(String row : rows)
		{
			String[] elements = row.split(",");
			
			String cleanedRow = "";
			for(String element : elements)
			{
				element = element.trim();
				cleanedRow += element + " ";
			}
			cleanedRow = cleanedRow.substring(0, cleanedRow.lastIndexOf(" ")-1);
			tabularASCII.append(cleanedRow + "\n");
		}
		
		FileUtils.writeTextFile(tabularASCII.toString(), ServerProperties.getInstance().getOutputDir().getAbsolutePath(), outputFileName);
		return outputURL;
	}
}//end class 
