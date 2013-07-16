/*
 * Created 14.08.2009
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
package org.mindswap.owls.process;

import org.mindswap.common.Visitor;
import org.mindswap.owls.process.execution.ExecutionSupport;

/**
 * In contrast to {@link ExecutionSupport}, this interface realizes a general
 * visitor pattern for control constructs intended for public use. The former
 * is meant to be implemented by execution engines only.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface ControlConstructVisitor extends Visitor<ControlConstructVisitor, ControlConstruct>
{
	public void visit(AnyOrder ao);
	public void visit(AsProcess ap);
	public void visit(Choice ch);
	public void visit(ForEach fe);
	public void visit(IfThenElse ite);
	public void visit(Perform pf);
	public void visit(Produce pr);
	public void visit(RepeatUntil ru);
	public void visit(RepeatWhile rw);
	public void visit(Sequence sq);
	public void visit(Set set);
	public void visit(Split st);
	public void visit(SplitJoin sj);
}
