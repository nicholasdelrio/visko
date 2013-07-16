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
 * Created on Dec 27, 2003
 *
 */
package org.mindswap.owls.service;

import java.util.List;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.profile.Profile;

/**
 * Represents the notion of OWL-S services.
 * <p>
 * OWL-S concept: http://www.daml.org/services/owl-s/1.2/Service.owl#Service
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:55 $
 */
public interface Service extends OWLIndividual
{

	/**
	 * Get a profile presented by to this service. If this service presents
	 * multiple profiles the result is arbitrarily selected. Consider to use
	 * {@link #getProfiles()} instead.
	 *
	 * @return A profile presented by this service.
	 */
	public Profile getProfile();

	/**
	 * Get all profile presented by to this service.
	 *
	 * @return All profiles presented by this service.
	 */
	public OWLIndividualList<Profile> getProfiles();

	/**
	 * Get the process described by this service.
	 *
	 * @return The process described by this service or <code>null</code> if this
	 * 	service
	 */
	public Process getProcess();

	/**
	 * @return Resources that provide this service.
	 */
	public OWLIndividualList<?> getProviders();

	/**
	 * Get the grounding that this service supports. If this service has
	 * multiple groundings the result is arbitrarily selected. Consider to use
	 * {@link #getGroundings()} instead.
	 *
	 * @return The grounding of the service.
	 */
	public Grounding<?,?> getGrounding();

	/**
	 * Get all groundings that this service supports.
	 *
	 * @return The groundings that this service supports.
	 */
	public OWLIndividualList<Grounding> getGroundings();

	/**
	 * Add a profile to be presented by this service.
	 *
	 * @param profile The profile to add.
	 */
	public void addProfile(Profile profile);

	/**
	 * Add a provider of this service.
	 *
	 * @param provider The provider of this service to add.
	 */
	public void addProvider(OWLIndividual provider);

	/**
	 * Set the process described by this service.
	 *
	 * @param process The process to set.
	 */
	public void setProcess(Process process);

	/**
	 * Add a grounding to be supported by this service. Note that implementations
	 * are not obliged to check if a grounding of the same type already exists.
	 * Consequently, a implementation may simply add the given grounding rather
	 * than re-using the grounding of the same type that already exists and
	 * add all atomic process groundings of the given grounding to the one that
	 * already exists. Hence, if it is important to prevent duplicated groundings
	 * of the same type you should only add a grounding if none of the same type
	 * exists, see {@link #getGroundings()}. Otherwise, use the existing grounding
	 * to manage the set of atomic process groundings.
	 *
	 * @param grounding The grounding to add.
	 */
	public void addGrounding(Grounding<?,?> grounding);

	/**
	 * Removes the profile presented by this service by breaking the link
	 * <code>service:presents</code>. The profile itself remains untouched.
	 *
	 * @param profile The profile to remove. A value of <code>null</code> will remove
	 * 	all profiles.
	 */
	public void removeProfile(Profile profile);

	/**
	 * Removes the provider of this service by breaking the link
	 * <code>service:providedBy</code>. The provider itself remains untouched.
	 *
	 * @param provider The provider to remove. A value of <code>null</code> will remove
	 * 	all service providers.
	 */
	public void removeProvider(OWLIndividual provider);

	/**
	 * Removes the process described by this service by breaking the link
	 * <code>service:describedBy</code>. The process itself remains untouched.
	 */
	public void removeProcess();

	/**
	 * Removes the grounding that this service supports by breaking the link
	 * <code>service:supports</code>. The grounding itself remains untouched.
	 *
	 * @param grounding The grounding to remove. A value of <code>null</code> will remove
	 * 	all groundings.
	 */
	public void removeGrounding(Grounding<?,?> grounding);

	/**
	 * @return The service name defined in the profile of this service. Check
	 * 	out {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig} to see
	 * 	how language identifiers will be resolved when searching for a name.
	 */
	public String getName();

	/**
	 * Get the service name defined in the profile of this service. The
	 * associated service name should have the same language identifier as given
	 * in the parameter.
	 *
	 * @param lang The language identifier to use.
	 * @return The name of this service. If a service name for that language is
	 * 	not found <code>null</code> will be returned even if there is another
	 * 	service name with a different language identifier.
	 */
	public String getName(String lang);

	/**
	 * @param name The name of this service using the given language identifier,
	 * 	which can be <code>null</code>.
	 */
	public void setName(String name);

	/**
	 * @return All service names written in any language. Use
	 * 	{@link OWLDataValue#getLanguage()} to retrieve the language of the
	 * 	corresponding name.
	 */
	public List<OWLDataValue> getNames();

}
