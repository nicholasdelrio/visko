/*
 * Created 26.10.2008
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
package org.mindswap.owl;

import org.mindswap.exceptions.TransformationException;


/**
 * Transformators are used to transform arbitrary objects to and from OWL data
 * values or OWL individuals. Note that this includes the special case where the
 * object is also a OWL data value or OWL individual.
 * <p>
 * We may also say that a transformator <em>mediates</em> between two different
 * representations of some data. Most likely this transformation happens on a
 * syntactical level only, that is, maintaining the semantics of the data.
 * Nevertheless, it is also imaginable that a transformator not only mediates
 * different syntactical representations but would also change the semantics of
 * the data as part of the transformation process. The current contract of this
 * interface does not define how far this transformation process can go. This
 * is up to actual implementations.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLTransformator // public interface OWLTransformator<S, T extends OWLValue>
{
	/**
	 * Transform some object into an OWL data value respectively individual.
	 *
	 * @param source The source object to transform.
	 * @param model Optional OWL model possibly required to guide the
	 * 	transformation process. Usage is optional and depends on the
	 * 	actual transformator, that is, can be left <code>null</code>.
	 * @return The source object transformed into another representation.
	 * @throws TransformationException In case transformation failed (for
	 * 	whatever reason).
	 */
	public OWLValue toOWL(Object source, OWLModel model) throws TransformationException;
//	public T toOWL(S source, OWLModel model);

	/**
	 * Transform a OWL data value respectively individual into another
	 * representation.
	 *
	 * @param source The source OWL object to transform.
	 * @param model Optional OWL model possibly required to guide the
	 * 	transformation process. Usage is optional and depends on the
	 * 	actual transformator, that is, can be <code>null</code>.
	 * @return The source object transformed into another representation.
	 * @throws TransformationException In case transformation failed (for
	 * 	whatever reason).
	 */
	public Object fromOWL(OWLValue source, OWLModel model) throws TransformationException;
//	public S fromOWL(T source, OWLModel model);
}
