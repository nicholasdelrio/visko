package trustlab.visko.execution;

import java.io.*;

import org.w3c.dom.*;

import trustlab.visko.ontology.service.OWLSService;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class PipelineToXMLVisualizationSet {

	/** Generate the XML document */
	public static String toXMLFromPipelineSet(PipelineSet pipelines,
			String nodesetURI, int maxResults) {
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = fact.newDocumentBuilder();
			Document doc = parser.newDocument();

			Element root = doc.createElement("VisualizationSet");

			if (nodesetURI != null)
				root.setAttribute("visualizes", nodesetURI);
			else if (pipelines.getArtifactURL() != null)
				root.setAttribute("visualizes", pipelines.getArtifactURL());

			doc.appendChild(root);

			for (int i = 0; i < maxResults && i < pipelines.size(); i++) {
				if (!hasVolumeRendering(pipelines.get(i))) {
					Element visualization = doc.createElement("Visualization");
					visualization.setAttribute("targetViewer", pipelines.get(i)
							.getViewer().getURI());

					String resultURL = pipelines.get(i).executePath(false);

					if (resultURL != null) {
						visualization
								.appendChild(doc.createTextNode(resultURL));
						root.appendChild(visualization);
					}
				}
			}

			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();
			return xmlString;
		} catch (Exception ex) {
			System.err.println("+============================+");
			System.err.println("|        XML Error           |");
			System.err.println("+============================+");
			System.err.println(ex.getClass());
			System.err.println(ex.getMessage());
			System.err.println("+============================+");
			return null;
		}
	}

	private static boolean hasVolumeRendering(Pipeline pipeline) {
		for (OWLSService service : pipeline) {
			if (service.getURI().contains("vtkVolumeService"))
				return true;
		}

		return false;
	}
}
