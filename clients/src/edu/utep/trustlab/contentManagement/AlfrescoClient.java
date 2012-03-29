/**
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH 
DAMAGE.
 */

package edu.utep.trustlab.contentManagement;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;

import edu.utep.trustlab.contentManagement.util.FileUtilities;


public class AlfrescoClient extends ContentManager {

	private static String workspacePath;
	
	public static void setWorkspacePath(String path){
		workspacePath = path;
	}
	
	public static String getWorkspacePath(){
		return workspacePath;
	}
	
	protected HttpConnectionManager connectionManager;
	protected String username;
	protected String password;
	protected static String alfrescoBaseUrl;
	protected boolean loggedIn = false;
	protected String parentProject;

	private String fileNodeURL;
	
	public static final String PATH_SERVICE = "/service/";
	
	public AlfrescoClient() {

		// Use the example from CommonsHTTPSender - we need to make sure connections are freed properly
		MultiThreadedHttpConnectionManager cm = new MultiThreadedHttpConnectionManager();
		cm.getParams().setDefaultMaxConnectionsPerHost(5);
		cm.getParams().setMaxTotalConnections(5);
		cm.getParams().setConnectionTimeout(8000);
		this.connectionManager = cm;
	}

	public AlfrescoClient(String server, String user, String pass){

		// Use the example from CommonsHTTPSender - we need to make sure connections are freed properly
		MultiThreadedHttpConnectionManager cm = new MultiThreadedHttpConnectionManager();
		cm.getParams().setDefaultMaxConnectionsPerHost(5);
		cm.getParams().setMaxTotalConnections(5);
		cm.getParams().setConnectionTimeout(8000);
		this.connectionManager = cm;
		this.logIn(user, pass, server);

	}

	
	public String getUsername(){return username;}
	public String getPassword(){return password;}
	public boolean isLoggedIn(){return loggedIn;}

	public void setBaseUrl(String alfrescoBaseUrl) {
		if(alfrescoBaseUrl.endsWith("/")){
			alfrescoBaseUrl = alfrescoBaseUrl.substring(0,alfrescoBaseUrl.length()-1);
		}
		AlfrescoClient.alfrescoBaseUrl = alfrescoBaseUrl;
	}

	public String getBaseUrl() {
		return alfrescoBaseUrl;
	}

	public boolean setAuthentication(String username, String password) throws Exception {

		this.username = username;
		this.password = password;

		// Try to access webdav URL to see if credentials are valid
		StringBuilder url = getWebdavUrl();
		GetMethod getMethod = new GetMethod();

		try {
			String encodedUrl = encode(url);
			String charset = getMethod.getParams().getUriCharset();
			URI uri = new URI(encodedUrl, true, charset);

			getMethod.setURI(uri);

			/////////////////////////////
			HttpClient httpclient = getHttpClient();
			httpclient.executeMethod(getMethod);

			if (getMethod.getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
				JOptionPane.showMessageDialog(null, "User credentials not valid.");
				return false;
			}
			/////////////////////////////


		} catch (IOException e) {
			throw new RuntimeException(e);

		} finally {
			getMethod.releaseConnection();
		}

		return true; 
	}

	protected StringBuilder getWebdavUrl() {
		StringBuilder url = new StringBuilder(alfrescoBaseUrl);
		url.append("/webdav/");
		return url;
	}

	protected void executeMethod(HttpMethod method) throws Exception {
		try {
			HttpClient httpclient = getHttpClient();
			httpclient.executeMethod(method);

			if (method.getStatusCode() == HttpStatus.SC_UNAUTHORIZED)
				throw new Exception("The user is not authorized to perform this operation.");

			if (method.getStatusCode() >= 400) {
				if(method.getStatusCode() == 511) {
					throw new RuntimeException("A resource with this name already exists!");

				} else {
					String response = method.getResponseBodyAsString();
					throw new RuntimeException("Error sending content: " + method.getStatusLine().toString() + "\n" + response);
				}
			}
		} catch (HttpException e) {
			throw new RuntimeException("Error sending content.", e);
		} catch (IOException e) {
			throw new RuntimeException("Error sending content.", e);
		}
	}

