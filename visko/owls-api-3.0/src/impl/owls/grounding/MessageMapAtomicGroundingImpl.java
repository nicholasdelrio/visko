/*
 * Created 27.09.2009
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

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.utils.URIUtils;

/**
 * Yet another layer of abstraction to be subclassed by atomic groundings that
 * use input and output {@link MessageMap message maps}.
 *
 * @author unascribed
 * @version $Rev: 2333 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public abstract class MessageMapAtomicGroundingImpl<T> extends AtomicGroundingImpl<T>
{
	private OWLIndividualList<? extends MessageMap<T>> inputMessageMappings;
	private OWLIndividualList<? extends MessageMap<T>> outputMessageMappings;

	public MessageMapAtomicGroundingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#addInputMap(org.mindswap.owls.grounding.MessageMap) */
	public void addInputMap(final MessageMap<T> map)
	{
		inputMessageMappings = null;
		addProperty(inputMessageMapProperty(), map);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#addMessageMap(org.mindswap.owls.process.variable.Parameter, java.lang.String, java.lang.Object) */
	public void addMessageMap(final Parameter owlsParameter, final String groundingParameter, final T transformation)
	{
		MessageMap<T> map = null;
		if (owlsParameter instanceof Input)
		{
			map = createInputMessageMap();
			addInputMap(map);
		}
		else if (owlsParameter instanceof Output)
		{
			map = createOutputMessageMap();
			addOutputMap(map);
		}
		else throw new IllegalArgumentException("OWL-S parameter is neither an Input nor Output!");

		map.setOWLSParameter(owlsParameter);
		map.setGroundingParameter(groundingParameter);

		if (transformation != null) map.setTransformation(transformation);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#addOutputMap(org.mindswap.owls.grounding.MessageMap) */
	public void addOutputMap(final MessageMap<T> map)
	{
		outputMessageMappings = null;
		addProperty(outputMessageMapProperty(), map);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getInputMappings() */
	public OWLIndividualList<? extends MessageMap<T>> getInputMappings()
	{
		if (inputMessageMappings == null)
		{
			inputMessageMappings = getPropertiesAs(inputMessageMapProperty(), messageMapType());
		}
		return inputMessageMappings;
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getOutputMappings() */
	public OWLIndividualList<? extends MessageMap<T>> getOutputMappings()
	{
		if (outputMessageMappings == null)
		{
			outputMessageMappings = getPropertiesAs(outputMessageMapProperty(), messageMapType());
		}
		return outputMessageMappings;
	}

	/**
	 * Utility method to be used by subclasses. It iterates either through the
	 * {@link #getInputMappings() input message maps} (<code>p instanceof Input</code>)
	 * or the {@link #getOutputMappings() output message maps}
	 * (<code>p instanceof Output</code>). Each mapping is checked if its
	 * {@link MessageMap#getOWLSParameter() parameter} is equal to the given
	 * parameter. If so, this message map will be returned.
	 */
	protected final MessageMap<T> getMessageMap(final Parameter p)
	{
		for (final MessageMap<T> messageMap : ((p instanceof Input)? getInputMappings() : getOutputMappings()))
		{
			if (messageMap.getOWLSParameter().equals(p))	return messageMap;
		}
		return null;
	}

	/**
	 * Utility method to be used by subclasses. It iterates either through the
	 * {@link #getInputMappings() input message maps} (<code>inputs == true</code>)
	 * or the {@link #getOutputMappings() output message maps}
	 * (<code>inputs == false</code>). Each mapping is checked if its
	 * {@link MessageMap#getGroundingParameter()} (which is actually a URI)
	 * is equal to the given URI. If the URIs do not fully match but they contain
	 * a fragment identifier those fragments will be compared alternatively. As
	 * soon as either the full URIs match or the fragment identifiers, the
	 * message map will be returned.
	 */
	protected final MessageMap<T> getMessageMap(final boolean inputs, final String uri)
	{
		String name = URIUtils.getLocalName(uri);
		for (final MessageMap<T> map : (inputs? getInputMappings() : getOutputMappings()))
		{
			final String u = map.getGroundingParameter();
			String n = URIUtils.getLocalName(u);
			if (uri.equals(u) || (name != null && name.equals(n))) return map;
		}

		return null;
	}

	/** Remove and delete all input (<code>true</code>) or output (<code>false</code>) message maps. */
	protected void removeMessageMaps(final boolean inputs)
	{
		OWLObjectProperty prop = (inputs)? inputMessageMapProperty() : outputMessageMapProperty();
		if (hasProperty(prop))
		{
			for (final OWLIndividual ind : getProperties(prop))
			{
				removeProperty(prop, ind);
				ind.delete();
			}
		}
	}

	protected abstract MessageMap<T> createInputMessageMap();
	protected abstract MessageMap<T> createOutputMessageMap();
	protected abstract OWLObjectProperty inputMessageMapProperty();
	protected abstract OWLObjectProperty outputMessageMapProperty();
	protected abstract Class<? extends MessageMap<T>> messageMapType();

}
