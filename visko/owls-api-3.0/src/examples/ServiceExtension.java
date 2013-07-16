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

import impl.owl.GenericOWLConverter;
import impl.owls.service.ServiceImpl;

import java.net.URI;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.RDFS;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.WSDLGrounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.URIUtils;

/**
 * Another example demonstrating the extensibility features of the API.
 *
 * This example shows how the default Service implementation can be extended to support
 * custom defined extensions of the service.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ServiceExtension
{
	private static final String EXTENDED_SERVICE_CLASS_NAME = "ExtendedService";

	public static void main(final String[] args) throws Exception
	{
		final ServiceExtension test = new ServiceExtension();

		// Add converter to return ExtendedService descriptions
		final OWLObjectConverter<ExtendedService> converter = new ExtendedServiceConverter();
		OWLObjectConverterRegistry.instance().registerConverter(ExtendedService.class, converter); // (1)
		OWLObjectConverterRegistry.instance().extendByConverter(Service.class, converter); // (2)

		test.runRead();
		System.out.println("");
		test.runCreate();
	}

	/**
	 * Programmatically create new OWL-S extended service profile and print basic
	 * informations about service, profile, process, grounding, ...
	 */
	public void runCreate() throws Exception
	{
		// Create a KB and an ontology
		final OWLKnowledgeBase kb = OWLFactory.createKB();
		kb.setReasoner("Pellet");
		final OWLOntology ont = kb.createOntology(ExampleURIs.SERVICE_EXTENSION_OWLS12);

		// ont is empty, hence, we create the extended service OWL class in order to stay in OWL DL
		final OWLClass ExtendedService = ont.createClass(
			URIUtils.createURI(ExampleURIs.SERVICE_EXTENSION_OWLS12, EXTENDED_SERVICE_CLASS_NAME));
		// make it a subclass of Service
		ExtendedService.addProperty(RDFS.subClassOf, OWLS.Service.Service);

		// create exemplary extended service (triple (<uri> rdf:type <ExtendedService>) added to ont)
		final URI serviceURI = URIUtils.createURI(ExampleURIs.SERVICE_EXTENSION_OWLS12, "TestService");
		final OWLIndividual serviceInd = ont.createInstance(ExtendedService, serviceURI);

		// cast is possible because of (2) and KB contains triple (ind rdf:type <ExtendedService>)
		final Service s = serviceInd.castTo(Service.class);

		// cast is possible because of (1) and KB contains triple (ind rdf:type <ExtendedService>)
		// we could also cast from 'serviceInd' instead of 's'
		final ExtendedService service = s.castTo(ExtendedService.class);

		final AtomicProcess process = ont.createAtomicProcess(
			URIUtils.createURI(ExampleURIs.SERVICE_EXTENSION_OWLS12, "TestProcess"));
		final Profile profile = ont.createProfile(
			URIUtils.createURI(ExampleURIs.SERVICE_EXTENSION_OWLS12, "TestProfile"));
		final WSDLGrounding grounding = ont.createWSDLGrounding(
			URIUtils.createURI(ExampleURIs.SERVICE_EXTENSION_OWLS12, "TestGrounding"));
		service.addProfile(profile);
		service.setProcess(process);
		service.addGrounding(grounding);
		service.setAdditionalProperty("test");

		// Print the results
		System.out.println("Create extended service:");
		System.out.println("Service name: " + service.getLocalName());
		System.out.println("Profile name: " + service.getProfile().getLocalName());
		System.out.println("Process name: " + service.getProcess().getLocalName());
		System.out.println("Grounding name: " + service.getGrounding().getLocalName());
		System.out.println("additional property: " + service.getAdditionalProperty());
	}

	/**
	 * Read already existing OWL-S extended service and print basic
	 * informations about service, profile, process, grounding, ...
	 */
	public void runRead() throws Exception
	{

		// Create a KB
		final OWLKnowledgeBase kb = OWLFactory.createKB();
		// use a reasoner that will understand class and property inheritance
		kb.setReasoner("Pellet");

		// Load an example service description; the service in the file is a ExtendedService
		final Service s = kb.readService(ExampleURIs.SERVICE_EXTENSION_OWLS12);

		final ExtendedService service = s.castTo(ExtendedService.class);
		final Profile profile = service.getProfile();
		final Process process = service.getProcess();
		final Grounding<?,?> grounding = service.getGrounding();
		final String additionalProperty = service.getAdditionalProperty();

		// Print the results
		System.out.println("Read extended service:");
		System.out.println("Service name: " + service.getLocalName());
		System.out.println("Profile name: " + profile.getLocalName());
		System.out.println("Process name: " + process.getLocalName());
		System.out.println("Grounding name: " + grounding.getLocalName());
		System.out.println("additional property: " + additionalProperty);
	}


	/**
	 * An extension to existing Service implementation extended by an additional
	 * property.
	 */
	public static class ExtendedService extends ServiceImpl
	{
		OWLDataProperty additionalProperty = getOntology().createDataProperty(
			URIUtils.createURI(ExampleURIs.SERVICE_EXTENSION_OWLS12, "additionalProperty"));

		public ExtendedService(final OWLIndividual ind) {
			super(ind);
		}

		public String getAdditionalProperty() {
			return getPropertyAsString(additionalProperty);
		}

		public void setAdditionalProperty(final String value) {
			setProperty(additionalProperty, value);
		}
	}

	/**
	 * A simple converter that will wrap existing OWLIndividuals as ExtendedProfile
	 * objects. Very similar to {@link GenericOWLConverter}.
	 */
	public static class ExtendedServiceConverter implements OWLObjectConverter<ExtendedService>
	{
		// the OWL class defined for extended services
		private static final OWLClass ExtendedService = OWLFactory.createKB().createClass(
			URIUtils.createURI(ExampleURIs.SERVICE_EXTENSION_OWLS12, EXTENDED_SERVICE_CLASS_NAME));

		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			return (object instanceof OWLIndividual) &&
				((OWLIndividual) object).isType(ExtendedService);
		}

		public ExtendedService cast(final OWLObject object, final boolean strictConversion)
		{
			if (canCast(object, strictConversion)) return new ExtendedService((OWLIndividual) object);
			throw CastingException.create(object, ExtendedService.class);
		}
	}
}
