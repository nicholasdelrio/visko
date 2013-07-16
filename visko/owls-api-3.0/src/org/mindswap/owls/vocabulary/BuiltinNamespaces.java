/*
 * Created 07.08.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package org.mindswap.owls.vocabulary;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.mindswap.owl.vocabulary.DC;
import org.mindswap.owl.vocabulary.KIF;
import org.mindswap.owl.vocabulary.OWL;
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.owl.vocabulary.RDFS;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.owl.vocabulary.XSD;

/**
 * This class comprises all namespaces which are <em>built-in</em>
 * to the OWL-S API.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public final class BuiltinNamespaces
{
	private final Set<String> namespaces;

	public BuiltinNamespaces()
	{
		Set<String> tmp = new HashSet<String>();
		// OWL-S namespaces ***** always refer via class OWLS: ensures that default OWL-S version is used
		tmp.add(OWLS.ACTOR_NS);
		tmp.add(OWLS.EXPRESSION_NS);
		tmp.add(OWLS.GROUNDING_NS);
		tmp.add(OWLS.LIST_NS);
		tmp.add(OWLS.PROCESS_NS);
		tmp.add(OWLS.PROFILE_ADDITIONAL_NS);
		tmp.add(OWLS.PROFILE_NS);
		tmp.add(OWLS.SERVICE_CATEGORY_NS);
		tmp.add(OWLS.SERVICE_PARAMETER_NS);
		tmp.add(OWLS.SERVICE_NS);
		// expression language namespaces
		tmp.add(KIF.ns);
		tmp.add(SWRL.NS);
		tmp.add(SWRLB.NS);
		// OWL+RDF namespaces
		tmp.add(XSD.ns);
		tmp.add(RDF.ns);
		tmp.add(RDFS.ns);
		tmp.add(OWL.ns);
		// Extensions
		tmp.add(FLAServiceOnt.NS);
		tmp.add(MoreGroundings.JAVA_GRDG_NS);
		tmp.add(OWLS_Extensions.PROCESS_NS);
		tmp.add(OWLS_Extensions.TERMINATION_NS);
		// helpers
		tmp.add(DC.ns);
		tmp.add(OWLS.TIME_NS);
		tmp.add(OWLS.SWRLX_NS);

		namespaces = Collections.unmodifiableSet(tmp);
	}

	/**
	 * @param ns The namespace URI to check. String should end with <tt>#</tt>.
	 * @return <code>true</code> if the given namespace belongs to the built-in
	 * 	namespaces.
	 */
	public boolean isBuiltinNamespace(final String ns)
	{
		return namespaces.contains(ns);
	}

}
