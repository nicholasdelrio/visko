package org.openvisko.module.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;

public class GetURLContents
{
  public static String getUniqueFileNameFromURL(String url){
    String fileName = url.substring(0, url.lastIndexOf("/"));
    return fileName + "-" + FileUtils.getRandomString() + ".nc";
  }
  
  /**
   * Encode the path and query segments of the given URL.
   * 
   * @param url
   * @return String encoded URL
   */
  public static String encode(String url) {
    try {
      return URIUtil.encodePathQuery(url);
    } catch (URIException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static File getFile(String url, File destinationFolder) throws Exception {

    GetMethod getMethod = new GetMethod();
    File file = null;

    try {
      String encodedUrl = encode(url);
      String charset = getMethod.getParams().getUriCharset();
     org.apache.commons.httpclient.URI uri = new org.apache.commons.httpclient.URI(encodedUrl, true, charset);

      getMethod.setURI(uri);
      executeMethod(getMethod);
      InputStream response = getMethod.getResponseBodyAsStream();

      // Get the file name
      String fileName = FileUtils.getFileName(url);

      // Then copy it to local file
      file = new File(destinationFolder, fileName);      
      FileUtils.copy(new BufferedInputStream(response), new BufferedOutputStream(new FileOutputStream(file)));

    } catch (IOException e) {
      throw new RuntimeException(e);

    } finally {
      getMethod.releaseConnection();
    }
    return file;
  }
  
  private static void executeMethod(HttpMethod method) throws Exception {
    try {
      HttpClient httpclient = new HttpClient();
      httpclient.executeMethod(method);

      if (method.getStatusCode() == HttpStatus.SC_UNAUTHORIZED)
        throw new RuntimeException("The user is not authorized to perform this operation.");

      if (method.getStatusCode() >= 400) {
        if(method.getStatusCode() == 511) {
          throw new RuntimeException("A resource with this name already exists!");

        } else {
          String response = "";
        
          try {
             response = getResponseBodyAsString(method);
          } catch (Throwable e) {
            e.printStackTrace();
          }
            
          throw new RuntimeException("Error sending content: " + method.getStatusLine().toString() + "\n" + response);
        }
      }
    } catch (HttpException e) {
      throw new RuntimeException("Error sending content.", e);
    } catch (IOException e) {
      throw new RuntimeException("Error sending content.", e);
    }
  }

  
  /**
   * Read the resonse to a stream and then return a string so we dont
   * get tons of warning messages in the log.
   * @param method
   * @return
   */
  private static String getResponseBodyAsString(HttpMethod method) throws Exception {
    InputStream is = method.getResponseBodyAsStream();
    return new String(IOUtils.toByteArray(is));
  }

  public static byte[] downloadFile(String url) 
  {//download file via http protocol and return contents as byte array

    try
    {
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      //initialize connection
      URL yahoo = new URL(url);  
      URLConnection fileLocation = yahoo.openConnection();

      BufferedInputStream bis = new BufferedInputStream(fileLocation.getInputStream());
      byte[] buff = new byte[1024];
      int bytesRead;

      // Simple read/write loop
      while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
        output.write(buff, 0, bytesRead);
      }

      //close inputstream
      bis.close();

      return output.toByteArray();

    } catch (MalformedURLException u){
      u.printStackTrace();
      return null;

    } catch (IOException i){

      i.printStackTrace();
      return null;
    } 
  }
  public static String downloadText(String _uri){
    try
    {
      return GetURLContents.downloadText(new URL(_uri));
    }catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public static String downloadText(URL u) throws IOException
  {

    URLConnection conn = u.openConnection();
    conn.setUseCaches(true);
    Object resp = conn.getContent();
    InputStream body = (InputStream) resp;
    InputStreamReader isr = new InputStreamReader(body);
    LineNumberReader lr = new LineNumberReader(isr);
    StringBuffer ret = new StringBuffer();

    while (true){
      String line = lr.readLine();
      // System.out.println("Line:" + line);
      if (line == null){
        break;
      }
      // ret.append(line).append(System.getProperty("line.separator"));
      ret.append(line).append("\n");
    }
    return ret.toString();
  }

  public static String downloadRawChars(String _uri) throws IOException,
  MalformedURLException
  {
    return GetURLContents.downloadRawChars(new URL(_uri));
  }

  public static String downloadRawChars(URL u) throws IOException
  {

    URLConnection conn = u.openConnection();
    Object resp = conn.getContent();
    InputStream body = (InputStream) resp;
    StringBuffer ret = new StringBuffer();
    int currentChar;
    while ((currentChar = body.read()) != -1)
    {
      ret.append(Character.toString((char) currentChar));
    }
    return ret.toString();
  }

  public static String getContentType(String urlStr) throws IOException, MalformedURLException
  {
    String cType = null;
    URL url = new URL(urlStr);
    URLConnection conn = url.openConnection();
    cType = conn.getContentType();
    return cType;
  }

} /* END of GetURLContents */
