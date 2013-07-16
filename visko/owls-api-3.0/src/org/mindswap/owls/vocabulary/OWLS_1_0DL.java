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

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.utils.URIUtils;

/**
 *
 * @author unascribed
 * @deprecated Use {@link OWLS_1_1} respectively {@link OWLS_1_2} instead.
 * @see <a href="http://www.daml.org/services/owl-s/1.0">OWL-S 1.0</a>
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
@Deprecated
public abstract class OWLS_1_0DL {
	public static final String base = OWLS_1_0.base;
	public static final String version = "1.0DL";
	public static final String URI = base + version + "/";

	public static abstract class Service {
		public static final String URI = OWLS_1_0DL.URI + "Service.owl#";

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

		static {
			Service = EntityFactory.createClass(URIUtils.createURI(URI + "Service"));
			ServiceProfile = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceProfile"));
			ServiceModel = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceModel"));
			ServiceGrounding = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceGrounding"));

			presentedBy = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "presentedBy"));
			presents    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "presents"));
			describedBy = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "describedBy"));
			describes   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "describes"));
			supportedBy = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "supportedBy"));
			supports    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "supports"));
		}
	}

	public static abstract class Profile {
		public static final String URI = OWLS_1_0DL.URI + "Profile.owl#";

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

		public static final OWLObjectProperty hasInput;
		public static final OWLObjectProperty hasOutput;
		public static final OWLObjectProperty hasPrecondition;
		public static final OWLObjectProperty hasParameter;
		public static final OWLObjectProperty hasResult;


		static {
			Profile         = EntityFactory.createClass(URIUtils.createURI(URI + "Profile"));
			serviceName     = EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceName"));
			textDescription = EntityFactory.createDataProperty(URIUtils.createURI(URI + "textDescription"));

			ServiceParameter = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceParameter"));
			serviceParameter = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "serviceParameter"));
			serviceParameterName = EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceParameterName"));
			sParameter       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "sParameter"));

			hasProcess      = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "has_process"));
			hasInput        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasInput"));
			hasOutput       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasOutput"));
			hasPrecondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasPrecondition"));
			hasParameter    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasParameter"));

			hasResult = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasResult"));


			ServiceCategory = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceCategory"));
			serviceCategory = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "serviceCategory"));
			categoryName    = EntityFactory.createDataProperty(URIUtils.createURI(URI + "categoryName"));
			taxonomy        = EntityFactory.createDataProperty(URIUtils.createURI(URI + "taxonomy"));
			value           = EntityFactory.createDataProperty(URIUtils.createURI(URI + "value"));
			code            = EntityFactory.createDataProperty(URIUtils.createURI(URI + "code"));
		}
	}

	public static abstract class Process {
		public static final String URI = OWLS_1_0DL.URI + "Process.owl#";

		public static final OWLClass ProcessModel;
		public static final OWLObjectProperty hasProcess;

		public static final OWLClass Process;
		public static final OWLClass AtomicProcess;
		public static final OWLClass CompositeProcess;
		public static final OWLClass SimpleProcess;
		public static final OWLClass Input;
		public static final OWLClass Output;
		public static final OWLClass Precondition;
		public static final OWLClass Effect;

		public static final OWLDataProperty parameterType;
		public static final OWLObjectProperty hasParameter;
		public static final OWLObjectProperty hasInput;
		public static final OWLObjectProperty hasOutput;
		public static final OWLObjectProperty hasPrecondition;
		public static final OWLObjectProperty hasEffect;

		public static final OWLDataProperty name;

		public static final OWLClass ControlConstruct;
		public static final OWLClass ControlConstructList;
		public static final OWLClass Sequence;
		public static final OWLClass Unordered;
		public static final OWLClass Choice;
		public static final OWLClass IfThenElse;
		public static final OWLClass Split;
		public static final OWLClass SplitJoin;
		public static final OWLClass Iterate;
		public static final OWLClass RepeatUntil;
		public static final OWLClass RepeatWhile;

		public static final OWLObjectProperty sameValues;
		public static final OWLClass ValueOf;
		public static final OWLObjectProperty atProcess;
		public static final OWLObjectProperty theParameter;

		public static final OWLObjectProperty composedOf;
		public static final OWLObjectProperty components;
		public static final OWLObjectProperty ifCondition;
		public static final OWLObjectProperty thenP;
		public static final OWLObjectProperty elseP;
		public static final OWLObjectProperty untilProcess;
		public static final OWLObjectProperty untilCondition;
		public static final OWLObjectProperty whileProcess;
		public static final OWLObjectProperty whileCondition;

		static {
			ProcessModel = EntityFactory.createClass(URIUtils.createURI(URI + "ProcessModel"));
			hasProcess = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasProcess"));

			Process          = EntityFactory.createClass(URIUtils.createURI(URI + "Process"));
			AtomicProcess    = EntityFactory.createClass(URIUtils.createURI(URI + "AtomicProcess"));
			CompositeProcess = EntityFactory.createClass(URIUtils.createURI(URI + "CompositeProcess"));
			SimpleProcess    = EntityFactory.createClass(URIUtils.createURI(URI + "SimpleProcess"));
			Input            = EntityFactory.createClass(URIUtils.createURI(URI + "Input"));
			Output           = EntityFactory.createClass(URIUtils.createURI(URI + "Output"));
			Precondition     = EntityFactory.createClass(URIUtils.createURI(URI + "Precondition"));
			Effect           = EntityFactory.createClass(URIUtils.createURI(URI + "Effect"));

			parameterType   = EntityFactory.createDataProperty(URIUtils.createURI(URI + "parameterType"));
			hasParameter    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasParameter"));
			hasInput        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasInput"));
			hasOutput       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasOutput"));
			hasPrecondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasPrecondition"));
			hasEffect       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasEffect"));

			name			= EntityFactory.createDataProperty(URIUtils.createURI(URI + "name"));

			ControlConstructList = EntityFactory.createClass(URIUtils.createURI(URI + "ControlConstructList"));
			ControlConstruct = EntityFactory.createClass(URIUtils.createURI(URI + "ControlConstruct"));
			Sequence         = EntityFactory.createClass(URIUtils.createURI(URI + "Sequence"));
			Unordered        = EntityFactory.createClass(URIUtils.createURI(URI + "Unordered"));
			Choice           = EntityFactory.createClass(URIUtils.createURI(URI + "Choice"));
			IfThenElse       = EntityFactory.createClass(URIUtils.createURI(URI + "If-Then-Else"));
			Split            = EntityFactory.createClass(URIUtils.createURI(URI + "Split"));
			SplitJoin        = EntityFactory.createClass(URIUtils.createURI(URI + "Split-Join"));
			Iterate          = EntityFactory.createClass(URIUtils.createURI(URI + "Iterate"));
			RepeatUntil      = EntityFactory.createClass(URIUtils.createURI(URI + "RepeatUntil"));
			RepeatWhile      = EntityFactory.createClass(URIUtils.createURI(URI + "RepeatWhile"));


			composedOf   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "composedOf"));
			components   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "components"));
			ifCondition  = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "ifCondition"));
			thenP        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "then"));
			elseP        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "else"));
			untilProcess   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "untilProcess"));
			untilCondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "untilCondition"));
			whileProcess   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "whileProcess"));
			whileCondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "whileCondition"));

			ValueOf = EntityFactory.createClass(URIUtils.createURI(URI + "ValueOf"));
			sameValues = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "sameValues"));
			atProcess = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "atProcess"));
			theParameter = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "theParameter"));
		}
	}

	public static abstract class Grounding {
		public static final String URI = OWLS_1_0DL.URI + "Grounding.owl#";

		public static final OWLClass WsdlGrounding;

		public static final OWLObjectProperty hasAtomicProcessGrounding;
		public static final OWLClass WsdlAtomicProcessGrounding;

		public static final OWLObjectProperty wsdlOperation;
		public static final OWLClass WsdlOperationRef;
		public static final OWLDataProperty portType;
		public static final OWLDataProperty operation;

		public static final OWLDataProperty wsdlDocument;
		public static final OWLObjectProperty owlsProcess;

		public static final OWLDataProperty wsdlInputMessage;
		public static final OWLObjectProperty wsdlInputs;

		public static final OWLDataProperty wsdlOutputMessage;
		public static final OWLObjectProperty wsdlOutputs;

		public static final OWLClass WsdlMessageMap;
		public static final OWLClass WsdlInputMessageMap;
		public static final OWLClass WsdlOutputMessageMap;
		public static final OWLDataProperty wsdlMessagePart;
		public static final OWLObjectProperty owlsParameter;
		public static final OWLDataProperty xsltTransformation;
		public static final OWLDataProperty xsltTransformationString;
		public static final OWLDataProperty xsltTransformationURI;

		static {
			WsdlGrounding = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlGrounding"));
			WsdlAtomicProcessGrounding = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlAtomicProcessGrounding"));
			WsdlOperationRef = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlOperationRef"));

			hasAtomicProcessGrounding = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasAtomicProcessGrounding"));
			wsdlDocument      = EntityFactory.createDataProperty(URIUtils.createURI(URI + "wsdlDocument"));
			wsdlOperation     = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlOperation"));
			portType          = EntityFactory.createDataProperty(URIUtils.createURI(URI + "portType"));
			operation         = EntityFactory.createDataProperty(URIUtils.createURI(URI + "operation"));
			owlsProcess       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "owlsProcess"));
			wsdlInputMessage  = EntityFactory.createDataProperty(URIUtils.createURI(URI + "wsdlInputMessage"));
			wsdlOutputMessage = EntityFactory.createDataProperty(URIUtils.createURI(URI + "wsdlOutputMessage"));
			wsdlInputs         = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlInputs"));
			wsdlOutputs        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlOutputs"));

			WsdlMessageMap    = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlMessageMap"));
			WsdlInputMessageMap    = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlInputMessageMap"));
			WsdlOutputMessageMap    = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlOutputMessageMap"));
			wsdlMessagePart   = EntityFactory.createDataProperty(URIUtils.createURI(URI + "wsdlMessagePart"));
			owlsParameter     = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "owlsParameter"));
			xsltTransformation = EntityFactory.createDataProperty(URIUtils.createURI(URI + "xsltTransformation"));
			xsltTransformationString = EntityFactory.createDataProperty(URIUtils.createURI(URI + "xsltTransformationString"));
			xsltTransformationURI = EntityFactory.createDataProperty(URIUtils.createURI(URI + "xsltTransformationURI"));
		}
	}

	public static final ListVocabulary<OWLIndividual> ObjList = new ListVocabulary<OWLIndividual>(
		URIUtils.createURI(URI + "generic/ObjectList.owl#"), OWLIndividual.class);
}
