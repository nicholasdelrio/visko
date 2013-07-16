// The MIT License
//
// Copyright (c) 2009 Thorsten Möller - University of Basel Switzerland
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
package org.mindswap.owls.validator;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.mindswap.owls.service.Service;

/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:55 $
 */
public class OWLSValidatorReport
{
	private final Map<Service, Set<OWLSValidatorMessage>> messageMap;

	public OWLSValidatorReport(final Map<Service, Set<OWLSValidatorMessage>> theMsgs)
	{
		messageMap = theMsgs;
	}

	public void print(final PrintStream theOut)
	{
		theOut.println("Validation Report");
		if (messageMap.isEmpty())
		{
			theOut.println("Valid:\ttrue");
			return;
		}

		for (Entry<Service, Set<OWLSValidatorMessage>> entry : messageMap.entrySet())
		{
			Service aService = entry.getKey();
			Set<OWLSValidatorMessage> msgSet = entry.getValue();
			boolean valid = msgSet.isEmpty();

			theOut.println("Service:\t" + aService);
			theOut.println("Valid:\t\t" + valid);
			if (!valid)
			{
				theOut.println("Validation messages: ");
				for (OWLSValidatorMessage msg : msgSet)
				{
					theOut.println(msg);
				}
			}
		}
	}

	public Map<Service, Set<OWLSValidatorMessage>> getMessages()
	{
		return messageMap;
	}

}
