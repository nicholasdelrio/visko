/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

-Redistributions of source code must retain the above copyright notice, this list of conditions
and the following disclaimer.
-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/

/*
 Copyright (c) 2012, University of Texas at El Paso
 All rights reserved.
 Redistribution and use in source and binary forms, with or without modification, are permitted
 provided that the following conditions are met:

 -Redistributions of source code must retain the above copyright notice, this list of conditions
 and the following disclaimer.
 -Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 and the following disclaimer in the documentation and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/

package edu.utep.trustlab.visko.planning.queryParsing;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class QueryParserV2 implements QueryParser {
	private String nodesetURI;
	private String formatURI;
	private String typeURI;
	private String viewURI;
	private String contentURL;
	private String viewerSetURI;
	private boolean isLocal;
	private HashMap<String, String> bindings;
	private ArrayList<String[]> bindingsList;
	private HashMap<String, String> prefixes;
	private Queue<String> tokenQueue;

	private String[] tokens;

	private static final String[] KEYWORDS = { "PREFIX", "SELECT", "FORMAT",
			"IN-VIEWER", "TYPE", "FROM", "NODESET", "LOCAL" };

	public static void main(String[] args) {
		// String query =
		// "PREFIX ns1 http://test1.owl# PREFIX ns http://test.owl# SELECT ns:contourMap IN-VIEWER ns1:viewer1 FROM LOCAL http://poop.owl FORMAT ns:format1 TYPE ns1:type1 WHERE http://spacing = 1 AND ns:color = marroon AND ns1:bg = black";
		// String query1 =
		// "PREFIX ns1 http://test1.owl# PREFIX ns http://test.owl# SELECT ns:contourMap IN-VIEWER ns1:viewer1 FROM LOCAL NODESET ns:somenodeset FORMAT ns:format1 TYPE ns1:type1 WHERE http://spacing = 1 AND ns:color = marroon AND ns1:bg = black";
		String query2 = "PREFIX ns1 http://test1.owl# PREFIX ns http://test.owl# SELECT * IN-VIEWER ns1:viewer1 FROM LOCAL NODESET ns:somenodeset FORMAT ns:format1 TYPE ns1:type1 WHERE http://spacing = 1 AND ns:color = marroon AND ns1:bg = black";
		// String query3 =
		// "SELECT * IN-VIEWER http://smelly.owl#viewer1 FROM http://somedadta.txt FORMAT http://format.owl TYPE http://type.owl WHERE http://spacing = 1 AND http://smell.owl = marroon";

		QueryParserV2 parser = new QueryParserV2(query2);
		parser.parse();

		System.out.println("viewerset: " + parser.getViewerSetURI());
		System.out.println("view: " + parser.getViewURI());
		System.out.println("contentURL: " + parser.getContentURL());
		System.out.println("format: " + parser.getFormatURI());
		System.out.println("type: " + parser.getSemanticTypeURI());
		System.out.println("nodeset: " + parser.nodesetURI);
		System.out.println("isLocal: " + parser.isLocal);

		Set<String> keys = parser.getParameterBindings().keySet();
		for (String key : keys) {
			System.out.println("key: " + key + " value: "
					+ parser.getParameterBindings().get(key));
		}
	}

	public String getContentURL() {
		return contentURL;
	}

	public String getFormatURI() {
		return formatURI;
	}

	public String getViewerSetURI() {
		return viewerSetURI;
	}

	public String getViewURI() {
		return viewURI;
	}

	public String getSemanticTypeURI() {
		return typeURI;
	}

	
	public HashMap<String,String> getPrefixes(){
		return this.prefixes;
	}
	
	
	public HashMap<String, String> getParameterBindings() {
		return bindings;
	}

	public String getNodesetURI() {
		return nodesetURI;
	}

	public QueryParserV2(String query) {
		bindingsList = new ArrayList<String[]>();
		bindings = new HashMap<String, String>();
		prefixes = new HashMap<String, String>();
		tokenQueue = new LinkedList<String>();
		System.out.println("query to parse...");
		System.out.println(query);

		query = query.replaceAll("\n", " ");
		query = query.replaceAll("\\s+", " ");

		tokens = query.split(" ");

		for (String token : tokens)
			tokenQueue.add(token);
	}

	public String[] getTokens() {
		return tokens;
	}

	public void parse() {
		startState();
		expandQNames();
	}

	private String expandQName(String qName) {
		String[] qNameSplit = qName.split(":");
		String uriBase = prefixes.get(qNameSplit[0]);
		return uriBase + qNameSplit[1];
	}

	private void expandQNames() {
		if (isQName(nodesetURI))
			nodesetURI = expandQName(nodesetURI);

		if (isQName(formatURI))
			formatURI = expandQName(formatURI);

		if (isQName(typeURI))
			typeURI = expandQName(typeURI);

		if (isQName(viewURI))
			viewURI = expandQName(viewURI);

		if (isQName(viewerSetURI))
			viewerSetURI = expandQName(viewerSetURI);

		String key;
		String expandedKey;
		String value;
		for (String[] binding : bindingsList) {
			key = binding[0];
			value = binding[1];

			if (isQName(key)) {
				expandedKey = expandQName(key);
				bindings.put(expandedKey, value);
				System.out.println("key: " + expandedKey + " value: " + value);
			} else {
				bindings.put(key, value);
				System.out.println("key: " + key + " value: " + value);
			}
		}
	}

	private void startState() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out.println("Expecting PREFIX or SELECT keyword, got: "
					+ token);
		else if (token.equalsIgnoreCase("PREFIX")) {
			getPrefixNS();
		} else if (token.equalsIgnoreCase("SELECT")) {
			getView();
		} else
			System.out.println("Expecting PREFIX or SELECT keyword, got: "
					+ token);
	}

	private void getPrefixNS() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out
					.println("Expecting a PREFIX namespace but got: " + token);
		else if (!isKeyword(token))
			getPrefixURI(token);
		else
			System.out
					.println("Expecting a PREFIX that is not a keyword, got: "
							+ token);

	}

	private void getPrefixURI(String prefix) {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting a URI to associate with PREFIX, got:"
					+ token);
		else if (isURI(token)) {
			prefixes.put(prefix, token);
			startState();
		} else
			System.out
					.println("Expecting a URI to associate with PREFIX, got: "
							+ token);
	}

	private void getView() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting a qName or URI of a view, got: "
					+ token);
		else if (isURI(token) || isQName(token) || token.equals("*")) {
			viewURI = token;
			viewSet();
		} else
			System.out.println("Expecting a qName or URI of a view, got: "
					+ token);
	}

	private void viewSet() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting IN-VIEWER or FROM keywords, got: "
					+ token);
		else if (token.equalsIgnoreCase("IN-VIEWER"))
			getViewerURI();
		else if (token.equalsIgnoreCase("FROM"))
			getDataInfo();
		else
			System.out.println("Expecing IN-VIEWER or FROM keywords, got: "
					+ token);
	}

	private void getViewerURI() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out.println("Expecting viewer URI, got: " + token);
		else if (isURI(token) || isQName(token)) {
			viewerSetURI = token;
			viewerSet();
		} else
			System.out.println("Expecting viewer URI, got: " + token);
	}

	private void viewerSet() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting FROM keyword, got: " + token);
		else if (token.equalsIgnoreCase("FROM"))
			getDataInfo();
		else
			System.out.println("Expecting FROM keyword, got: " + token);
	}

	private void getDataInfo() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out
					.println("Expecting LOCAL or NODESET keywords or URL, got: "
							+ token);
		else if (token.equalsIgnoreCase("LOCAL")) {
			isLocal = true;
			getDataInfo();
		} else if (token.equalsIgnoreCase("NODESET"))
			getNodeset();
		else {
			contentURL = token;
			dataSet();
		}
	}

	private void getNodeset() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out.println("Expecting nodesetURI, got: " + token);
		else if (isURI(token) || isQName(token)) {
			nodesetURI = token;
			endState();
		}

	}

	private void dataSet() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting FORMAT keyword, got: " + token);
		else if (token.equalsIgnoreCase("FORMAT")) {
			getFormat();
		} else
			System.out.println("Expecting FORMAT keyword, got: " + token);
	}

	private void getFormat() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out.println("Expecting format URI, got: " + token);
		else if (isURL(token) || isQName(token)) {
			formatURI = token;
			endState();
		} else
			System.out.println("Expecting formatURI, got: " + token);
	}

	private void endState() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("donesky!!!!");
		else if (token.equalsIgnoreCase("TYPE"))
			getTypeURI();
		else if (token.equalsIgnoreCase("WHERE"))
			getBindings();
		else
			;// ignore extra shit
	}

	private void getTypeURI() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out.println("Expecting type URI, got: " + token);
		else if (isURI(token) || isQName(token)) {
			typeURI = token;
			endState();
		} else
			System.out.println("Expecting type URI, got: " + token);
	}

	private void getBindings() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting paramter URI, got: " + token);
		else if (isURI(token) || isQName(token))
			getEquals(token);
		else
			System.out.println("Expecting parameter URI, got: " + token);
	}

	private void getEquals(String token1) {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting = sign, got: " + token);
		else if (token.equalsIgnoreCase("="))
			getValue(token1);
		else
			System.out.println("Expecting = sign, got: " + token);
	}

	private void getValue(String paramURI) {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting anyString, got: " + token);
		else {
			bindingsList.add(new String[] { paramURI, token });
			endState1();
		}
	}

	private void endState1() {
		String token = tokenQueue.poll();
		if (token == null)
			;// that's find, this is an endstate
		else if (token.equalsIgnoreCase("AND"))
			getBindings();
		else
			;// that's fine, we can ignore the rest of the shit cause we are
			// done
	}

	public static boolean isURL(String url) {
		boolean isURL;
		try {
			new URL(url);
			isURL = true;
		} catch (Exception e) {
			isURL = false;
		}

		return isURL;
	}

	private static boolean isURI(String uri) {
		boolean isURI;
		try {
			new URL(uri);
			isURI = true;
		} catch (Exception e) {
			isURI = false;
		}

		return isURI;
	}

	private static boolean isKeyword(String word) {
		for (String keyword : KEYWORDS) {
			if (word.equalsIgnoreCase(keyword))
				return true;
		}

		return false;
	}

	private static boolean isQName(String qName) {
		if (qName != null)
			return qName.contains(":") && !qName.contains("https://")
					&& !qName.contains("http://");
		return false;
	}
	
	@Override
	public boolean isValidQuery() {
		if(this.getFormatURI() != null && this.getViewerSetURI() != null)
			return true;
		return false;
	}

	@Override
	public boolean dataIsFiltered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTargetFormatURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTargetTypeURI() {
		// TODO Auto-generated method stub
		return null;
	}
}