package edu.utep.trustlab.visko.ontology.writer;


import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.ontology.ViskoIndividual;
import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;

public abstract class ViskoWriter {
	protected ViskoIndividual viskoIndividual;
	
	protected OWLSModel owlsModel = new OWLSModel();
	protected ViskoModel viskoModel = new ViskoModel();
	
	public abstract String toRDFString();
	
	public String getURI() {
		return viskoIndividual.getURI();
	}
	
	public String saveDocument() {
		String fileContents = toRDFString();
		return Server.getServer().saveDocument(fileContents, viskoIndividual.getFileName());
	}
}
