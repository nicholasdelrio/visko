/*
 * package edu.utep.trustlab.visko.web.html.provenance;
 * 
 * import java.util.List; import java.util.Vector;
 * 
 * import org.inference_web.pml.v2.pmlj.IWInferenceStep; import
 * org.inference_web.pml.v2.pmlj.IWNodeSet; import
 * org.inference_web.pml.v2.util.PMLObjectManager; import
 * org.mindswap.pellet.jena.PelletReasonerFactory;
 * 
 * import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO; import
 * edu.utep.trustlab.visko.sparql.ViskoTripleStore; import
 * edu.utep.trustlab.visko.util.ResultSetToVector;
 * 
 * 
 * import com.hp.hpl.jena.ontology.OntClass; import
 * com.hp.hpl.jena.ontology.OntModelSpec; import com.hp.hpl.jena.query.Query;
 * import com.hp.hpl.jena.query.QueryExecution; import
 * com.hp.hpl.jena.query.QueryExecutionFactory; import
 * com.hp.hpl.jena.query.QueryFactory; import
 * com.hp.hpl.jena.query.QuerySolution; import com.hp.hpl.jena.query.ResultSet;
 * import com.hp.hpl.jena.rdf.model.InfModel; import
 * com.hp.hpl.jena.rdf.model.Literal; import com.hp.hpl.jena.rdf.model.Model;
 * import com.hp.hpl.jena.rdf.model.ModelFactory; import
 * com.hp.hpl.jena.tdb.TDBFactory;
 * 
 * public class DataProvenanceHTML { private static String QUERY_PREFIX =
 * "PREFIX viskoV: <http://trust.utep.edu/visko/ontology/visko-view-v3.owl#> " +
 * "PREFIX viskoO: <http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#> "
 * +
 * "PREFIX viskoS: <http://trust.utep.edu/visko/ontology/visko-service-v3.owl#> "
 * + "PREFIX owlsService: <http://www.daml.org/services/owl-s/1.2/Service.owl#>"
 * + "PREFIX owlsProcess: <http://www.daml.org/services/owl-s/1.2/Process.owl#>"
 * + "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
 * "PREFIX	pmlj: <http://inference-web.org/2.0/pml-justification.owl#> " +
 * "PREFIX	pmlp: <http://inference-web.org/2.0/pml-provenance.owl#> " +
 * "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
 * "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
 * "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
 * 
 * private static final String SEARCHMapps =
 * "http://rio.cs.utep.edu/ciserver/ciprojects/wdo/AerostatWDO#m1"; private
 * IWNodeSet searchMAPPSNodeset;
 * 
 * public static void main(String[] args) { DataProvenanceHTML view = new
 * DataProvenanceHTML(
 * "http://rio.cs.utep.edu/ciserver/ciprojects/pmlj/HTTP_Subsetter_03730391059904816.owl#answer"
 * ); System.out.println(view.getGiovanniUserSelectionTable()); }
 * 
 * public DataProvenanceHTML(String nodesetURI) { IWNodeSet ns =
 * PMLObjectManager.getNodeSet(nodesetURI, 100000); searchMAPPSNodeset =
 * findSearchMappsNodeset(ns); }
 * 
 * private IWNodeSet findSearchMappsNodeset(IWNodeSet ns) { IWInferenceStep is =
 * (IWInferenceStep)ns.getIsConsequentOf().get(0); if(is == null) return null;
 * else if(is.getHasInferenceRule() == null) return null; else if
 * (is.getHasInferenceRule().getClassURI() == null) return null; else
 * if(is.getHasInferenceRule
 * ().getClassURI().equals(DataProvenanceHTML.SEARCHMapps)) return ns; else
 * if(is.getHasAntecedentList().size() == 0) return null; else { List
 * antecedents = is.getHasAntecedentList(); for(Object aNS : antecedents) {
 * IWNodeSet returnNS = findSearchMappsNodeset((IWNodeSet)aNS); if(returnNS !=
 * null) return returnNS; } return null; } }
 * 
 * public String getGiovanniUserSelectionTable() { if(searchMAPPSNodeset ==
 * null) return "<p>No Provenance Associated With Dataset</p>";
 * 
 * IWInferenceStep is =
 * (IWInferenceStep)searchMAPPSNodeset.getIsConsequentOf().get(0); List
 * antecedents = is.getHasAntecedentList();
 * 
 * String html =
 * "<table border=1><tr><td><b>User Selection</b></td><td><b>Value</b></td><td><b>Comment</b></td></tr>"
 * ; IWNodeSet ns; String classURI; OntClass informationClass; String label;
 * String value; String comment; for(Object antecedent : antecedents) { ns =
 * (IWNodeSet) antecedent; informationClass =
 * ns.getHasConclusion().getOntologyClass(); classURI =
 * informationClass.getURI(); label = informationClass.getLabel(null); value =
 * ns.getHasConclusion().getHasRawString(); comment =
 * informationClass.getComment(null);
 * 
 * html += "<tr>"; html += "<td><a href=\"" + classURI + "\">" + label +
 * "</a></td>"; html += "<td>" + value + "</td>"; html += "<td>" + comment +
 * "</td>"; html += "</tr>"; }
 * 
 * html += "</table>"; return html;
 * 
 * } }
 */