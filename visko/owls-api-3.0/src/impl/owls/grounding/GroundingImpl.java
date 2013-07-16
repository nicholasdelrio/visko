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
 * Created on Dec 27, 2003
 */
package impl.owls.grounding;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public abstract class GroundingImpl<A extends AtomicGrounding<T>, T> extends WrappedIndividual
	implements Grounding<A, T>
{

	/**
	 * @see WrappedIndividual#WrappedIndividual(OWLIndividual)
	 */
	public GroundingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.Grounding#addGrounding(org.mindswap.owls.grounding.AtomicGrounding) */
	public final void addGrounding(final A apg)
	{
		addProperty(OWLS.Grounding.hasAtomicProcessGrounding, apg);
	}


	/* @see org.mindswap.owls.grounding.Grounding#removeAtomicGrounding(org.mindswap.owls.grounding.AtomicGrounding) */
	public final void removeAtomicGrounding(final A apg)
	{
		removeProperty(OWLS.Grounding.hasAtomicProcessGrounding, apg);
	}

	/* @see org.mindswap.owls.grounding.Grounding#removeAtomicGroundings(org.mindswap.owls.process.AtomicProcess) */
	public final void removeAtomicGroundings(final AtomicProcess process)
	{
		final OWLIndividualList<A> list = getAtomicGroundings();
		for (final A apg : list)
		{
			if (apg.hasProperty(OWLS.Grounding.owlsProcess, process))
				removeProperty(OWLS.Grounding.hasAtomicProcessGrounding, apg);
		}
	}

	/* @see org.mindswap.owls.grounding.Grounding#removeAtomicGroundings() */
	public final void removeAtomicGroundings()
	{
		removeProperty(OWLS.Grounding.hasAtomicProcessGrounding, null);
	}

	/* @see org.mindswap.owls.grounding.Grounding#getService() */
	public final Service getService()
	{
		return getPropertyAs(OWLS.Service.supportedBy, Service.class);
	}

	/* @see org.mindswap.owls.grounding.Grounding#removeService() */
	public final void removeService()
	{
		removeProperty(OWLS.Service.supportedBy, getService());
	}

	/* @see org.mindswap.owls.grounding.Grounding#setService(org.mindswap.owls.service.Service) */
	public final void setService(final Service service)
	{
		if (hasProperty(OWLS.Service.supportedBy, service)) return;
		setProperty(OWLS.Service.supportedBy, service);
		service.addGrounding(this);
	}

	protected final <G extends AtomicGrounding<T>> G getAtomicGrounding(final AtomicProcess process,
		final Class<G> clazz)
	{
		final OWLIndividualList<?> groundings = getProperties(OWLS.Grounding.hasAtomicProcessGrounding);
		for (final OWLIndividual grounding : groundings)
		{
			if (grounding.hasProperty(OWLS.Grounding.owlsProcess, process)) return grounding.castTo(clazz);
		}
		return null;
	}

	protected final <G extends AtomicGrounding<T>> OWLIndividualList<G> getAtomicGroundings(final Class<G> clazz)
	{
		return getPropertiesAs(OWLS.Grounding.hasAtomicProcessGrounding, clazz);
	}

	protected final <G extends AtomicGrounding<T>> OWLIndividualList<G> getAtomicGroundings(
		final AtomicProcess process, final Class<G> clazz)
	{
		final OWLIndividualList<?> tmp = getProperties(OWLS.Grounding.hasAtomicProcessGrounding);
		final OWLIndividualList<G> atGroundings = OWLFactory.createIndividualList();
		for (final OWLIndividual ind : tmp)
		{
			if (ind.hasProperty(OWLS.Grounding.owlsProcess, process)) atGroundings.add(ind.castTo(clazz));
		}
		return atGroundings;
	}

}
