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

import impl.owl.WrappedIndividual;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.TransformationException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLTransformator;
import org.mindswap.owls.grounding.JavaVariable;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.vocabulary.MoreGroundings.Java;

/**
 *
 * @author unascribed
 * @version $Rev: 2316 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class JavaVariableImpl extends WrappedIndividual implements JavaVariable
{
	/** Used for caching purposes to increase performance when Java groundings are invoked repeatedly. */
	private OWLTransformator transformator;

	public JavaVariableImpl(final OWLIndividual ind) {
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#getJavaType() */
	public String getJavaType() {
		return getPropertyAsString(Java.javaType);
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#getOWLSParameter() */
	public Parameter getOWLSParameter() {
		return getPropertyAs(Java.owlsParameter, Parameter.class);
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#removeJavaType() */
	public void removeJavaType() {
		if (hasProperty(Java.javaType))
			removeProperty(Java.javaType, null);
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#removeOWLSParameter() */
	public void removeOWLSParameter() {
		if (hasProperty(Java.owlsParameter))
			removeProperty(Java.owlsParameter, null);
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#setJavaType(java.lang.String) */
	public void setJavaType(final String type) {
		setProperty(Java.javaType, type);
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#setOWLSParameter(org.mindswap.owls.process.Parameter) */
	public void setOWLSParameter(final Parameter parameter) {
		setProperty(OWLS.Grounding.owlsParameter, parameter);
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#getTransformator() */
	public String getTransformator() {
		return getProperty(Java.transformatorClass).getLexicalValue();
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#getTransformator(boolean) */
	public OWLTransformator getTransformator(final boolean failIfUnspecified) throws ExecutionException
	{
		if (transformator == null)
		{
			final String transformerName = getTransformator();
			if (transformerName != null && (transformerName.trim().length() > 0))
			{
				try
				{
					final Class<? extends OWLTransformator> transformerClass =
						Class.forName(transformerName).asSubclass(OWLTransformator.class);
					transformator = transformerClass.newInstance();
				}
				catch (final ClassNotFoundException e) { throw new ExecutionException(e); }
				catch (final InstantiationException e) { throw new ExecutionException(e); }
				catch (final IllegalAccessException e) { throw new ExecutionException(e); }
				catch (final SecurityException e) { throw new ExecutionException(e); }
				catch (final IllegalArgumentException e) { throw new ExecutionException(e); }
			}
			else if (failIfUnspecified) throw new TransformationException(
				"Required OWLTransformator not specified for JavaVariable " + this + " (Required when process " +
				"parameter type and Java grounding type are different, i.e., one needs to be transformed into the other).");
		}
		return transformator;
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#removeTransformator() */
	public void removeTransformator()
	{
		this.transformator = null;
		if (hasProperty(Java.transformatorClass))
			removeProperty(Java.transformatorClass, null);
	}

	/* @see org.mindswap.owls.grounding.JavaVariable#setTransformator(java.lang.String) */
	public void setTransformator(final String transformator)
	{
		this.transformator = null;
		setProperty(Java.transformatorClass, transformator);
	}

}
