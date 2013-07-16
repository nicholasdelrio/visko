// The MIT License
//
// Copyright 2004 Evren Sirin
// Copyright 2009 Thorsten Möller - University of Basel Switzerland
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
 * Created on Dec 16, 2004
 */
package impl.jena;

import impl.owls.process.AtomicProcessImpl;
import impl.owls.process.CompositeProcessImpl;
import impl.owls.process.ResultImpl;
import impl.owls.process.binding.InputBindingImpl;
import impl.owls.process.binding.LinkBindingImpl;
import impl.owls.process.binding.LocBindingImpl;
import impl.owls.process.binding.OutputBindingImpl;
import impl.owls.process.binding.ValueConstantImpl;
import impl.owls.process.binding.ValueOfImpl;
import impl.owls.process.constructs.AnyOrderImpl;
import impl.owls.process.constructs.ChoiceImpl;
import impl.owls.process.constructs.ForEachImpl;
import impl.owls.process.constructs.IfThenElseImpl;
import impl.owls.process.constructs.PerformImpl;
import impl.owls.process.constructs.ProduceImpl;
import impl.owls.process.constructs.RepeatUntilImpl;
import impl.owls.process.constructs.RepeatWhileImpl;
import impl.owls.process.constructs.SequenceImpl;
import impl.owls.process.constructs.SplitImpl;
import impl.owls.process.constructs.SplitJoinImpl;
import impl.owls.process.variable.ExistentialImpl;
import impl.owls.process.variable.InputImpl;
import impl.owls.process.variable.LinkImpl;
import impl.owls.process.variable.LocImpl;
import impl.owls.process.variable.OutputImpl;
import impl.owls.process.variable.ParticipantImpl;
import impl.owls.profile.ProfileImpl;
import impl.owls.profile.ServiceCategoryImpl;
import impl.owls.profile.ServiceParameterImpl;
import impl.owls.service.ServiceImpl;

import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.mindswap.common.Variable;
import org.mindswap.exceptions.ParseException;
import org.mindswap.exceptions.UnboundVariableException;
import org.mindswap.owl.OWLAnnotationProperty;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.OWLWriter;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.JavaGrounding;
import org.mindswap.owls.grounding.JavaGroundingFactory;
import org.mindswap.owls.grounding.JavaParameter;
import org.mindswap.owls.grounding.JavaVariable;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.grounding.UPnPGrounding;
import org.mindswap.owls.grounding.UPnPGroundingFactory;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLGrounding;
import org.mindswap.owls.grounding.WSDLGroundingFactory;
import org.mindswap.owls.grounding.WSDLOperationRef;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Existential;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.InputBinding;
import org.mindswap.owls.process.variable.Link;
import org.mindswap.owls.process.variable.LinkBinding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.LocBinding;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.OutputBinding;
import org.mindswap.owls.process.variable.Participant;
import org.mindswap.owls.process.variable.ValueConstant;
import org.mindswap.owls.process.variable.ValueFunction;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceCategory;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.BuiltinNamespaces;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.pellet.datatypes.Datatype;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.Query;
import org.mindswap.query.QueryLanguage;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.utils.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aterm.ATermAppl;
import ch.unibas.on.sem.datatypes.TypeMapper;

import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.enhanced.UnsupportedPolymorphismException;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.ConversionException;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.util.iterator.Map1;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

