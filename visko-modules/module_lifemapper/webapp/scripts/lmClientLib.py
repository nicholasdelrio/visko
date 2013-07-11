"""
@summary: Client library for Lifemapper web services
@author: CJ Grady
@contact: cjgrady [at] ku [dot] edu
@organization: Lifemapper (http://lifemapper.org)
@version: 2.0
@status: beta

@license: Copyright (C) 2012, University of Kansas Center for Research

          Lifemapper Project, lifemapper [at] ku [dot] edu, 
          Biodiversity Institute,
          1345 Jayhawk Boulevard, Lawrence, Kansas, 66045, USA
   
          This program is free software; you can redistribute it and/or modify 
          it under the terms of the GNU General Public License as published by 
          the Free Software Foundation; either version 2 of the License, or (at 
          your option) any later version.
  
          This program is distributed in the hope that it will be useful, but 
          WITHOUT ANY WARRANTY; without even the implied warranty of 
          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
          General Public License for more details.
  
          You should have received a copy of the GNU General Public License 
          along with this program; if not, write to the Free Software 
          Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
          02110-1301, USA.

@note: Additional service documentation can be found at:
          http://lifemapper.org/schemas/services.wadl
"""
import cookielib
import glob
import os
import StringIO
from types import ListType
import urllib
import urllib2
import xml.etree.ElementTree as ET
import zipfile


from constants import DEFAULT_POST_USER, DEFAULT_USER, SHAPEFILE_EXTENSIONS, \
                      WEBSITE_ROOT
from rad import RADClient
from sdm import SDMClient


# .............................................................................
class LMClient(object):
   """
   @summary: Lifemapper client library class
   """
   # .........................................
   def __init__(self, userId=DEFAULT_POST_USER, pwd=None):
      """
      @summary: Constructor
      @param userId: (optional) The id of the user to use for this session
      @param pwd: (optional) The password for the specified user
      @note: Lifemapper RAD services are not available anonymously
      """
      self._cl = _Client(userId=userId, pwd=pwd)
      self.sdm = SDMClient(self._cl)
      if userId not in [DEFAULT_POST_USER, DEFAULT_USER]:
         self.rad = RADClient(self._cl)
   
   # .........................................
   def logout(self):
      """
      @summary: Log out of a session
      """
      self._cl.logout()

# .............................................................................
class _Client(object):
   """
   @summary: Private Lifemapper client class
   """
   # .........................................
   def __init__(self, userId=DEFAULT_POST_USER, pwd=None):
      """
      @summary: Constructor of LMClient
      @param userId: (optional) User id to use if different from the default
                        [string]
      @param pwd: (optional) Password for optional user id. [string]
      """
      self.userId = userId
      self.pwd = pwd
      
      self.cookieJar = cookielib.LWPCookieJar()
      opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(self.cookieJar))
      urllib2.install_opener(opener)
      self._login()
      
   # .........................................
   def getAutozipShapefileStream(self, fn):
      """
      @summary: Automatically creates a zipped version of a shapefile from the
                   shapefile's .shp file.  Finds the rest of the files it needs
                   and includes them in one package
      @param fn: Path to the shapefile's .shp file
      @return: The zipped shapefile
      @rtype: String
      """
      files = []
      if fn.endswith('.shp'):
         for f in glob.iglob("%s*" % fn.strip('.shp')):
            ext = f.split('.')[-1]
            if ext in SHAPEFILE_EXTENSIONS:
               files.append(f)
      else:
         raise Exception ("Filename must end in '.shp'")
      
      outStream = StringIO.StringIO()
      zf = zipfile.ZipFile(outStream, 'w')
      for f in files:
         zf.write(f, os.path.basename(f))
      zf.close()
      outStream.seek(0)
      return outStream.getvalue()

   # .........................................
   def getCount(self, url, parameters=[]):
      """
      @summary: Gets the item count from a count service
      @param url: A URL pointing to a count service end-point
      @param parameters: (optional) List of query parameters for the request
      """
      obj = self.makeRequest(url, method="GET", parameters=parameters, 
                                                                objectify=True)
      count = int(obj.items.itemCount)
      return count
   
   # .........................................
   def getList(self, url, parameters=[]):
      """
      @summary: Gets a list of items from a list service
      @param url: A URL pointing to a list service end-point
      @param parameters: (optional) List of query parameters for the request
      """
      obj = self.makeRequest(url, method="GET", parameters=parameters, 
                                                                objectify=True)
      try:
         if isinstance(obj.items, ListType):
            lst = obj.items
         else:
            lst = obj.items.item
         if lst is not None:
            if not isinstance(lst, ListType):
               lst = [lst]
            return lst
      except Exception, e:
         print e
      return []

   # .........................................
   def makeRequest(self, url, method="GET", parameters=[], body=None, 
                         headers={}, objectify=False):
      """
      @summary: Performs an HTTP request
      @param url: The url endpoint to make the request to
      @param method: (optional) The HTTP method to use for the request
      @param parameters: (optional) List of url parameters
      @param body: (optional) The payload of the request
      @param headers: (optional) Dictionary of HTTP headers
      @param objectify: (optional) Should the response be turned into an object
      @return: Response from the server
      """
      parameters = removeNonesFromTupleList(parameters)
      urlparams = urllib.urlencode(parameters)
      
      if body is None and len(parameters) > 0 and method.lower() == "post":
         body = urlparams
      else:
         url = "%s?%s" % (url, urlparams)
      req = urllib2.Request(url, data=body, headers=headers)
      ret = urllib2.urlopen(req)
      resp = ''.join(ret.readlines())
      if objectify:
         return self.objectify(resp)
      else:
         return resp

   # .........................................
   def objectify(self, xmlString):
      """
      @summary: Takes an XML string and processes it into a python object
      @param xmlString: The xml string to turn into an object
      @note: Uses LmAttList and LmAttObj
      @note: Object attributes are defined on the fly
      """
      return deserialize(ET.fromstring(xmlString))   

   # .........................................
   def _login(self):
      """
      @summary: Attempts to log a user in
      @todo: Handle login failures
      """
      if self.userId != DEFAULT_POST_USER and self.userId != DEFAULT_USER and \
                        self.pwd is not None:
         url = "%s/login" % WEBSITE_ROOT
         
         urlParams = [("username", self.userId), ("pword", self.pwd)]
         
         self.makeRequest(url, parameters=urlParams)

   # .........................................
   def logout(self):
      """
      @summary: Logs the user out
      """
      url = '/'.join((WEBSITE_ROOT, "logout"))
      self.makeRequest(url)

