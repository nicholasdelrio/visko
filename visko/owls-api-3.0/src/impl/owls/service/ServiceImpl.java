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
package impl.owls.service;

import impl.owl.WrappedIndividual;

import java.util.List;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.Utils;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class ServiceImpl extends WrappedIndividual implements Service
{
	public ServiceImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.service.Service#addGrounding(org.mindswap.owls.grounding.Grounding) */
	public void addGrounding(final Grounding<?,?> grounding)
	{
		addProperty(OWLS.Service.supports, grounding);
		grounding.setService(this);
	}

	/* @see org.mindswap.owls.service.Service#addProfile(org.mindswap.owls.profile.Profile) */
	public void addProfile(final Profile profile)
	{
		addProperty(OWLS.Service.presents, profile);
		profile.setService(this);
	}

	/* @see org.mindswap.owls.service.Service#addProvider(org.mindswap.owl.OWLIndividual) */
	public void addProvider(final OWLIndividual provider)
	{
		addProperty(OWLS.Service.providedBy, provider);
		provider.addProperty(OWLS.Service.provides, this);
	}

	/* @see org.mindswap.owls.service.Service#getGrounding() */
	public Grounding<?,?> getGrounding()
	{
		return getPropertyAs(OWLS.Service.supports, Grounding.class);
	}

	/* @see org.mindswap.owls.service.Service#getGroundings() */
	public OWLIndividualList<Grounding> getGroundings()
	{
		return getPropertiesAs(OWLS.Service.supports, Grounding.class);
	}

	/* @see org.mindswap.owls.service.Service#getName() */
	public String getName()
	{
		final Profile profile = getProfile();
		return profile == null ? null : profile.getServiceName();
	}

	/* @see org.mindswap.owls.service.Service#getName(java.lang.String) */
	public String getName(final String lang)
	{
		final Profile profile = getProfile();
		return profile == null ? null : profile.getServiceName(lang);
	}

	/* @see org.mindswap.owls.service.Service#getNames() */
	public List<OWLDataValue> getNames()
	{
		final Profile profile = getProfile();
		return profile == null ? null : profile.getServiceNames();
	}

	/* @see org.mindswap.owls.service.Service#getProcess() */
	public Process getProcess()
	{
		return getPropertyAs(OWLS.Service.describedBy, Process.class);
	}

	/* @see org.mindswap.owls.service.Service#getProfile() */
	public Profile getProfile()
	{
		return getPropertyAs(OWLS.Service.presents, Profile.class);
	}

	/* @see org.mindswap.owls.service.Service#getProfiles() */
	public OWLIndividualList<Profile> getProfiles()
	{
		return getPropertiesAs(OWLS.Service.presents, Profile.class);
	}

	/* @see org.mindswap.owls.service.Service#getProviders() */
	public OWLIndividualList<?> getProviders()
	{
		return getProperties(OWLS.Service.providedBy);
	}

	/* @see org.mindswap.owls.service.Service#removeGrounding(org.mindswap.owls.grounding.Grounding) */
	public void removeGrounding(final Grounding<?,?> grounding)
	{
		if (hasProperty(OWLS.Service.supports, grounding))
		{
			if (grounding != null)
			{
				removeProperty(OWLS.Service.supports, grounding);
				grounding.removeService();
			}
			else
			{
				for (Grounding<?,?> g : getGroundings())
				{
					removeProperty(OWLS.Service.supports, g);
					g.removeService();
				}
			}
		}
	}

	/* @see org.mindswap.owls.service.Service#removeProcess() */
	public void removeProcess()
	{
		if (hasProperty(OWLS.Service.describedBy))
		{
			final Process process = getProcess();
			removeProperty(OWLS.Service.describedBy, null);
			if (process != null) process.removeService();
		}
	}

	/* @see org.mindswap.owls.service.Service#removeProfile(org.mindswap.owls.profile.Profile) */
	public void removeProfile(final Profile profile)
	{
		if (hasProperty(OWLS.Service.presents, profile))
		{
			if (profile != null)
			{
				removeProperty(OWLS.Service.presents, profile);
				profile.removeService();
			}
			else
			{
				for (Profile p : getProfiles())
				{
					removeProperty(OWLS.Service.presents, p);
					p.removeService();
				}
			}
		}
	}

	/* @see org.mindswap.owls.service.Service#removeProvider(org.mindswap.owl.OWLIndividual) */
	public void removeProvider(final OWLIndividual provider)
	{
		removeProperty(OWLS.Service.providedBy, provider);
	}

	/* @see org.mindswap.owls.service.Service#setName(java.lang.String) */
	public void setName(final String name)
	{
		final Profile profile = getProfile();
		if (profile != null) profile.setServiceName(name);
	}

	/* @see org.mindswap.owls.service.Service#setProcess(org.mindswap.owls.process.Process) */
	public void setProcess(final Process process)
	{
		if (hasProperty(OWLS.Service.describedBy, process)) return;
		setProperty(OWLS.Service.describedBy, process);
		process.setService(this);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder();
		str.append("Service    ");
		Object o = getLabel(null);
		if (o != null) str.append(o).append(" ");
		o = getURI();
		if (o != null) str.append(o);
		str.append(Utils.LINE_SEPARATOR);
		str.append("Profiles   ").append(getProfiles()).append(Utils.LINE_SEPARATOR);
		str.append("Process    ").append(getProcess()).append(Utils.LINE_SEPARATOR);
		str.append("Groundings ").append(getGroundings()).append(Utils.LINE_SEPARATOR);
		return str.toString();
	}

}