	protected HttpClient getHttpClient() {
		// Instantiating the way CommonsHTTPSender does
		HttpClient httpclient = new HttpClient(this.connectionManager);

		// the timeout value for allocation of connections from the pool
		// 0 means infinite timeout
		httpclient.getParams().setConnectionManagerTimeout(0);

		// we're hoping this speeds things up by eliminating the handshake
		//httpclient.getParams().setBooleanParameter(HttpClientParams.PREEMPTIVE_AUTHENTICATION, true);

		if(username != null) {
			Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
			httpclient.getState().setCredentials(AuthScope.ANY, defaultcreds);
		}

		// Set the proxy server
		String proxyHost = System.getProperties().getProperty("http.proxyHost");
		String proxyPort = System.getProperties().getProperty("http.proxyPort");

		if(proxyHost != null && proxyPort != null) {
			System.out.println("HttpClient configuring proxy " + proxyHost + ":" + proxyPort);
			httpclient.getHostConfiguration().setProxy(proxyHost, Integer.valueOf(proxyPort));
		}

		return httpclient;

	}

	protected StringBuilder getCMISWebScriptUrl() {
		StringBuilder url = new StringBuilder(alfrescoBaseUrl);
		url.append(PATH_SERVICE);
		url.append("cmis");
		return url;
	}
	protected StringBuilder getAPIWebScriptUrl() {
		StringBuilder url = new StringBuilder(alfrescoBaseUrl);
		url.append(PATH_SERVICE);
		url.append("api");
		return url;
	}

	protected static StringBuilder getDerivAWebScriptUrl() {
		StringBuilder url = new StringBuilder(alfrescoBaseUrl);
		url.append(PATH_SERVICE);
		url.append("derivA/");
		return url;
	}

	//here down was copied from other utility classes
	public static final String SLASH = "/";
	public static final String QUESTION = "?";
	public static final String EQUALS = "=";
	public static final String AMPERSAND = "&";

	/**
	 * Append the given String paths to the {@link StringBuilder} URL, separating with {@link #SLASH}. The {@link StringBuilder} when finished will not end in a {@link #SLASH}.
	 *
	 * @param url
	 *          {@link StringBuilder} to append to
	 * @param paths
	 *          String array (varargs) of paths to append
	 */
	public static void appendPaths(StringBuilder url, String... paths) {
		if (!endsWith(url, SLASH)) {
			url.append(SLASH);
		}

		for (String path : paths) {
			if (!path.isEmpty()) {
				url.append(path);
			}

			if (!endsWith(url, SLASH)) {
				url.append(SLASH);
			}
		}

		if (endsWith(url, SLASH)) {
			url.deleteCharAt(url.length() - 1);
		}
	}

	/**
	 * Test if the given {@link StringBuilder} ends with the given String.
	 *
	 * @param stringBuilder
	 *          {@link StringBuilder} to test
	 * @param value
	 *          String to test if at the end of the {@link StringBuilder}
	 * @return boolean true if the {@link StringBuilder} ends with the String
	 */
	private static boolean endsWith(StringBuilder stringBuilder, String value) {
		int index = stringBuilder.lastIndexOf(value);

		return index == (stringBuilder.length() - 1);
	}

	/**
	 * Append the given request parameter name and value to the {@link StringBuilder} URL.
	 *
	 * @param url
	 *          {@link StringBuilder} to append parameter to
	 * @param name
	 *          String name of the parameter
	 * @param value
	 *          String value of the parameter
	 */
	public static void appendParameter(StringBuilder url, String name, String value) {
		if (!name.isEmpty()) {
			if (!contains(url, QUESTION)) {
				url.append(QUESTION);
			}

			if (!endsWith(url, QUESTION) && !endsWith(url, AMPERSAND)) {
				url.append(AMPERSAND);
			}

			url.append(name);
			url.append(EQUALS);

			if (!value.isEmpty()) {
				url.append(value);
			}
		}
	}

