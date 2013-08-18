package org.openvisko.module;

/*
 Author:Joseph Aguirre
 Date:7/13/13 
 Version: 1.0
 Company: Cyber-ShARE at UTEP
 Description:This program was developed for the Cyber-ShARE group at utep to
 invoke or call an external program that will take a URL and a save path
 and creates a PDF version of the webpage at the given URL. The PDFs created
 will be used to by the Cyber-ShARE group to develop a page to show thumbnails
 of webpages.
 */
//import java.io.*;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class PDFConverter extends ToolkitOperator {

	public static final String convertexe = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/bin/" + "wkhtmltopdf";

	public PDFConverter(String url) {
		super(url, "pdfDocument.pdf");
	}

	public String transform() {
		try {

			String commandString = "\"" + convertexe + "\" " + inputDatasetURL + " \"" + outputPath + "\"";

			int exitVal = CommandRunner.run(commandString);
			
			/*
			String array[] = { convertexe, inputDatasetURL, outputPath };
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(array);
			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = null;
			
			while ((line = input.readLine()) != null)
				System.out.println(line);
			
			int exitVal = pr.waitFor();*/
			if (exitVal == 0) {
				System.out.println("Done.");
			}
			else {
				System.out.println("Program terminated with an error "+ exitVal);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return outputURL;
	}
}