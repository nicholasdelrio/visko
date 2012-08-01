package edu.utep.trustlab.visko.planning.sadi;

import java.util.Vector;

import org.apache.log4j.Logger;

import ca.wilkinsonlab.sadi.service.annotations.Name;
import ca.wilkinsonlab.sadi.service.annotations.ContactEmail;
import ca.wilkinsonlab.sadi.service.annotations.InputClass;
import ca.wilkinsonlab.sadi.service.annotations.OutputClass;
import ca.wilkinsonlab.sadi.service.simple.SimpleSynchronousServiceServlet;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
//import com.hp.hpl.jena.rdf.model.Statement;
//import com.hp.hpl.jena.rdf.model.StmtIterator;

import edu.utep.trustlab.visko.planning.*;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

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
		//needed by visko pipeline generator to identify pipelines
		ViskoTripleStore.setEndpointURL("http://iw.cs.utep.edu/visko-web/ViskoServletManager?requestType=query-triple-store");
		ViskoTripleStore tripleStore = new ViskoTripleStore();
		
		//extract inputs
		Resource dataset = input.getPropertyResourceValue(Vocab.wasDerivedFrom);
		String format = dataset.getProperty(Vocab.hasFormat).getObject().toString();
		String url = dataset.getProperty(Vocab.hasURL).getObject().toString();
		String viewerSet = input.getProperty(Vocab.hasResultViewableIn).getObject().toString();
		
		System.out.println("format: " + format);
		System.out.println("url: " + url);
		System.out.println("viewerSet: " + viewerSet);
		
		//construct visko query
		Query viskoQuery = new Query(url, format, viewerSet);
		
		//execute query engine to get set of pipelines back
		edu.utep.trustlab.visko.planning.QueryEngine engine = new edu.utep.trustlab.visko.planning.QueryEngine(viskoQuery);
		PipelineSet pipes = engine.getPipelines();
		
		//return query plan RDF
		Pipeline pipe;
		String owlsServiceURI;
		Resource currentServiceInvocation = null;
		Resource previousServiceInvocation = null;
		
		if(pipes.size() > 0){
			pipe = findLongestPipeline(pipes);
						
			for(int i = pipe.size() - 1; i >= 0; i --){
				owlsServiceURI = pipe.get(i);
				
				currentServiceInvocation = output.getModel().createResource(Vocab.serviceInvocation);
				setEquivalentSADIURI(currentServiceInvocation, owlsServiceURI, tripleStore);
						
				if(previousServiceInvocation == null)
					output.addProperty(Vocab.hadActivity, currentServiceInvocation);
				
				if(previousServiceInvocation != null){
					previousServiceInvocation.addProperty(Vocab.wasInformedBy, currentServiceInvocation);
					output.getModel().add(previousServiceInvocation, Vocab.wasInformedBy, currentServiceInvocation);
				}
				
				if(i == 0){
					Literal urlLiteral = output.getModel().createLiteral(url);
					currentServiceInvocation.addProperty(Vocab.used, urlLiteral);
					output.getModel().add(currentServiceInvocation, Vocab.used, urlLiteral);
				}
				previousServiceInvocation = currentServiceInvocation;
			}
		}
		
		System.out.println("setting prefixes...");
		
		//add prefixes to make rdf pretty
		output.getModel().setNsPrefix("pml-provenance", Vocab.pmlpPrefix);
		output.getModel().setNsPrefix("prov", Vocab.provPrefix);
		output.getModel().setNsPrefix("visko-query", Vocab.viskoQueryPrefix);
	}
	
	private static void setEquivalentSADIURI(Resource serviceInvocation, String owlsServiceURI, ViskoTripleStore tripleStore){
		Vector<String> sadiService = ResultSetToVector.getVectorFromResultSet(tripleStore.getEquivalentSADI(owlsServiceURI), "sadiService");
		if(sadiService.size() > 0)
			serviceInvocation.addProperty(Vocab.wasAttributedTo, sadiService.firstElement());
		else
			serviceInvocation.addProperty(Vocab.wasAttributedTo, owlsServiceURI);
	}
	
	private static Pipeline findLongestPipeline(PipelineSet set){
		Pipeline longestPipe = null;
		
		for(Pipeline pipe : set){
			if(longestPipe == null || pipe.size() > longestPipe.size()){
				longestPipe = pipe;
			}
		}
		System.out.println("longest pipe is length: " + longestPipe.size());
		return longestPipe;
	}

	@SuppressWarnings("unused")
	private static final class Vocab
	{
		private static Model m_model = ModelFactory.createDefaultModel();
		
		private static final String provPrefix = "http://www.w3.org/ns/prov/";
		private static final String viskoQueryPrefix = "https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-query.owl#";
		private static final String pmlpPrefix = "http://inference-web.org/2.0/pml-provenance.owl#";
		
		public static final Resource QueryPlan = m_model.createResource("https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-query.owl#QueryPlan");
		public static final Resource QueryPlanRequest = m_model.createResource("https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-query.owl#QueryPlanRequest");
		
		//prov related properties
		public static final Resource Activity = m_model.createResource(provPrefix + "Activity");
		public static final Property wasDerivedFrom = m_model.createProperty(provPrefix + "wasDerivedFrom");
		public static final Property hadActivity = m_model.createProperty(provPrefix + "hadActivity");
		public static final Property wasInformedBy = m_model.createProperty(provPrefix + "wasInformedBy");
		public static final Property used = m_model.createProperty(provPrefix + "used");
		public static final Property wasAttributedTo = m_model.createProperty(provPrefix + "wasAttributedTo");
		
		//viskoQuery properties
		public static final Property hasResultViewableIn = m_model.createProperty(viskoQueryPrefix + "hasResultViewableIn");
		public static final Resource serviceInvocation = m_model.createResource(viskoQueryPrefix + "ServiceInvocation");
		
		//pml-p related properties
		public static final Property hasFormat = m_model.createProperty(pmlpPrefix + "hasFormat");
		public static final Property hasURL = m_model.createProperty(pmlpPrefix + "hasURL");
	}
}