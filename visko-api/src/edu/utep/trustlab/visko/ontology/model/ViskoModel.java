package edu.utep.trustlab.visko.ontology.model;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.vocabulary.ESIPData;
import edu.utep.trustlab.visko.ontology.vocabulary.OWLS_Process;
import edu.utep.trustlab.visko.ontology.vocabulary.OWLS_Service;
import edu.utep.trustlab.visko.ontology.vocabulary.PMLP;
import edu.utep.trustlab.visko.ontology.vocabulary.RDFS;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

public class ViskoModel{
	private OntModel model;
	private Ontology ontology;
	private static Vector<ViskoModel> models = new Vector<ViskoModel>();
	
	public ViskoModel() {
		models.add(this);
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	}

	public static void closeModels() {
		for (ViskoModel model : models) {
			model.closeModel();
		}
	}

	public static OntModel getOntModelFromRDFFile(String rdfFileContents) {
		OntModel model = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		StringReader reader = new StringReader(rdfFileContents);
		model.read(reader, null);
		return model;
	}

	public void createOntology(String uri) {
		if (ontology != null)
			System.out
					.println("this model already has an ontology and will not create another one...");
		else
			ontology = model.createOntology(uri);
	}

	private OntModel getRelevantOntModel(String uri) {
		OntModel ontModel = null;
		if (uri.contains(ViskoV.ONTOLOGY_VISKO_V_URI)) {
			ontModel = ViskoV.getModel();
			this.addViskoVImport();
		} else if (uri.contains(ESIPData.ONTOLOGY_ESIP_NS)) {
			ontModel = ESIPData.getModel();
			this.addESIPImport();
		} else if (uri.contains(ViskoO.ONTOLOGY_VISKO_O_URI)) {
			ontModel = ViskoO.getModel();
			this.addViskoOImport();
		} else if (uri.contains(ViskoS.ONTOLOGY_VISKO_S_URI)) {
			ontModel = ViskoS.getModel();
			this.addViskoSImport();
		} else if (uri.contains(PMLP.ONTOLOGY_PMLP_URI)) {
			ontModel = ViskoO.getModel();
			this.addPMLPImport();
		} else if (uri.contains(OWLS_Service.ONTOLOGY_OWLS_SERVICE_URI)) {
			ontModel = OWLS_Service.getModel();
			this.addOWLSServiceImport();
		} else if (uri.contains(OWLS_Process.ONTOLOGY_OWLS_PROCESS_URI)) {
			ontModel = OWLS_Process.getModel();
			this.addOWLSProcessImport();
		} else if (uri.contains(RDFS.ONTOLOGY_RDFS_URI)) {
			ontModel = RDFS.getModel();
		} else
			System.out
					.println("Couldn't find OntClass in VisKo ontology collection...");

		return ontModel;

	}

	public String getModelAsRDFString() {
		StringWriter wtr = new StringWriter();
		model.write(wtr, "RDF/XML-ABBREV");
		return wtr.toString();
	}

	public Individual getIndividual(String uri) {
		model.read(uri);
		return model.getIndividual(uri);
	}

	public OntClass getOntClass(String uri) {
		OntModel relevantModel = getRelevantOntModel(uri);
		return relevantModel.getOntClass(uri);
	}

	public ObjectProperty getObjectProperty(String uri) {
		OntModel relevantModel = getRelevantOntModel(uri);
		return relevantModel.getObjectProperty(uri);
	}

	public Property getProperty(String uri) {
		OntModel relevantModel = getRelevantOntModel(uri);
		return relevantModel.getProperty(uri);
	}

	public Individual createIndividual(String uri, OntClass ontClass) {
		return model.createIndividual(uri, ontClass);
	}

	public Literal createLiteral(String value) {
		return model.createLiteral(value);
	}

	public Literal createTypedLiteral(String value, String typeURI) {
		return model.createTypedLiteral(value, typeURI);
	}

	public DatatypeProperty getDatatypeProperty(String uri) {
		OntModel relevantModel = getRelevantOntModel(uri);
		return relevantModel.getDatatypeProperty(uri);
	}

	public void closeModel() {
		model.close();
	}

	private void addViskoVImport() {
		if (ontology != null) {
			ontology.addImport(ViskoV.getOntology());
			model.addLoadedImport(ViskoV.ONTOLOGY_VISKO_V_URI);
			model.setNsPrefix("viskoV", ViskoV.ONTOLOGY_VISKO_V_URI + "#");
		}
	}

	private void addViskoOImport() {
		if (ontology != null) {
			ontology.addImport(ViskoO.getOntology());
			model.addLoadedImport(ViskoO.ONTOLOGY_VISKO_O_URI);
			model.setNsPrefix("viskoO", ViskoO.ONTOLOGY_VISKO_O_URI + "#");
		}
	}

	private void addViskoSImport() {
		if (ontology != null) {
			ontology.addImport(ViskoS.getOntology());
			model.addLoadedImport(ViskoS.ONTOLOGY_VISKO_S_URI);
			model.setNsPrefix("viskoS", ViskoS.ONTOLOGY_VISKO_S_URI + "#");
		}
	}

	private void addESIPImport() {
		if (ontology != null) {
			ontology.addImport(ESIPData.getOntology());
			model.addLoadedImport(ESIPData.ONTOLOGY_ESIP_URI);
			model.setNsPrefix("esip", ESIPData.ONTOLOGY_ESIP_NS + "#");
		}
	}

	private void addPMLPImport() {
		if (ontology != null) {
			ontology.addImport(PMLP.getOntology());
			model.addLoadedImport(PMLP.ONTOLOGY_PMLP_URI);
			model.setNsPrefix("pmlp", PMLP.ONTOLOGY_PMLP_URI + "#");
		}
	}

	private void addOWLSServiceImport() {
		if (ontology != null) {
			ontology.addImport(OWLS_Service.getOntology());
			model.addLoadedImport(OWLS_Service.ONTOLOGY_OWLS_SERVICE_URI);
			model.setNsPrefix("owlsService",
					OWLS_Service.ONTOLOGY_OWLS_SERVICE_URI + "#");
		}
	}

	private void addOWLSProcessImport() {
		if (ontology != null) {
			ontology.addImport(OWLS_Process.getOntology());
			model.addLoadedImport(OWLS_Process.ONTOLOGY_OWLS_PROCESS_URI);
			model.setNsPrefix("owlsProcess",
					OWLS_Process.ONTOLOGY_OWLS_PROCESS_URI + "#");
		}
	}
}
