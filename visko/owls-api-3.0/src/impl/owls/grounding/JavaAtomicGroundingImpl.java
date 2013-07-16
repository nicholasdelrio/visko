// The MIT License
//
// Copyright (c) 2007 University of Zürich Switzerland
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
package impl.owls.grounding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.JavaGrounding;
import org.mindswap.owls.grounding.JavaParameter;
import org.mindswap.owls.grounding.JavaVariable;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.vocabulary.MoreGroundings.Java;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2335 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class JavaAtomicGroundingImpl extends AtomicGroundingImpl<Object> implements JavaAtomicGrounding
{
	private static final String NO_MESSAGE_MAPS = "Java atomic process grounding does not use message maps.";

	private static final Logger logger = LoggerFactory.getLogger(JavaAtomicGroundingImpl.class);

	// The following 8 fields are all lazily initialized and act as caches if this
	// grounding is invoked/executed repeatedly.
	private Class<?> clazz;
	private Method method;
	private Class<?> methodParamsType[];

	// We need to make sure that corresponding elements have same index! invoke(..) method depends on this
	private OWLIndividualList<JavaParameter> inputParameters;
	private OWLIndividualList<Input> owlsInputs;

	private JavaVariable outputParameter;
	private Output owlsOutput;
	private OWLType owlsOutputType;

	/**
	 * @see AtomicGroundingImpl#AtomicGroundingImpl(OWLIndividual)
	 */
	public JavaAtomicGroundingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#setClazz(java.lang.String) */
	public void setClazz(final String claz)
	{
		this.clazz = null;
		setProperty(Java.javaClass, claz);
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#getClazz() */
	public String getClazz() {
		return getPropertyAsString(Java.javaClass);
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#setMethod(java.lang.String) */
	public void setMethod(final String method)
	{
		this.method = null;
		this.methodParamsType = null;
		setProperty(Java.javaMethod, method);
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#getMethod() */
	public String getMethod() {
		return getPropertyAsString(Java.javaMethod);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getDescriptionURL() */
	public URL getDescriptionURL()
	{
		throw new UnsupportedOperationException("This property does not exist for Java atomic process groundings.");
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getGrounding() */
	public JavaGrounding getGrounding()
	{
		return getGrounding(JavaGrounding.class);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#addInputMap(org.mindswap.owls.grounding.MessageMap) */
	public void addInputMap(final MessageMap<Object> map)
	{
		throw new UnsupportedOperationException(NO_MESSAGE_MAPS);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#addOutputMap(org.mindswap.owls.grounding.MessageMap) */
	public void addOutputMap(final MessageMap<Object> map)
	{
		throw new UnsupportedOperationException(NO_MESSAGE_MAPS);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#addMessageMap(org.mindswap.owls.process.Parameter, java.lang.String, java.lang.String) */
	public void addMessageMap(final Parameter owlsParameter, final String groundingParameter,
		final Object transformation)
	{
		throw new UnsupportedOperationException(NO_MESSAGE_MAPS);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getInputMappings() */
	public OWLIndividualList<MessageMap<Object>> getInputMappings()
	{
		throw new UnsupportedOperationException(NO_MESSAGE_MAPS);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getOutputMappings() */
	public OWLIndividualList<MessageMap<Object>> getOutputMappings()
	{
		throw new UnsupportedOperationException(NO_MESSAGE_MAPS);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#invoke(org.mindswap.query.ValueMap) */
	public ValueMap<Output, OWLValue> invoke(final ValueMap<Input, OWLValue> values) throws ExecutionException
	{
		return invoke(values, OWLFactory.createKB());
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#invoke(org.mindswap.query.ValueMap, org.mindswap.owl.OWLKnowledgeBase) */
	public ValueMap<Output, OWLValue> invoke(final ValueMap<Input, OWLValue> inputs, final OWLKnowledgeBase kb)
		throws ExecutionException
	{
		// init target class and target method reference
		initClazz();
		initMethod();

		// prepare target method parameter values
		initInputs();
		final Object methodParams[] = new Object[owlsInputs.size()];
		int i = 0;
		for (Input input : owlsInputs)
		{
			final OWLValue inputValue = getParameterValue(input, inputs);
			methodParams[i] = getParameterValue(inputParameters.get(i), methodParamsType[i], inputValue);
			i++;
		}

		// invoke target Java method
		Object result;
		try
		{
			final Object instance;
			if (Modifier.isStatic(method.getModifiers())) instance = null;
			else instance = clazz.newInstance();
			result = method.invoke(instance, methodParams);
		}
		catch (final InvocationTargetException e)
		{
			throw new ExecutionException("Method " + method + " threw exception. Details: " + e.getTargetException());
		}
		catch (final Exception e)
		{
			throw new ExecutionException("Failed to invoke method " + method + ". Details: " + e);
		}

		// set output from return value of Java method
		final ValueMap<Output, OWLValue> results = new ValueMap<Output, OWLValue>();
		if (result != null)
		{
			final JavaVariable outputVariable = getOutput();
			if (outputVariable == null)
			{
				// We should not throw ExecutionException because it may be intended to ignore return
				// value (but it may also be a mistake, i.e., if one forgot to specify Java output variable).
				logger.debug("Return value of {} ignored because JavaAtomicGrounding does not specify javaOutput property.",
					toString());
			}
			else
			{
				initOutput();
				if (owlsOutputType.isDataType())
					results.setValue(owlsOutput, getOntology().createDataValue(result));
				else
					results.setValue(owlsOutput, outputVariable.getTransformator(false).toOWL(result, getKB()));
			}
		}

		return results;
	}

	private Object getParameterValue(final JavaParameter param, final Class<?> paramType,
		final OWLValue sourceValue) throws ExecutionException
	{
		if (paramType.isInstance(sourceValue)) return sourceValue;

		if (sourceValue.isDataValue()) return ((OWLDataValue) sourceValue).getValue();

		// else it can only be an OWLIndividual --> needs to be transformed
		return param.getTransformator(true).fromOWL(sourceValue, getKB());
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#setOutput(java.net.URI, java.lang.String, org.mindswap.owls.process.Output) */
	public void setOutput(final URI uri, final String type, final Output owlsParameter)
	{
		outputParameter = null;
		owlsOutput = null;
		owlsOutputType = null;
		final OWLIndividual ind = getOntology().createInstance(Java.JavaVariable, uri);
		ind.setProperty(Java.javaType, type);
		ind.setProperty(Java.owlsParameter, owlsParameter);
		setProperty(Java.javaOutput, ind);
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#addInputParameter(java.net.URI, java.lang.String, int, org.mindswap.owls.process.Input) */
	public void addInputParameter(final URI uri, final String type, final int index, final Input owlsParameter)
	{
		inputParameters = null;
		owlsInputs = null;
		final OWLIndividual ind = getOntology().createInstance(Java.JavaParameter, uri);
		ind.setProperty(Java.javaType, type);
		ind.setProperty(Java.owlsParameter, owlsParameter);
		ind.setProperty(Java.paramIndex, Integer.valueOf(index));
		addProperty(Java.hasJavaParameter, ind);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getGroundingType() */
	public String getGroundingType() {
		return AtomicGrounding.GROUNDING_JAVA;
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("Java grounding method ");
		sb.append(getClazz()).append(".").append(getMethod()).append("(");

		for (JavaParameter param : getInputParameters())
		{
			sb.append(param.getJavaType());
			if (param.getParameterIndex() > 0) sb.append(", ");
		}
		sb.append(")");

		return sb.toString();
	}

	private void removeAll() {
		removeProperty(Java.javaClass, null);
		removeProperty(Java.javaMethod, null);

		// TODO an rdf:type property of the related input stays persistent. why????
		if (hasProperty(Java.hasJavaParameter))
		{
			final OWLIndividualList<?> indList = getProperties(Java.hasJavaParameter);
			for (OWLIndividual ind : indList)
			{
				ind.removeProperty(Java.javaType, null);
				ind.removeProperty(Java.owlsParameter, null);
				ind.removeProperty(Java.paramIndex, null);
				removeProperty(Java.hasJavaParameter, ind);
				ind.delete();
			}
		}

		if (hasProperty(Java.javaOutput)) {
			final OWLIndividual ind = getProperty(Java.javaOutput);
			ind.removeProperty(Java.javaType, null);
			ind.removeProperty(Java.owlsParameter, null);
			removeProperty(Java.javaOutput, null);
			ind.delete();
		}

		removeProperty(OWLS.Grounding.owlsProcess, null);
	}

	/* @see impl.owl.WrappedIndividual#delete() */
	@Override
	public void delete()
	{
		removeAll();
		super.delete();
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#getInputParamter(org.mindswap.owls.process.Input) */
	public JavaParameter getInputParamter(final Input input)
	{
		for (JavaParameter param : getInputParameters())
		{
			final Parameter owlsParam = param.getOWLSParameter();
			if (owlsParam.equals(input)) return param;
		}
		return null;
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#getOutput() */
	public JavaVariable getOutput()
	{
		if (outputParameter == null)
		{
			outputParameter = getPropertyAs(Java.javaOutput, JavaVariable.class);
		}
		return outputParameter;
	}

	/* @see org.mindswap.owls.grounding.JavaAtomicGrounding#getInputParameters() */
	public OWLIndividualList<JavaParameter> getInputParameters()
	{
		if (inputParameters == null)
		{
			inputParameters = getPropertiesAs(Java.hasJavaParameter, JavaParameter.class);

			// need to sort them according to the their index
			Collections.sort(inputParameters, new Comparator<JavaParameter>() {
				public int compare(final JavaParameter p1, final JavaParameter p2)
				{
					final Integer i1 = Integer.valueOf(p1.getParameterIndex());
					final Integer i2 = Integer.valueOf(p2.getParameterIndex());
					return i1.compareTo(i2);
				}
			});
		}
		return inputParameters;
	}

	private void initClazz() throws ExecutionException
	{
		if (clazz == null)
		{
			// get and check target class
			final String clazzName = getClazz();
			if ((clazzName == null) || (clazzName.trim().length() == 0))
				throw new ExecutionException("Java class name not specified in JavaAtomicGrounding!");

			try
			{
				clazz = Class.forName(clazzName);
			}
			catch (final Exception e)
			{
				throw new ExecutionException(e);
			}
		}
	}

	private void initMethod() throws ExecutionException
	{
		if (method == null)
		{
			// get and check target method
			final String methodName = getMethod();
			if ((methodName == null) || (methodName.trim().length() == 0))
				throw new ExecutionException("Java method name not specified in JavaAtomicGrounding!");

			// prepare parameter types
			final OWLIndividualList<JavaParameter> params = getInputParameters();
			methodParamsType = new Class[params.size()];
			int i = 0;
			for (JavaParameter param : params)
			{
				final String paramClassName = param.getJavaType();
				methodParamsType[i] = ReflectionUtils.getClassFromString(paramClassName);
				i++;
			}

			try
			{
				method = clazz.getMethod(methodName, methodParamsType);
			}
			catch (final Exception e)
			{
				throw new ExecutionException(e);
			}
		}
	}

	/**
	 * Assigns the {@link #owlsInputs OWL-S Input Parameters} from
	 * {@link #getInputParameters() Java input parameter} individuals.
	 */
	private void initInputs()
	{
		if (owlsInputs == null)
		{
			owlsInputs = OWLFactory.createIndividualList(getInputParameters().size());
			for (JavaParameter inputParam : inputParameters)
			{
				final Input input = inputParam.getOWLSParameter().castTo(Input.class);
				owlsInputs.add(input);
			}
		}
	}

	/**
	 * Assigns the {@link #owlsOutput OWL-S Output Parameter} and its
	 * {@link #owlsOutputType type} from {@link #getOutput() Java output variable}
	 * individual.
	 */
	private void initOutput()
	{
		if (owlsOutput == null)
		{
			JavaVariable outputVariable = getOutput();
			final OWLIndividual ind = outputVariable.getOWLSParameter();
			if (ind != null)
			{
				owlsOutput = ind.castTo(Output.class);
				owlsOutputType = owlsOutput.getParamType();
			}
		}
	}

}
