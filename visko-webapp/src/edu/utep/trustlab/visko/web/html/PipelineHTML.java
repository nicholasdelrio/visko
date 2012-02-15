package edu.utep.trustlab.visko.web.html;
import java.util.HashMap;
import java.util.Vector;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.variable.Input;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.RedirectURI;
import edu.utep.trustlab.visko.util.ResultSetToVector;
import edu.utep.trustlab.visko.execution.Pipeline;
import edu.utep.trustlab.visko.ontology.operator.Viewer;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.service.OWLSService;

public class PipelineHTML
{	
	
	public static String getPipelineHTML(Pipeline pipe)
	{
		String html = "<ol>";

		HashMap<String, String> bindings = pipe.getParameterBindings();
		
		for(OWLSService service : pipe)
		{	
			String trueURI = service.getURI();
			String resolvableURI = RedirectURI.redirectHack(trueURI, Repository.getServer().getBaseURL());
			html += "<li><b>Service Name:</b> <a href=\"" + resolvableURI + "\">" + trueURI + "</a></li>";
			html += "<ul>";
			
			html += "<li><b>Operator Type:</b> ";
	
			String operatorURI = service.getConceptualOperator().getURI();
			ViskoTripleStore ts = new ViskoTripleStore();
			
			if(!ts.isMapper(operatorURI))
			{html += "<a href=\"http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Transformer\">Transformer</a>";}
			
			else
			{
				Vector<String> viewURIs = ResultSetToVector.getVectorFromResultSet(ts.getViewsGeneratedFrom(operatorURI), "view");
				html += "<a href=\"http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Mapper\">Mapper</a>";
				html += "<ul><li>Generates View: <a href=\"" + viewURIs.firstElement() + "\">" + viewURIs.firstElement() + "</a></li></ul>";
			}
			
			
			OWLIndividualList<Input> paramList = service.getIndividual().getProfile().getInputs();
			String parameterURI;
			String parameterResolvableURI;
			String parameterValue;
			if(paramList.size() > 0)
			{
				for(Input inputParameter : paramList)
				{
					parameterURI = inputParameter.getURI().toASCIIString();
					parameterResolvableURI = RedirectURI.redirectHack(parameterURI, Repository.getServer().getBaseURL());
					parameterValue = bindings.get(parameterURI);
	
					if(!parameterURI.contains("url") && !parameterURI.contains("datasetURL"))
					{
						html += "<li><b>Parameter:</b> <a href=\"" + parameterResolvableURI + "\">" + parameterURI + " </a><b>=</b> " + parameterValue;
					}
				}
			}
			
			else
			{html += "<li><b>No Parameters</b></li>";}
		
			html += "</ul>";
			
			html += "<ul>";
			html += "<li><b>Supporting Toolkit:</b> <a href=\"" + service.getSupportingToolkit().getURI() + "\">" + service.getSupportingToolkit().getLabel() + "</a></li>";
			html += "<li><b>Implements Operator:</b> <a href=\"" + service.getConceptualOperator().getURI() + "\">" + service.getConceptualOperator().getURI() + "</a></li>";
			html += "<li><b>Input Format(s):</b>";
			html += getInputFormatList(service.getConceptualOperator().getOperatesOnFormats()) + "</li>";
			html += "</ul>";
			html += "<p>---------------------------------------------------------------------------------------------------</p>";
		}
		html += "</ol>";
		
		return html;
	}
	
	private static String getInputFormatList(Vector<Format> formats)
	{
		String html = "<ul>";
		for(Format format : formats)
		{html += "<li><a href=\"" + format.getURI() + "\">" + format.getURI() + "</a>";}
		html += "</ul>";
		
		return html;
	}
	
	
	public static String getViewerHTML(Pipeline pipe)
	{
		String html;
		Viewer viewer = pipe.getViewer();
		if(viewer != null)
		{
			html = "<ul>";
			html += "<li>" + viewer.getLabel() + "</li>";
			html += "<li><a href=\"" + viewer.getURI() + "\">" + viewer.getURI() + "</a></li>";
			
			html += "<li><b>Input Format(s):</b>";
			html += getInputFormatList(viewer.getOperatesOnFormats()) + "</li>";
			
			html += "<ul>";
		}
		else
			html = "<p>Viewer was not specified.</p>";
		
		return html;
	}
	
}