	/**
	 * Test if the given {@link StringBuilder} contains the given String.
	 *
	 * @param stringBuilder
	 *          {@link StringBuilder} to search within
	 * @param value
	 *          String to search for
	 * @return boolean true if the {@link StringBuilder} contains the given String
	 */
	private static boolean contains(StringBuilder stringBuilder, String value) {
		int index = stringBuilder.indexOf(value);

		return index != -1;
	}

	/**
	 * Encode the path and query segments of the given {@link StringBuilder} URL.
	 *
	 * @param url
	 *          {@link StringBuilder} URL with path and query segments
	 * @return String encoded URL
	 */
	public static String encode(StringBuilder url) {
		try {
			return URIUtil.encodePathQuery(url.toString());
		} catch (URIException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * create a black node and return the URI in order to 
	 * later add content to it.
	 * @return URI of node
	 */
	public String createNode(String project, String filename){

		try {
			filename = URLEncoder.encode(filename, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		StringBuilder createNodeURL = getDerivAWebScriptUrl();
		createNodeURL.append("createNode");
		appendParameter(createNodeURL, "project", project);
		appendParameter(createNodeURL, "filename", filename);

		GetMethod getMethod = new GetMethod();

		try {
			String encodedUrl = encode(createNodeURL);
			String charset = getMethod.getParams().getUriCharset();
			URI uri = new URI(encodedUrl, true, charset);

			getMethod.setURI(uri);

			System.out.println(uri);
			executeMethod(getMethod);

			return getMethod.getResponseBodyAsString();

		} catch (IOException e) {
			throw new RuntimeException(e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		} 

		return null;
	}

	/**
	 * Upload a given file to an already created node in Alfresco
	 */
	public void addContentToNode(String nodeURL, File file){

		String[] uuid = nodeURL.split("/");

		PostMethod postMethod = new PostMethod();

		StringBuilder uploadFileURL = getDerivAWebScriptUrl();
		uploadFileURL.append("fileUploader");
		appendParameter(uploadFileURL, "uuid", uuid[5]);

		try {
			String encodedUrl = encode(uploadFileURL);
			String charset = postMethod.getParams().getUriCharset();
			URI uri = new URI(encodedUrl, true, charset);

			postMethod.setURI(uri);
			postMethod.setContentChunked(true);
			FileRequestEntity fre = new FileRequestEntity(file, null);
			postMethod.setRequestEntity(fre);

			executeMethod(postMethod);

		} catch (IOException e) {
			throw new RuntimeException(e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}   
	}

	/**
	 * Full process of uploading a file and getting the final URI for the file.
	 * @param file itself
	 * @return Final URI of the file
	 */
	public String uploadFile(File file){

		String nodeURL = createNode(projectName, file.getName());

		addContentToNode(nodeURL, file);

		return alfrescoBaseUrl + nodeURL;
	}

	public void logIn(String Username, String Password, String Server){
		setBaseUrl(Server);
		try {
			setAuthentication(Username, Password);
		} catch (Exception e) {
			System.out.println("Incorrect Credentials");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		AlfrescoClient ac = new AlfrescoClient();
		File file = new File("C:\\Users\\Public\\git\\visko\\rdf\\contourer.owl");
		ac.logIn("admin", "booger1", "http://localhost:8080/alfresco");
		String URI = ac.uploadFile(file);
		System.out.println("URI: " + URI);
	}

	@Override
	public String saveDocument(File file) {
		addContentToNode(fileNodeURL, file);
		return fileNodeURL;
	}

	@Override
	public String saveDocument(String fileContents, String fileName) {
		String absolutePath = FileUtilities.writeTextFile(fileContents, AlfrescoClient.getWorkspacePath(), fileName);
		File file = new File(absolutePath);
		addContentToNode(fileNodeURL, file);
		return fileNodeURL;
	}

	@Override
	public String getBaseURL(String fileName) {
		fileNodeURL = createNode(projectName, fileName);
		
		//return just base URL without file name appended
		return fileNodeURL.replaceAll(fileName, "");
	}
}