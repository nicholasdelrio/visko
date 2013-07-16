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

package impl.owls.process.constructs;

import java.util.Iterator;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.process.CollectionControlConstruct;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public abstract class CollectionControlConstructImpl<C extends CollectionControlConstructImpl<C>>
	extends ControlConstructImpl<C> implements CollectionControlConstruct
{

	/**
	 * @see impl.owl.WrappedIndividual#WrappedIndividual(OWLIndividual)
	 */
	public CollectionControlConstructImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.process.CollectionControlConstruct#addComponent(org.mindswap.owls.process.ControlConstruct) */
	public void addComponent(final ControlConstruct component)
	{
		OWLList<ControlConstruct> components = getComponents0();
		if (components.isEmpty())
		{
			components = components.with(component);
			setProperty(OWLS.Process.components, components);
		}
		else components.with(component);
	}

//	/* @see org.mindswap.owls.process.CollectionControlConstruct#getComponents() */
//	public OWLIndividualList<ControlConstruct> getComponents()
//	{
//		final OWLIndividualList<ControlConstruct> components = OWLFactory.createIndividualList();
//		for (ControlConstruct cc : getComponents0())
//		{
//			components.add(cc);
//		}
//		return components;
//	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructs() */
	public OWLIndividualList<ControlConstruct> getConstructs()
	{
		final OWLIndividualList<ControlConstruct> components = OWLFactory.createIndividualList();
		for (ControlConstruct cc : getComponents0())
		{
			components.add(cc);
		}
		return components;
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getAllProcesses(boolean) */
	public OWLIndividualList<Process> getAllProcesses(final boolean recursive)
	{
		final OWLIndividualList<Process> processes = OWLFactory.createIndividualList();
		for (ControlConstruct cc : getComponents0())
		{
			processes.addAll(cc.getAllProcesses(recursive));
		}
		return processes;
	}

	/* @see impl.owls.process.constructs.ControlConstructImpl#removeConstruct(org.mindswap.owls.process.ControlConstruct) */
	@Override
	public boolean removeConstruct(final ControlConstruct cc)
	{
		OWLList<ControlConstruct> components = getComponents0();
		int index;
		boolean removed = false;
		while ((index = components.indexOf(cc)) > -1) // cc may be contained multiple times
		{
			components = components.remove(index);
			removed = true;
		}
		if (removed) setProperty(OWLS.Process.components, components);
		return removed;
	}

	/* @see org.mindswap.owls.process.CollectionControlConstruct#removeComponents() */
	public void removeComponents()
	{
		OWLList<ControlConstruct> components = getComponents0();
		components = components.clear();
		setProperty(OWLS.Process.components, components);
	}

	/* @see org.mindswap.owls.process.CollectionControlConstruct#deleteComponents() */
	public void deleteComponents()
	{
		OWLList<ControlConstruct> components = getComponents0();
		while (!components.isEmpty())
		{
			ControlConstruct cc = components.getFirst();
			components = components.remove(cc);
			cc.delete();
		}
		setProperty(OWLS.Process.components, components);
	}

	/* @see impl.owl.WrappedIndividual#delete() */
	@Override
	public void delete()
	{
		deleteComponents();
		super.delete();
	}

	/* @see org.mindswap.owls.process.CollectionControlConstruct#size() */
	public int size()
	{
		return getComponents0().size();
	}

	/* @see java.lang.Iterable#iterator() */
	public Iterator<ControlConstruct> iterator()
	{
		return getComponents0().iterator();
	}

	final OWLList<ControlConstruct> getComponents0()
	{
		// The fact that we search in ontology (and not its KB) is a deliberate
		// decision. We want to make sure that subsequent modifications of the
		// list will be done in our ontology (and not the base ontology of the
		// KB, which would be the case if we would search the entire KB). This
		// is a slight limitation as we assume all statements that make up the
		// components list to be part of our ontology.
		// Note that if (some) statements that make up the components list would
		// be part of a imported ontology of our ontology they would be found,
		// but modifications would be written to our ontology.
		final OWLIndividual cs = getOntology().getProperty(this, OWLS.Process.components);
		if (cs != null) return cs.castToList(OWLS.Process.CCBag);

		// create new and empty components list and set it
		final OWLList<ControlConstruct> ncs =  getOntology().createControlConstructBag(null);
		setProperty(OWLS.Process.components, ncs);
		return ncs;
	}

	final OWLIndividualList<ControlConstruct> prepare(
		final OWLIndividualList<ControlConstruct> constructs, final ExecutionContext context)
	{
		final OWLIndividualList<ControlConstruct> cs = OWLFactory.createIndividualList(constructs.size());
		for (ControlConstruct cc : constructs)
		{
			cs.add(cc.prepare(context));
		}
		return cs;
	}
}