# .............................................................................
class LmAttObj(object):
   """
   @summary: Object that includes attributes.  Compare this to the attributes
                attached to XML elements and the object members would be the
                sub-elements of the element.
   @note: <someElement att1="value1" att2="value2">
             <subEl1>1</subEl1>
             <subEl2>banana</subEl2>
          </someElement>
          
          translates to:
          
          obj.subEl1 = 1
          obj.subEl2 = 'banana'
          obj.getAttributes() = {'att1': 'value1', 'att2': 'value2'}
          obj.att1 = 'value1'
          obj.att2 = 'value2'
   """
   # ......................................
   def __init__(self, attribs={}, name="LmObj"):
      """
      @summary: Constructor
      @param attribs: (optional) Dictionary of attributes to attach to the 
                         object
      @param name: (optional) The name of the object (useful for serialization)
      """
      self.__name__ = name
      self._attribs = attribs
   
   # ......................................
   def __getattr__(self, name):
      """
      @summary: Called if the default getattribute method fails.  This will 
                   attempt to return the value from the attribute dictionary
      @param name: The name of the attribute to return
      @return: The value of the attribute
      """
      return self._attribs[name]

   # ......................................
   def getAttributes(self):
      """
      @summary: Gets the dictionary of attributes attached to the object
      @return: The attribute dictionary
      @rtype: Dictionary
      """
      return self._attribs
   
   # ......................................
   def setAttribute(self, name, value):
      """
      @summary: Sets the value of an attribute in the attribute dictionary
      @param name: The name of the attribute to set
      @param value: The new value for the attribute
      """
      self._attribs[name] = value

# .............................................................................
class LmAttList(list, LmAttObj):
   """
   @summary: Extension to lists that adds attributes
   @note: obj = LmAttList([1, 2, 3], {'id': 'attList'})
          print obj[0] >>  1
          obj.append('apple')
          print obj >> [1, 2, 3, 'apple']
          print obj.id >> 'attList'
   """
   def __init__(self, items=[], attribs={}, name="LmList"):
      """
      @summary: Constructor
      @param items: (optional) A list of initial values for the list
      @param attribs: (optional) Dictionary of attributes to attach to the list
      @param name: (optional) The name of the object (useful for serialization) 
      """
      LmAttObj.__init__(self, attribs, name)
      for item in items:
         self.append(item)
   
# =============================================================================
# =                             Helper Functions                              =
# =============================================================================
# .............................................................................
def deserialize(element, removeNS=True):
   """
   @summary: Deserializes an ElementTree (Sub)Element into an object
   @param element: The element to deserialize
   @param removeNS: (optional) If true, removes the namespaces from the tags
   @return: A new object
   """
   # If removeNS is set to true, look for namespaces in the tag and remove them
   #    They are enclosed in curly braces {namespace}tag
   if removeNS:
      processTag = lambda s: s.split("}")[1] if s.find("}") >= 0 else s
   else:
      processTag = lambda s: s
   
   # If the element has no children, just get the text   
   if len(list(element)) == 0 and len(element.attrib.keys()) == 0:
      try:
         val = element.text.strip()
         if len(val) > 0:
            return val
         else:
            return None
      except:
         return None
   else:
      attribs = dict([(processTag(key), element.attrib[key]) for key in element.attrib.keys()])
      obj = LmAttObj(attribs=attribs, name=processTag(element.tag))

      try:
         val = element.text.strip()
         if len(val) > 0:
            obj.value = val
      except:
         pass
      
      # Get a list of all of the element's children's tags
      # If they are all the same type and match the parent, make one list
      tags = [child.tag for child in list(element)]
      reducedTags = list(set(tags))
      
      if len(reducedTags) == 1 and reducedTags[0] == element.tag[:-1]: # or len(tags) > 1):
         obj = LmAttList([], attribs=attribs, name=processTag(element.tag))
         for child in list(element):
            obj.append(deserialize(child, removeNS))
      else:
         # Process the children
         for child in list(element):
            if hasattr(obj, processTag(child.tag)):
               tmp = obj.__getattribute__(processTag(child.tag))
               if isinstance(tmp, ListType):
                  tmp.append(deserialize(child, removeNS))
               else:
                  tmp = LmAttList([tmp, deserialize(child, removeNS)], name=processTag(child.tag)+'s')
               setattr(obj, processTag(child.tag), tmp)
            else:
               setattr(obj, processTag(child.tag), deserialize(child, removeNS))
      return obj

# .............................................................................
def removeNonesFromTupleList(paramsList):
   """
   @summary: Removes parameter values that are None
   @param paramsList: List of parameters (name, value) [list of tuples]
   @return: List of parameters that are not None [list of tuples]
   """
   ret = []
   for param in paramsList:
      if param[1] is not None:
         ret.append(param)
   return ret

