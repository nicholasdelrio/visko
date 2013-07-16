/*
 * Created on Apr 14, 2005
 */
package examples;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceCategory;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;

/**
 * This example shows how to access {@link Profile#getServiceParameters()} and
 * {@link Profile#getCategories()} values. Different reasoners will provide
 * different information depending on their capabilities. When there is no
 * reasoner associated with the KB, no information about some profile can be
 * retrieved because it cannot be verified that profile is actually an instance
 * of profile:Profile concept. If strict type checking is disabled (see
 * {@link OWLKnowledgeBase#setStrictConversion(boolean)}) then some information
 * can be retrieved.
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ServiceParameterExample {
	private final OWLKnowledgeBase kb = OWLFactory.createKB();

	public static void main(final String[] args) throws Exception {
		final ServiceParameterExample test = new ServiceParameterExample();
		test.run();
	}

	public void run() throws Exception {
		System.out.print("Reading service... ");
		final long time = System.currentTimeMillis();

		// http://www.daml.org/services/owl-s/1.2/BravoAirService.owl indirectly
		// imports time-entry.owl which was relocated to another location
		kb.getReader().setIgnoreFailedImport(true);
		final Service service = kb.readService(ExampleURIs.BRAVO_AIR_SERVICE_OWLS12);
		System.out.println("done, took " + (System.currentTimeMillis() - time) + "ms");

		printProfileInfo(service, null);
		printProfileInfo(service, "RDFS"); // Jena RDFS reasoner
		printProfileInfo(service, "OWLMicro"); // Jena OWL-Micro reasoner
		printProfileInfo(service, "Pellet");

		kb.setStrictConversion(false);
		printProfileInfo(service, null);
	}

	public void printProfileInfo( final Service s, final String reasoner ) {
		try {
			final long time = System.currentTimeMillis();
			final String name = reasoner == null ? "no" : reasoner;
			System.out.println( "===================================" );
			System.out.println( "Results using " + name + " reasoner" );
			System.out.println( "Strict type checking: " + kb.isStrictConversion());
			System.out.println( "===================================" );

			kb.setReasoner( reasoner );

			final Profile profile = s.getProfile();
			System.out.println("Service name: " + profile.getServiceName());
			System.out.println();

			// Print the ServiceParameters
			System.out.println("Display service parameters");
			System.out.println("--------------------------");
			final OWLIndividualList<ServiceParameter> params = profile.getServiceParameters();
			for (ServiceParameter param : params)
			{
				System.out.println("Service Parameter: ");
				System.out.println("  Name  : "  + trimString(param.getName()));
				System.out.println("  Value : "  + param.getParameter());
			}
			System.out.println();

			// Print the ServiceCategories
			System.out.println("Display service categories");
			System.out.println("--------------------------");
			final OWLIndividualList<ServiceCategory> categories = profile.getCategories();
			for (ServiceCategory category : categories)
			{
				System.out.println("Service Category: ");
				System.out.println("  Name  : "  + trimString(category.getName()));
				System.out.println("  Code : "  + trimString(category.getCode()));
				System.out.println("  Taxonomy : "  + trimString(category.getTaxonomy()));
				System.out.println("  Value : "  + trimString(category.getValue()));
			}
			System.out.println("Elapsed time to infer information: " + (System.currentTimeMillis() - time) + "ms");
		} catch(final RuntimeException e) {
			System.out.println("The following error occurred: ");
			System.out.println(e);
		}
		System.out.println();
	}

	public String trimString(final String str) {
		return str == null ? "<null>" : str.trim();
	}
}
