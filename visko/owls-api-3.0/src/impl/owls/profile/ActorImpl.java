//The MIT License
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
 * Created on Jan 7, 2005
 */
package impl.owls.profile;

import impl.owl.WrappedIndividual;

import java.net.URL;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.profile.Actor;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * A wrapper around an OWLIndividual that defines utility functions to access
 * contact information.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ActorImpl extends WrappedIndividual implements Actor
{
	public ActorImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.profile.Actor#getName() */
	public String getName()
	{
		return getPropertyAsString(OWLS.Actor.name);
	}

	/* @see org.mindswap.owls.profile.Actor#getTitle() */
	public String getTitle()
	{
		return getPropertyAsString(OWLS.Actor.title);
	}

	/* @see org.mindswap.owls.profile.Actor#getURL() */
	public URL getURL()
	{
		return getPropertyAsURL(OWLS.Actor.webURL);
	}

	/* @see org.mindswap.owls.profile.Actor#getEMail() */
	public String getEMail()
	{
		return getPropertyAsString(OWLS.Actor.email);
	}

	/* @see org.mindswap.owls.profile.Actor#getPhone() */
	public String getPhone()
	{
		return getPropertyAsString(OWLS.Actor.phone);
	}

	/* @see org.mindswap.owls.profile.Actor#getFax() */
	public String getFax()
	{
		return getPropertyAsString(OWLS.Actor.fax);
	}

	/* @see org.mindswap.owls.profile.Actor#getAddress() */
	public String getAddress()
	{
		return getPropertyAsString(OWLS.Actor.physicalAddress).trim();
	}

	/* @see org.mindswap.owls.profile.Actor#setName(java.lang.String) */
	public void setName(final String name)
	{
		setProperty(OWLS.Actor.name, name);
	}

	/* @see org.mindswap.owls.profile.Actor#setURL(java.net.URL) */
	public void setURL(final URL url)
	{
		setProperty(OWLS.Actor.webURL, url);
	}

	/* @see org.mindswap.owls.profile.Actor#setEMail(java.lang.String) */
	public void setEMail(final String email)
	{
		setProperty(OWLS.Actor.email, email);
	}

	/* @see org.mindswap.owls.profile.Actor#setPhone(java.lang.String) */
	public void setPhone(final String phone)
	{
		setProperty(OWLS.Actor.phone, phone);
	}

	/* @see org.mindswap.owls.profile.Actor#setFax(java.lang.String) */
	public void setFax(final String fax)
	{
		setProperty(OWLS.Actor.fax, fax);
	}

	/* @see org.mindswap.owls.profile.Actor#setAddress(java.lang.String) */
	public void setAddress(final String address)
	{
		setProperty(OWLS.Actor.physicalAddress, address);
	}

	/* @see org.mindswap.owls.profile.Actor#setTitle(java.lang.String) */
	public void setTitle(final String title)
	{
		setProperty(OWLS.Actor.title, title);
	}
}