/**
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public abstract class OWLModelImpl implements OWLModel
{
	private static final Logger logger = LoggerFactory.getLogger(OWLModelImpl.class);

	private static final BuiltinNamespaces owlsNamespaces = new BuiltinNamespaces();

	private static final Set<String> SKIP_NAMESPACES = new HashSet<String>(Arrays.asList(
		new String[] { OWL.NS, RDF.getURI(), RDFS.getURI(), ReasonerVocabulary.directSubClassOf.getNameSpace()
	}));

	protected int bulkUpdate;
	protected OntModel ontModel;
	protected OWLWriter writer; // lazy initialization

	public OWLModelImpl(final OntModel model)
	{
		bulkUpdate = 0;
		ontModel = model;
	}

	/* @see org.mindswap.owl.OWLModel#getImplementation() */
	public OntModel getImplementation() {
		return ontModel;
	}

	/* @see org.mindswap.owl.OWLModel#prepare() */
	public void prepare()
	{
		if (isBulkUpdate()) return;
		ontModel.prepare();
	}

	/* @see org.mindswap.owl.OWLModel#classify() */
	public void classify()
	{
		if (isBulkUpdate()) return;
		if (isPelletReasonerAttached()) ((PelletInfGraph) ontModel.getGraph()).classify();
	}

	/* @see org.mindswap.owl.OWLModel#isClassified() */
	public boolean isClassified()
	{
		if (isPelletReasonerAttached()) return ((PelletInfGraph) ontModel.getGraph()).isClassified();
		return true;
	}

	/* @see org.mindswap.owl.OWLModel#isConsistent() */
	public boolean isConsistent()
	{
		if (isPelletReasonerAttached()) return ((PelletInfGraph) ontModel.getGraph()).isConsistent();
		return true;

//		ValidityReport validation = ontModel.validate();
//// if there is no validation report assume everything is fine
//return ( validation == null ) || validation.isValid();
	}

	protected void setReasoner(final Reasoner newReasoner)
	{
		final Reasoner oldReasoner = ontModel.getReasoner();
		if (oldReasoner != newReasoner)
		{
			final OntModelSpec spec = ontModel.getSpecification();
			spec.setReasoner(newReasoner);
			if (newReasoner == null) spec.setReasonerFactory(null);

			final List<Graph> subGraphs = ontModel.getSubGraphs();
			ontModel = ModelFactory.createOntologyModel(spec, ontModel.getBaseModel());

			for (final Graph graph : subGraphs)
			{
				final Model wrap = ModelFactory.createModelForGraph(graph);
				ontModel.addSubModel(wrap, false);
			}

			ontModel.rebind();

			reasonerSet(oldReasoner);
		}
	}

	/* @see org.mindswap.owl.OWLModel#setReasoner(java.lang.String) */
	public void setReasoner(final String reasonerName) {
		final Reasoner r = (Reasoner) OWLFactory.createReasoner(reasonerName);
		if (reasonerName != null && r == null)
			throw new IllegalArgumentException("Reasoner named " + reasonerName + " not found!");

		setReasoner(r);
	}

	/* @see org.mindswap.owl.OWLModel#setReasoner(java.lang.Object) */
	public void setReasoner(final Object reasoner) {
		if (reasoner instanceof Reasoner || reasoner == null)
			setReasoner((Reasoner) reasoner);
		else
			throw new IllegalArgumentException(
				"Jena implementation only supports reasoners that implement " + Reasoner.class);
	}

	/* @see org.mindswap.owl.OWLModel#getReasoner() */
	public Object getReasoner() {
		return ontModel.getReasoner();
	}

	/* @see org.mindswap.owl.OWLModel#createList(org.mindswap.owl.list.ListVocabulary) */
	public <V extends OWLValue> OWLList<V> createList(final ListVocabulary<V> vocabulary)
	{
		final OntResource nil = ontModel.createOntResource(vocabulary.nil().getURI().toString());

		return wrapList(nil, vocabulary);
	}

	/* @see org.mindswap.owl.OWLModel#createList(org.mindswap.owl.list.ListVocabulary, org.mindswap.owl.OWLIndividual) */
	public <V extends OWLValue> OWLList<V> createList(final ListVocabulary<V> vocabulary, final V item)
	{
		final OWLList<V> list = createList(vocabulary);
		return (item != null)? list.cons(item) : list;
	}

	/* @see org.mindswap.owl.OWLModel#createList(org.mindswap.owl.list.ListVocabulary, org.mindswap.owl.OWLIndividualList) */
	public <V extends OWLValue> OWLList<V> createList(final ListVocabulary<V> vocabulary, final List<V> items)
	{
		OWLList<V> list = createList(vocabulary);
		for (final ListIterator<V> it = items.listIterator(items.size() - 1); it.hasPrevious(); )
		{
			list = list.cons(it.previous()); // cons is faster than with <-- that's why we iterate end to start
		}
		return list;
	}

	/* @see org.mindswap.owl.OWLModel#getType(java.net.URI) */
	public OWLType getType(final URI uri)
	{
		final OWLDataType type = getDataType(uri);
		return (type == null)? getClass(uri) : type;
	}

	/* @see org.mindswap.owl.OWLModel#getDataType(java.lang.Class) */
	public OWLDataType getDataType(final Class<?> clazz)
	{
		final RDFDatatype dt = TypeMapper.getInstance().getTypeByClass(clazz);
		return (dt != null)? createDataType(URI.create(dt.getURI())) : null;
	}

	/* @see org.mindswap.owl.OWLModel#getDataType(java.net.URI) */
	public OWLDataType getDataType(final URI uri) {
		return XSDDataTypes.getDataType(uri);
	}

	/* @see org.mindswap.owl.OWLModel#getEntity(java.net.URI) */
	public OWLEntity getEntity(final URI uri)
	{
		OWLEntity entity = getClass(uri);
		if (entity == null) entity = getIndividual(uri);
		if (entity == null) entity = getObjectProperty(uri);
		if (entity == null) entity = getDataProperty(uri);
		if (entity == null) entity = getAnnotationProperty(uri);
		return entity;
	}

	/* @see org.mindswap.owl.OWLModel#getClass(java.net.URI) */
	public OWLClass getClass(final URI uri)
	{
		final OntClass c = ontModel.getOntClass(uri.toString());
		return (c != null)? wrapClass(c) : null;
	}

	/* @see org.mindswap.owl.OWLModel#getClasses() */
	public Set<OWLClass> getClasses(final boolean named)
	{
		return getAllClasses(named? ontModel.listNamedClasses() : ontModel.listClasses());
	}

	/* @see org.mindswap.owl.OWLModel#getIndividual(java.net.URI) */
	public OWLIndividual getIndividual(final URI uri)
	{
		final OntResource r = ontModel.getOntResource(uri.toString());
		return (r != null)? wrapIndividual(r) : null;
	}

	/* @see org.mindswap.owl.OWLModel#getIndividuals(boolean) */
	public OWLIndividualList<?> getIndividuals(final boolean baseModelOnly)
	{
		return getAllIndividuals((baseModelOnly)?	ModelFactory.createOntologyModel(
			ontModel.getSpecification(), ontModel.getBaseModel()).listSubjects().mapWith(new InModelMapper()) :
				ontModel.listSubjects());
//		return getAllIndividuals(ontModel.listIndividuals(), null );
	}

	/* @see org.mindswap.owl.OWLModel#getProperty(java.net.URI) */
	public OWLProperty getProperty(final URI uri)
	{
		OWLProperty prop = getObjectProperty(uri);
		if (prop == null) prop = getDataProperty(uri);
		if (prop == null) prop = getAnnotationProperty(uri);
		return prop;
	}

	/* @see org.mindswap.owl.OWLModel#getAnnotationProperty(java.net.URI) */
	public OWLAnnotationProperty getAnnotationProperty(final URI uri)
	{
		final AnnotationProperty prop = ontModel.createAnnotationProperty(uri.toString());
		return (prop != null)? wrapAnnotationProperty(prop) : null;
	}

	/* @see org.mindswap.owl.OWLModel#getObjectProperty(java.net.URI) */
	public OWLObjectProperty getObjectProperty(final URI uri)
	{
		final ObjectProperty prop = ontModel.getObjectProperty(uri.toString());
		return (prop != null)? wrapObjectProperty(prop) : null;
	}

	/* @see org.mindswap.owl.OWLModel#getDataProperty(java.net.URI) */
	public OWLDataProperty getDataProperty(final URI uri)
	{
		final DatatypeProperty prop = ontModel.getDatatypeProperty(uri.toString());
		return (prop != null)? wrapDataProperty(prop) : null;
	}

	/* @see org.mindswap.owl.OWLModel#isSameAs(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLIndividual) */
	public boolean isSameAs(final OWLIndividual ind1, final OWLIndividual ind2) {
		return ontModel.contains((Resource) ind1.getImplementation(), OWL.sameAs,
			(Resource) ind2.getImplementation());
	}

	/* @see org.mindswap.owl.OWLModel#getSameIndividuals(org.mindswap.owl.OWLIndividual) */
	public OWLIndividualList<?> getSameIndividuals(final OWLIndividual ind) {
		return getAllIndividuals(
			ontModel.listResourcesWithProperty(OWL.sameAs, (Resource) ind.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#isDifferentFrom(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLIndividual) */
	public boolean isDifferentFrom(final OWLIndividual ind1, final OWLIndividual ind2) {
		return ontModel.contains((Resource) ind1.getImplementation(), OWL.differentFrom,
			(Resource) ind2.getImplementation());
	}

	public OWLIndividualList<?> getDifferentIndividuals(final OWLIndividual ind) {
		return getAllIndividuals(
			ontModel.listResourcesWithProperty(OWL.differentFrom,	(Resource) ind.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#isComplement(org.mindswap.owl.OWLClass, org.mindswap.owl.OWLClass) */
	public boolean isComplement(final OWLClass c1, final OWLClass c2) {
		return ontModel.contains(
			(Resource) c1.getImplementation(), OWL.complementOf, (Resource) c2.getImplementation());
	}

	/* @see org.mindswap.owl.OWLModel#isEquivalent(org.mindswap.owl.OWLType, org.mindswap.owl.OWLType) */
	public boolean isEquivalent(final OWLType t1, final OWLType t2)
	{
		if (t1.equals(t2)) return true; // checks if URIs are the same
		if (t1.isClass() && t2.isClass())
		{
			return ontModel.contains(
				(Resource) t1.getImplementation(), OWL.equivalentClass, (Resource) t2.getImplementation());
		}
		if (t1.isDataType() && t2.isDataType())
		{
			if (isPelletReasonerAttached())
			{
				final PelletInfGraph g = (PelletInfGraph) ontModel.getGraph();
				final Datatype d1 = g.getKB().getDatatypeReasoner().getDatatype(t1.getURI().toString());
				final Datatype d2 = g.getKB().getDatatypeReasoner().getDatatype(t2.getURI().toString());
				return g.getKB().isSubTypeOf(d1.getName(), d2.getName()) &&
					g.getKB().isSubTypeOf(d2.getName(), d1.getName());
			}
			logger.warn("Cannot determine if {} is equivalent to {} because no reasoner with datatype " +
				"reasoning support attached. Checking if datatype URIs are the same instead.", t1, t2);

			return t1.equals(t2);
		}

		return false;
	}

	/* @see org.mindswap.owl.OWLModel#isDisjoint(org.mindswap.owl.OWLType, org.mindswap.owl.OWLType) */
	public boolean isDisjoint(final OWLType t1, final OWLType t2)
	{
		if (t1.isClass() && t2.isClass())
		{
			return ontModel.contains(
				(Resource) t1.getImplementation(), OWL.disjointWith, (Resource) t2.getImplementation());
		}
		if (t1.isDataType() && t2.isDataType())
		{
			if (isPelletReasonerAttached())
			{
				final PelletInfGraph g = (PelletInfGraph) ontModel.getGraph();
				final Datatype d1 = g.getKB().getDatatypeReasoner().getDatatype(t1.getURI().toString());
				final Datatype d2 = g.getKB().getDatatypeReasoner().getDatatype(t2.getURI().toString());
				return g.getKB().getDatatypeReasoner().intersection(
					new ATermAppl[] {d1.getName(), d2.getName()}).isEmpty();
			}
			logger.warn("Cannot determine if {} is disjoint with {} because no reasoner with datatype " +
				"reasoning support attached. Checking if datatype URIs are different instead.", t1, t2);

			return !t1.equals(t2);
		}

		return true;
	}

	/* @see org.mindswap.owl.OWLModel#getSubClasses(org.mindswap.owl.OWLClass, boolean) */
	public Set<OWLClass> getSubClasses(final OWLClass c, final boolean direct)
	{
		final Property subClassOf = (direct)? ReasonerVocabulary.directSubClassOf : RDFS.subClassOf;

		return getAllClasses(ontModel.listResourcesWithProperty(
			subClassOf,	(Resource) c.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#getSubProperties(org.mindswap.owl.OWLProperty) */
	public Set<OWLProperty> getSubProperties(final OWLProperty p)
	{
		if (p.isObjectProperty())
		{
			return getAllObjectProperties(
				ontModel.listResourcesWithProperty(RDFS.subPropertyOf, (Resource) p.getImplementation()));
		}
		return getAllDataProperties(
			ontModel.listResourcesWithProperty(RDFS.subPropertyOf, (Resource) p.getImplementation()));
	}

	public Set<OWLProperty> getEquivalentProperties(final OWLProperty p)
	{
		if (p.isObjectProperty())
		{
			return getAllObjectProperties(
				ontModel.listResourcesWithProperty(OWL.equivalentProperty, (Resource) p.getImplementation()));
		}
		return getAllDataProperties(
			ontModel.listResourcesWithProperty(OWL.equivalentProperty, (Resource) p.getImplementation()));
	}

	public Set<OWLProperty> getSuperProperties(final OWLProperty p)
	{
		if (p.isObjectProperty())
		{
			return getAllObjectProperties(
				ontModel.listObjectsOfProperty((Resource) p.getImplementation(), RDFS.subPropertyOf));
		}
		return getAllDataProperties(
			ontModel.listObjectsOfProperty((Resource) p.getImplementation(), RDFS.subPropertyOf));
	}

	/* @see org.mindswap.owl.OWLModel#getSuperClasses(org.mindswap.owl.OWLClass, boolean) */
	public Set<OWLClass> getSuperClasses(final OWLClass c, final boolean direct)
	{
		final Property subClassOf = (direct)? ReasonerVocabulary.directSubClassOf : RDFS.subClassOf;

		return getAllClasses(
			ontModel.listObjectsOfProperty((Resource) c.getImplementation(), subClassOf));
	}

	/* @see org.mindswap.owl.OWLModel#getEquivalentClasses(org.mindswap.owl.OWLClass) */
	public Set<OWLClass> getEquivalentClasses(final OWLClass c)
	{
		return getAllClasses(
			ontModel.listResourcesWithProperty(OWL.equivalentClass, (Resource) c.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#getInstances(org.mindswap.owl.OWLClass, boolean) */
	public OWLIndividualList<?> getInstances(final OWLClass c, final boolean baseModelOnly)
	{
		return getInstances(c, baseModelOnly, null);
	}

	/* @see org.mindswap.owl.OWLModel#getTypes(org.mindswap.owl.OWLIndividual) */
	public Set<OWLClass> getTypes(final OWLIndividual ind)
	{
		return getAllClasses(
			ontModel.listObjectsOfProperty((Resource) ind.getImplementation(), RDF.type));
	}

	/* @see org.mindswap.owl.OWLModel#getProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividual getProperty(final OWLIndividual ind, final OWLObjectProperty prop)
	{
		final Statement s = ontModel.getProperty((Resource) ind.getImplementation(), (Property) prop.getImplementation());
		final RDFNode n = (s != null)? s.getObject() : null;
		return (n != null)? wrapIndividual(n.as(Resource.class)) : null;

//		return getFirstIndividual(ontModel.listObjectsOfProperty((Resource) ind.getImplementation(),
//			(Property) prop.getImplementation()), ind.getOntology());
	}

	/* @see org.mindswap.owl.OWLModel#getProperties(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividualList<?> getProperties(final OWLIndividual ind, final OWLObjectProperty prop)
	{
		return getAllIndividuals(ontModel.listObjectsOfProperty(
			(Resource) ind.getImplementation(), (Property) prop.getImplementation()));
	}

	private Map<OWLProperty, List<OWLValue>> getProperties(final Resource resource)
	{
		final Map<OWLProperty, List<OWLValue>> result = new HashMap<OWLProperty, List<OWLValue>>();

		final StmtIterator it = ontModel.listStatements(resource, null, (RDFNode) null);
		while (it.hasNext())
		{
			final Statement stmt = it.next();
			final Property stmtPred = stmt.getPredicate();

			// not ultimately required but may speed things up
			if (SKIP_NAMESPACES.contains(stmtPred.getNameSpace())) continue;

			final RDFNode stmtObject = stmt.getObject();
			if (stmtObject.isResource()) // thus, stmtPred is a RDF property or a OWL object property
			{
				final OWLObjectProperty prop;
				try {	prop = wrapObjectProperty(stmtPred.as(ObjectProperty.class)); }
				catch (final UnsupportedPolymorphismException e) { continue; } // happens if stmtPred is a RDF property

				List<OWLValue> list = result.get(prop);
				if (list == null) {
					list = new ArrayList<OWLValue>();
					result.put(prop, list);
				}
				list.add(wrapIndividual(stmtObject.as(Resource.class)));
			}
			else // stmtObject is a literal, thus, stmtPred is a RDF property or a OWL datatype property
			{
				final OWLDataProperty prop;
				try { prop = wrapDataProperty(stmtPred.as(DatatypeProperty.class)); }
				catch (final UnsupportedPolymorphismException e) { continue; } // happens if stmtPred is a RDF property

				List<OWLValue> list = result.get(prop);
				if (list == null) {
					list = new ArrayList<OWLValue>();
					result.put(prop, list);
				}
				list.add(wrapDataValue(stmtObject.as(Literal.class)));
			}
		}

		return result;
	}

	/* @see org.mindswap.owl.OWLModel#getDeclaredProperties(org.mindswap.owl.OWLClass, boolean) */
	public List<OWLProperty> getDeclaredProperties(final OWLClass clazz, final boolean direct) {
		final List<OWLProperty> result = new ArrayList<OWLProperty>();

		// clazz may not be attached to this model, hence, we need to check first if it exists in this model
		final OntClass ontClaz = ontModel.getOntClass(clazz.getURI().toString());
		if (ontClaz == null) return result;

		final ExtendedIterator<OntProperty> it = ontClaz.listDeclaredProperties(direct);
		while (it.hasNext())
		{
			final OntProperty ontProp = it.next();

			if (ontProp.isDatatypeProperty()) result.add(wrapDataProperty(ontProp.asDatatypeProperty()));
			else if (ontProp.isObjectProperty()) result.add(wrapObjectProperty(ontProp.asObjectProperty()));
		}

		return result;
	}

	public Map<OWLProperty, List<OWLValue>> getProperties(final OWLClass clazz) {
		return getProperties((Resource) clazz.getImplementation());
	}

	public Map<OWLProperty, List<OWLValue>> getProperties(final OWLIndividual ind) {
		return getProperties((Resource) ind.getImplementation());
	}

	/* @see org.mindswap.owl.OWLModel#getProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty) */
	public OWLDataValue getProperty(final OWLIndividual ind, final OWLDataProperty prop) {
		OWLDataValue result = null;
		int maxPriority = -1;
		final List<OWLDataValue> list = getAllDataValues(ontModel.listObjectsOfProperty(
			(Resource) ind.getImplementation(), (Property) prop.getImplementation()));

		for (final OWLDataValue value : list)
		{
			final int priority = OWLConfig.getDefaultLanguages().indexOf(value.getLanguage());

			if (priority == 0) return value;
			else if (priority > maxPriority)
			{
				result = value;
				maxPriority = priority;
			}
		}

		return result;
	}

	/* @see org.mindswap.owl.OWLModel#getPropertyAsOWLType(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty) */
	public OWLType getPropertyAsOWLType(final OWLIndividual ind, final OWLDataProperty prop)
	{
		final NodeIterator it = ontModel.listObjectsOfProperty(
			(Resource) ind.getImplementation(), (Property) prop.getImplementation());
		while (it.hasNext())
		{
			final RDFNode node = it.next();
			if (node.isLiteral())
			{
				final String uri = ((Literal) node).getLexicalForm().trim();
				final OWLType result = getType(URIUtils.createURI(uri));
				return result;
			}
		}
		return null;
	}

	/* @see org.mindswap.owl.OWLModel#getProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public OWLDataValue getProperty(final OWLIndividual ind, final OWLDataProperty prop, final String lang)
	{
		final List<OWLDataValue> list = getAllDataValues(ontModel.listObjectsOfProperty(
			(Resource) ind.getImplementation(), (Property) prop.getImplementation()));

		for (final OWLDataValue value : list)
		{
			if (lang == null || value.getLanguage().equals(lang)) return value;
		}

		return null;
	}

	/* @see org.mindswap.owl.OWLModel#getProperties(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty) */
	public List<OWLDataValue> getProperties(final OWLIndividual ind, final OWLDataProperty prop)
	{
		return getAllDataValues(ontModel.listObjectsOfProperty(
			(Resource) ind.getImplementation(),	(Property) prop.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#getIncomingProperty(org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual) */
	public OWLIndividual getIncomingProperty(final OWLObjectProperty prop, final OWLIndividual ind)
	{
		return getFirstIndividual(ontModel.listResourcesWithProperty(
			(Property) prop.getImplementation(), (Resource) ind.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#getIncomingProperties(org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual) */
	public OWLIndividualList<?> getIncomingProperties(final OWLObjectProperty prop, final OWLIndividual ind)
	{
		return getAllIndividuals(ontModel.listResourcesWithProperty(
			(Property) prop.getImplementation(), (Resource) ind.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#getIncomingProperties(org.mindswap.owl.OWLIndividual) */
	public OWLIndividualList<?> getIncomingProperties(final OWLIndividual ind)
	{
		return getAllIndividuals(ontModel.listStatements(null, null,
			(Resource) ind.getImplementation()).mapWith(new Map1<Statement, Resource>() {
				public Resource map1(final Statement o)
				{
					return o.getSubject();
				}
			}));
	}

	/* @see org.mindswap.owl.OWLModel#getIncomingProperty(org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public OWLIndividual getIncomingProperty(final OWLDataProperty prop, final OWLDataValue value)
	{
		return getFirstIndividual(ontModel.listResourcesWithProperty(
			(Property) prop.getImplementation(),	(Literal) value.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#getIncomingProperties(org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public OWLIndividualList<?> getIncomingProperties(final OWLDataProperty prop, final OWLDataValue value)
	{
		return getAllIndividuals(ontModel.listResourcesWithProperty(
			(Property) prop.getImplementation(), (Literal) value.getImplementation()));
	}

	/* @see org.mindswap.owl.OWLModel#contains(org.mindswap.owl.OWLEntity, org.mindswap.owl.OWLProperty, org.mindswap.owl.OWLValue) */
	public boolean contains(final OWLEntity s, final OWLProperty p, final OWLValue o)
	{
		return ontModel.contains(
			(s == null)? null : (Resource) s.getImplementation(),
			(p == null)? null : (Property) p.getImplementation(),
			(o == null)? null : (RDFNode) o.getImplementation());
	}

	/* @see org.mindswap.owl.OWLModel#getType(org.mindswap.owl.OWLIndividual) */
	public OWLClass getType(final OWLIndividual ind) {
		final Statement stmt = ontModel.getProperty((Resource) ind.getImplementation(), RDF.type);
		final RDFNode classNode = (stmt == null)? null : stmt.getObject();
		if (classNode == null)
		{
			return null;
		}
		try
		{
			final OntClass c = classNode.as(OntClass.class);
			return wrapClass(c);
		}
		catch (final ConversionException e)
		{
			// we should not return owl:Thing because it could be also owl:Nothing
			return null;
		}
	}

	/* @see org.mindswap.owl.OWLModel#isType(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLClass) */
	public boolean isType(final OWLIndividual ind, final OWLClass c) {
		return ontModel.contains((Resource) ind.getImplementation(), RDF.type, (Resource) c.getImplementation());
	}

	/* @see org.mindswap.owl.OWLModel#isEnumerated(org.mindswap.owl.OWLClass) */
	public boolean isEnumerated(final OWLClass c) {
		return ontModel.contains((Resource) c.getImplementation(), OWL.oneOf, (Resource) null);
	}

	/* @see org.mindswap.owl.OWLModel#getEnumerations(org.mindswap.owl.OWLClass) */
	public OWLIndividualList<?> getEnumerations(final OWLClass c)
	{
		final Statement stmt = ontModel.getProperty((Resource) c.getImplementation(), OWL.oneOf);
		if (stmt == null) return null;

		final RDFList oneOf = stmt.getObject().as(RDFList.class);
		return getAllIndividuals(oneOf.iterator());
	}

	protected Set<OWLProperty> getAllObjectProperties(final ExtendedIterator<? extends RDFNode> it)
	{
		final Set<OWLProperty> set = new HashSet<OWLProperty>();
		try
		{
			while (it.hasNext())
			{
				// we can omit additional canAs test because we know that elements are object properties
				set.add(wrapObjectProperty(it.next().as(ObjectProperty.class)));
			}
			return set;
		}
		finally
		{
			it.close();
		}
	}

	protected Set<OWLProperty> getAllDataProperties(final ExtendedIterator<? extends RDFNode> it)
	{
		final Set<OWLProperty> set = new HashSet<OWLProperty>();
		try
		{
			while (it.hasNext())
			{
				// we can omit additional canAs test because we know that elements are datatype properties
				set.add(wrapDataProperty(it.next().as(DatatypeProperty.class)));
			}
			return set;
		}
		finally
		{
			it.close();
		}
	}

	protected Set<OWLClass> getAllClasses(final ExtendedIterator<? extends RDFNode> it)
	{
		final Set<OWLClass> set = new HashSet<OWLClass>();
		try
		{
			while (it.hasNext())
			{
				set.add(wrapClass(it.next().as(OntClass.class)));
			}
			return set;
		}
		finally
		{
			it.close();
		}
	}

	protected OWLIndividualList<?> getAllIndividuals(final ExtendedIterator<? extends RDFNode> it)
	{
		final OWLIndividualList<OWLIndividual> list = OWLFactory.createIndividualList();
		try
		{
			while(it.hasNext())
			{
				list.add(wrapIndividual(it.next().as(Resource.class)));
			}
			return list;
		}
		finally
		{
			it.close();
		}
	}

	protected OWLIndividual getFirstIndividual(final ExtendedIterator<? extends RDFNode> it)
	{
		try
		{
			while(it.hasNext())
			{
				return wrapIndividual(it.next().as(Resource.class));
			}
			return null;
		}
		finally
		{
			it.close();
		}
	}

	protected List<OWLDataValue> getAllDataValues(final ExtendedIterator<? extends RDFNode> it)
	{
		final List<OWLDataValue> list = new ArrayList<OWLDataValue>();
		try
		{
			while(it.hasNext())
			{
				list.add(wrapDataValue(it.next().as(Literal.class)));
			}
			return list;
		}
		finally
		{
			it.close();
		}
	}

	protected final OWLDataValue wrapDataValue(final Literal l)
	{ return new OWLDataValueImpl(l); }

	protected final OWLIndividual wrapIndividual(final Resource r)
	{ return new OWLIndividualImpl(getOntology(), r); }

	protected final OWLClass wrapClass(final OntClass c)
	{ return new OWLClassImpl(getOntology(), c); }

	protected final OWLAnnotationProperty wrapAnnotationProperty(final AnnotationProperty p)
	{ return new OWLAnnotationPropertyImpl(getOntology(), p); }

	protected final OWLObjectProperty wrapObjectProperty(final ObjectProperty p)
	{ return new OWLObjectPropertyImpl(getOntology(), p); }

	protected final OWLDataProperty wrapDataProperty(final DatatypeProperty p)
	{ return new OWLDataPropertyImpl(getOntology(), p); }

	protected final <V extends OWLValue> OWLList<V> wrapList(final OntResource r, final ListVocabulary<V> vocabulary)
	{ return new OWLListImpl<V>(getOntology(), r, vocabulary); }

	/* @see org.mindswap.owl.OWLModel#evaluateGround(org.mindswap.owl.list.OWLList) */
	public void evaluateGround(final OWLList<Atom> atoms)
	{
		evaluate(atoms, true);
	}

	/* @see org.mindswap.owl.OWLModel#evaluate(org.mindswap.owl.list.OWLList) */
	public void evaluate(final OWLList<Atom> atoms) throws UnboundVariableException
	{
		evaluate(atoms, false);
	}

	private void evaluate(final OWLList<Atom> atoms, final boolean skipUnground)
	{
		LOOP:
			for (final Atom atom : atoms)
			{
				for (int i = 0; i < atom.getArgumentCount(); i++)
				{
					final SWRLObject arg = atom.getArgument(i);
					if (arg.isVariable())
					{
						if (skipUnground) continue LOOP;
						throw new UnboundVariableException((Variable) arg);
					}
				}

				atom.evaluate(null);
			}
	}

	/* @see org.mindswap.owl.OWLModel#makeQuery(java.lang.String, org.mindswap.query.QueryLanguage) */
	public Query<Variable> makeQuery(final String queryStr, final QueryLanguage lang) throws ParseException
	{
		// Even if this query is executed just once it makes sense to use a prepared
		// query since creating it does not incur additional work.
		return StandardPreparedQuery.createQuery(queryStr, lang, this);
	}

	/* @see org.mindswap.owl.OWLModel#makeQuery(org.mindswap.query.ABoxQuery) */
	public <V extends Variable> Query<V> makeQuery(final ABoxQuery<V> query)
	{
		return makeQuery(query.getBody(), query.getResultVariables());
	}

	/* @see org.mindswap.owl.OWLModel#makeQuery(org.mindswap.owl.list.OWLList, java.util.List) */
	public <V extends Variable> Query<V> makeQuery(final OWLList<Atom> atoms, final List<V> resultVars)
	{
		// Even if this query is executed just once it makes sense to use a prepared
		// query since creating it does not incur additional work.
		return new StandardPreparedQuery<V>(atoms, resultVars, this);
	}

	/* @see org.mindswap.owl.OWLModel#isSubTypeOf(org.mindswap.owl.OWLType, org.mindswap.owl.OWLType) */
	public boolean isSubTypeOf(final OWLType t1, final OWLType t2)
	{
		if (t1.equals(t2)) return true; // checks if URIs are the same - every type is sub type of itself
		if (t1.isClass() && t2.isClass())
		{
			return ontModel.contains(
				(Resource) t1.getImplementation(), RDFS.subClassOf, (Resource) t2.getImplementation());
		}
		if (t1.isDataType() && t2.isDataType())
		{
			if (isPelletReasonerAttached())
			{
				final PelletInfGraph g = (PelletInfGraph) ontModel.getGraph();
				final Datatype d1 = g.getKB().getDatatypeReasoner().getDatatype(t1.getURI().toString());
				final Datatype d2 = g.getKB().getDatatypeReasoner().getDatatype(t2.getURI().toString());
				return g.getKB().isSubTypeOf(d1.getName(), d2.getName());
			}
			logger.warn("Cannot determine if {} is a sub type of {} because no reasoner with datatype" +
				" reasoning support attached. Checking if datatype URIs are the same instead.", t1, t2);
			return t1.equals(t2);
		}
		return false;
	}

	/* @see org.mindswap.owl.OWLModel#createClass(java.net.URI) */
	public OWLClass createClass(final URI classURI)
	{
		final OntClass c = ontModel.createClass(classURI != null? classURI.toString() : null);
//		refresh();
		return wrapClass(c);
	}

	/* @see org.mindswap.owl.OWLModel#createDataType(java.net.URI) */
	public OWLDataType createDataType(final URI dataTypeURI)
	{
		final OWLDataType t = getDataType(dataTypeURI); // reuse existing data type if it already exists
		return (t != null)? t :
			new OWLDataTypeImpl(getOntology().getKB(), new BaseDatatype(dataTypeURI.toString()));
	}

	/* @see org.mindswap.owl.OWLModel#createAnnotationProperty(java.net.URI) */
	public OWLAnnotationProperty createAnnotationProperty(final URI annotationPropertyURI)
	{
		final AnnotationProperty p = ontModel.createAnnotationProperty(annotationPropertyURI.toString());
//		refresh();
		return wrapAnnotationProperty(p);
	}

	/* @see org.mindswap.owl.OWLModel#createObjectProperty(java.net.URI) */
	public OWLObjectProperty createObjectProperty(final URI objectPropertyURI)
	{
		final ObjectProperty p = ontModel.createObjectProperty(objectPropertyURI.toString());
//		refresh();
		return wrapObjectProperty(p);
	}

	/* @see org.mindswap.owl.OWLModel#createDataProperty(java.net.URI) */
	public OWLDataProperty createDataProperty(final URI datatypePropertyURI)
	{
		final DatatypeProperty p = ontModel.createDatatypeProperty(datatypePropertyURI.toString());
//		refresh();
		return wrapDataProperty(p);
	}

	/* @see org.mindswap.owl.OWLModel#createIndividual(java.net.URI, java.net.URI) */
	public OWLIndividual createIndividual(final URI type, final URI indURI)
	{
		final OntResource r = ontModel.createOntResource((indURI == null)? null : indURI.toString());

		// Set rdf:type in any case, even if there is no knowledge in the model asserting
		// that type URI refers a OWL class. This is deliberate behavior.
		if (type != null) r.addRDFType(getOntology().getKB().ontModel.createResource(type.toString()));
//		refresh();
		return wrapIndividual(r);
	}

	/* @see org.mindswap.owl.OWLModel#createInstance(org.mindswap.owl.OWLClass, java.net.URI) */
	public OWLIndividual createInstance(final OWLClass clazz, final URI indURI)
	{
		final Resource c = (clazz == null)? OWL.Thing : ((OWLClassImpl) clazz).getImplementation();
		final Individual ind = ontModel.createIndividual((indURI == null)? null : indURI.toString(), c);
//		refresh();
		return wrapIndividual(ind);
	}

	/* @see org.mindswap.owl.OWLModel#createDataValue(java.lang.String) */
	public OWLDataValue createDataValue(final String value) {
		return new OWLDataValueImpl(ontModel.createLiteral(value));
	}

	/* @see org.mindswap.owl.OWLModel#createDataValue(java.lang.String, java.lang.String) */
	public OWLDataValue createDataValue(final String value, final String language) {
		return new OWLDataValueImpl(ontModel.createLiteral(value, language));
	}

	/* @see org.mindswap.owl.OWLModel#createDataValue(java.lang.Object, java.net.URI) */
	public OWLDataValue createDataValue(final Object value, final URI datatypeURI) {
		if (value instanceof OWLDataValue) return (OWLDataValue) value;

		// also works for RDF#XMLLiteral
		return new OWLDataValueImpl(ontModel.createTypedLiteral(value, datatypeURI.toString()));
	}

	/* @see org.mindswap.owl.OWLModel#createDataValue(java.lang.Object) */
	public OWLDataValue createDataValue(final Object value) {
		if (value instanceof OWLDataValue) return (OWLDataValue) value;

		// should work whenever value.getClass() maps to a RDFDatatype by com.hp.hpl.jena.datatypes.TypeMapper
		return new OWLDataValueImpl(ontModel.createTypedLiteral(value));
	}

	public AnyOrder createAnyOrder(final URI uri)
	{ return new AnyOrderImpl(createInstance(OWLS.Process.AnyOrder, uri)); }

	public AtomicProcess createAtomicProcess(final URI uri)
	{ return new AtomicProcessImpl(createInstance(OWLS.Process.AtomicProcess, uri)); }

	public Choice createChoice(final URI uri)
	{ return new ChoiceImpl(createInstance(OWLS.Process.Choice, uri)); }

	public CompositeProcess createCompositeProcess(final URI uri)
	{ return new CompositeProcessImpl(createInstance(OWLS.Process.CompositeProcess, uri)); }

	public Condition.SWRL createSWRLCondition(final URI uri)
	{ return new SWRLExpressionImpl(createInstance(OWLS.Expression.SWRL_Condition, uri)); }

	public Expression.SWRL createSWRLExpression(final URI uri)
	{ return new SWRLExpressionImpl(createInstance(OWLS.Expression.SWRL_Expression, uri)); }

	public Condition.SPARQL createSPARQLCondition(final URI uri)
	{ return new SPARQLExpressionImpl(createInstance(OWLS.Expression.SPARQL_Condition, uri)); }

	public Expression.SPARQL createSPARQLExpression(final URI uri)
	{ return new SPARQLExpressionImpl(createInstance(OWLS.Expression.SPARQL_Expression, uri)); }

	public ForEach createForEach(final URI uri)
	{ return new ForEachImpl(createInstance(OWLS.Process.ForEach, uri)); }

	public WSDLGrounding createWSDLGrounding(final URI uri)
	{ return WSDLGroundingFactory.createWSDLGrounding(uri, this); }

	public IfThenElse createIfThenElse(final URI uri)
	{ return new IfThenElseImpl(createInstance(OWLS.Process.IfThenElse, uri)); }

	public Input createInput(final URI uri) { return new InputImpl(createInstance(OWLS.Process.Input, uri)); }

	public InputBinding createInputBinding(final URI uri)
	{ return new InputBindingImpl(createInstance(OWLS.Process.InputBinding, uri));  }

	public Existential createExistential(final URI uri)
	{ return new ExistentialImpl(createInstance(OWLS.Process.Existential, uri)); }

	public Loc createLoc(final URI uri) { return new LocImpl(createInstance(OWLS.Process.Loc, uri)); }

	public LocBinding createLocBinding(final URI uri)
	{ return new LocBindingImpl(createInstance(OWLS.Process.LocBinding, uri)); }

	public Link createLink(final URI uri) { return new LinkImpl(createInstance(OWLS.Process.Link, uri)); }

	public LinkBinding createLinkBinding(final URI uri)
	{ return new LinkBindingImpl(createInstance(OWLS.Process.LinkBinding, uri)); }

	public Participant createParticipant(final URI uri)
	{ return new ParticipantImpl(createInstance(OWLS.Process.Participant, uri)); }

	public MessageMap<String> createWSDLInputMessageMap(final URI uri)
	{ return WSDLGroundingFactory.createWSDLInputMessageMap(uri, this);	}

	public MessageMap<String> createWSDLOutputMessageMap(final URI uri)
	{ return WSDLGroundingFactory.createWSDLOutputMessageMap(uri, this); }

	public MessageMap<String> createUPnPMessageMap(final URI uri)
	{ return UPnPGroundingFactory.createUPnPMessageMap(uri, this); }

	public Output createOutput(final URI uri)
	{ return new OutputImpl(createInstance(OWLS.Process.Output, uri)); }

	public OutputBinding createOutputBinding(final URI uri)
	{ return new OutputBindingImpl(createInstance(OWLS.Process.OutputBinding, uri));  }

	public Perform createPerform(final URI uri)
	{ return new PerformImpl(createInstance(OWLS.Process.Perform, uri)); }

	public Produce createProduce(final URI uri)
	{ return new ProduceImpl(createInstance(OWLS.Process.Produce, uri)); }

	public Profile createProfile(final URI uri)
	{ return new ProfileImpl(createInstance(OWLS.Profile.Profile, uri)); }

	public RepeatUntil createRepeatUntil(final URI uri)
	{ return new RepeatUntilImpl(createInstance(OWLS.Process.RepeatUntil, uri)); }

	public RepeatWhile createRepeatWhile(final URI uri)
	{ return new RepeatWhileImpl(createInstance(OWLS.Process.RepeatWhile, uri)); }

	public Result createResult(final URI uri)
	{ return new ResultImpl(createInstance(OWLS.Process.Result, uri));	}

	public Sequence createSequence(final URI uri)
	{ return new SequenceImpl(createInstance(OWLS.Process.Sequence, uri)); }

	public Service createService(final URI uri)
	{ return new ServiceImpl(createInstance(OWLS.Service.Service, uri)); }

	public ServiceCategory createServiceCategory(final URI uri)
	{ return new ServiceCategoryImpl(createInstance(OWLS.ServiceCategory.ServiceCategory, uri));	}

	public ServiceParameter createServiceParameter(final URI uri)
	{ return new ServiceParameterImpl(createInstance(OWLS.ServiceParameter.ServiceParameter, uri));	}

	public Split createSplit(final URI uri) { return new SplitImpl(createInstance(OWLS.Process.Split, uri)); }

	public SplitJoin createSplitJoin(final URI uri)
	{ return new SplitJoinImpl(createInstance(OWLS.Process.SplitJoin, uri)); }

	public UPnPGrounding createUPnPGrounding(final URI uri)
	{ return UPnPGroundingFactory.createUPnPGrounding(uri, this); }

	public UPnPAtomicGrounding createUPnPAtomicGrounding(final URI uri)
	{ return UPnPGroundingFactory.createUPnPAtomicGrounding(uri, this); }

	public JavaAtomicGrounding createJavaAtomicGrounding(final URI uri)
	{ return JavaGroundingFactory.createJavaAtomicGrounding(uri, this); }

	public JavaGrounding createJavaGrounding(final URI uri)
	{ return JavaGroundingFactory.createJavaGrounding(uri, this); }

	public JavaParameter createJavaParameter(final URI uri)
	{ return JavaGroundingFactory.createJavaParameter(uri, this); }

	public JavaVariable createJavaVariable(final URI uri)
	{ return JavaGroundingFactory.createJavaVariable(uri, this); }

	public ValueOf createValueOf(final URI uri)
	{ return new ValueOfImpl(createInstance(OWLS.Process.ValueOf, uri)); }

	public ValueConstant createValueConstant(final OWLValue dataValue, final Binding<?> enclosingBinding)
	{ return new ValueConstantImpl(dataValue, enclosingBinding); }

	public ValueFunction<Expression.SPARQL> createSPARQLValueFunction(final Expression.SPARQL expression, final Binding<?> enclosingBinding)
	{ return new SPARQLValueFunction(expression, enclosingBinding); }

	public WSDLAtomicGrounding createWSDLAtomicGrounding(final URI uri)
	{ return WSDLGroundingFactory.createWSDLAtomicGrounding(uri, this);	}

	public WSDLOperationRef createWSDLOperationRef(final URI uri)
	{ return WSDLGroundingFactory.createWSDLOperationRef(uri, this); }

	/* @see org.mindswap.owl.OWLModel#createControlConstructList(org.mindswap.owls.process.ControlConstruct) */
	public OWLList<ControlConstruct> createControlConstructList(final ControlConstruct cc)
	{
		final OWLList<ControlConstruct> list = createList(OWLS.Process.CCList);
		return (cc != null)? list.cons(cc) : list;
	}

	/* @see org.mindswap.owl.OWLModel#createControlConstructBag(org.mindswap.owls.process.ControlConstruct) */
	public OWLList<ControlConstruct> createControlConstructBag(final ControlConstruct cc)
	{
		final OWLList<ControlConstruct> bag = createList(OWLS.Process.CCBag);
		return (cc != null)? bag.cons(cc) : bag;
	}

	/* @see org.mindswap.owl.OWLModel#getServices(boolean) */
	public OWLIndividualList<Service> getServices(final boolean baseModelOnly)
	{
		final OWLIndividualList<OWLIndividual> services = OWLFactory.createIndividualList();

		// if a reasoner is attached, we assume that it does the job of inferring all
		// instances of owls:Service, i.e., including those of sub classes
		services.addAll(getInstances(OWLS.Service.Service, baseModelOnly));

		return OWLFactory.wrapList(services, Service.class);
	}

	/* @see org.mindswap.owl.OWLModel#getService(java.net.URI) */
	public Service getService(final URI serviceURI)
	{
		if (serviceURI == null) // get the first one that exists
		{
			final List<Service> list = getServices(false);
			return list.isEmpty()? null : list.get(0);
		}

		final OWLIndividual ind = getIndividual(serviceURI);
		return (ind == null)? null : ind.castTo(Service.class);
	}

	/* @see org.mindswap.owl.OWLModel#getProfiles(boolean) */
	public OWLIndividualList<Profile> getProfiles(final boolean baseModelOnly)
	{
		final OWLIndividualList<OWLIndividual> profiles = OWLFactory.createIndividualList();

		// if a reasoner is attached, we assume that it does the job of inferring all
		// instances of owls:Profile, i.e., including those of sub classes
		profiles.addAll(getInstances(OWLS.Profile.Profile, baseModelOnly));

		return OWLFactory.wrapList(profiles, Profile.class);
	}

	/* @see org.mindswap.owl.OWLModel#getProfile(java.net.URI) */
	public Profile getProfile(final URI profileURI) {
		final OWLIndividual ind = getIndividual(profileURI);

		return (ind == null)? null : ind.castTo(Profile.class);
	}

	/* @see org.mindswap.owl.OWLModel#getProcesses(boolean) */
	public OWLIndividualList<Process> getProcesses(final boolean baseModelOnly)
	{
		return OWLFactory.wrapList(getInstances(OWLS.Process.Process, baseModelOnly), Process.class);
	}

	/* @see org.mindswap.owl.OWLModel#getProcesses(int) */
	public OWLIndividualList<? extends Process> getProcesses(final int type, final boolean baseModelOnly)
	{
		switch(type)
		{
			case Process.ANY:
				return OWLFactory.wrapList(getInstances(OWLS.Process.Process, baseModelOnly), Process.class);
			case Process.ATOMIC:
				return OWLFactory.wrapList(getInstances(OWLS.Process.AtomicProcess, baseModelOnly), AtomicProcess.class);
			case Process.COMPOSITE:
				return OWLFactory.wrapList(getInstances(OWLS.Process.CompositeProcess, baseModelOnly), CompositeProcess.class);
			case Process.SIMPLE:
				return OWLFactory.wrapList(getInstances(OWLS.Process.SimpleProcess, baseModelOnly), SimpleProcess.class);
			default:
				break;
		}

		return null;
	}

	/* @see org.mindswap.owl.OWLModel#getProcess(java.net.URI) */
	public Process getProcess(final URI processURI) {
		final OWLIndividual ind = getIndividual(processURI);

		return (ind == null)? null : ind.castTo(Process.class);
	}

	/* @see org.mindswap.owl.OWLModel#isLockSupported() */
	public boolean isLockSupported() {
		return true;
	}

	/* @see org.mindswap.owl.OWLModel#lockForRead() */
	public void lockForRead()
	{
		ontModel.enterCriticalSection(Lock.READ);
	}

	/* @see org.mindswap.owl.OWLModel#lockForWrite() */
	public void lockForWrite()
	{
		ontModel.enterCriticalSection(Lock.WRITE);
	}

	/* @see org.mindswap.owl.OWLModel#releaseLock() */
	public void releaseLock()
	{
		ontModel.leaveCriticalSection();
	}

	/* @see org.mindswap.owl.OWLModel#getWriter() */
	public OWLWriter getWriter()
	{
		if (writer == null)
		{
			writer = new OWLWriterImpl();
			writer.setNsPrefix("service", OWLS.SERVICE_NS);
			writer.setNsPrefix("profile", OWLS.PROFILE_NS);
			writer.setNsPrefix("process", OWLS.PROCESS_NS);
			writer.setNsPrefix("grounding", OWLS.GROUNDING_NS);
			writer.setNsPrefix("expr", OWLS.EXPRESSION_NS);
			writer.setNsPrefix("list", OWLS.LIST_NS);
			writer.setNsPrefix("swrl", SWRL.NS);

			writer.addPrettyType(OWLS.Service.Service);
			writer.addPrettyType(OWLS.Service.ServiceGrounding);
			writer.addPrettyType(OWLS.Profile.Profile);
			writer.addPrettyType(OWLS.Process.Process);
			writer.addPrettyType(OWLS.Process.AtomicProcess);
			writer.addPrettyType(OWLS.Process.CompositeProcess);
			writer.addPrettyType(OWLS.Process.SimpleProcess);
			writer.addPrettyType(OWLS.Process.Perform);
			writer.addPrettyType(OWLS.Grounding.WsdlGrounding);
			writer.addPrettyType(OWLS.Grounding.WsdlAtomicProcessGrounding);
		}
		return writer;
	}

	/* @see org.mindswap.owl.OWLModel#size() */
	public long size()
	{
		return ontModel.size();
	}

	/* @see org.mindswap.owl.OWLModel#write(java.io.Writer, java.net.URI) */
	public void write(final Writer out, final URI baseURI) {
		getWriter().write(this, out, baseURI);
	}

	/* @see org.mindswap.owl.OWLModel#write(java.io.OutputStream, java.net.URI) */
	public void write(final OutputStream out, final URI baseURI) {
		getWriter().write(this, out, baseURI);
	}

	/* @see org.mindswap.owl.OWLModel#getNonLanguageIndividuals() */
	public List<OWLIndividual> getNonLanguageIndividuals()
	{
		final List<OWLClass> classes = getNonLanguageClasses();
		final List<OWLIndividual> inds = new ArrayList<OWLIndividual>();
		for (final OWLClass c : classes)
		{
			inds.addAll(getInstances(c, false));
		}
		return inds;
	}

	/* @see org.mindswap.owl.OWLModel#getNonLanguageClasses() */
	public List<OWLClass> getNonLanguageClasses() {
		final ExtendedIterator<OntClass> iter = ontModel.listNamedClasses();
		final List<OWLClass> list = new ArrayList<OWLClass>();
		while (iter.hasNext()) {
			final OntClass resource = iter.next();
			if (isInLanguageNamespace(resource)) continue;
			list.add(wrapClass(resource));
		}
		return list;
	}

	/* @see org.mindswap.owl.OWLModel#getNonLanguageDataProperties() */
	public List<OWLDataProperty> getNonLanguageDataProperties() {
		final ExtendedIterator<DatatypeProperty> iter = ontModel.listDatatypeProperties();
		final List<OWLDataProperty> list = new ArrayList<OWLDataProperty>();
		while (iter.hasNext()) {
			final DatatypeProperty property = iter.next();
			if (isInLanguageNamespace(property)) continue;
			list.add(wrapDataProperty(property));
		}
		return list;
	}

	/* @see org.mindswap.owl.OWLModel#getNonLanguageObjectProperties() */
	public List<OWLObjectProperty> getNonLanguageObjectProperties() {
		final ExtendedIterator<ObjectProperty> iter = ontModel.listObjectProperties();
		final List<OWLObjectProperty> list = new ArrayList<OWLObjectProperty>();
		while (iter.hasNext()) {
			final ObjectProperty property = iter.next();
			if (isInLanguageNamespace(property)) continue;
			list.add(wrapObjectProperty(property));
		}
		return list;
	}

	/* @see org.mindswap.owl.OWLModel#setProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public void setProperty(final OWLIndividual s, final OWLDataProperty p, final String o) {
		final Resource subject = (Resource) s.getImplementation();
		final Property predicate = (Property) p.getImplementation();

		removeProperty(s, p, null);
		ontModel.add(subject, predicate, o);
//		refresh();
	}

	/* @see org.mindswap.owl.OWLModel#setProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty, java.lang.Object) */
	public void setProperty(final OWLIndividual s, final OWLDataProperty p, final Object o) {
		setProperty(s, p, createDataValue(o));
	}

	/* @see org.mindswap.owl.OWLModel#setProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public void setProperty(final OWLIndividual s, final OWLDataProperty p, final OWLDataValue o) {
		final Resource subject = (Resource) s.getImplementation();
		final Property predicate = (Property) p.getImplementation();
		final Literal object = (Literal) o.getImplementation();

		removeProperty(s, p, null);
		ontModel.add(subject, predicate, object);
//		refresh();
	}

	/* @see org.mindswap.owl.OWLModel#addProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public void addProperty(final OWLIndividual s, final OWLDataProperty p, final OWLDataValue o) {
		ontModel.add(
			(Resource) s.getImplementation(),
			(Property) p.getImplementation(),
			(Literal)  o.getImplementation());
//		refresh();
	}

	/* @see org.mindswap.owl.OWLModel#addProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public void addProperty(final OWLIndividual s, final OWLDataProperty p, final String o) {
		ontModel.add((Resource) s.getImplementation(), (Property) p.getImplementation(), o);
//		refresh();
	}

	/* @see org.mindswap.owl.OWLModel#addProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty, java.lang.Object) */
	public void addProperty(final OWLIndividual s, final OWLDataProperty p, final Object o) {
		addProperty(s, p, createDataValue(o));
	}

	/* @see org.mindswap.owl.OWLModel#addProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual) */
	public void addProperty(final OWLIndividual s, final OWLObjectProperty p, final OWLIndividual o) {
		ontModel.add(
			(Resource) s.getImplementation(),
			(Property) p.getImplementation(),
			(Resource) o.getImplementation());
//		refresh();
	}

	/* @see org.mindswap.owl.OWLModel#setProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual) */
	public void setProperty(final OWLIndividual s, final OWLObjectProperty p, final OWLIndividual o) {
		final Resource subject = (Resource) s.getImplementation();
		final Property predicate = (Property) p.getImplementation();
		final Resource object = (Resource) o.getImplementation();

		removeProperty(s, p, null);
		ontModel.add(subject, predicate, object);
//		refresh();
	}

	/* @see org.mindswap.owl.OWLModel#addType(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLClass) */
	public void addType(final OWLIndividual ind, final OWLClass c)
	{
		ontModel.add((Resource) ind.getImplementation(), RDF.type, (Resource) c.getImplementation());
//		refresh();
	}

	/* @see org.mindswap.owl.OWLModel#remove(org.mindswap.owl.OWLIndividual, boolean) */
	public void remove(final OWLIndividual ind, final boolean recursive)
	{
		removeAll((Resource) ind.getImplementation(), recursive);
	}

	/* @see org.mindswap.owl.OWLModel#removeProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLProperty, org.mindswap.owl.OWLValue) */
	public void removeProperty(final OWLIndividual s, final OWLProperty p, final OWLValue o)
	{
		final Resource subject = (Resource) s.getImplementation();
		final Property predicate = (Property) p.getImplementation();
		final RDFNode object = (o == null)? null : (RDFNode) o.getImplementation();

		removeAll(subject, predicate, object);
	}

	/* @see org.mindswap.owl.OWLModel#removeTypes(org.mindswap.owl.OWLIndividual) */
	public void removeTypes(final OWLIndividual ind)
	{
		final Resource subject = (Resource) ind.getImplementation();

		removeAll(subject, RDF.type, null);
	}

	/* @see org.mindswap.owl.OWLModel#parseLiteral(java.lang.String) */
	public OWLIndividual parseLiteral(final String literal)
	{
		final StringReader in = new StringReader(literal);
		final OntModel literalModel;
		try
		{
			// Need to disable reasoner while loading/parsing the literal since this
			// may cause the subsequent search of the return value to fail, because
			// there may be additional inferred triples in the model.
			final OntModelSpec spec = new OntModelSpec(ontModel.getSpecification());
			spec.setReasoner(null);
			spec.setReasonerFactory(null);
			literalModel = ModelFactory.createOntologyModel(spec);
			literalModel.read(in, getOntology().getURI().toString());
		}
		finally
		{
			in.close();
		}

		if (literalModel.isEmpty()) // sanity check
		{
			logger.debug("Parsing XML/RDF literal resulted in empty model. This may indicate a syntax error.");
			return null;
		}

		ontModel.add(literalModel, true); // obviously, all statements should be part of this model

		// This algorithm does the following: It returns either the first individual found
		// from those which never occur as the object in any (S,P,O) triple; or, if all
		// individuals appear as an object, it returns the one that has the most properties.
		int outCount = 0;
		Resource tmp, result = null;
		final ResIterator subjects = literalModel.listSubjects();
		while (subjects.hasNext())
		{
			tmp = subjects.next();
			final StmtIterator stmts = literalModel.listStatements(null, null, tmp);
			if (stmts.hasNext())
			{
				final int newCount = tmp.listProperties().toList().size();
				if (result == null || newCount > outCount)
				{
					result = tmp;
					outCount = newCount;
				}
			}
			else // found individual which never occurs as the object in any (S,P,O)
			{
				result = tmp;
				break;
			}
		}

		return (result == null)? null : wrapIndividual(result.inModel(ontModel).as(Resource.class));
	}

	/* @see org.mindswap.owl.OWLModel#startBulkUpdate() */
	public void startBulkUpdate()
	{
		bulkUpdate++;
	}

	/* @see org.mindswap.owl.OWLModel#endBulkUpdate(boolean) */
	public void endBulkUpdate(final boolean refresh)
	{
		if (bulkUpdate > 0) bulkUpdate--;
		if (refresh && bulkUpdate == 0) refresh(); // do refresh only when outermost bulk update is complete
	}

	/* @see org.mindswap.owl.OWLModel#isBulkUpdate() */
	public boolean isBulkUpdate()
	{
		return bulkUpdate > 0;
	}

	/**
	 * Get the base ontology if this is a OWLKnowledge base and 'this' if this
	 * is a OWLOntology.
	 */
	protected abstract OWLOntologyImpl getOntology();

	/**
	 * This method is invoked if a new reasoner was attached or if an existing
	 * one was detached. Implementations may react to this event, e.g., to
	 * trigger cleanup or initialization.
	 */
	protected abstract void reasonerSet(Reasoner oldReasoner);

	/**
	 * Implementations need to remove all triples. A value of <code>null</code>
	 * for one or more parameter is to be interpreted as the wildcard. For
	 * instance, (s, p, null) means to remove *all* objects of the subject s
	 * with property p.
	 */
	protected abstract void removeAll(Resource s, Property p, RDFNode o);

	protected OWLIndividualList<?> getInstances(final OWLClass c, final boolean baseModelOnly, final OntModel model)
	{
		final Model m = (baseModelOnly)?
			ModelFactory.createOntologyModel(ontModel.getSpecification(), ontModel.getBaseModel()) : ontModel;

		ExtendedIterator<Resource> it = m.listResourcesWithProperty(RDF.type, (Resource) c.getImplementation());

		// if a model is given this means that we need to filter out all instances that are not in model
		it = (model != null)? it.filterKeep(new IsInModelFilter(model)) : it;

		return getAllIndividuals((baseModelOnly)? it.mapWith(new InModelMapper()) : it);
	}

	protected boolean isInLanguageNamespace(final String namespace)
	{
		return owlsNamespaces.isBuiltinNamespace(namespace);
	}

	protected boolean isInLanguageNamespace(final Resource resource)
	{
		return owlsNamespaces.isBuiltinNamespace(resource.getNameSpace());
	}

	private boolean isPelletReasonerAttached()
	{
		return ontModel.getGraph() instanceof PelletInfGraph;
	}

	private void removeAll(final Resource resource, final boolean recursive)
	{
		if (recursive)
		{
			final StmtIterator propIter = ontModel.listStatements(resource, null, (RDFNode) null);
			RDFNode node;
			while (propIter.hasNext())
			{
				node = propIter.nextStatement().getObject();
				if (node.isResource())
					removeAll((Resource) node, recursive);
			}
		}

		// removes all statements with the given individual in the subject
		logger.debug("Removing all statements ({}, *, *)", resource);
		removeAll(resource, null, null);

		// removes all statements with the given individual in the object
		logger.debug("Removing all statements (*, *, {})", resource);
		removeAll(null, null, resource);
	}

	/**
	 * Returns the given resource (<code>this</code>), if it's already in
	 * <code>ontModel</code>, a copy in <code>ontModel</code> otherwise.
	 */
	final class InModelMapper implements Map1<Resource, RDFNode>
	{
		InModelMapper() { super(); }

		public RDFNode map1(final Resource o)
		{
			return o.inModel(ontModel);
		}
	}

	/** Those resources that are in the given model pass this filter. */
	static final class IsInModelFilter extends Filter<Resource>
	{
		private final Model model;

		IsInModelFilter(final Model model) { this.model = model; }

		@Override
		public boolean accept(final Resource item)
		{
			return model.containsResource(item);
		}
	}

}
