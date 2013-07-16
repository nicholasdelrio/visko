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
 * Created on Jan 6, 2005
 */
package examples;

import impl.owl.WrappedIndividual;
import impl.owls.profile.ProfileImpl;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * An example demonstrating the extensibility features of the API.
 *
 * This example shows how the default Profile implementation can be extended
 * to support custom defined extensions of the.
 *
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class OWLSExtensions {
	public static void main(final String[] args) throws Exception {
		final OWLSExtensions test = new OWLSExtensions();
		test.run();
	}

	public void run() throws Exception {
		// register a converter for Owner class
		OWLObjectConverterRegistry.instance().registerConverter(OwnerEntity.class, new OwnerConverter());
		// Override the default Profile converter to return ExtendedProfile descriptions
		OWLObjectConverterRegistry.instance().registerConverter(Profile.class, new ExtendedProfileConverter());

		// Create a KB
		final OWLKnowledgeBase kb = OWLFactory.createKB();
		// use a reasoner that will understand class and property inheritance
		kb.setReasoner("Pellet");

		// Load an example service description
		final Service s = kb.readService(ExampleURIs.DICTIONARY_OWLS12);
		// Cast the profile to an ExtendedProfile (since default converter is overridden)
		final ExtendedProfile profile = s.getProfile().castTo(ExtendedProfile.class);
		// Get the owner info
		final OwnerEntity owner = profile.getOwner();

		// Print the results
		System.out.println("Service name: " + profile.getServiceName());
		System.out.println();

		System.out.println("Display service parameters using generic functions");
		System.out.println("--------------------------------------------------");
		final OWLIndividualList<ServiceParameter> params = profile.getServiceParameters();
		for (ServiceParameter param : params)
		{
			System.out.println("Service Parameter: ");
			System.out.println("  Name  : "  + param.getName());
			System.out.println("  Value : "  + param.getParameter());
		}
		System.out.println();

		System.out.println("Display service parameters using custom functions");
		System.out.println("-------------------------------------------------");
		System.out.println("Owner: ");
		System.out.println("  Name : "  + owner.getLabel(null));
		System.out.println("  ID   : "  + owner.getEntityID());
	}

	/**
	 * An extension to existing Profile implementation to return contact information.
	 *
	 */
	public static class ExtendedProfile extends ProfileImpl {
		/**
		 * Wrap the given OWLIndividual as a profile instance.
		 */
		public ExtendedProfile(final OWLIndividual ind) {
			super(ind);
		}

		/**
		 * Simply query the ontology for contactInformation property and cast the
		 * result to an Actor object.
		 */
		public OwnerEntity getOwner() {
			final OWLIndividual ind = getServiceParameterValue(FLAServiceOnt.ownedBy);
			return (ind == null ) ? null : ind.castTo(OwnerEntity.class);
		}
	}

	/**
	 * A wrapper around an OWLIndividual that defines utility functions to access
	 * Owner information defined in {@link FLAServiceOnt} ontology.
	 */
	public static class OwnerEntity extends WrappedIndividual
	{
		public OwnerEntity(final OWLIndividual ind) {
			super(ind);
		}

		public String getEntityID() {
			return getPropertyAsString(FLAServiceOnt.ownerEntityID);
		}
	}

	/**
	 * A simple converter that will wrap existing OWLIndividuals as OwnerEntity
	 * objects. The individual should have (ind rdf:type fla:OwnerEntity) triple
	 * in order for the wrapping to work.
	 */
	public static class OwnerConverter implements OWLObjectConverter<OwnerEntity>
	{
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			return (object instanceof OWLIndividual) &&
				((OWLIndividual) object).isType(FLAServiceOnt.OwnerEntity);
		}

		public OwnerEntity cast(final OWLObject object, final boolean strictConversion)
		{
			if (canCast(object, strictConversion)) return new OwnerEntity((OWLIndividual) object);

			throw CastingException.create(object, OwnerEntity.class);
		}
	}

	/**
	 * A simple converter that will wrap existing OWLIndividuals as ExtendedProfile objects.
	 * Very similar to the default Profile converter.
	 */
	public static class ExtendedProfileConverter implements OWLObjectConverter<Profile>
	{
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			return (object instanceof OWLIndividual) && ((OWLIndividual) object).isType(OWLS.Profile.Profile);
		}

		public Profile cast(final OWLObject object, final boolean strictConversion)
		{
			if (canCast(object, strictConversion)) return new ExtendedProfile((OWLIndividual) object);
			throw CastingException.create(object, ExtendedProfile.class);
		}
	}
}
