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

import impl.owls.expression.LogicLanguageImpl;
import impl.owls.process.constructs.PerformImpl;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.vocabulary.Vocabulary;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.LogicLanguage;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.vocabulary.OWLS_1_2.Expression.AlwaysTrue;
import org.mindswap.utils.URIUtils;

/**
 * This class defines constants for each element of the common vocabulary
 * OWL-S, version 1.1.
 *
 * @author unascribed
 * @see <a href="http://www.daml.org/services/owl-s/1.1">OWL-S 1.1</a>
 * @see <a href="http://www.w3.org/Submission/OWL-S">OWL-S W3C Member Submission</a>
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 * @deprecated Default version used is OWL-S 1.2, see {@link OWLS_1_2}.
 */
@Deprecated
public abstract class OWLS_1_1 extends Vocabulary
{
	public static final String base = OWLS_1_0.base;
	public static final String version = "1.1";
	public static final String URI = base + version + "/";

	// The following URIs are not declared in inner classes because we want to prevent rather costly
	// initialization of ontologies if only the URI is referenced but none of the vocabulary constants.
	public static final String ACTOR_URI = URI + "ActorDefault.owl#";
	public static final String EXPRESSION_URI = URI + "generic/Expression.owl#";
	public static final String GROUNDING_URI = URI + "Grounding.owl#";
	public static final String PROCESS_URI = URI + "Process.owl#";
	public static final String PROFILE_URI = URI + "Profile.owl#";
	public static final String PROFILE_ADDITIONAL_URI = URI + "ProfileAdditionalParameters.owl#";
	public static final String SERVICE_URI = URI + "Service.owl#";

	public static final ListVocabulary<OWLIndividual> ObjList;

