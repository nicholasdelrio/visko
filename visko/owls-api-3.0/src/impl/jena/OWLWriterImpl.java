// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 12, 2004
 */
package impl.jena;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLWriter;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFSyntax;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLWriterImpl implements OWLWriter
{
	private static final Charset defaultCharset = Charset.forName("UTF8");
	private final List<Object> prettyTypes;
	private final Map<String, String> prefixMap;
	private boolean showXmlDeclaration;

	public OWLWriterImpl()
	{
		prettyTypes = new ArrayList<Object>();
		prefixMap = new HashMap<String, String>();
		showXmlDeclaration = true;
	}

	/* @see org.mindswap.owl.OWLWriter#isShowXmlDeclaration() */
	public boolean isShowXmlDeclaration() {
		return showXmlDeclaration;
	}

	/* @see org.mindswap.owl.OWLWriter#setShowXmlDeclaration(boolean) */
	public void setShowXmlDeclaration( final boolean showXmlDeclaration ) {
		this.showXmlDeclaration = showXmlDeclaration;
	}

	private void writeInternal(final OWLModel owlModel, final Writer out, final URI baseURI) {
		OntModel model = null;
		String baseURIasString = baseURI != null? baseURI.toString() : null;

		if (owlModel instanceof OWLKnowledgeBaseImpl)
		{
			// let's create the output model as the union of all not imported ontologies that make up the KB
			model = ModelFactory.createOntologyModel(JenaOWLProvider.createOntSpec());

			final OWLKnowledgeBaseImpl kb = (OWLKnowledgeBaseImpl) owlModel;

			// create owl:Ontology node and add (i) imports of sub models (ontologies), (ii) sub models themselves
			final Ontology aOntNode = model.createOntology(baseURIasString);

			Model baseModel;
			for (final OWLOntology ontology : kb.getOntologies(false))
			{
				baseModel = ((OWLOntologyImpl) ontology).getImplementation().getBaseModel();
				handle(model, aOntNode, baseModel);
			}
		}
		else
		{
			// otherwise it is a model/ontology --> let's just get its implementation model and write that.
			model = ((OWLOntologyImpl) owlModel).getImplementation();
		}

		final RDFWriter writer = model.getWriter("RDF/XML-ABBREV");

		// we don't want literals to be used as attributes
		writer.setProperty("blockRules", new Resource[] {RDFSyntax.propertyAttr, RDFSyntax.sectionListExpand});
		// show XML header <?xml version="..." ...?>
		writer.setProperty("showXmlDeclaration", Boolean.valueOf(showXmlDeclaration));
//		// include !ENTITY declaration for each prefix mapping --> reduces size of documents having many URIs
//		writer.setProperty("showDoctypeDeclaration", Boolean.TRUE);
		writer.setProperty("allowBadURIs", Boolean.TRUE);
		writer.setProperty("prettyTypes", prettyTypes.toArray(new Resource[prettyTypes.size()]));

		for (final Entry<String, String> entry : prefixMap.entrySet())
		{
			model.setNsPrefix(entry.getKey(), entry.getValue());
		}

		if (baseURIasString != null)
		{
			writer.setProperty("xmlbase", baseURIasString);
			model.setNsPrefix("", baseURIasString);
		}

		writer.write(model.getBaseModel(), out, baseURIasString);
	}

	private void handle(final OntModel outputModel, final Ontology outputOnt, final Model ontModel)
	{
		// First, entirely add the ontology model to the output model.
		outputModel.add(ontModel);

		// Second, write import statements (if any) that exist in ontModel to outputModel.
		// Note that all imports will be collected under the outputOnt, hence, we need to remove
		// the original statement; otherwise, we would write them "twice".
		StmtIterator it = ontModel.listStatements(null, OWL.imports, (RDFNode) null);
		while (it.hasNext())
		{
			Statement stmt = it.nextStatement();

			// add import to dedicated outputOnt
			RDFNode node = stmt.getObject();
			if (node.isResource()) outputOnt.addImport(node.as(Resource.class));

			// remove all statements involving the subject (owl:Ontology) from the output model
			outputModel.removeAll(stmt.getSubject(), null, null);
		}
	}

	/* @see org.mindswap.owl.OWLWriter#setNsPrefix(java.lang.String, java.net.URI) */
	public void setNsPrefix(final String prefix, final String uri) {
		prefixMap.put(prefix, uri);
	}

	/* @see org.mindswap.owl.OWLWriter#addPrettyType(org.mindswap.owl.OWLClass) */
	public void addPrettyType(final OWLClass c) {
		prettyTypes.add(c.getImplementation());
	}

	/* @see org.mindswap.owl.OWLWriter#write(org.mindswap.owl.OWLModel, java.io.Writer, java.net.URI) */
	public void write(final OWLModel ont, final Writer writer, final URI baseURI) {
		writeInternal(ont, writer, baseURI);
	}

	/* @see org.mindswap.owl.OWLWriter#write(org.mindswap.owl.OWLModel, java.io.OutputStream, java.net.URI) */
	public void write(final OWLModel ont, final OutputStream out, final URI baseURI) {
		writeInternal(ont, new OutputStreamWriter(out, defaultCharset), baseURI);
	}
}
