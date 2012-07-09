package edu.utep.trustlab.visko.execution.sadi;

import org.apache.log4j.Logger;

import ca.wilkinsonlab.sadi.service.annotations.Name;
import ca.wilkinsonlab.sadi.service.annotations.ContactEmail;
import ca.wilkinsonlab.sadi.service.annotations.InputClass;
import ca.wilkinsonlab.sadi.service.annotations.OutputClass;
import ca.wilkinsonlab.sadi.service.simple.SimpleSynchronousServiceServlet;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
//import com.hp.hpl.jena.rdf.model.Statement;
//import com.hp.hpl.jena.rdf.model.StmtIterator;

import edu.utep.trustlab.visko.execution.*;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

@Name("planner")
@ContactEmail("nicholas.delrio@gmail.com")
@InputClass("https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-query.owl#QueryPlanRequest")
@OutputClass("https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-query.owl#QueryPlan")
public class QueryEngine extends SimpleSynchronousServiceServlet
{
	private static final Logger log = Logger.getLogger(QueryEngine.class);
	private static final long serialVersionUID = 1L;

	@Override
	public void processInput(Resource input, Resource output)
	{
		ViskoTripleStore.setEndpointURL("http://iw.cs.utep.edu:8080/visko-web/ViskoServletManager?requestType=query-triple-store");
		
		String viewerSet = input.getProperty(Vocab.hasResultViewableIn).getObject().toString();
		
		Resource dataset = input.getPropertyResourceValue(Vocab.wasDerivedFrom);
		String format = dataset.getProperty(Vocab.hasFormat).getObject().toString();
		String url = dataset.getProperty(Vocab.hasURL).getObject().toString();
		
		Query viskoQuery = new Query(url, format, viewerSet);
		edu.utep.trustlab.visko.execution.QueryEngine engine = new edu.utep.trustlab.visko.execution.QueryEngine(viskoQuery);
		PipelineSet pipes = engine.getPipelines();
		
		Pipeline pipe;
		String serviceURI;
		Resource currentService = null;
		Resource previousService = null;
		//only return single pipe for now

		if(pipes.size() > 0){
			pipe = pipes.firstElement();
			
			for(int i = pipe.size() - 1; i >= 0; i --){
				serviceURI = pipe.get(i);
				currentService = Vocab.m_model.createResource(serviceURI);
				
				if(i == pipe.size()){
					output.addProperty(Vocab.hadActivity, currentService);
				}
				if(previousService != null){
					currentService.addProperty(Vocab.wasInformedBy, previousService);
				}
				previousService = currentService;
			}		
		}
	}

	@SuppressWarnings("unused")
	private static final class Vocab
	{
		public static Model m_model = ModelFactory.createDefaultModel();
		
		public static final Resource QueryPlan = m_model.createResource("https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-query.owl#QueryPlan");
		public static final Resource QueryPlanRequest = m_model.createResource("https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-query.owl#QueryPlanRequest");
		public static final Property wasDerivedFrom = m_model.createProperty("http://www.w3.org/ns/prov/wasDerivedFrom");
		public static final Property hasFormat = m_model.createProperty("http://inference-web.org/2.0/pml-provenance.owl#hasFormat");
		public static final Property hasURL = m_model.createProperty("http://inference-web.org/2.0/pml-provenance.owl#hasURL");
		public static final Property hasResultViewableIn = m_model.createProperty("https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-query.owl#hasResultViewableIn"); 
		public static final Property hadActivity = m_model.createProperty("http://www.w3.org/ns/prov/hadActivity");
		public static final Property wasInformedBy = m_model.createProperty("http://www.w3.org/ns/prov/wasInformedBy");
	}
}