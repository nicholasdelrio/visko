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

public class QueryParserV3 implements QueryParser {
	private String nodesetURI;
	private String formatURI;
	private String typeURI;
	private String viewURI;
	private String contentURL;
	private String viewerSetURI;
	private boolean isLocal;
	private boolean isFiltered;
	
	private HashMap<String, String> bindings;
	private ArrayList<String[]> bindingsList;
	private HashMap<String, String> prefixes;
	private Queue<String> tokenQueue;

	private String[] tokens;

	private static final String[] KEYWORDS = { "PREFIX", "VISUALIZE", "FILTERED", "AS", "IN", "NODESET", "LOCALLY"};

	public static void main(String[] args) {
		//String query1 = "PREFIX ns1 http://test1.owl# PREFIX ns http://test.owl# VISUALIZE LOCAL NODESET http://nodeset1.owl AS * IN ns1:viewer1 WHERE FORMAT = http://someformat.owl AND TYPE = http://sometype.owl AND http://spacing = 1 AND ns:color = marroon AND ns1:bg = black";
		//String query2 = "PREFIX ns1 http://test1.owl# PREFIX ns http://test.owl# VISUALIZE http://dataset1.txt AS * IN ns1:viewer1 WHERE FORMAT = http://someformat.owl AND TYPE = http://sometype.owl AND http://spacing = 1 AND ns:color = marroon AND ns1:bg = black";
		String query3 = "VISUALIZE FILTERED http://someartifact.txt AS * IN https://raw.github.com/nicholasdelrio/visko/master/rdf/mozilla-firefox.owl#mozilla-firefox WHERE FORMAT = https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/TIFF.owl#TIFF AND TYPE = http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d13 AND https://raw.github.com/nicholasdelrio/visko/master/rdf/grdcontour.owl#A = k AND https://raw.github.com/nicholasdelrio/visko/master/rdf/grdimage.owl#J = 0";
		
		String query4 = "VISUALIZE NODESET http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt_08475589929900726.owl#answer AS * IN https://raw.github.com/nicholasdelrio/visko/master/rdf/mozilla-firefox.owl#mozilla-firefox";	
		
		
		QueryParserV3 parser = new QueryParserV3(query3);
		parser.parse();

		System.out.println("viewerset: " + parser.getViewerSetURI());
		System.out.println("view: " + parser.getViewURI());
		System.out.println("contentURL: " + parser.getContentURL());
		System.out.println("format: " + parser.getFormatURI());
		System.out.println("type: " + parser.getSemanticTypeURI());
		System.out.println("nodeset: " + parser.nodesetURI);
		System.out.println("isLocal: " + parser.isLocal);
		System.out.println("isfiltered: " + parser.isFiltered);

		Set<String> keys = parser.getParameterBindings().keySet();
		for (String key : keys) {
			System.out.println("key: " + key + " value: " + parser.getParameterBindings().get(key));
		}
		
		System.out.println("isValid: " + parser.isValidQuery());
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

	public HashMap<String, String> getParameterBindings() {
		return bindings;
	}
	
	public HashMap<String, String> getPrefixes(){
		return prefixes;
	}
	
	public String getNodesetURI() {
		return nodesetURI;
	}

	public QueryParserV3(String query) {
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
			System.out.println("Expecting PREFIX or VISUALIZE keywords, got: " + token);
		else if (token.equalsIgnoreCase("PREFIX")) {
			getPrefixNS();
		} else if (token.equalsIgnoreCase("VISUALIZE")) {
			checkIfFiltered();
		} else
			System.out.println("Expecting PREFIX or VISUALIZE keywords, got: " + token);
	}

	private void getPrefixNS() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out.println("Expecting a PREFIX namespace but got: " + token);
		else if (!isKeyword(token))
			getPrefixURI(token);
		else
			System.out.println("Expecting a PREFIX that is not a keyword, got: "+ token);

	}

	private void getPrefixURI(String prefix) {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting a URI to associate with PREFIX, got:" + token);
		else if (isURI(token)) {
			prefixes.put(prefix, token);
			startState();
		} else
			System.out.println("Expecting a URI to associate with PREFIX, got: " + token);
	}
	
	private void getAs(){
		String token = tokenQueue.poll();
		
		if (token == null)
			System.out.println("Expecting AS keyword, got: " + token);
		else if (token.equalsIgnoreCase("AS")) {
			getView();
		} else
			System.out.println("Expecting AS keywork, got: " + token);
	}

	private void getView() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting a qName or URI of a view, got: " + token);
		else if (isURI(token) || isQName(token) || token.equals("*")) {
			viewURI = token;
			getIN();
		} else
			System.out.println("Expecting a qName or URI of a view, got: " + token);
	}

	private void getIN() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting IN keyword, got: " + token);
		else if (token.equalsIgnoreCase("IN"))
			getViewerSet();
		else
			System.out.println("Expecing IN keyword, got: " + token);
	}

	private void getViewerSet() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out.println("Expecting viewer URI, got: " + token);
		else if (isURI(token) || isQName(token)) {
			viewerSetURI = token;
			endState();
		} else
			System.out.println("Expecting viewer URI, got: " + token);
	}
	
	private void checkIfFiltered(){
		String token = tokenQueue.peek();
		if(token.equalsIgnoreCase("FILTERED")){
			isFiltered = true;
			tokenQueue.poll();
			getDataInfo();
		}
		else
			getDataInfo();
	}

	private void getDataInfo() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting LOCAL or NODESET keywords or URL, got: " + token);
		else if (token.equalsIgnoreCase("LOCAL")) {
			isLocal = true;
			getDataInfo();
		} else if (token.equalsIgnoreCase("NODESET"))
			getNodeset();
		else {
			contentURL = token;
			getAs();
		}
	}

	private void getNodeset() {
		String token = tokenQueue.poll();
		if (token == null)
			System.out.println("Expecting nodesetURI, got: " + token);
		else if (isURI(token) || isQName(token)) {
			nodesetURI = token;
			getAs();
		}

	}

	private void endState() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("end of tokens!!!!");
		else if (token.equalsIgnoreCase("WHERE"))
			getBindings();
		else
			;// ignore extra tokens, they are garbage in terms of our syntax
	}

	private void getBindings() {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting paramter URI, got: " + token);
		else if (isURI(token) || isQName(token))
			getEquals(token);
		else if (token.equalsIgnoreCase("FORMAT"))
			getFormatOrTypeEquals(true);
		else if(token.equalsIgnoreCase("TYPE"))
			getFormatOrTypeEquals(false);
		else
			System.out.println("Expecting parameter URI, got: " + token);
	}

	private void getFormatOrTypeEquals(boolean isFormat) {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting = sign, got: " + token);
		else if (token.equalsIgnoreCase("="))
			getValueOfFormatOrType(isFormat);
		else
			System.out.println("Expecting = sign, got: " + token);
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
	
	private void getValueOfFormatOrType(boolean isFormat) {
		String token = tokenQueue.poll();

		if (token == null)
			System.out.println("Expecting anyString, got: " + token);
		
		else {
			
			if(isFormat)
				formatURI = token;
			else
				typeURI = token;

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
			return qName.contains(":") && !qName.contains("https://") && !qName.contains("http://");
		return false;
	}

	@Override
	public boolean isValidQuery() {
		if((this.getFormatURI() != null && this.getViewerSetURI() != null) || this.getNodesetURI() != null)
			return true;
		return false;
	}

	@Override
	public boolean dataIsFiltered() {
		// TODO Auto-generated method stub
		return isFiltered;
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
