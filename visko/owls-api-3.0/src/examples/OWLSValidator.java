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
package examples;

import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.service.Service;
import org.mindswap.utils.OutputFormatter;

/**
 * Validate OWL-S service descriptions to find errors such as flaws in the data flow specification,
 * grounding description and so on. Validator will catch common RDF errors and print them.
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class OWLSValidator {
	boolean parseRDFFinished = false;

	List<String> serviceURIs = new ArrayList<String>();
	List<String> errors = new ArrayList<String>();
	List<String> warnings = new ArrayList<String>();
	List<String> rdfProblems = new ArrayList<String>();

	public OWLSValidator() {

	}

	public boolean validate(final String fileURI) throws Exception {
		return validate(fileURI, new PrintWriter(System.out));
	}

	public boolean validate(final String fileURI, final PrintWriter output) throws Exception {
		return validate(fileURI, output, false);
	}

	public boolean validate(final String fileURI, final PrintWriter output, final boolean isHTML) throws Exception {
		return validate(fileURI, new OutputFormatter(output, isHTML));
	}

	public boolean validate(final String fileURI, final OutputFormatter out) throws Exception {
		final URI uri = new URI(fileURI);

		final OWLKnowledgeBase kb = OWLFactory.createKB();

		final OWLOntology ont = kb.read(uri);

		final List<Service> services = new ArrayList<Service>();

		out.printBold("Number of services found: ").println(ont.getServices(false).size());
		out.printBold("Number of valid services: ").println(services.size());
		out.println();

		printMesssages(out, "RDF Problems: ", rdfProblems.iterator());
		printMesssages(out, "Errors: ", errors.iterator());
		printMesssages(out, "Warnings: ", warnings.iterator());

		for (Service service : services)
		{
			out.printBold("Service: ").printLink(service.getURI().toString());
//			out.print(" (Version: ").print(service.getOWLSVersion()).print(")").println();
			out.printBold("Name: ").println(service.getLabel(null));
			out.printBold("Description: ").println(service.getProfile().getTextDescription());
		}

		out.flush();

		// FIXME return value for validation
		// return services.size() > 0 && errors.isEmpty();
		return true;
	}

	public void printMesssages(final OutputFormatter out, final String header, final Iterator<String> i) {
		if(i.hasNext()) {
			out.printBold(header).println();

			while(i.hasNext()) {
				final String error = i.next();

				if(out.isFormatHTML()) out.print("<ul>");
				if(out.isFormatHTML()) {
					out.print("<li>");
					out.print(format(error));
					out.print("</li>");
				}
				else
					out.println(error);
				if(out.isFormatHTML()) out.print("</ul>");
			}

			out.println();
		}
	}

	public String format(final String str) {
		return str;
//		return str.replaceAll("\n", "<br>").replaceAll(" ", "&nbsp;");
//		StringBuffer buffer = new StringBuffer();
//		String link;
//		int start = 0, finish;
//		int index = str.indexOf("http://");

//		while(index != -1) {
//		buffer.append(str.substring(start, index));

//		finish = Math.min(str.indexOf(' ', index), str.indexOf('\n', index));
//		if(finish > str.indexOf('<', index)) finish = str.indexOf('\n', index);
//		if(finish == -1) finish = str.length();
//		link = str.substring(index, finish);
//		buffer.append("<a href=\"").append(link).append("\">");
//		buffer.append(link).append("</a>");
//		start = index + link.length();
//		index = str.indexOf("http://", start);
//		}
//		buffer.append(str.substring(start));

//		return buffer.toString().replaceAll("\n", "<br>");
	}

	public String getServiceURI() {
		return serviceURIs.get(serviceURIs.size() - 1);
	}

	public void put(final Map<String, List<String>> table, final String key, final String value) {
		List<String> list = table.get(key);

		if(list == null) {
			list = new ArrayList<String>();
			table.put(key, list);
		}

		list.add(value);
	}

	public void startService(final String serviceURI) {
		serviceURIs.add(serviceURI);
	}

	public void finishService(final String serviceURI) {
	}

	public void warning(final String msg) {
		if(parseRDFFinished)	warnings.add(msg);
		else rdfProblems.add(msg);
	}

	public void error(final String msg) {
		if(parseRDFFinished)	errors.add(msg);
		else rdfProblems.add(msg);
	}


	public void startParseRDF() {
		parseRDFFinished = false;
	}

	public void finishParseRDF() {
		parseRDFFinished = true;
	}

	public static void main(final String[] args) throws Exception {
		if(args.length != 1) {
			if(args.length < 1) System.err.println("Not enough parameters");
			if(args.length > 1) System.err.println("Too many parameters");
			System.err.println("usage: java OWLSValidator <serviceURI>");
			System.exit(0);
		}

		final OWLSValidator validator = new OWLSValidator();
		System.out.println("Valid: " + validator.validate(args[0]));
	}

}
