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
 * Created on Jan 23, 2004
 */
package org.mindswap.owl;

/**
 * Basic interface for OWL error handlers. {@link OWLReader OWL readers} use
 * instances of this interface to notify them about warnings and errors when
 * parsing some OWL file (into an in-memory model).
 * <p>
 * In terms of its functional specification, this interface is almost equivalent
 * to {@link org.xml.sax.ErrorHandler SAX parser error handler}.
 *
 * @author unascribed
 * @version $Rev: 2124 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLErrorHandler
{
	/**
	 * Receive notification of a warning.
	 * <p>
	 * {@link OWLReader} will use this method to report conditions that are not
	 * errors or fatal errors. The default behavior is to take no action.
	 * </p>
	 * <p>
	 * The OWL reader is able to continue normally reading/parsing the document
	 * to the end after invoking this method.
	 * <p>
	 *
	 * @param exception The warning information encapsulated in an exception.
	 */
	public void warning(Exception exception);

	/**
	 * Receive notification of a recoverable error.
	 * <p>
	 * It should still be possible for the {@link OWLReader} to read/parse the
	 * OWL document through to the end. If the reader cannot do so, then it
	 * should report a fatal error.
	 * </p>
	 *
	 * @param exception The error information encapsulated in an exception.
	 */
	public void error(Exception exception);

	/**
	 * Receive notification of a non-recoverable error.
	 * <p>
	 * The application must assume that the OWL document the {@link OWLReader} is
	 * currently reading/parsing is unusable after the reader has invoked this
	 * method, and should continue (if at all) only for the sake of collecting
	 * additional error messages: in fact, OWL readers are free to stop reporting
	 * any other events once this method has been invoked.
	 * </p>
	 *
	 * @param exception The error information encapsulated in an exception.
	 */
	public void fatal(Exception exception);
}
