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
 * Created on May 7, 2005
 */
package org.mindswap.owls.io;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;

import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.process.Process;
import org.mindswap.swrl.Atom;
import org.mindswap.utils.QNameProvider;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface ProcessWriter
{
	public void setWriter(Writer out);

	public Writer getWriter();

	public void setWriter(OutputStream out);

	public ExpressionWriter<String> getSparqlExpressionWriter();

	public ExpressionWriter<OWLList<Atom>> getSwrlExpressionWriter();

	public void write(Process process);

	public void write(Process process, Writer out);

	public void write(Process process, OutputStream out);

	public void write(Collection<Process> processes);

	public void write(Collection<Process> processes, Writer out);

	public void write(Collection<Process> processes, OutputStream out);

	public QNameProvider getQNames();

	public void setQNames(QNameProvider qnames);
}
