// The MIT License
//
// Copyright (c) 2005 Evren Sirin
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

import org.mindswap.owls.expression.Expression;
import org.mindswap.utils.QNameProvider;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface ExpressionWriter<B>
{
    public void setWriter(Writer out);

    public Writer getWriter();

    public void setWriter(OutputStream out);

    public void write(Expression<B> expr);

    public void write(Expression<B> expr, Writer out);

    public void write(Expression<B> expr, OutputStream out);

    public String getIndent();

    public void setIndent(String indent);

    public boolean getFirstLineIndent();

    public void setFirstLineIndent(boolean indent);

    public void setQNames(QNameProvider qnames);

    public QNameProvider getQNames();
}
