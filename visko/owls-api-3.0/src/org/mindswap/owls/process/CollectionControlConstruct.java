/*
 * Created 10.09.2009
 *
 * The MIT License
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
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
package org.mindswap.owls.process;


/**
 * Just another layer of abstraction for control constructs that are either
 * based on bags (such as {@link AnyOrder} or {@link Split}), or lists
 * (such as {@link Sequence}).
 *
 * @author unascribed
 * @version $Rev: 2321 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface CollectionControlConstruct extends ControlConstruct, Iterable<ControlConstruct>
{
//	/**
//	 * Returns the control constructs this collection is composed of. Note that
//	 * modifying operations on the returned are <strong>not</strong> reflected,
//	 * in the backing OWL model. Use the modification operations provided by this
//	 * interface instead to safely modify the components of this collection.
//	 *
//	 * @return The control constructs this collection contains. Returns the empty
//	 * 	list if this collection contains no control constructs.
//	 */
//	public OWLIndividualList<ControlConstruct> getComponents();

	/**
	 * Adds a control construct to this collection. Note that the order of
	 * components may or may not be relevant depending on the actual sub class.
	 * Also, the same control construct may be added multiple times.
	 *
	 * @param component The control construct to add to this collection.
	 */
	public void addComponent(ControlConstruct component);

	/**
	 * Removes all control constructs contained in this collection, that is,
	 * empties the components list. This collection and previously containing
	 * control constructs remain untouched. Use {@link #deleteComponents()} if
	 * you want to delete all components entirely.
	 */
	public void removeComponents();

	/**
	 * Removes and entirely deletes all components of this collection in the
	 * backing KB.
	 */
	public void deleteComponents();

	/**
	 * @return Number of elements this collection contains. This is <strong>
	 * 	not</strong> the number of distinct control constructs, i.e., if a
	 * 	named control construct is contained <tt>n</tt> (<tt>n</tt> &gt; 1)
	 * 	times it will be counted <tt>n</tt> times (and not just once)!
	 */
	public int size();
}