	static
	{
		final String uri = OWLS_1_1.URI + "generic/ObjectList.owl#";
		final OWLOntology ont = loadOntology(uri);
		ObjList = new ListVocabulary<OWLIndividual>(URIUtils.createURI(uri), ont, OWLIndividual.class);
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
			final OWLOntology ont = loadOntology(SERVICE_URI);

			Service = ont.getClass(URIUtils.createURI(SERVICE_URI + "Service"));
			ServiceProfile = ont.getClass(URIUtils.createURI(SERVICE_URI + "ServiceProfile"));
			ServiceModel = ont.getClass(URIUtils.createURI(SERVICE_URI + "ServiceModel"));
			ServiceGrounding = ont.getClass(URIUtils.createURI(SERVICE_URI + "ServiceGrounding"));

			presentedBy = ont.getObjectProperty(URIUtils.createURI(SERVICE_URI + "presentedBy"));
			presents    = ont.getObjectProperty(URIUtils.createURI(SERVICE_URI + "presents"));
			describedBy = ont.getObjectProperty(URIUtils.createURI(SERVICE_URI + "describedBy"));
			describes   = ont.getObjectProperty(URIUtils.createURI(SERVICE_URI + "describes"));
			supportedBy = ont.getObjectProperty(URIUtils.createURI(SERVICE_URI + "supportedBy"));
			supports    = ont.getObjectProperty(URIUtils.createURI(SERVICE_URI + "supports"));
			providedBy  = ont.getObjectProperty(URIUtils.createURI(SERVICE_URI + "providedBy"));
			provides    = ont.getObjectProperty(URIUtils.createURI(SERVICE_URI + "provides"));
		}
	}

	public static abstract class Profile
	{
		public static final OWLClass Profile;

		public static final OWLDataProperty serviceName;
		public static final OWLDataProperty textDescription;

		public static final OWLObjectProperty hasProcess;

		public static final OWLClass ServiceParameter;
		public static final OWLObjectProperty serviceParameter;
		public static final OWLDataProperty serviceParameterName;
		public static final OWLObjectProperty sParameter;

		public static final OWLClass ServiceCategory;
		public static final OWLObjectProperty serviceCategory;
		public static final OWLDataProperty categoryName;
		public static final OWLDataProperty taxonomy;
		public static final OWLDataProperty value;
		public static final OWLDataProperty code;

		public static final OWLDataProperty serviceClassification;
		public static final OWLDataProperty serviceProduct;

		public static final OWLObjectProperty hasInput;
		public static final OWLObjectProperty hasOutput;
		public static final OWLObjectProperty hasPrecondition;
		public static final OWLObjectProperty hasParameter;
		public static final OWLObjectProperty hasResult;

		public static final OWLObjectProperty contactInformation;

		static
		{
			final OWLOntology ont = loadOntology(PROFILE_URI);

			Profile         = ont.getClass(URIUtils.createURI(PROFILE_URI + "Profile"));
			serviceName     = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "serviceName"));
			textDescription = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "textDescription"));

			ServiceParameter = ont.getClass(URIUtils.createURI(PROFILE_URI + "ServiceParameter"));
			serviceParameter = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "serviceParameter"));
			serviceParameterName = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "serviceParameterName"));
			sParameter       = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "sParameter"));

			hasProcess      = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "has_process"));
			hasInput        = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "hasInput"));
			hasOutput       = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "hasOutput"));
			hasPrecondition = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "hasPrecondition"));
			hasParameter    = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "hasParameter"));

			hasResult = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "hasResult"));

			ServiceCategory = ont.getClass(URIUtils.createURI(PROFILE_URI + "ServiceCategory"));
			serviceCategory = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "serviceCategory"));
			categoryName    = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "categoryName"));
			taxonomy        = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "taxonomy"));
			value           = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "value"));
			code            = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "code"));

			serviceClassification = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "serviceClassification"));
			serviceProduct = ont.getDataProperty(URIUtils.createURI(PROFILE_URI + "serviceProduct"));

			contactInformation = ont.getObjectProperty(URIUtils.createURI(PROFILE_URI + "contactInformation"));
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
			final OWLOntology ont = loadOntology(PROFILE_ADDITIONAL_URI);

			NAICS = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_URI + "NAICS"));
			UNSPSC = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_URI + "UNSPSC"));
			MaxResponseTime = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_URI + "MaxResponseTime"));
			AverageResponseTime = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_URI + "AverageResponseTime"));
			GeographicRadius = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_URI + "GeographicRadius"));
			Duration = ont.getClass(URIUtils.createURI(PROFILE_ADDITIONAL_URI + "Duration"));
		}
	}

	public static abstract class Process
	{
		public static final OWLClass Process;
		public static final OWLClass AtomicProcess;
		public static final OWLClass CompositeProcess;
		public static final OWLClass SimpleProcess;
		public static final OWLClass Parameter;
		public static final OWLClass Input;
		public static final OWLClass Output;

		public static final OWLDataProperty parameterType;
		public static final OWLObjectProperty hasParameter;
		public static final OWLObjectProperty hasInput;
		public static final OWLObjectProperty hasOutput;
		public static final OWLObjectProperty hasPrecondition;

		public static final OWLDataProperty name;

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
		 * This is not a standard part of OWL-S 1.1
		 */
		public static final OWLClass ForEach;
		/**
		 * This is not a standard part of OWL-S 1.1. Supposed to be used with ForEach construct.
		 */
		public static final OWLObjectProperty theList;
		/**
		 * This is not a standard part of OWL-S 1.1. Supposed to be used with ForEach construct.
		 */
		public static final OWLObjectProperty theLoopVar;
		/**
		 * This is not a standard part of OWL-S 1.1. Supposed to be used with ForEach construct.
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

		public static final OWLObjectProperty hasLocal;
		public static final OWLClass Local;

		public static final OWLObjectProperty inCondition;
		public static final OWLObjectProperty hasEffect;
		public static final OWLObjectProperty toParam;
		public static final OWLObjectProperty withOutput;
		public static final OWLObjectProperty valueSource;
		public static final OWLObjectProperty fromProcess;
		public static final OWLObjectProperty theVar;

		public static final OWLDataProperty valueSpecifier;
		public static final OWLDataProperty valueFunction;
		public static final OWLDataProperty valueForm;
		public static final OWLDataProperty valueData;
		/**
		 * This is not is not a standard part of OWL-S 1.1. Supposed to be used
		 * whenever some Binding should refer to some (constant) individual, which
		 * is to be used as the value for the parameter. Since it is declared as
		 * an object property it is not similar to {@link #valueData}. The latter
		 * represents a constant value by means of an XML literal (no matter what
		 * this literal actually represents), whereas this property refers to
		 * some OWL individual.
		 */
		public static final OWLObjectProperty valueObject;

		/**
		 * A special-purpose object, used to refer, at runtime, to the execution
		 * instance of the enclosing composite process definition.
		 */
		public static final Perform TheParentPerform;
		/**
		 * A special-purpose object, used to refer, at runtime, to the execution
		 * instance of the enclosing atomic process definition.
		 */
		public static final Perform ThisPerform;

		public static final ListVocabulary<ControlConstruct> CCList;
		public static final ListVocabulary<ControlConstruct> CCBag;

		static
		{
			final OWLOntology ont = loadOntology(PROCESS_URI);

			Process          = ont.getClass(URIUtils.createURI(PROCESS_URI + "Process"));
			AtomicProcess    = ont.getClass(URIUtils.createURI(PROCESS_URI + "AtomicProcess"));
			CompositeProcess = ont.getClass(URIUtils.createURI(PROCESS_URI + "CompositeProcess"));
			SimpleProcess    = ont.getClass(URIUtils.createURI(PROCESS_URI + "SimpleProcess"));
			Parameter        = ont.getClass(URIUtils.createURI(PROCESS_URI + "Parameter"));
			Input            = ont.getClass(URIUtils.createURI(PROCESS_URI + "Input"));
			Output           = ont.getClass(URIUtils.createURI(PROCESS_URI + "Output"));

			parameterType   = ont.getDataProperty(URIUtils.createURI(PROCESS_URI + "parameterType"));
			hasParameter    = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasParameter"));
			hasInput        = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasInput"));
			hasOutput       = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasOutput"));
			hasPrecondition = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasPrecondition"));

			name			= ont.getDataProperty(URIUtils.createURI(PROCESS_URI + "name"));

			ControlConstructList = ont.getClass(URIUtils.createURI(PROCESS_URI + "ControlConstructList"));
			ControlConstructBag = ont.getClass(URIUtils.createURI(PROCESS_URI + "ControlConstructBag"));
			ControlConstruct = ont.getClass(URIUtils.createURI(PROCESS_URI + "ControlConstruct"));
			Sequence         = ont.getClass(URIUtils.createURI(PROCESS_URI + "Sequence"));
			AnyOrder         = ont.getClass(URIUtils.createURI(PROCESS_URI + "Any-Order"));
			Choice           = ont.getClass(URIUtils.createURI(PROCESS_URI + "Choice"));
			IfThenElse       = ont.getClass(URIUtils.createURI(PROCESS_URI + "If-Then-Else"));
			Produce          = ont.getClass(URIUtils.createURI(PROCESS_URI + "Produce"));
			Split            = ont.getClass(URIUtils.createURI(PROCESS_URI + "Split"));
			SplitJoin        = ont.getClass(URIUtils.createURI(PROCESS_URI + "Split-Join"));
			Iterate          = ont.getClass(URIUtils.createURI(PROCESS_URI + "Iterate"));
			RepeatUntil      = ont.getClass(URIUtils.createURI(PROCESS_URI + "Repeat-Until"));
			RepeatWhile      = ont.getClass(URIUtils.createURI(PROCESS_URI + "Repeat-While"));

			ForEach    = ont.createClass(URIUtils.createURI(PROCESS_URI + "For-Each"));
			theList = ont.createObjectProperty(URIUtils.createURI(PROCESS_URI + "theList"));
			theLoopVar = ont.createObjectProperty(URIUtils.createURI(PROCESS_URI + "theLoopVar"));
			iterateBody = ont.createObjectProperty(URIUtils.createURI(PROCESS_URI + "iterateBody"));

			ValueOf          = ont.getClass(URIUtils.createURI(PROCESS_URI + "ValueOf"));

			composedOf   = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "composedOf"));
			invocable    = ont.getDataProperty(URIUtils.createURI(PROCESS_URI + "invocable"));
			computedInput  = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "computedInput"));
			computedOutput = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "computedOutput"));
			computedPrecondition = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "computedPrecondition"));
			computedEffect = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "computedEffect"));

			components   = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "components"));
			ifCondition  = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "ifCondition"));
			thenP        = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "then"));
			elseP        = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "else"));
			untilProcess   = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "untilProcess"));
			untilCondition = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "untilCondition"));
			whileProcess   = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "whileProcess"));
			whileCondition = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "whileCondition"));
			producedBinding = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "producedBinding"));
			realizedBy      = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "realizedBy"));
			realizes        = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "realizes"));
			expandsTo       = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "expandsTo"));
			collapsesTo     = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "collapsesTo"));
			timeout         = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "timeout"));

			Perform = ont.getClass(URIUtils.createURI(PROCESS_URI + "Perform"));
			process = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "process"));
			hasDataFrom = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasDataFrom"));
			Binding = ont.getClass(URIUtils.createURI(PROCESS_URI + "Binding"));
			InputBinding = ont.getClass(URIUtils.createURI(PROCESS_URI + "InputBinding"));
			OutputBinding = ont.getClass(URIUtils.createURI(PROCESS_URI + "OutputBinding"));

			parameterValue = ont.getDataProperty(URIUtils.createURI(PROCESS_URI + "parameterValue"));

			hasResult = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasResult"));
			Result = ont.getClass(URIUtils.createURI(PROCESS_URI + "Result"));
			ResultVar = ont.getClass(URIUtils.createURI(PROCESS_URI + "ResultVar"));
			hasResultVar = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasResultVar"));

			hasLocal = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasLocal"));
			Local = ont.getClass(URIUtils.createURI(PROCESS_URI + "Local"));

			inCondition = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "inCondition"));
			hasEffect    = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "hasEffect"));
			toParam = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "toParam"));
			withOutput = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "withOutput"));
			valueSource = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "valueSource"));
			fromProcess = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "fromProcess"));
			theVar = ont.getObjectProperty(URIUtils.createURI(PROCESS_URI + "theVar"));

			valueSpecifier = ont.getDataProperty(URIUtils.createURI(PROCESS_URI + "valueSpecifier"));
			valueFunction = ont.getDataProperty(URIUtils.createURI(PROCESS_URI + "valueFunction"));
			valueForm = ont.getDataProperty(URIUtils.createURI(PROCESS_URI + "valueForm"));
			valueData = ont.getDataProperty(URIUtils.createURI(PROCESS_URI + "valueData"));
         valueObject = ont.createObjectProperty(URIUtils.createURI(PROCESS_URI + "valueObject"));

         // Retrieval of the two Perfom individuals MUST come AFTER creating the two specialized
         // lists because the method castTo(..) will indirectly cause initialization of all
         // OWLObjectConverters. The class OWLSConverters initializes several ListConverter(s),
         // one for CCList and one for CCBag. They do access those fields and would throw NPEs
         // if not yet assigned.
         CCList = ObjList.specialize(ControlConstructList, ControlConstruct.class);
         CCBag = ObjList.specialize(ControlConstructBag, ControlConstruct.class);

         // We must not use OWLObject.castTo(..) here because this would introduce a circular
         // dependency with class OWLSConverters! But going the direct way and instantiate the
         // perform directly is not dangerous because we know that the individuals are Performs.
			TheParentPerform = new PerformImpl(ont.getIndividual(URIUtils.createURI(PROCESS_URI + "TheParentPerform")));
			ThisPerform = new PerformImpl(ont.getIndividual(URIUtils.createURI(PROCESS_URI + "ThisPerform")));
		}
	}

	public static abstract class Grounding
	{
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
		public static final OWLDataProperty wsdlMessagePart;
		public static final OWLObjectProperty owlsParameter;
		public static final OWLDataProperty xsltTransformation;
		public static final OWLDataProperty xsltTransformationString;
		public static final OWLDataProperty xsltTransformationURI;

		static
		{
			final OWLOntology ont = loadOntology(GROUNDING_URI);

			hasAtomicProcessGrounding = ont.getObjectProperty(URIUtils.createURI(GROUNDING_URI + "hasAtomicProcessGrounding"));
			owlsProcess            = ont.getObjectProperty(URIUtils.createURI(GROUNDING_URI + "owlsProcess"));

			WsdlGrounding              = ont.getClass(URIUtils.createURI(GROUNDING_URI + "WsdlGrounding"));
			WsdlAtomicProcessGrounding = ont.getClass(URIUtils.createURI(GROUNDING_URI + "WsdlAtomicProcessGrounding"));
			WsdlOperationRef           = ont.getClass(URIUtils.createURI(GROUNDING_URI + "WsdlOperationRef"));

			wsdlVersion       = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "wsdlVersion"));
			wsdlDocument      = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "wsdlDocument"));
			wsdlService       = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "wsdlService"));
			wsdlOperation     = ont.getObjectProperty(URIUtils.createURI(GROUNDING_URI + "wsdlOperation"));
			wsdlPort          = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "wsdlPort"));
			portType          = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "portType"));
			operation         = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "operation"));
			wsdlInputMessage  = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "wsdlInputMessage"));
			wsdlOutputMessage = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "wsdlOutputMessage"));
			wsdlInput         = ont.getObjectProperty(URIUtils.createURI(GROUNDING_URI + "wsdlInput"));
			wsdlOutput        = ont.getObjectProperty(URIUtils.createURI(GROUNDING_URI + "wsdlOutput"));

			WsdlMessageMap           = ont.getClass(URIUtils.createURI(GROUNDING_URI + "WsdlMessageMap"));
			WsdlInputMessageMap      = ont.getClass(URIUtils.createURI(GROUNDING_URI + "WsdlInputMessageMap"));
			WsdlOutputMessageMap     = ont.getClass(URIUtils.createURI(GROUNDING_URI + "WsdlOutputMessageMap"));
			wsdlMessagePart          = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "wsdlMessagePart"));
			owlsParameter            = ont.getObjectProperty(URIUtils.createURI(GROUNDING_URI + "owlsParameter"));
			xsltTransformation       = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "xsltTransformation"));
			xsltTransformationString = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "xsltTransformationString"));
			xsltTransformationURI    = ont.getDataProperty(URIUtils.createURI(GROUNDING_URI + "xsltTransformationURI"));

		}
	}

	public static abstract class Expression
	{
		public static final OWLClass LogicLanguage;
		public static final OWLClass Expression;
		public static final OWLClass Condition;

		public static final LogicLanguage KIF;
		public static final LogicLanguage SWRL;
		public static final LogicLanguage DRS;

		public static final OWLClass KIF_Condition;
		public static final OWLClass SWRL_Condition;
		public static final OWLClass DRS_Condition;

		public static final OWLClass KIF_Expression;
		public static final OWLClass SWRL_Expression;
		public static final OWLClass DRS_Expression;

		public static final OWLDataProperty refURI;
		public static final OWLObjectProperty expressionLanguage;
		public static final OWLDataProperty expressionBody;

		/**
		 * This is not is not a standard part of OWL-S 1.1. It represents an
		 * alternative to {@link #expressionBody} and is supposed to be used
		 * whenever the body of some expression should refer to some individual.
		 * Since it is declared as an object property it is not similar to
		 * {@link #expressionBody}. The range of the latter are XML literals
		 * (no matter what this literal actually represents), whereas this
		 * property refers to some OWL individual.
		 */
		public static final OWLObjectProperty expressionObject;

		public static final Condition<?> AlwaysTrue;

		static
		{
			final OWLOntology ont = loadOntology(EXPRESSION_URI);

			LogicLanguage = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "LogicLanguage"));
			Expression    = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "Expression"));
			Condition     = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "Condition"));

			expressionLanguage = ont.getObjectProperty(URIUtils.createURI(EXPRESSION_URI + "expressionLanguage"));
			expressionBody     = ont.getDataProperty(URIUtils.createURI(EXPRESSION_URI + "expressionBody"));
			expressionObject   = ont.createObjectProperty(URIUtils.createURI(EXPRESSION_URI + "expressionObject"));
			refURI             = ont.getDataProperty(URIUtils.createURI(EXPRESSION_URI + "refURI"));

			KIF_Condition  = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "KIF-Condition"));
			SWRL_Condition = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "SWRL-Condition"));
			DRS_Condition  = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "DRS-Condition"));

			KIF_Expression  = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "KIF-Expression"));
			SWRL_Expression = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "SWRL-Expression"));
			DRS_Expression  = ont.getClass(URIUtils.createURI(EXPRESSION_URI + "DRS-Expression"));

			KIF  = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_URI + "KIF")));
			SWRL = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_URI + "SWRL")));
			DRS  = new LogicLanguageImpl(ont.getIndividual(URIUtils.createURI(EXPRESSION_URI + "DRS")));

			// We must not use OWLObject.castTo(..) here because this would introduce a circular
         // dependency with class OWLSConverters! But going the direct way and instantiate the
         // object directly is not dangerous because we know that the individual is a Expression
			// (actually, it is a SWRL-Condition).
			AlwaysTrue = new AlwaysTrue(ont.getIndividual(URIUtils.createURI(EXPRESSION_URI + "AlwaysTrue")));
		}
	}

	/**
	 * Vocabulary for the Actor ontology
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
			final OWLOntology ont = loadOntology(ACTOR_URI);

			Actor = ont.getClass(URIUtils.createURI(ACTOR_URI + "Actor"));
			email = ont.getDataProperty(URIUtils.createURI(ACTOR_URI + "email"));
			fax = ont.getDataProperty(URIUtils.createURI(ACTOR_URI + "fax"));
			name = ont.getDataProperty(URIUtils.createURI(ACTOR_URI + "name"));
			phone = ont.getDataProperty(URIUtils.createURI(ACTOR_URI + "phone"));
			physicalAddress = ont.getDataProperty(URIUtils.createURI(ACTOR_URI + "physicalAddress"));
			title = ont.getDataProperty(URIUtils.createURI(ACTOR_URI + "title"));
			webURL = ont.getDataProperty(URIUtils.createURI(ACTOR_URI + "webURL"));
		}
	}
}
