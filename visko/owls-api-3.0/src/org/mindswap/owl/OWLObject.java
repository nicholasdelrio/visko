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
 * Created on Dec 12, 2004
 */
package org.mindswap.owl;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLObject
{
	/**
	 * @return The underlying object representation as it is represented in the
	 * 	underlying (low level) API.
	 */
	public Object getImplementation();

	/**
	 * @return If this object can be viewed as other object type(s) the next one is
	 * 	returned. Such views are lazily initialized, that is, whenever
	 * 	{@link #castTo(Class)} was successful in casting to another object type
	 * 	this view instance is internally added (and only then it can be accessed
	 * 	by this method). Otherwise, this method returns <code>this</code>.
	 */
	public OWLObject getNextView();

	/**
	 * This method should be used internally only!
	 *
	 * @param nextView Set another object representation.
	 */
	public void setNextView(OWLObject nextView);

	/**
	 * OWLObjects can be casted to (viewed as) different implementation types.
	 * This method tries to cast this object into another OWL type, if possible.
	 * Whether this is possible depends on the axioms, assertions, and inferences
	 * in the OWL ontology or knowledge base this object is a member of.
	 * <p>
	 * If this instance has no model attached, it can only be viewed as a type
	 * it already has, see {@link #getNextView()}.
	 *
	 * @param target The target OWL object type.
	 * @return This object casted to the target type.
	 * @throws CastingException If this object can not be casted to the target.
	 * @throws NullPointerException If <code>target</code> is <code>null</code>.
	 */
	public <T extends OWLObject> T castTo(Class<T> target);

	/**
	 * This method is quite similar to {@link #canCastTo(Class)} except that it
	 * checks if this OWL object can be viewed as a {@link OWLList} according to
	 * the given list vocabulary (which specifies the properties that need to be
	 * provided in order to be viewable as this kind of list).
	 *
	 * @param vocabulary The vocabulary supposed to be used by the target list.
	 * @return <code>true</code> if this object can be viewed as an instance of
	 * 	OWLList containing items that can contain items of the given class.
	 */
	public boolean canCastToList(final ListVocabulary<? extends OWLValue> vocabulary);

	/**
	 * Check if this object can be viewed as an instance of target type, that is,
	 * if it has already been viewed in this way, or if it has an attached model
	 * in which it has properties that permit it to be viewed in this way. If
	 * this method returns <code>true</code>, {@link #castTo(Class)} on this
	 * instance should return an instance of <code>target</code>.
	 *
	 * @param target The target type.
	 * @return <code>true</code> if this object can be viewed as an instance of
	 * 	the target type.
	 */
	public boolean canCastTo(Class<? extends OWLObject> target);

	/**
	 * This method tries to cast this object into a {@link OWLList}, if possible.
	 * Whether this is possible depends on the axioms, assertions asserted about
	 * this OWL object and that will be matched again the given vocabulary.
	 *
	 * @param vocabulary The vocabulary supposed to be used by the target list.
	 * @return This object casted to OWLList and parameterized according to the
	 * 	list item class.
	 * @throws CastingException If this object can not be casted.
	 * @throws NullPointerException If <code>vocabulary</code> is <code>null</code>.
	 */
	public <T extends OWLValue> OWLList<T> castToList(final ListVocabulary<T> vocabulary);
}
