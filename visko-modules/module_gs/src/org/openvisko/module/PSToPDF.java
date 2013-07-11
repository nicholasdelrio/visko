package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;

public class PSToPDF extends ToolkitOperator{
	
	private static final String GHOSTSCRIPT = "gs";

	public PSToPDF(String psFileURL){	
		super(psFileURL, "document.ps", true, false, "pdfDocument.pdf");
	}
	
	public String transform(){
		String command = GHOSTSCRIPT + " " + "-dNOPAUSE -sOutputFile=" + outputPath + " -q -dBATCH -sDEVICE=pdfwrite " + inputPath;
		CommandRunner.run(command);
		
		return outputURL;
	}
} 
