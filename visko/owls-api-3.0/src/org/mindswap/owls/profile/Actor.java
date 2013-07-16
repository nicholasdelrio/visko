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
 * Created on Jan 18, 2004
 */
package org.mindswap.owls.profile;

import java.net.URL;

import org.mindswap.owl.OWLIndividual;

/**
 * Represents the OWL-S actor.
 * <p>
 * Corresponding OWL-S concept: {@link org.mindswap.owls.vocabulary.OWLS.Actor#Actor}.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface Actor extends OWLIndividual
{
	public String getName();
	public void setName(String name);

	public String getTitle();
	public void setTitle(String title);

	public String getPhone();
	public void setPhone(String phone);

	public String getFax();
	public void setFax(String fax);

	public String getEMail();
	public void setEMail(String email);

	public String getAddress();
	public void setAddress(String address);

	public URL getURL();
	public void setURL(URL url);
}
