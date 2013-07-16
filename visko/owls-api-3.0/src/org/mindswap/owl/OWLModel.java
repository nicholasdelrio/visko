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
 * Created on Dec 16, 2004
 */
package org.mindswap.owl;

import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.common.Variable;
import org.mindswap.exceptions.ParseException;
import org.mindswap.exceptions.UnboundVariableException;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.JavaGrounding;
import org.mindswap.owls.grounding.JavaParameter;
import org.mindswap.owls.grounding.JavaVariable;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.grounding.UPnPGrounding;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLGrounding;
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
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.Query;
import org.mindswap.query.QueryLanguage;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.BuiltinAtom;

/**
 * A OWLModel is the data structure to manage a set of OWL axioms and assertions.
 * It is structured hierarchically, that is, it consists of a {@link #getBaseModel()
 * base model} and may contain any number of sub models, whereby sub models may
 * again contain sub models. If this model is actually a {@link OWLKnowledgeBase}
 * then its sub models are {@link OWLOntology ontologies} that were loaded into
 * the KB. On the other hand, if it is a {@link OWLOntology} then the base model
 * represents the document that was loaded from a (remote) file, a stream, or a
 * reader, and its sub models are imported ontologies (if any) of this base
 * ontology.
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLModel {
	/**
	 * Return the implementation specific object for this model.
	 * <p>
	 * This method is rather intended to be used internally.
	 *
	 * @return The underlying data structure that represents this model.
	 */
	public Object getImplementation();

	/**
	 * Get the model where the changes will be made (create and add) by default.
	 * (Note that set and remove methods invoked on this model my cause updates
	 * throughout the entire model, i.e., also in sub models of this model.)
	 * <p>
	 * If this model is a {@link OWLKnowledgeBase KB} then it is the base
	 * ontology of the KB. At the creation time of each KB an empty ontology is
	 * created to be the base ontology of the KB. All createXXX and addXXX
	 * methods will actually modify this base ontology. However, removeXXX and
	 * setXXX methods may modify any sub ontology in the KB, provided that it is
	 * not in {@link OWLOntology#isReadOnly() read-only} mode.
	 * <p>
	 * If this model is a {@link OWLOntology ontology} then the base model is
	 * the ontology not containing the statements of its imported ontologies
	 * (if any).
	 *
	 * @return The base model.
	 */
	public OWLModel getBaseModel();

	/**
	 * If a reasoner is attached this method will force it to rebind this model,
	 * thus, refreshing all its content.
	 * <p>
	 * This method is
	 */
	public void refresh();

	/**
	 * Use this method when a bulk of modifying operations should be done. This
	 * will temporarily deactivate {@link #refresh() refreshing} that would
	 * otherwise be done after each single operation is finished, thus,
	 * preventing additional delays consumed by refreshing.
	 * <p>
	 * Use {@link #endBulkUpdate(boolean)} to indicate that the bulk operation
	 * is complete.
	 * <p>
	 * This model will not perform {@link #refresh()}, {@link #classify()}, nor
	 * {@link #prepare()} operations while a bulk update is not yet finished.
	 * <p>
	 * This method is reentrant. Repeated invocation without interleaving
	 * invocations of {@link #endBulkUpdate(boolean)} is possible to support
	 * nested bulk updates. Note that {@link #endBulkUpdate(boolean)} needs to
	 * be invoked exactly as often as this method was invoked in order to
	 * entirely end a bulk update.
	 */
	public void startBulkUpdate();

	/**
	 * Use this method to announce the end of a modifying bulk operation.
	 *
	 * @param refresh If <code>true</code> and this method invocation represents
	 * 	the completion of the outermost bulk operation (i.e. {@link #isBulkUpdate()}
	 * 	returns <code>false</code> afterwards) this method will also perform a
	 * 	{@link #refresh()}.
	 */
	public void endBulkUpdate(final boolean refresh);

	/**
	 * @return <code>true</code> while a modifying bulk operation is on the way.
	 */
	public boolean isBulkUpdate();

	/**
	 * Check if the model is consistent or not. Since this involves reasoning
	 * a {@link #getReasoner() reasoner} needs to be attached, otherwise the
	 * result is meaningless.
	 *
	 * @return <code>true</code> if there is no reasoner attached or the
	 * 	reasoner used does not support consistency check.
	 */
	public boolean isConsistent();

	/**
	 * Prepare the reasoner (if any) associated with this model.
	 */
	public void prepare();

	/**
	 * Tell the reasoner (if any) to compute the subclass relations between all
	 * the named classes and find the instances of each class. The results will
	 * be cached and subsequent calls to query the model will use the cache.
	 * This call has no effect if reasoner does not support classification.
	 */
	public void classify();

	/**
	 * @return <code>true</code> if the model is classified.
	 */
	public boolean isClassified();

	/**
	 * Attach a reasoner to this model using a reasoner name. See
	 * {@link OWLFactory#getReasonerNames()} for available reasoner names.
	 * <p>
	 * For the Jena based implementation available reasoner names are:
	 * <ul>
	 * 	<li>RDFS</li>
	 * 	<li>Transitive</li>
	 * 	<li>OWL</li>
	 * 	<li>OWLMini</li>
	 * 	<li>OWLMicro</li>
	 * 	<li>Pellet</li>
	 * </ul>
	 * </p>
	 *
	 * @param reasonerName The name of the reasoner. If <code>null</code> no
	 * 	reasoner will be used at all.
	 */
	public void setReasoner(String reasonerName);

	/**
	 * Set the reasoner using an implementation specific reasoner object. The
	 * exact type of the parameter is different for Jena and OWL-API.
	 *
	 * @param reasoner The reasoner that should be used for (logic based)
	 * 	inferencing. If <code>null</code> no reasoner will be used at all.
	 */
	public void setReasoner(Object reasoner);

	/**
	 * @return The reasoner used by this model, or <code>null</code> if none
	 * 	is attached.
	 */
	public Object getReasoner();

	/**
	 * @param uri The URI for the class, or <code>null</code> for an anonymous class.
	 * 	If a class with the given URI already exists in the model, it will be re-used.
	 * @return A OWL Class resource.
	 */
	public OWLClass createClass(URI uri);
	/**
	 * @param uri
	 * @return
	 */
	public OWLDataType createDataType(URI uri);
	/**
	 * @param uri The URI for the annotation property. Must not be <code>null</code>.
	 * @return The object property resource. If a object property of the given
	 * 	URI already existed in the model it will be re-used.
	 * @throws NullPointerException if <code>uri</code> is <code>null</code>.
	 */
	public OWLAnnotationProperty createAnnotationProperty(URI uri);
	/**
	 * @param uri The URI for the object property. Must not be <code>null</code>.
	 * @return The object property resource. If a object property of the given
	 * 	URI already existed in the model it will be re-used.
	 * @throws NullPointerException if <code>uri</code> is <code>null</code>.
	 */
	public OWLObjectProperty createObjectProperty(URI uri);
	/**
	 * @param uri The URI for the datatype property. Must not be <code>null</code>.
	 * @return The datatype property resource. If a datatype property of the given
	 * 	URI already existed in the model it will be re-used.
	 * @throws NullPointerException if <code>uri</code> is <code>null</code>.
	 */
	public OWLDataProperty createDataProperty(URI uri);
	/**
	 * Either create a plain individual, i.e., a resource that does not has the
	 * property <tt>rdf:type</tt> set, or an individual where <tt>rdf:type</tt>
	 * refers to the given resource.
	 * <p>
	 * Note that this method does not check if the resource identified by
	 * <code>rdfType</code> is known to be a OWL class. Consequently, this model
	 * may transit to OWL Full if this model does not entail the resource
	 * referred to by <code>rdfType</code> to be a OWL class.
	 *
	 * @param rdfType The URI to be set for <tt>rdf:type</tt>. If <code>null</code>
	 * 	no <tt>rdf:type</tt> statement will be added to the returned resource.
	 * @param uri The URI for the individual, or <code>null</code> for an anonymous
	 * 	individual.
	 * @return A new individual identified by the given URI, if not <code>null</code>.
	 */
	public OWLIndividual createIndividual(URI rdfType, URI uri);
	/**
	 * Create an instance (individual) of the given OWL class (concept).
	 *
	 * @param c The OWL class determining which type of OWL individual to create.
	 * 	A value of <code>null</code> forces the method to create an instance of
	 * 	<tt>owl:Thing</tt>.
	 * @param uri The URI for the individual, or <code>null</code> for an anonymous
	 * 	individual.
	 * @return A new instance of the given OWL class (optionally identified by the
	 * 	given URI).
	 */
	public OWLIndividual createInstance(OWLClass c, URI uri);
	/**
	 * @param value
	 * @return
	 */
	public OWLDataValue createDataValue(String value);
	/**
	 * @param value
	 * @param language
	 * @return
	 */
	public OWLDataValue createDataValue(String value, String language);
	/**
	 * @param value
	 * @param datatypeURI
	 * @return
	 */
	public OWLDataValue createDataValue(Object value, URI datatypeURI);
	/**
	 * @param value
	 * @return
	 */
	public OWLDataValue createDataValue(Object value);

	/**
	 * @param uri
	 * @return
	 */
	public OWLClass getClass(URI uri);

	/**
	 * @param named If <code>true</code> include only named classes, otherwise
	 * 	include all kinds of classes (enumerated, union, complement, intersection,
	 * 	named, and restriction classes).
	 * @return The list of classes contained in this model.
	 */
	public Set<OWLClass> getClasses(boolean named);
	/**
	 * @param uri
	 * @return
	 */
	public OWLIndividual getIndividual(URI uri);

	/**
	 * @param baseModelOnly If <code>true</code> query only the base model,
	 * 	otherwise extend the query to the entire model.
	 * @return All OWL individuals, or an empty list if none exist.
	 */
	public OWLIndividualList<?> getIndividuals(boolean baseModelOnly);

	/**
	 * @param uri
	 * @return
	 */
	public OWLProperty getProperty(URI uri);

	/**
	 * @param uri
	 * @return
	 */
	public OWLAnnotationProperty getAnnotationProperty(URI uri);

	/**
	 * @param uri
	 * @return
	 */
	public OWLObjectProperty getObjectProperty(URI uri);
	/**
	 * @param uri
	 * @return
	 */
	public OWLDataProperty getDataProperty(URI uri);
	/**
	 * This method can be used to retrieve the OWL datatype to which a given
	 * Java class can be mapped. This is not a fully fledged (generic) type
	 * mapping mechanism as it works only for primitive Java types and their
	 * wrapper types as well as URI, URL, Date, and Calendar.
	 *
	 * @param clazz The Java class.
	 * @return The OWL datatype to which the Java type is mapped, or <code>null</code>
	 * 	if the type can not be directly mapped.
	 */
	public OWLDataType getDataType(Class<?> clazz);
	/**
	 * @param uri
	 * @return
	 */
	public OWLDataType getDataType(URI uri);
	/**
	 * @param uri
	 * @return
	 */
	public OWLType getType(URI uri);
	/**
	 * @param uri
	 * @return
	 */
	public OWLEntity getEntity(URI uri);

	/**
	 * Set the value for the given data property to the given plain literal
	 * string (no language identifier). All the existing data values (that have
	 * no language identifier) will be removed.
	 *
	 * @param ind The individual on which to set the property.
	 * @param prop The data property to set on the individual.
	 * @param value The string to set as plain literal.
	 */
	public void setProperty(OWLIndividual ind, OWLDataProperty prop, String value);

	/**
	 * Set the value for the given data property. All the existing data values
	 * (that have the same language identifier with the given value) will be removed.
	 *
	 * @param s The individual on which to set the property.
	 * @param p The data property to set on the individual.
	 * @param o The data value to set.
	 */
	public void setProperty(OWLIndividual s, OWLDataProperty p, OWLDataValue o);

	/**
	 * Set the value for the given data property to the given literal by
	 * determining the RDF datatype from Java class. This function is
	 * equivalent to <code>setProperty(s, p, createDataValue(o))</code>.
	 *
	 * @param s The individual on which to set the property.
	 * @param p The data property to set on the individual.
	 * @param o The value to set.
	 */
	public void setProperty(OWLIndividual s, OWLDataProperty p, Object o);

	/**
	 * @param s The individual to which the property is to be added.
	 * @param p The data property to add to the individual.
	 * @param o The data value to use.
	 */
	public void addProperty(OWLIndividual s, OWLDataProperty p, OWLDataValue o);

	/**
	 * Add the value for the given data property to the given literal by
	 * determining the RDF datatype from Java class. This function is
	 * equivalent to <code>addProperty(s, p, createDataValue(o))</code>.
	 *
	 * @param s The individual to which the property is to be added.
	 * @param p The data property to add to the individual.
	 * @param o
	 */
	public void addProperty(OWLIndividual s, OWLDataProperty p, Object o);

	/**
	 * @param s The individual to which the property is to be added.
	 * @param p The data property to add to the individual.
	 * @param o
	 */
	public void addProperty(OWLIndividual s, OWLDataProperty p, String o);

	/**
	 * Removes all statements from the model where the given OWL individual is
	 * the object or subject, therefore removing each occurrence of the given
	 * individual.
	 * <p>
	 * If the <code>recursive</code> parameter is set, the method iterates
	 * recursively through all properties of the given individual and removes
	 * them all.
	 *
	 * @param ind The individual to remove.
	 * @param recursive <code>true</code>, if recursive removal is desired.
	 * 	<code>false</code> otherwise.
	 */
	public void remove(OWLIndividual ind, boolean recursive);

	/**
	 * Removes all statements matching (s,p,o) from this Model.
	 *
	 * @param s The target individual (subject).
	 * @param p The target property (predicate).
	 * @param o The value to remove (object). A value of <code>null</code>
	 * 	will remove all statements (s,p,*) from the model.
	 */
	public void removeProperty(OWLIndividual s, OWLProperty p, OWLValue o);

	/**
	 * Adds the specified statements to the Model.
	 *
	 * @param s The target individual (subject).
	 * @param p The target object property (predicate).
	 * @param o The individual to add (object).
	 */
	public void addProperty(OWLIndividual s, OWLObjectProperty p, OWLIndividual o);

	/**
	 *
	 * @param ind
	 * @param prop
	 * @param value
	 */
	public void setProperty(OWLIndividual ind, OWLObjectProperty prop, OWLIndividual value);

	/**
	 * @param ind
	 * @param c
	 */
	public void addType(OWLIndividual ind, OWLClass c);

	/**
	 * @param ind
	 */
	public void removeTypes(OWLIndividual ind);

	/**
	 * @param c The class to analyze.
	 * @return <code>true</code> if <code>c</code> is an enumerated class
	 * 	(defined with owl:oneOf).
	 */
	public boolean isEnumerated(OWLClass c);

	/**
	 * Returns the enumeration for the class (the list of individuals declared
	 * in the owl:oneOf list).
	 *
	 * @param c The class to analyze.
	 * @return List of individuals or <code>null</code> if the class is not
	 * 	enumerated.
	 */
	public OWLIndividualList<?> getEnumerations(OWLClass c);

	/**
	 * Determine if DL based subsumption relation holds for the given types,
	 * i.e., whether the <tt>t1</tt> is subsumed by <tt>t2</tt>. The types
	 * should be either two OWL classes or two datatypes.
	 * <p>
	 * Note that a reasoner should be attached since this method likely depends
	 * on inferencing and logical deduction.
	 *
	 * @param t1
	 * @param t2
	 * @return <code>true</code> if <tt>t1</tt> is subsumed by <tt>t2</tt>.
	 */
	public boolean isSubTypeOf(OWLType t1, OWLType t2);

	/**
	 * Determine if DL based equivalence relation holds for the given types,
	 * which should be either two OWL classes or two OWL datatypes.
	 * <p>
	 * Note that a reasoner should be attached since this method likely depends
	 * on inferencing and logical deduction.
	 *
	 * @param t1
	 * @param t2
	 * @return <code>true</code> if <tt>t1</tt> is equivalent with <tt>t2</tt>.
	 */
	public boolean isEquivalent(OWLType t1, OWLType t2);

	/**
	 * Determine if DL based disjointness relation holds for the given types,
	 * which should be either two OWL classes or two OWL datatypes.
	 * <p>
	 * Note that a reasoner should be attached since this method likely depends
	 * on inferencing and logical deduction.
	 *
	 * @param t1
	 * @param t2
	 * @return <code>true</code> if there can possibly be no individual that may
	 * 	belong to both classes, or no data value in the value range of both
	 * 	datatypes.
	 */
	public boolean isDisjoint(OWLType t1, OWLType t2);

	/**
	 * Determine if two classes are complementary.
	 *
	 * @param c1
	 * @param c2
	 * @return <code>true</code> if both classes are complementary, <code>false</code>
	 * 	otherwise.
	 */
	public boolean isComplement(OWLClass c1, OWLClass c2);

	/**
	 * Get all the (direct) subclasses of the given class
	 *
	 * @param c
	 * @param direct
	 * @return
	 */
	public Set<OWLClass> getSubClasses(OWLClass c, boolean direct);

	/**
	 * Get all the (direct) subclasses of the given class
	 *
	 * @param c
	 * @param direct
	 * @return
	 */
	public Set<OWLClass> getSuperClasses(OWLClass c, boolean direct);

	/**
	 * @param c
	 * @return
	 */
	public Set<OWLClass> getEquivalentClasses(OWLClass c);

	/**
	 * @param p
	 * @return
	 */
	public Set<OWLProperty> getSubProperties(OWLProperty p);
	/**
	 * @param p
	 * @return
	 */
	public Set<OWLProperty> getSuperProperties(OWLProperty p);
	/**
	 * @param p
	 * @return
	 */
	public Set<OWLProperty> getEquivalentProperties(OWLProperty p);

	/**
	 * @param c The OWL class for which to return instances in this model.
	 * @param baseModelOnly If <code>true</code> query only the base model,
	 * 	otherwise extend the query to the entire model.
	 * @return All instances of the given class, or an empty list if none exist.
	 * 	Note that the extent of the result is likely to be larger if a reasoner
	 * 	is attached to the model, as the reasoner will add inferred instances.
	 */
	public OWLIndividualList<?> getInstances(OWLClass c, boolean baseModelOnly);

	/**
	 * @param ind
	 * @param c
	 * @return
	 */
	public boolean isType(OWLIndividual ind, OWLClass c);

	/**
	 * @param ind The individual to query for its OWL class.
	 * @return The <code>rdf:type</code> (i.e. the class) of the given individual,
	 * 	provided that the value can be viewed as a OWL class. If there is more
	 * 	than one type for this individual, the return value will be one of the
	 * 	classes, but it is not specified which one (nor that it will consistently
	 * 	be the same one each time).
	 * @see #getTypes(OWLIndividual)
	 */
	public OWLClass getType(OWLIndividual ind);

	/**
	 * @param ind
	 * @return
	 */
	public Set<OWLClass> getTypes(OWLIndividual ind);

	/**
	 * @param ind
	 * @param prop
	 * @return
	 */
	public OWLIndividual getProperty(OWLIndividual ind, OWLObjectProperty prop);
	/**
	 * @param ind
	 * @param prop
	 * @return
	 */
	public OWLIndividualList<?> getProperties(OWLIndividual ind, OWLObjectProperty prop);
	/**
	 * @param ind
	 * @return
	 */
	public Map<OWLProperty, List<OWLValue>> getProperties(OWLIndividual ind);

	/**
	 * Returns all properties for a class. Note that a class is an instance in
	 * OWL and therefore the properties given to the instance at definition are
	 * returned and NOT the properties that are declared for this class. Use
	 * {@link #getDeclaredProperties(OWLClass, boolean)} instead.
	 *
	 * @param claz The class whose properties will be returned.
	 * @return All properties (data- and object-properties) of the given class.
	 * @see #getDeclaredProperties(OWLClass, boolean)
	 */
	public Map<OWLProperty, List<OWLValue>> getProperties(OWLClass claz);

	/**
	 * Returns all declared properties for a class. Use
	 * {@link #getProperties(OWLClass)} if you want the properties
	 * of the instance at class definition.
	 *
	 * @param claz the class whose properties will be returned
	 * @param direct <code>true</code>, if only properties of the given class
	 * 	itself should be returned. <code>false</code>, if properties of
	 * 	super classes should be returned too.
	 * @return all properties (data- and object-properties) of the given class
	 */
	public List<OWLProperty> getDeclaredProperties(OWLClass claz, boolean direct);

	/**
	 * @param ind
	 * @param prop
	 * @return
	 */
	public OWLDataValue getProperty(OWLIndividual ind, OWLDataProperty prop);

	/**
	 * This method can be used if it is known that the range of the datatype
	 * property is <code>xsd:anyURI</code>. The method tries to return the
	 * corresponding OWL type identified by the URI value.
	 *
	 * @param ind
	 * @param prop
	 * @return
	 */
	public OWLType getPropertyAsOWLType(OWLIndividual ind, OWLDataProperty prop);
	/**
	 * @param ind
	 * @param prop
	 * @param lang
	 * @return
	 */
	public OWLDataValue getProperty(OWLIndividual ind, OWLDataProperty prop, String lang);
	/**
	 * @param ind
	 * @param prop
	 * @return
	 */
	public List<OWLDataValue> getProperties(OWLIndividual ind, OWLDataProperty prop);

	/**
	 * Get the individual that refers to the given OWL individual via the given
	 * object property. Using RDF terminology, we can say that this method
	 * returns the subject <tt>s</tt> that refers via the predicate <tt>p</tt>
	 * (<tt>prop</tt>) to the object o (<tt>ind</tt>).
	 * <p>
	 * If the underlying model contains multiple individuals that relate via the
	 * property to the same individual, the first one found is returned, i.e., it
	 * should only be used for inverse functional properties (provided that the
	 * underlying model is consistent). In case of standard and functional
	 * properties it is recommended to use
	 * {@link #getIncomingProperties(OWLObjectProperty, OWLIndividual)} instead.
	 *
	 * @param prop The connecting property.
	 * @param ind The object individual.
	 * @return The subject individual, or <code>null</code> if no such relation
	 * 	exists in this model.
	 */
	public OWLIndividual getIncomingProperty(OWLObjectProperty prop, OWLIndividual ind);

	/**
	 * Get all individuals that refer to the given OWL individual via the given
	 * object property. Using RDF terminology, we can say that this method
	 * returns all subjects <tt>s</tt> that refer via the predicate <tt>p</tt>
	 * (<tt>prop</tt>) to the object o (<tt>ind</tt>).
	 *
	 * @param prop The connecting property.
	 * @param ind The object individual.
	 * @return All subject individuals, or an empty list if no such relation
	 * 	exists in this model.
	 */
	public OWLIndividualList<?> getIncomingProperties(OWLObjectProperty prop, OWLIndividual ind);

	/**
	 * Get all individuals that refer to the given OWL individual via any
	 * object property. Using RDF terminology, we can say that this method
	 * returns all subjects <tt>s</tt> that refer via any predicate <tt>p</tt>
	 * (<tt>prop</tt>) to the object o (<tt>ind</tt>).
	 *
	 * @param ind The object individual.
	 * @return All subject individuals, or an empty list if the given individual
	 * 	does not occur as the subject in the underlying model.
	 */
	public OWLIndividualList<?> getIncomingProperties(OWLIndividual ind);

	/**
	 * Get the individual that refers to the given OWL data value via the given
	 * data property. Using RDF terminology, we can say that this method
	 * returns the subject <tt>s</tt> that refers via the predicate <tt>p</tt>
	 * (<tt>prop</tt>) to the object o (<tt>value</tt>).
	 * <p>
	 * If the underlying model contains multiple individuals that relate via the
	 * property to the same data value, the first one found is returned, i.e., it
	 * should only be used for inverse functional properties (provided that the
	 * underlying model is consistent). In case of standard and functional
	 * properties it is recommended to use
	 * {@link #getIncomingProperties(OWLDataProperty, OWLDataValue)} instead.
	 *
	 * @param prop The connecting property.
	 * @param value The object data value.
	 * @return The subject individual, or <code>null</code> if no such relation
	 * 	exists in this model.
	 */
	public OWLIndividual getIncomingProperty(OWLDataProperty prop, OWLDataValue value);

	/**
	 * Get all individuals that refer to the given OWL data value via the given
	 * data property. Using RDF terminology, we can say that this method
	 * returns all subjects <tt>s</tt> that refer via the predicate <tt>p</tt>
	 * (<tt>prop</tt>) to the object o (<tt>value</tt>).
	 *
	 * @param prop The connecting property.
	 * @param value The object data value.
	 * @return All subject individuals, or an empty list if no such relation
	 * 	exists in this model.
	 */
	public OWLIndividualList<?> getIncomingProperties(OWLDataProperty prop, OWLDataValue value);

	/**
	 * Determine if an (S, P, O) pattern is present in this model, with
	 * <code>null</code> allowed to represent a wildcard match.
	 *
	 * @param s The subject of the statement tested (<code>null</code> as wildcard).
	 * @param p The predicate of the statement tested (<code>null</code> as wildcard).
	 * @param o The object of the statement tested (<code>null</code> as wildcard).
	 * @return <code>true</code> if the statement with subject <code>o</code>,
	 * 	property <code>p</code> and object <code>o</code> is in this model,
	 * 	<code>false</code> otherwise.
	 */
	public boolean contains(OWLEntity s, OWLProperty p, OWLValue o);

	/**
	 * Evaluates the given set of (ground) atoms, that is, it simply adds new
	 * assertions to the model. Note that {@link BuiltinAtom built-in atoms}
	 * that represent a function (e.g. add, equals, and so on) can not be
	 * evaluated.
	 *
	 * @param atoms The list of atoms to evaluate.
	 * @throws UnboundVariableException If some atom is not grounded.
	 */
	public void evaluate(OWLList<Atom> atoms) throws UnboundVariableException;

	/**
	 * Similar to {@link #evaluate(OWLList)} but simply skips the non-grounded
	 * atoms instead of throwing an exception. Atoms that contain variables
	 * are called non-grounded.
	 *
	 * @param atoms The list of atoms to evaluate.
	 */
	public void evaluateGround(OWLList<Atom> atoms);

	/**
	 * Transform the given ABox query into an equivalent query that can be
	 * executed against this model.
	 *
	 * @param <V> The kind of result variables.
	 * @param aboxQuery The ABox query to transform.
	 * @return A query against this model.
	 */
	public <V extends Variable> Query<V> makeQuery(ABoxQuery<V> aboxQuery);

	/**
	 * This method is functionally equivalent to invoking <code>
	 * makeQuery(new AboxQuery(atoms, resultVars))</code>.
	 * Or the other way around
	 * <code>makeQuery(myAboxQuery.getBody(), myAboxQuery.getResultVars())</code>.
	 */
	public <V extends Variable> Query<V> makeQuery(OWLList<Atom> constraints, List<V> resultVars);

	/**
	 * Parse the given SPARQL, SPARQL-DL, or RDQL query into a query that can
	 * be executed against this model.
	 * <p>
	 * Note that execution of SPARQL-DL queries requires an appropriate reasoner
	 * (Pellet) to be attached to this model.
	 *
	 * @param query The query string.
	 * @param lang The syntax/language of the query string. Defaults to
	 * 	{@link QueryLanguage#SPARQL} if parameter is <code>null</code>.
	 * @return A query against this model.
	 * @throws ParseException In case of invalid syntax of the query string.
	 * @throws IllegalArgumentException If the type of the query is neither
	 * 	SELECT nor ASK, i.e., in case of CONSTRUCT or DESCRIBE queries.
	 */
	public Query<Variable> makeQuery(String query, QueryLanguage lang) throws ParseException;

	/**
	 * Checks if this implementation supports locking to synchronize concurrent
	 * access.
	 *
	 * @return <code>true</code> if this model can be locked for read and/or
	 * 	write access.
	 * @see #lockForRead()
	 * @see #lockForWrite()
	 * @see #releaseLock()
	 */
	public boolean isLockSupported();

	/**
	 * Lock the model for read operations. Multiple read operations are allowed
	 * for the same model. When the model is locked for reading no operation that
	 * would modify the model should be called.
	 * <p>
	 * This method is reentrant so an application can have nested critical sections.
	 * However, the application must call {@link #releaseLock()} as often as this
	 * method was invoked to release the lock.
	 *
	 * @throws UnsupportedOperationException If the implementation does not support
	 * 	locking, i.e., {@link #isLockSupported()} returns <code>false</code>.
	 * @see #isLockSupported()
	 * @see #lockForWrite()
	 * @see #releaseLock()
	 */
	public void lockForRead();

	/**
	 * Lock the model for write operations. No other thread can access the model
	 * while there is a write lock.
	 * <p>
	 * This method is reentrant so an application can have nested critical sections.
	 * However, the application must call {@link #releaseLock()} as often as this
	 * method was invoked to release the lock.
	 *
	 * @throws UnsupportedOperationException If the implementation does not support
	 * 	locking, i.e., {@link #isLockSupported()} returns <code>false</code>.
	 * @see #isLockSupported()
	 * @see #lockForRead()
	 * @see #releaseLock()
	 */
	public void lockForWrite();

	/**
	 * Releases the lock form the matching lockForXXX method. If there was no lock
	 * set before this method has no effect.
	 * <p>
	 * Since locking is reentrant an application must call this method as often as
	 * the matching lockForXXX method was invoked.
	 *
	 * @throws UnsupportedOperationException If the implementation does not support
	 * 	locking, i.e., {@link #isLockSupported()} returns <code>false</code>.
	 * @see #isLockSupported()
	 * @see #lockForRead()
	 * @see #lockForWrite()
	 */
	public void releaseLock();

	/**
	 * @param baseModelOnly If <code>true</code> query only the base model,
	 * 	otherwise extend the query to the entire model.
	 * @return All OWL-S service instances, or an empty list if none exist.
	 */
	public OWLIndividualList<Service> getServices(boolean baseModelOnly);
	public Service getService(URI serviceURI);

	/**
	 * @param baseModelOnly If <code>true</code> query only the base model,
	 * 	otherwise extend the query to the entire model.
	 * @return All OWL-S profile instances, or an empty list if none exist.
	 */
	public OWLIndividualList<Profile> getProfiles(boolean baseModelOnly);
	public Profile getProfile(URI profileURI);

	/**
	 * @param baseModelOnly If <code>true</code> query only the base model,
	 * 	otherwise extend the query to the entire model.
	 * @return All OWL-S process instances, or an empty list if none exist.
	 */
	public OWLIndividualList<Process> getProcesses(boolean baseModelOnly);
	public OWLIndividualList<? extends Process> getProcesses(int type, final boolean baseModelOnly);
	public Process getProcess(URI processURI);

	public AnyOrder createAnyOrder(URI uri);

	public AtomicProcess createAtomicProcess(URI uri);

	public Choice createChoice(URI uri);

	public CompositeProcess createCompositeProcess(URI uri);


	public OWLList<ControlConstruct> createControlConstructList(ControlConstruct item);
	public OWLList<ControlConstruct> createControlConstructBag(ControlConstruct item);

	public Condition.SWRL createSWRLCondition(URI uri);
	public Expression.SWRL createSWRLExpression(URI uri);

	public Condition.SPARQL createSPARQLCondition(URI uri);
	public Expression.SPARQL createSPARQLExpression(URI uri);

	public ForEach createForEach(URI uri);

	public IfThenElse createIfThenElse(URI uri);

	public Input createInput(URI uri);

	public InputBinding createInputBinding(URI uri);

	public Existential createExistential(URI uri);

	public Loc createLoc(URI uri);

	public LocBinding createLocBinding(URI uri);

	public Link createLink(URI uri);

	public LinkBinding createLinkBinding(URI uri);

	public Participant createParticipant(URI uri);

	public Output createOutput(URI uri);

	public OutputBinding createOutputBinding(URI uri);

	public Perform createPerform(URI uri);

	public Produce createProduce(URI uri);

	public Profile createProfile(URI uri);

	public RepeatUntil createRepeatUntil(URI uri);

	public RepeatWhile createRepeatWhile(URI uri);

	public Result createResult(URI uri);

	public Sequence createSequence(URI uri);

	public Service createService(URI uri);

	public ServiceCategory createServiceCategory(URI uri);

	public ServiceParameter createServiceParameter(URI uri);

	public Split createSplit(URI uri);

	public SplitJoin createSplitJoin(URI uri);

	public JavaAtomicGrounding createJavaAtomicGrounding(URI uri);
	public JavaGrounding createJavaGrounding(URI uri);
	public JavaParameter createJavaParameter(final URI uri);
	public JavaVariable createJavaVariable(final URI uri);

	public UPnPAtomicGrounding createUPnPAtomicGrounding(URI uri);
	public UPnPGrounding createUPnPGrounding(URI uri);
	public MessageMap<String> createUPnPMessageMap(URI uri);

	public WSDLAtomicGrounding createWSDLAtomicGrounding(URI uri);
	public WSDLGrounding createWSDLGrounding(URI uri);
	public WSDLOperationRef createWSDLOperationRef(URI uri);
	public MessageMap<String> createWSDLInputMessageMap(URI uri);
	public MessageMap<String> createWSDLOutputMessageMap(URI uri);


	public ValueConstant createValueConstant(OWLValue constantValue, Binding<?> enclosingBinding);

	public ValueFunction<Expression.SPARQL> createSPARQLValueFunction(Expression.SPARQL expression, Binding<?> enclosingBinding);

	public ValueOf createValueOf(URI uri);

	public <V extends OWLValue> OWLList<V> createList(ListVocabulary<V> vocabulary);

	public <V extends OWLValue> OWLList<V> createList(ListVocabulary<V> vocabulary, V item);

	public <V extends OWLValue> OWLList<V> createList(ListVocabulary<V> vocabulary, List<V> items);

	/**
	 * Turn the XML/RDF representation into an OWLIndividual assuming that
	 * the given string is not a valid XML document but just a fragment.
	 * It must not start with XML processing instructions, must not contain
	 * any document type declaration, nor may the fragment be enclosed by the
	 * root <code>&lt;rdf:RDF&gt;</code> tag.
	 * <p>
	 * The XML/RDF representation should use absolute URIs as identifiers. In
	 * case it contains relative URIs then they will be interpreted relative to
	 * the URI of the {@link OWLKnowledgeBase#getBaseModel() base ontology}
	 * if this model is actually a knowledge base, or it is interpreted relative
	 * to the {@link OWLOntology#getURI() ontology URI} if this model is a
	 * ontology.
	 * <p>
	 * The XML/RDF representation may represent a tree but must not be forest,
	 * i.e., there should be exactly one individual that never occurs as the
	 * subject of some statement but only as the object. This is important since
	 * it is the criteria to determine the individual which will be returned.
	 * <p>
	 * All statements of the given fragment will be added to this model.
	 *
	 * @param literal The XML/RDF serialized individual.
	 * @return The individual or <code>null</code> in case parsing failed
	 * 	for whatever reason.
	 */
	public OWLIndividual parseLiteral(String literal);

	/**
	 * @param ind1 Some individual (part of this model).
	 * @param ind2 Another individual (part of this model).
	 * @return <code>true</code> if this model entails the two individuals to
	 * 	be the same as defined by the semantics of the OWL
	 * 	<code>owl:sameAs</code> property.
	 */
	public boolean isSameAs(OWLIndividual ind1, OWLIndividual ind2);

	public OWLIndividualList<?> getSameIndividuals(OWLIndividual ind);

	/**
	 * @param ind1 Some individual (part of this model).
	 * @param ind2 Another individual (part of this model).
	 * @return <code>true</code> if this model entails the two individuals
	 * 	to be different from each other as defined by the semantics of the
	 * 	OWL <code>owl:differentFrom</code> property.
	 */
	public boolean isDifferentFrom(OWLIndividual ind1, OWLIndividual ind2);

	public OWLIndividualList<?> getDifferentIndividuals(OWLIndividual ind);

	public OWLWriter getWriter();
	public void write(Writer writer, URI baseURI);
	public void write(OutputStream out, URI baseURI);

	/**
    * Returns the number of statements in a this model. If a reasoner is
    * attached, it will return an estimated lower bound for the number of
    * statements in the model but it is possible for a subsequent retrieval
    * on such a model to discover more statements than size() indicated.
    *
    * @return The number of statements in this model or an estimated lower
    * 	bound on the number of statements if a reasoner is bound.
    */
	public long size();

	/**
	 * Returns a list with all individuals that do not belong to the OWL-S language
	 * definition and its base and helpers languages such as OWL, RDF or SWRL.
	 *
	 * @return all individuals do not belong to the OWL-S language definition
	 */
	public List<OWLIndividual> getNonLanguageIndividuals();

	/**
	 * Returns a list with all named classes (i.e. resources with
	 * <tt>rdf:type owl:Class</tt> (or equivalent) and a node URI) that do not
	 * belong to the OWL-S language definition and its base and helpers languages
	 * such as OWL, RDF or SWRL.
	 *
	 * @return all classes do not belong to the OWL-S language definition
	 */
	public List<OWLClass> getNonLanguageClasses();
	/**
	 * Returns a list with all object properties that do not belong to the OWL-S language
	 * definition and its base and helpers languages such as OWL, RDF or SWRL.
	 *
	 * @return all object properties do not belong to the OWL-S language definition
	 */
	public List<OWLObjectProperty> getNonLanguageObjectProperties();
	/**
	 * Returns a list with all datatype properties that do not belong to the OWL-S language
	 * definition and its base and helpers languages such as OWL, RDF or SWRL.
	 *
	 * @return all datatype do not belong to the OWL-S language definition
	 */
	public List<OWLDataProperty> getNonLanguageDataProperties();

}
