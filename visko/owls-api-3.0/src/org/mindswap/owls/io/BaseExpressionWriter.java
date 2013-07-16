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
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.expression.Expression;
import org.mindswap.utils.QNameProvider;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class BaseExpressionWriter<B> implements ExpressionWriter<B>
{
	protected QNameProvider qnames;
	protected PrintWriter out;
	protected String indent;
	protected boolean firstLineIndent;

	public BaseExpressionWriter()
	{
		firstLineIndent = false;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#setWriter(java.io.Writer) */
	public void setWriter(final Writer out)
	{
		if (out instanceof PrintWriter) this.out = (PrintWriter) out;
		else this.out = new PrintWriter(out);
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#getWriter() */
	public PrintWriter getWriter()
	{
		return out;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#setWriter(java.io.OutputStream) */
	public void setWriter(final OutputStream out)
	{
		this.out = new PrintWriter(out);
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#getIndent() */
	public String getIndent()
	{
		return indent;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#setIndent(java.lang.String) */
	public void setIndent(final String indent)
	{
		this.indent = indent;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#getFirstLineIndent() */
	public boolean getFirstLineIndent()
	{
		return firstLineIndent;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#setFirstLineIndent(boolean) */
	public void setFirstLineIndent(final boolean indent)
	{
		firstLineIndent = indent;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#setQNames(org.mindswap.utils.QNameProvider) */
	public void setQNames(final QNameProvider qnames)
	{
		this.qnames = qnames;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#getQNames() */
	public QNameProvider getQNames()
	{
		return qnames;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#write(org.mindswap.owls.expression.Expression, java.io.Writer) */
	public void write(final Expression<B> expr, final Writer outWriter)
	{
		setWriter(outWriter);

		final boolean noQNameProvider = (qnames == null);
		if (noQNameProvider) qnames = expr.getKB().getQNames();

		write(expr.getBody());

		if (noQNameProvider) qnames = null;
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#write(org.mindswap.owls.expression.Expression, java.io.OutputStream) */
	public void write(final Expression<B> expr, final OutputStream outStream)
	{
		setWriter(outStream);
		write(expr);
	}

	/* @see org.mindswap.owls.io.ExpressionWriter#write(org.mindswap.owls.expression.Expression) */
	public void write(final Expression<B> expr)
	{
		write(expr.getBody());
	}

	/**
	 * Entry point for subclasses to implement the functionality required to
	 * write the body of an expression.
	 *
	 * @param exprBody The body of the expression to be written. Value can be
	 * 	<code>null</code> if the expression has no body.
	 */
	protected abstract void write(B exprBody);

	protected void print(final OWLValue value)
	{
		try
		{
			if (value instanceof OWLIndividual)
			{
				final OWLIndividual ind = (OWLIndividual) value;
				if (ind.isAnon()) out.print("<< Anonymous Individual >>");
				else print(ind.getURI());
			}
			else out.print(value);
		}
		catch (final RuntimeException e)
		{
			out.print("<< Invalid URI >>");
		}
	}

	protected void print(final OWLClass cls)
	{
		if (cls.isAnon()) out.print("<< Anonymous Class >>");
		else print(cls.getURI());
	}

	protected void print(final OWLProperty prop)
	{
		print(prop.getURI());
	}

	protected void print(final URI uri)
	{
		out.print(qnames.shortForm(uri));
	}

}
