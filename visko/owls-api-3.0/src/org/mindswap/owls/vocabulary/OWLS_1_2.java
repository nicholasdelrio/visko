//The MIT License
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

package org.mindswap.owls.vocabulary;

import impl.owl.WrappedIndividual;
import impl.owls.expression.LogicLanguageImpl;
import impl.owls.process.constructs.PerformImpl;
import impl.owls.process.variable.ParticipantImpl;

import java.util.List;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.SingleClosableIterator;
import org.mindswap.common.Variable;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.vocabulary.Vocabulary;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.LogicLanguage;
import org.mindswap.owls.io.ProcessWriter;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.variable.Participant;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.URIUtils;

/**
 * This class defines constants for each element of the common vocabulary
 * OWL-S, version 1.2.
 *
 * @author unascribed
 * @see <a href="http://www.ai.sri.com/daml/services/owl-s/1.2/">OWL-S 1.2</a>
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class OWLS_1_2 extends Vocabulary
{
	public static final String base = OWLS_1_0.base;
	public static final String version = "1.2";
	public static final String URI = base + version + "/";

	//	The following URIs are not declared in inner classes because we want to prevent rather costly
	// initialization of ontologies if only the URI is referenced but none of the vocabulary constants.
	public static final String ACTOR_NS = URI + "ActorDefault.owl#";
	public static final String EXPRESSION_NS = URI + "generic/Expression.owl#";
	public static final String GROUNDING_NS = URI + "Grounding.owl#";
	public static final String LIST_NS = URI + "generic/ObjectList.owl#";
	public static final String PROCESS_NS = URI + "Process.owl#";
	public static final String PROFILE_NS = URI + "Profile.owl#";
	public static final String PROFILE_ADDITIONAL_NS = URI + "ProfileAdditionalParameters.owl#";
	public static final String SERVICE_NS = URI + "Service.owl#";
	public static final String SERVICE_CATEGORY_NS = URI + "ServiceCategory.owl#";
	public static final String SERVICE_PARAMETER_NS = URI + "ServiceParameter.owl#";

	// helpers - do not directly belong to OWL-S but are imported by some of the ontologies
	public static final String SWRLX_NS = URI + "generic/swrlx.owl#";
	public static final String TIME_NS = "http://www.isi.edu/~pan/damltime/time-entry.owl#";


	public static abstract class ObjectList
	{
		public static final ListVocabulary<OWLIndividual> List;

		static
		{
			final OWLOntology ont = loadOntology(LIST_NS);
			List = new ListVocabulary<OWLIndividual>(URIUtils.createURI(LIST_NS), ont, OWLIndividual.class);
		}
	}

	public static abstract class Service
	{
		public static final OWLClass Service;
		public static final OWLClass ServiceProfile;
		public static final OWLClass ServiceModel;
		public static final OWLClass ServiceGrounding;

		public static final OWLObjectProperty presentedBy;
		public static final OWLObjectProperty presents;
		public static final OWLObjectProperty describedBy;
		public static final OWLObjectProperty describes;
		public static final OWLObjectProperty supportedBy;
		public static final OWLObjectProperty supports;
		public static final OWLObjectProperty providedBy;
		public static final OWLObjectProperty provides;

		static
		{
			final OWLOntology ont = loadOntology(SERVICE_NS);

			Service = ont.getClass(URIUtils.createURI(SERVICE_NS + "Service"));
			ServiceProfile = ont.getClass(URIUtils.createURI(SERVICE_NS + "ServiceProfile"));
			ServiceModel = ont.getClass(URIUtils.createURI(SERVICE_NS + "ServiceModel"));
			ServiceGrounding = ont.getClass(URIUtils.createURI(SERVICE_NS + "ServiceGrounding"));

			presentedBy = ont.getObjectProperty(URIUtils.createURI(SERVICE_NS + "presentedBy"));
			presents    = ont.getObjectProperty(URIUtils.createURI(SERVICE_NS + "presents"));
			describedBy = ont.getObjectProperty(URIUtils.createURI(SERVICE_NS + "describedBy"));
			describes   = ont.getObjectProperty(URIUtils.createURI(SERVICE_NS + "describes"));
			supportedBy = ont.getObjectProperty(URIUtils.createURI(SERVICE_NS + "supportedBy"));
			supports    = ont.getObjectProperty(URIUtils.createURI(SERVICE_NS + "supports"));
			providedBy  = ont.getObjectProperty(URIUtils.createURI(SERVICE_NS + "providedBy"));
			provides    = ont.getObjectProperty(URIUtils.createURI(SERVICE_NS + "provides"));
		}
	}

	public static abstract class ServiceCategory
	{
		public static final OWLClass ServiceCategory;
		public static final OWLObjectProperty serviceCategory;
		public static final OWLDataProperty categoryName;
		public static final OWLDataProperty taxonomy;
		public static final OWLDataProperty value;
		public static final OWLDataProperty code;

		public static final OWLDataProperty serviceClassification;

		static
		{
			final OWLOntology ont = loadOntology(SERVICE_CATEGORY_NS);

			ServiceCategory = ont.getClass(URIUtils.createURI(SERVICE_CATEGORY_NS + "ServiceCategory"));
			serviceCategory = ont.getObjectProperty(URIUtils.createURI(SERVICE_CATEGORY_NS + "serviceCategory"));
			categoryName    = ont.getDataProperty(URIUtils.createURI(SERVICE_CATEGORY_NS + "categoryName"));
			taxonomy        = ont.getDataProperty(URIUtils.createURI(SERVICE_CATEGORY_NS + "taxonomy"));
			value           = ont.getDataProperty(URIUtils.createURI(SERVICE_CATEGORY_NS + "value"));
			code            = ont.getDataProperty(URIUtils.createURI(SERVICE_CATEGORY_NS + "code"));

			serviceClassification = ont.getDataProperty(URIUtils.createURI(SERVICE_CATEGORY_NS + "serviceClassification"));
		}
	}

	public static abstract class ServiceParameter
	{
		public static final OWLClass ServiceParameter;
		public static final OWLObjectProperty serviceParameter;
		public static final OWLDataProperty serviceParameterName;
		public static final OWLObjectProperty sParameter;

		static
		{
			final OWLOntology ont = loadOntology(SERVICE_PARAMETER_NS);

			ServiceParameter = ont.getClass(URIUtils.createURI(SERVICE_PARAMETER_NS + "ServiceParameter"));
			serviceParameter = ont.getObjectProperty(URIUtils.createURI(SERVICE_PARAMETER_NS + "serviceParameter"));
			serviceParameterName = ont.getDataProperty(URIUtils.createURI(SERVICE_PARAMETER_NS + "serviceParameterName"));
			sParameter       = ont.getObjectProperty(URIUtils.createURI(SERVICE_PARAMETER_NS + "sParameter"));
		}
	}

	public static abstract class Profile
	{
		public static final OWLClass Profile;

		public static final OWLDataProperty serviceName;
		public static final OWLDataProperty textDescription;

		public static final OWLObjectProperty hasProcess;

		public static final OWLObjectProperty hasInput;
		public static final OWLObjectProperty hasOutput;
		public static final OWLObjectProperty hasPrecondition;
		public static final OWLObjectProperty hasParameter;
		public static final OWLObjectProperty hasResult;

		public static final OWLObjectProperty contactInformation;

		static
		{
			final OWLOntology ont = loadOntology(PROFILE_NS);

			Profile         = ont.getClass(URIUtils.createURI(PROFILE_NS + "Profile"));
			serviceName     = ont.getDataProperty(URIUtils.createURI(PROFILE_NS + "serviceName"));
			textDescription = ont.getDataProperty(URIUtils.createURI(PROFILE_NS + "textDescription"));

			hasProcess      = ont.getObjectProperty(URIUtils.createURI(PROFILE_NS + "has_process"));
			hasInput        = ont.getObjectProperty(URIUtils.createURI(PROFILE_NS + "hasInput"));
			hasOutput       = ont.getObjectProperty(URIUtils.createURI(PROFILE_NS + "hasOutput"));
			hasPrecondition = ont.getObjectProperty(URIUtils.createURI(PROFILE_NS + "hasPrecondition"));
			hasParameter    = ont.getObjectProperty(URIUtils.createURI(PROFILE_NS + "hasParameter"));

			hasResult = ont.getObjectProperty(URIUtils.createURI(PROFILE_NS + "hasResult"));

			contactInformation = ont.getObjectProperty(URIUtils.createURI(PROFILE_NS + "contactInformation"));
		}
	}

	public static abstract class ProfileAdditional
	{
		public static final OWLClass NAICS;
		public static final OWLClass UNSPSC;
		public static final OWLClass MaxResponseTime;
		public static final OWLClass AverageResponseTime;
		public static final OWLClass GeographicRadius;
		public static final OWLClass Duration;

		static
		{
			final OWLOntology ont = loadOntology(PROFILE_ADDITIONAL_NS);

			NAICS = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_NS + "NAICS"));
			UNSPSC = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_NS + "UNSPSC"));
			MaxResponseTime = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_NS + "MaxResponseTime"));
			AverageResponseTime = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_NS + "AverageResponseTime"));
			GeographicRadius = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_NS + "GeographicRadius"));
			Duration = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_NS + "Duration"));
		}
	}

	public static abstract class Process
	{
		public static final OWLClass Process;
		public static final OWLClass AtomicProcess;
		public static final OWLClass CompositeProcess;
		public static final OWLClass SimpleProcess;
		public static final OWLClass ProcessVar;
		public static final OWLClass Parameter;
		public static final OWLClass Input;
		public static final OWLClass Output;
		public static final OWLClass Existential;

		public static final OWLDataProperty parameterType;
		public static final OWLObjectProperty hasVar;
		public static final OWLObjectProperty hasParameter;
		public static final OWLObjectProperty hasInput;
		public static final OWLObjectProperty hasOutput;
		public static final OWLObjectProperty hasExistential;
		public static final OWLObjectProperty hasPrecondition;

		public static final OWLClass Participant;
		public static final OWLObjectProperty hasParticipant;
		public static final OWLObjectProperty hasClient;
		public static final OWLObjectProperty performedBy;
		public static final Participant TheClient;
		public static final Participant TheServer;

		public static final OWLDataProperty name;

		public static final OWLClass AsProcess;
		public static final OWLClass Set;
		public static final OWLClass ControlConstruct;
		public static final OWLClass ControlConstructList;
		public static final OWLClass ControlConstructBag;
		public static final OWLClass Sequence;
		public static final OWLClass AnyOrder;
		public static final OWLClass Choice;
		public static final OWLClass IfThenElse;
		public static final OWLClass Produce;
		public static final OWLClass Split;
		public static final OWLClass SplitJoin;
		public static final OWLClass Iterate;
		public static final OWLClass RepeatUntil;
		public static final OWLClass RepeatWhile;

		/**
		 * This is not a standard part of OWL-S 1.2
		 */
		public static final OWLClass ForEach;
		/**
		 * This is not a standard part of OWL-S 1.2. Supposed to be used in
		 * conjunction with {@link #ForEach} construct.
		 */
		public static final OWLObjectProperty theList;
		/**
		 * This is not a standard part of OWL-S 1.2. Supposed to be used in
		 * conjunction with {@link #ForEach} construct.
		 */
		public static final OWLObjectProperty theLoopVar;
		/**
		 * This is not a standard part of OWL-S 1.2. Supposed to be used in
		 * conjunction with {@link #ForEach} construct.
		 */
		public static final OWLObjectProperty iterateBody;

		public static final OWLClass ValueOf;

		public static final OWLObjectProperty composedOf;
		public static final OWLDataProperty invocable;
		public static final OWLObjectProperty computedInput;
		public static final OWLObjectProperty computedOutput;
		public static final OWLObjectProperty computedPrecondition;
		public static final OWLObjectProperty computedEffect;

		public static final OWLObjectProperty components;
		public static final OWLObjectProperty ifCondition;
		public static final OWLObjectProperty thenP;
		public static final OWLObjectProperty elseP;
		public static final OWLObjectProperty untilProcess;
		public static final OWLObjectProperty untilCondition;
		public static final OWLObjectProperty whileProcess;
		public static final OWLObjectProperty whileCondition;
		public static final OWLObjectProperty producedBinding;
		public static final OWLObjectProperty realizedBy;
		public static final OWLObjectProperty realizes;
		public static final OWLObjectProperty expandsTo;
		public static final OWLObjectProperty collapsesTo;
		public static final OWLObjectProperty setBinding;
		public static final OWLObjectProperty timeout;

		public static final OWLClass Perform;
		public static final OWLObjectProperty process;
		public static final OWLObjectProperty hasDataFrom;
		public static final OWLClass Binding;
		public static final OWLClass InputBinding;
		public static final OWLClass OutputBinding;

		public static final OWLDataProperty parameterValue;

		public static final OWLObjectProperty hasResult;
		public static final OWLClass Result;
		public static final OWLClass ResultVar;
		public static final OWLObjectProperty hasResultVar;

		public static final OWLClass Local;
		public static final OWLClass Loc;
		public static final OWLClass LocBinding;
		public static final OWLClass Link;
		public static final OWLClass LinkBinding;
		public static final OWLObjectProperty hasLocal;
		public static final OWLDataProperty initialValue;

		public static final OWLObjectProperty inCondition;
		public static final OWLObjectProperty hasEffect;
		public static final OWLObjectProperty toVar;
		public static final OWLObjectProperty withOutput;
		public static final OWLObjectProperty valueSource;
		public static final OWLObjectProperty fromProcess;
		public static final OWLObjectProperty theVar;
		public static final OWLObjectProperty theParam;

		public static final OWLDataProperty valueSpecifier;
		public static final OWLDataProperty valueFunction;
		public static final OWLDataProperty valueForm;
		public static final OWLDataProperty valueData;
		public static final OWLDataProperty valueType;

		/**
		 * Supposed to be used
		 * whenever some Binding should refer to some (constant) individual, which
		 * is to be used as the value for the parameter. Since it is declared as
		 * an object property it is not similar to {@link #valueData}. The latter
		 * represents a constant value by means of an XML literal (independent of
		 * the syntax and semantics if this literal), whereas this property refers
		 * to some OWL individual.
		 */
		public static final OWLObjectProperty valueObject;

		/**
		 * A special-purpose object, used to refer, at runtime, to the execution
		 * instance of the enclosing process whose definition it occurs in.
		 */
		public static final Perform ThisPerform;

		public static final ListVocabulary<ControlConstruct> CCList;
		public static final ListVocabulary<ControlConstruct> CCBag;

		static
		{
			final OWLOntology ont = loadOntology(PROCESS_NS);

			Process          = ont.getClass(URIUtils.createURI(PROCESS_NS + "Process"));
			AtomicProcess    = ont.getClass(URIUtils.createURI(PROCESS_NS + "AtomicProcess"));
			CompositeProcess = ont.getClass(URIUtils.createURI(PROCESS_NS + "CompositeProcess"));
			SimpleProcess    = ont.getClass(URIUtils.createURI(PROCESS_NS + "SimpleProcess"));
			ProcessVar       = ont.getClass(URIUtils.createURI(PROCESS_NS + "ProcessVar"));
			Parameter        = ont.getClass(URIUtils.createURI(PROCESS_NS + "Parameter"));
			Input            = ont.getClass(URIUtils.createURI(PROCESS_NS + "Input"));
			Output           = ont.getClass(URIUtils.createURI(PROCESS_NS + "Output"));
			Existential      = ont.getClass(URIUtils.createURI(PROCESS_NS + "Existential"));

			parameterType   = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "parameterType"));
			hasVar          = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasVar"));
			hasParameter    = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasParameter"));
			hasInput        = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasInput"));
			hasOutput       = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasOutput"));
			hasExistential  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasExistential"));
			hasPrecondition = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasPrecondition"));

			name			= ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "name"));

			Participant    = ont.getClass(URIUtils.createURI(PROCESS_NS + "Participant"));
			hasParticipant = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasParticipant"));
			hasClient      = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasClient"));
			performedBy    = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "performedBy"));
			TheClient      = new ParticipantImpl(ont.getIndividual(URIUtils.createURI(PROCESS_NS + "TheClient")));
			TheServer      = new ParticipantImpl(ont.getIndividual(URIUtils.createURI(PROCESS_NS + "TheServer")));

			ControlConstructList = ont.getClass(URIUtils.createURI(PROCESS_NS + "ControlConstructList"));
			ControlConstructBag  = ont.getClass(URIUtils.createURI(PROCESS_NS + "ControlConstructBag"));
			ControlConstruct = ont.getClass(URIUtils.createURI(PROCESS_NS + "ControlConstruct"));
			AsProcess        = ont.getClass(URIUtils.createURI(PROCESS_NS + "AsProcess"));
			Set              = ont.getClass(URIUtils.createURI(PROCESS_NS + "Set"));
			Sequence         = ont.getClass(URIUtils.createURI(PROCESS_NS + "Sequence"));
			AnyOrder         = ont.getClass(URIUtils.createURI(PROCESS_NS + "Any-Order"));
			Choice           = ont.getClass(URIUtils.createURI(PROCESS_NS + "Choice"));
			IfThenElse       = ont.getClass(URIUtils.createURI(PROCESS_NS + "If-Then-Else"));
			Produce          = ont.getClass(URIUtils.createURI(PROCESS_NS + "Produce"));
			Split            = ont.getClass(URIUtils.createURI(PROCESS_NS + "Split"));
			SplitJoin        = ont.getClass(URIUtils.createURI(PROCESS_NS + "Split-Join"));
			Iterate          = ont.getClass(URIUtils.createURI(PROCESS_NS + "Iterate"));
			RepeatUntil      = ont.getClass(URIUtils.createURI(PROCESS_NS + "Repeat-Until"));
			RepeatWhile      = ont.getClass(URIUtils.createURI(PROCESS_NS + "Repeat-While"));

			ForEach     = ont.getClass(URIUtils.createURI(PROCESS_NS + "For-Each"));
			theList     = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "theList"));
			theLoopVar  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "theLoopVar"));
			iterateBody = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "iterateBody"));

			ValueOf          = ont.getClass(URIUtils.createURI(PROCESS_NS + "ValueOf"));

			composedOf     = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "composedOf"));
			invocable      = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "invocable"));
			computedInput  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "computedInput"));
			computedOutput = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "computedOutput"));
			computedPrecondition = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "computedPrecondition"));
			computedEffect = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "computedEffect"));

			components      = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "components"));
			ifCondition     = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "ifCondition"));
			thenP           = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "then"));
			elseP           = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "else"));
			untilProcess    = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "untilProcess"));
			untilCondition  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "untilCondition"));
			whileProcess    = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "whileProcess"));
			whileCondition  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "whileCondition"));
			producedBinding = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "producedBinding"));
			realizedBy      = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "realizedBy"));
			realizes        = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "realizes"));
			expandsTo       = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "expandsTo"));
			collapsesTo     = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "collapsesTo"));
			setBinding      = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "setBinding"));
			timeout         = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "timeout"));

			Perform       = ont.getClass(URIUtils.createURI(PROCESS_NS + "Perform"));
			process       = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "process"));
			hasDataFrom   = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasDataFrom"));
			Binding       = ont.getClass(URIUtils.createURI(PROCESS_NS + "Binding"));
			InputBinding  = ont.getClass(URIUtils.createURI(PROCESS_NS + "InputBinding"));
			OutputBinding = ont.getClass(URIUtils.createURI(PROCESS_NS + "OutputBinding"));

			parameterValue = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "parameterValue"));

			hasResult = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasResult"));
			Result = ont.getClass(URIUtils.createURI(PROCESS_NS + "Result"));
			ResultVar = ont.getClass(URIUtils.createURI(PROCESS_NS + "ResultVar"));
			hasResultVar = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasResultVar"));

			Local        = ont.getClass(URIUtils.createURI(PROCESS_NS + "Local"));
			Loc          = ont.getClass(URIUtils.createURI(PROCESS_NS + "Loc"));
			LocBinding   = ont.getClass(URIUtils.createURI(PROCESS_NS + "LocBinding"));
			Link         = ont.getClass(URIUtils.createURI(PROCESS_NS + "Link"));
			LinkBinding  = ont.getClass(URIUtils.createURI(PROCESS_NS + "LinkBinding"));
			hasLocal     = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasLocal"));
			initialValue = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "initialValue"));

			inCondition  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "inCondition"));
			hasEffect    = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasEffect"));
			toVar        = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "toVar"));
			withOutput   = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "withOutput"));
			valueSource  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "valueSource"));
			fromProcess  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "fromProcess"));
			theVar       = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "theVar"));
			theParam     = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "theParam"));

			valueSpecifier = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "valueSpecifier"));
			valueFunction  = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "valueFunction"));
			valueForm      = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "valueForm"));
			valueData      = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "valueData"));
         valueObject    = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "valueObject"));
         valueType      = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "valueType"));

			// Retrieval of the ThisPerfom individual MUST come AFTER creating the two specialized
         // lists because the method castTo(..) will indirectly cause initialization of all
         // OWLObjectConverters. The class OWLSConverters initializes several ListConverter(s),
         // one for CCList and one for CCBag. They do access those fields and would throw NPEs
         // if not yet assigned.
         CCList = ObjectList.List.specialize(ControlConstructList, ControlConstruct.class);
         CCBag = ObjectList.List.specialize(ControlConstructBag, ControlConstruct.class);

         // We must not use OWLObject.castTo(..) here because this would introduce a circular
         // dependency with class OWLSConverters! But going the direct way and instantiate the
         // perform directly is not dangerous because we know that the individuals are Performs.
			ThisPerform = new PerformImpl(ont.getIndividual(URIUtils.createURI(PROCESS_NS + "ThisPerform")));
		}
	}

	public static abstract class Grounding
	{
		public static final OWLClass Grounding;
		public static final OWLClass AtomicProcessGrounding;
		public static final OWLClass MessageMap;
		public static final OWLClass InputMessageMap;
		public static final OWLClass OutputMessageMap;

		public static final OWLObjectProperty hasAtomicProcessGrounding;
		public static final OWLObjectProperty owlsProcess;

		public static final OWLClass WsdlGrounding;
		public static final OWLClass WsdlAtomicProcessGrounding;
		public static final OWLObjectProperty wsdlOperation;
		public static final OWLClass WsdlOperationRef;
		public static final OWLDataProperty portType;
		public static final OWLDataProperty operation;
		public static final OWLDataProperty wsdlPort;
		public static final OWLDataProperty wsdlVersion;
		public static final OWLDataProperty wsdlDocument;
		public static final OWLDataProperty wsdlService;

		public static final OWLDataProperty wsdlInputMessage;
		public static final OWLObjectProperty wsdlInput;

		public static final OWLDataProperty wsdlOutputMessage;
		public static final OWLObjectProperty wsdlOutput;

		public static final OWLClass WsdlMessageMap;
		public static final OWLClass WsdlInputMessageMap;
		public static final OWLClass WsdlOutputMessageMap;
		public static final OWLClass DirectInputMessageMap;
		public static final OWLClass DirectOutputMessageMap;
		public static final OWLClass XSLTInputMessageMap;
		public static final OWLClass XSLTOutputMessageMap;
		public static final OWLDataProperty wsdlMessagePart;
		public static final OWLObjectProperty owlsParameter;
		public static final OWLDataProperty xsltTransformation;
		public static final OWLDataProperty xsltTransformationString;
		public static final OWLDataProperty xsltTransformationURI;

		static
		{
			final OWLOntology ont = loadOntology(GROUNDING_NS);

			Grounding                  = ont.getClass(URIUtils.createURI(GROUNDING_NS + "Grounding"));
			AtomicProcessGrounding     = ont.getClass(URIUtils.createURI(GROUNDING_NS + "AtomicProcessGrounding"));
			MessageMap                 = ont.getClass(URIUtils.createURI(GROUNDING_NS + "MessageMap"));
			InputMessageMap            = ont.getClass(URIUtils.createURI(GROUNDING_NS + "InputMessageMap"));
			OutputMessageMap           = ont.getClass(URIUtils.createURI(GROUNDING_NS + "OutputMessageMap"));
			WsdlGrounding              = ont.getClass(URIUtils.createURI(GROUNDING_NS + "WsdlGrounding"));
			WsdlAtomicProcessGrounding = ont.getClass(URIUtils.createURI(GROUNDING_NS + "WsdlAtomicProcessGrounding"));
			WsdlOperationRef           = ont.getClass(URIUtils.createURI(GROUNDING_NS + "WsdlOperationRef"));

			hasAtomicProcessGrounding = ont.getObjectProperty(URIUtils.createURI(GROUNDING_NS + "hasAtomicProcessGrounding"));
			wsdlVersion       = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "wsdlVersion"));
			wsdlDocument      = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "wsdlDocument"));
			wsdlService       = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "wsdlService"));
			wsdlOperation     = ont.getObjectProperty(URIUtils.createURI(GROUNDING_NS + "wsdlOperation"));
			wsdlPort          = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "wsdlPort"));
			portType          = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "portType"));
			operation         = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "operation"));
			owlsProcess       = ont.getObjectProperty(URIUtils.createURI(GROUNDING_NS + "owlsProcess"));
			wsdlInputMessage  = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "wsdlInputMessage"));
			wsdlOutputMessage = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "wsdlOutputMessage"));
			wsdlInput         = ont.getObjectProperty(URIUtils.createURI(GROUNDING_NS + "wsdlInput"));
			wsdlOutput        = ont.getObjectProperty(URIUtils.createURI(GROUNDING_NS + "wsdlOutput"));

			WsdlMessageMap           = ont.getClass(URIUtils.createURI(GROUNDING_NS + "WsdlMessageMap"));
			WsdlInputMessageMap      = ont.getClass(URIUtils.createURI(GROUNDING_NS + "WsdlInputMessageMap"));
			WsdlOutputMessageMap     = ont.getClass(URIUtils.createURI(GROUNDING_NS + "WsdlOutputMessageMap"));
			DirectInputMessageMap    = ont.getClass(URIUtils.createURI(GROUNDING_NS + "DirectInputMessageMap"));
			DirectOutputMessageMap   = ont.getClass(URIUtils.createURI(GROUNDING_NS + "DirectOutputMessageMap"));
			XSLTInputMessageMap      = ont.getClass(URIUtils.createURI(GROUNDING_NS + "XSLTInputMessageMap"));
			XSLTOutputMessageMap     = ont.getClass(URIUtils.createURI(GROUNDING_NS + "XSLTOutputMessageMap"));
			wsdlMessagePart          = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "wsdlMessagePart"));
			owlsParameter            = ont.getObjectProperty(URIUtils.createURI(GROUNDING_NS + "owlsParameter"));
			xsltTransformation       = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "xsltTransformation"));
			xsltTransformationString = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "xsltTransformationString"));
			xsltTransformationURI    = ont.getDataProperty(URIUtils.createURI(GROUNDING_NS + "xsltTransformationURI"));
		}
	}

	public static abstract class Expression
	{
		public static final OWLClass LogicLanguage;
		public static final OWLClass Expression;
		public static final OWLClass Condition;
		public static final OWLClass QuotedExpression;
		public static final OWLClass UnquotedExpression;
		public static final OWLClass VariableBinding;

		public static final LogicLanguage KIF;
		public static final LogicLanguage SWRL;
		public static final LogicLanguage DRS;
		public static final LogicLanguage SPARQL;
		public static final LogicLanguage RDQL;
		public static final LogicLanguage SWRL_FOL;

		public static final OWLClass KIF_Condition;
		public static final OWLClass SWRL_Condition;
		public static final OWLClass DRS_Condition;
		public static final OWLClass SPARQL_Condition;
		public static final OWLClass RDQL_Condition;
		public static final OWLClass SWRL_FOL_Condition;

		public static final OWLClass KIF_Expression;
		public static final OWLClass SWRL_Expression;
		public static final OWLClass DRS_Expression;
		public static final OWLClass SPARQL_Expression;
		public static final OWLClass RDQL_Expression;
		public static final OWLClass SWRL_FOL_Expression;

		public static final OWLDataProperty refURI;
		public static final OWLObjectProperty expressionLanguage;
		public static final OWLDataProperty expressionData;
		/** @deprecated Exists for backward compatibility. Use equivalent property {@link #expressionData} instead. */
		@Deprecated public static final OWLDataProperty expressionBody;
		public static final OWLObjectProperty expressionObject;
		public static final OWLObjectProperty variableBinding;
		public static final OWLDataProperty theVariable;
		public static final OWLObjectProperty theObject;

		public static final Condition<?> AlwaysTrue;

		static
		{
			final OWLOntology ont = loadOntology(EXPRESSION_NS);

			LogicLanguage      = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "LogicLanguage"));
			Expression         = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "Expression"));
			Condition          = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "Condition"));
			QuotedExpression   = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "QuotedExpression"));
			UnquotedExpression = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "UnquotedExpression"));
			VariableBinding    = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "VariableBinding"));

			expressionData     = ont.getDataProperty(URIUtils.createURI(EXPRESSION_NS + "expressionData"));
			expressionLanguage = ont.getObjectProperty(URIUtils.createURI(EXPRESSION_NS + "expressionLanguage"));
			expressionBody     = ont.getDataProperty(URIUtils.createURI(EXPRESSION_NS + "expressionBody"));
			expressionObject   = ont.getObjectProperty(URIUtils.createURI(EXPRESSION_NS + "expressionObject"));
			refURI             = ont.getDataProperty(URIUtils.createURI(EXPRESSION_NS + "refURI"));
			variableBinding    = ont.getObjectProperty(URIUtils.createURI(EXPRESSION_NS + "variableBinding"));
			theVariable        = ont.getDataProperty(URIUtils.createURI(EXPRESSION_NS + "theVariable"));
			theObject          = ont.getObjectProperty(URIUtils.createURI(EXPRESSION_NS + "theObject"));

			KIF_Condition      = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "KIF-Condition"));
			SWRL_Condition     = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "SWRL-Condition"));
			DRS_Condition      = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "DRS-Condition"));
			SPARQL_Condition   = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "SPARQL-Condition"));
			RDQL_Condition     = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "RDQL-Condition"));
			SWRL_FOL_Condition = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "SWRL-FOL-Condition"));

			KIF_Expression      = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "KIF-Expression"));
			SWRL_Expression     = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "SWRL-Expression"));
			DRS_Expression      = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "DRS-Expression"));
			SPARQL_Expression   = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "SPARQL-Expression"));
			RDQL_Expression     = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "RDQL-Expression"));
			SWRL_FOL_Expression = ont.getClass(URIUtils.createURI(EXPRESSION_NS + "SWRL-FOL-Expression"));

			KIF      = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_NS + "KIF")));
			SWRL     = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_NS + "SWRL")));
			DRS      = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_NS + "DRS")));
			SPARQL   = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_NS + "SPARQL")));
			RDQL     = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_NS + "RDQL")));
			SWRL_FOL = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_NS + "SWRL-FOL")));

			AlwaysTrue = new AlwaysTrue(ont.getIndividual(URIUtils.createURI(EXPRESSION_NS + "AlwaysTrue")));

		}

		/** Singleton class only required to realize the special AlwaysTrue condition. */
		static final class AlwaysTrue extends WrappedIndividual implements Condition<String>
		{
			AlwaysTrue(final OWLIndividual ind) { super(ind); }
			public void evaluate(final ValueMap<? extends Variable, ? extends OWLValue> bindings) { /* nothing to do */ }
			public boolean isTrue(final ValueMap<? extends Variable, ? extends OWLValue> binding) { return true; }
			public boolean isTrue(final OWLModel model, final ValueMap<? extends Variable, ? extends OWLValue> binding) { return true; }
			public String getBody() { /* has no body */ return null; }
			public LogicLanguage getLanguage() { /* has no language */ return null; }
			public void setBody(final String body) { /* ignore */ }
			public <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> solutions(
				final ValueMap<? extends Variable, ? extends OWLValue> binding, final List<V> solutionVars)
			{
				return SingleClosableIterator.<V>oneElement();
			}
			public <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> solutions(final OWLModel model,
				final ValueMap<? extends Variable, ? extends OWLValue> binding, final List<V> solutionVars)
			{
				return SingleClosableIterator.<V>oneElement();
			}
			public void writeTo(final ProcessWriter writer, final String indent)
			{  /* we slightly abuse the SPARQL writer here, which is OK because SPARQL expressions are Strings. */
				writer.getSparqlExpressionWriter().write(this);
			}
		}
	}

	/**
	 * Vocabulary for the Actor ontology.
	 */
	public static abstract class Actor
	{
		public static final OWLClass Actor;
		public static final OWLDataProperty email;
		public static final OWLDataProperty fax;
		public static final OWLDataProperty name;
		public static final OWLDataProperty phone;
		public static final OWLDataProperty physicalAddress;
		public static final OWLDataProperty title;
		public static final OWLDataProperty webURL;

		static
		{
			final OWLOntology ont = loadOntology(ACTOR_NS);

			Actor = ont.getClass(URIUtils.createURI(ACTOR_NS + "Actor"));
			email = ont.getDataProperty(URIUtils.createURI(ACTOR_NS + "email"));
			fax = ont.getDataProperty(URIUtils.createURI(ACTOR_NS + "fax"));
			name = ont.getDataProperty(URIUtils.createURI(ACTOR_NS + "name"));
			phone = ont.getDataProperty(URIUtils.createURI(ACTOR_NS + "phone"));
			physicalAddress = ont.getDataProperty(URIUtils.createURI(ACTOR_NS + "physicalAddress"));
			title = ont.getDataProperty(URIUtils.createURI(ACTOR_NS + "title"));
			webURL = ont.getDataProperty(URIUtils.createURI(ACTOR_NS + "webURL"));
		}
	}
}
