/*
 * Created 24.08.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package impl.owls.grounding;

import impl.owl.GenericOWLConverter;

import java.net.URI;

import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.JavaGrounding;
import org.mindswap.owls.grounding.JavaGroundingProvider;
import org.mindswap.owls.grounding.JavaParameter;
import org.mindswap.owls.grounding.JavaVariable;
import org.mindswap.owls.vocabulary.MoreGroundings;
import org.mindswap.owls.vocabulary.MoreGroundings.Java;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class JavaGroundingProviderImpl implements JavaGroundingProvider
{
	// Implementation note: This class should *not* maintain any state between
	// the registerConverters(..) method derived from OWLObjectConverterProvider
	// and methods directly derived from JavaGroundingProvider! The reason is
	// that two instances of this class will be created; one used by the
	// OWLObjectConverterRegistry to register all converters and one hold by the
	// JavaGroundingFactory.

	/* @see org.mindswap.owls.grounding.JavaGroundingProvider#createJavaAtomicGrounding(java.net.URI, org.mindswap.owl.OWLModel) */
	public JavaAtomicGrounding createJavaAtomicGrounding(final URI uri, final OWLModel model)
	{
		return new JavaAtomicGroundingImpl(model.createInstance(MoreGroundings.Java.JavaAtomicProcessGrounding, uri));
	}

	/* @see org.mindswap.owls.grounding.JavaGroundingProvider#createJavaGrounding(java.net.URI, org.mindswap.owl.OWLModel) */
	public JavaGrounding createJavaGrounding(final URI uri, final OWLModel model)
	{
		return new JavaGroundingImpl(model.createInstance(MoreGroundings.Java.JavaGrounding, uri));
	}

	/* @see org.mindswap.owls.grounding.JavaGroundingProvider#createJavaParameter(java.net.URI, org.mindswap.owl.OWLModel) */
	public JavaParameter createJavaParameter(final URI uri, final OWLModel model)
	{
		return new JavaParameterImpl(model.createInstance(MoreGroundings.Java.JavaParameter, uri));
	}

	/* @see org.mindswap.owls.grounding.JavaGroundingProvider#createJavaVariable(java.net.URI, org.mindswap.owl.OWLModel) */
	public JavaVariable createJavaVariable(final URI uri, final OWLModel model)
	{
		return new JavaVariableImpl(model.createInstance(MoreGroundings.Java.JavaVariable, uri));
	}

	/* @see org.mindswap.owl.OWLObjectConverterProvider#registerConverters(org.mindswap.owl.OWLObjectConverterRegistry) */
	public void registerConverters(final OWLObjectConverterRegistry registry)
	{
		final OWLObjectConverter<JavaGrounding> gc =	new GenericOWLConverter<JavaGrounding>(
			JavaGroundingImpl.class, MoreGroundings.Java.JavaGrounding);

		final OWLObjectConverter<JavaAtomicGrounding> agc = new GenericOWLConverter<JavaAtomicGrounding>(
			JavaAtomicGroundingImpl.class, MoreGroundings.Java.JavaAtomicProcessGrounding);

		registry.registerConverter(JavaVariable.class,
			new GenericOWLConverter<JavaVariable>(JavaVariableImpl.class, Java.JavaVariable));
		registry.registerConverter(JavaParameter.class,
			new GenericOWLConverter<JavaParameter>(JavaParameterImpl.class, Java.JavaParameter));

		registry.registerConverter(JavaGrounding.class, gc);
		registry.registerConverter(JavaAtomicGrounding.class, agc);

		registry.extendByConverter(Grounding.class, gc);
		registry.extendByConverter(AtomicGrounding.class, agc);
	}

}
