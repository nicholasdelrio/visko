"""
@summary: Client functions for Lifemapper RAD web services
@author: CJ Grady
@version: 1.0
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
"""
from types import ListType

from constants import WEBSITE_ROOT

# .............................................................................
class RADClient(object):
   """
   @summary: Lifemapper RAD web service functions
   """
   # .........................................
   def __init__(self, cl):
      """
      @summary: Constructor
      """
      self.cl = cl
   
   # .........................................
   def addAncLayer(self, expId, lyrId, attrValue=None, 
                   calculateMethod="weightedMean", minPercent=None):
      """
      @summary: Adds an ancillary layer to a RAD experiment
      @param expId: The id of the experiment to add the ancillary layer to
      @param lyrId: The id of the layer to add
      @param attrValue: (optional) The attribute value
      @param calculateMethod: (optional) The method used for calculation
      @param minPercent: (optional) The minimum percentage for presence
      """
      postXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<wps:Execute version="1.0.0" service="WPS" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xmlns="http://www.opengis.net/wps/1.0.0" 
             xmlns:wfs="http://www.opengis.net/wfs" 
             xmlns:wps="http://www.opengis.net/wps/1.0.0" 
             xmlns:ows="http://www.opengis.net/ows/1.1" 
             xmlns:xlink="http://www.w3.org/1999/xlink" 
             xmlns:lmRad="http://lifemapper.org"
             xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd">
  <ows:Identifier>addanclayer</ows:Identifier>
  <wps:DataInputs>
      <wps:Input>
         <ows:Identifier>ancLayer</ows:Identifier>
         <wps:Data>
            <wps:ComplexData>
               <lmRad:layerId>%s</lmRad:layerId>
               <lmRad:parameters>
%s%s%s
               </lmRad:parameters>
            </wps:ComplexData>
         </wps:Data>
      </wps:Input>
  </wps:DataInputs>
  <wps:ResponseForm>
    <wps:RawDataOutput mimeType="application/gml-3.1.1">
      <ows:Identifier>result</ows:Identifier>
    </wps:RawDataOutput>
  </wps:ResponseForm>
</wps:Execute>
""" % (lyrId,
       "                  <lmRad:attrValue>%s</lmRad:attrValue>\n" 
          % attrValue if attrValue is not None else "",
       "                  <lmRad:calculateMethod>%s</lmRad:calculateMethod>\n" 
          % calculateMethod if calculateMethod is not None else "",
       "                  <lmRad:minPercent>%s</lmRad:minPercent>\n" 
          % minPercent if minPercent is not None else "",
      )
      
      url = "%s/services/rad/experiments/%s/addanclayer" % (WEBSITE_ROOT, expId)
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=[("request", "Execute")], 
                                body=postXml, 
                                headers={"Content-Type" : "application/xml"}, 
                                objectify=True)
      if obj.Status.ProcessSucceeded is not None:
         return True
      else:
         return False
   
   # .........................................
   def addPALayer(self, expId, lyrId, attrPresence, minPresence, 
                  maxPresence, percentPresence, attrAbsence=None, 
                  minAbsence=None, maxAbsence=None, percentAbsence=None):
      """
      @summary: Adds a presence / absence layer to an experiment
      @param expId: The id of the experiment to add this layer for
      @param lyrId: The id of the layer to add
      @param attrPresence: The attribute indicating presence
      @param minPresence: The minimum value indicating presence
      @param maxPresence: The maximum value indicating presence
      @param percentPresence: The proportion required to indicate presence
      @param attrAbsence: (optional) The attribute indicating absence
      @param minAbsence: (optional) The minimum value indicating absence
      @param maxAbsence: (optional) The maximum value indicating absence
      @param percentAbsence: (optional) The portion required to indicate absence
      """
      postXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<wps:Execute version="1.0.0" service="WPS" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xmlns="http://www.opengis.net/wps/1.0.0" 
             xmlns:wfs="http://www.opengis.net/wfs" 
             xmlns:wps="http://www.opengis.net/wps/1.0.0" 
             xmlns:ows="http://www.opengis.net/ows/1.1" 
             xmlns:xlink="http://www.w3.org/1999/xlink" 
             xmlns:lmRad="http://lifemapper.org"
             xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd">
  <ows:Identifier>addpalayer</ows:Identifier>
  <wps:DataInputs>
      <wps:Input>
         <ows:Identifier>paLayer</ows:Identifier>
         <wps:Data>
            <wps:ComplexData>
               <lmRad:layerId>%s</lmRad:layerId>
               <lmRad:parameters>
%s%s%s%s%s%s%s%s
               </lmRad:parameters>
            </wps:ComplexData>
         </wps:Data>
      </wps:Input>
  </wps:DataInputs>
  <wps:ResponseForm>
    <wps:RawDataOutput mimeType="application/gml-3.1.1">
      <ows:Identifier>result</ows:Identifier>
    </wps:RawDataOutput>
  </wps:ResponseForm>
</wps:Execute>
""" % (lyrId,
       "                  <lmRad:attrPresence>%s</lmRad:attrPresence>\n" 
          % attrPresence if attrPresence is not None else "",
       "                  <lmRad:minPresence>%s</lmRad:minPresence>\n" 
          % minPresence if minPresence is not None else "",
       "                  <lmRad:maxPresence>%s</lmRad:maxPresence>\n" 
          % maxPresence if maxPresence is not None else "",
       "                  <lmRad:percentPresence>%s</lmRad:percentPresence>\n" 
          % percentPresence if percentPresence is not None else "",
       "                  <lmRad:attrAbsence>%s</lmRad:attrAbsence>\n" 
          % attrAbsence if attrAbsence is not None else "",
       "                  <lmRad:minAbsence>%s</lmRad:minAbsence>\n" 
          % minAbsence if minAbsence is not None else "",
       "                  <lmRad:maxAbsence>%s</lmRad:maxAbsence>\n" 
          % maxAbsence if maxAbsence is not None else "",
       "                  <lmRad:percentAbsence>%s</lmRad:percentAbsence>\n" 
          % percentAbsence if percentAbsence is not None else "",
      )
      
      url = "%s/services/rad/experiments/%s/addpalayer" % (WEBSITE_ROOT, expId)
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=[("request", "Execute")], 
                                body=postXml, 
                                headers={"Content-Type" : "application/xml"},
                                objectify=True)
      if obj.Status.ProcessSucceeded is not None:
         return True
      else:
         return False
   
   # .........................................
   def addBucket(self, expId, shpName, cellShape, cellSize, mapUnits, epsgCode, bbox, cutout=None):
      """
      @summary: Adds a bucket to an experiment
      @param expId: The id of the experiment to add a bucket to
      @param shpName: The name of this new bucket's shapegrid
      @param cellShape: The shape of the cells for the shapegrid
      @param cellSize: The size of the cells in mapUnits
      @param mapUnits: The units of the cell size (ie: dd for decimal degrees)
      @param epsgCode: The EPSG code representing the projection of the bucket
      @param bbox: The bounding box for the new bucket
      @param cutout: (optional) WKT representing the area to cut out
      """
      postXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<wps:Execute version="1.0.0" service="WPS" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xmlns="http://www.opengis.net/wps/1.0.0" 
             xmlns:wfs="http://www.opengis.net/wfs" 
             xmlns:wps="http://www.opengis.net/wps/1.0.0" 
             xmlns:ows="http://www.opengis.net/ows/1.1" 
             xmlns:xlink="http://www.w3.org/1999/xlink" 
             xmlns:lmRad="http://lifemapper.org"
             xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd">
  <ows:Identifier>addbucket</ows:Identifier>
  <wps:DataInputs>
      <wps:Input>
         <ows:Identifier>bucket</ows:Identifier>
         <wps:Data>
            <wps:ComplexData>
               <lmRad:shapegrid>
                  <lmRad:name>%s</lmRad:name>
                  <lmRad:cellShape>%s</lmRad:cellShape>
                  <lmRad:cellSize>%s</lmRad:cellSize>
                  <lmRad:mapUnits>%s</lmRad:mapUnits>
                  <lmRad:epsgCode>%s</lmRad:epsgCode>
                  <lmRad:bounds>%s</lmRad:bounds>
%s
               </lmRad:shapegrid>
            </wps:ComplexData>
         </wps:Data>
      </wps:Input>
  </wps:DataInputs>
  <wps:ResponseForm>
    <wps:RawDataOutput mimeType="application/gml-3.1.1">
      <ows:Identifier>result</ows:Identifier>
    </wps:RawDataOutput>
  </wps:ResponseForm>
</wps:Execute>
""" % (shpName, cellShape, cellSize, mapUnits, epsgCode, bbox, "                 <lmRad:cutout>%s</lmRad:cutout>" % cutout if cutout is not None else "")
      
      url = "%s/services/rad/experiments/%s/addbucket" % (WEBSITE_ROOT, expId)
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=[("request", "Execute")], 
                                body=postXml, 
                                headers={"Content-Type" : "application/xml"},
                                objectify=True)
      if obj.Status.ProcessSucceeded is not None:
         return obj.Status.ProcessOutputs.Output.Data.LiteralData.value
      else:
         return False

   # .........................................
   def intersect(self, expId, bucketId=None):
      """
      @summary: Requests that an intersection is performed against a bucket or
                   all of the buckets in an experiment
      @param expId: The id of the experiment to perform intersections for
      @param bucketId: (optional) The id of the bucket to intersect.  If no 
                          bucket id is provided, all buckets in the experiment
                          will be intersected.
      """
      postXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<wps:Execute version="1.0.0" service="WPS" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xmlns="http://www.opengis.net/wps/1.0.0" 
             xmlns:wfs="http://www.opengis.net/wfs" 
             xmlns:wps="http://www.opengis.net/wps/1.0.0" 
             xmlns:ows="http://www.opengis.net/ows/1.1" 
             xmlns:xlink="http://www.w3.org/1999/xlink" 
             xmlns:lmRad="http://lifemapper.org"
             xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd">
  <ows:Identifier>intersect</ows:Identifier>
  <wps:DataInputs>
  </wps:DataInputs>
  <wps:ResponseForm>
    <wps:RawDataOutput mimeType="application/gml-3.1.1">
      <ows:Identifier>result</ows:Identifier>
    </wps:RawDataOutput>
  </wps:ResponseForm>
</wps:Execute>
""" 
      
      url = "%s/services/rad/experiments/%s/%sintersect" % (WEBSITE_ROOT, expId, 
               "buckets/%s/" % bucketId if bucketId is not None else "")
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=[("request", "Execute")], 
                                body=postXml, 
                                headers={"Content-Type" : "application/xml"},
                                objectify=True)
      if obj.Status.ProcessAccepted is not None:
         if bucketId is not None:
            return lambda : self.getBucketStatus(expId, bucketId)
         else:
            return lambda : self.getExpStatus(expId)
      else:
         return False

   # .........................................
   def calculate(self, expId, bucketId, pamsumId=None):
      """
      @summary: Requests that statistics are calculated for a pamsum or all of
                   the pamsums in a bucket
      @param expId: The id of the experiment that contains the buckets / pamsums
      @param bucketId: The id of the bucket
      @param pamSumId: (optional) The id of the pamsum to calculate statistics
                          for.  If this is not provided, statistics will be 
                          calculated for all pamsums in the bucket
      """
      postXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<wps:Execute version="1.0.0" service="WPS" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xmlns="http://www.opengis.net/wps/1.0.0" 
             xmlns:wfs="http://www.opengis.net/wfs" 
             xmlns:wps="http://www.opengis.net/wps/1.0.0" 
             xmlns:ows="http://www.opengis.net/ows/1.1" 
             xmlns:xlink="http://www.w3.org/1999/xlink" 
             xmlns:lmRad="http://lifemapper.org"
             xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd">
  <ows:Identifier>calculate</ows:Identifier>
  <wps:DataInputs>
%s
  </wps:DataInputs>
  <wps:ResponseForm>
    <wps:RawDataOutput mimeType="application/gml-3.1.1">
      <ows:Identifier>result</ows:Identifier>
    </wps:RawDataOutput>
  </wps:ResponseForm>
</wps:Execute>
""" % """\
      <wps:Input>
         <ows:Identifier>pamsumId</ows:Identifier>
         <wps:Data>
            <wps:LiteralData>%s</wps:LiteralData>
         </wps:Data>
      </wps:Input>
""" % pamsumId if pamsumId is not None else """\
      <wps:Input>
         <ows:Identifier>bucketId</ows:Identifier>
         <wps:Data>
            <wps:LiteralData>%s</wps:LiteralData>
         </wps:Data>
      </wps:Input>
""" % bucketId
   
      url = "%s/services/rad/experiments/%s/buckets/%s/%scalculate" % (
               WEBSITE_ROOT, expId, bucketId, 
               "pamsums/%s/" % pamsumId if pamsumId is not None else "")
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=[("request", "Execute")], 
                                body=postXml, 
                                headers={"Content-Type" : "application/xml"},
                                objectify=True)
      if obj.Status.ProcessAccepted is not None:
         if pamsumId is not None:
            return lambda : self.getPamSumStatus(expId, bucketId, pamsumId)
         else:
            return lambda : self.getBucketStatus(expId, bucketId)
      else:
         return False

   # .........................................
   def compress(self, expId, bucketId, pamsumId=None):
      """
      @summary: Requests that a pamsum (or pamsums) is compressed
      @param expId: The id of the experiment containing the bucket
      @param bucketId: The id of the bucket containing the pamsum(s)
      @param pamsumId: (optional) The id of the pamsum to compress.  If this is
                          not provided, all pamsums in a bucket will be 
                          compressed
      """
      postXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<wps:Execute version="1.0.0" service="WPS" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xmlns="http://www.opengis.net/wps/1.0.0" 
             xmlns:wfs="http://www.opengis.net/wfs" 
             xmlns:wps="http://www.opengis.net/wps/1.0.0" 
             xmlns:ows="http://www.opengis.net/ows/1.1" 
             xmlns:xlink="http://www.w3.org/1999/xlink" 
             xmlns:lmRad="http://lifemapper.org"
             xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd">
  <ows:Identifier>compress</ows:Identifier>
  <wps:DataInputs>
%s
  </wps:DataInputs>
  <wps:ResponseForm>
    <wps:RawDataOutput mimeType="application/gml-3.1.1">
      <ows:Identifier>result</ows:Identifier>
    </wps:RawDataOutput>
  </wps:ResponseForm>
</wps:Execute>
""" % """\
      <wps:Input>
         <ows:Identifier>pamsumId</ows:Identifier>
         <wps:Data>
            <wps:LiteralData>%s</wps:LiteralData>
         </wps:Data>
      </wps:Input>
""" % pamsumId if pamsumId is not None else ""
   
      url = "%s/services/rad/experiments/%s/buckets/%s/compress" % (
                                                                  WEBSITE_ROOT, 
                                                                  expId, 
                                                                  bucketId)
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=[("request", "Execute")], 
                                body=postXml, 
                                headers={"Content-Type" : "application/xml"},
                                objectify=True)
      if obj.Status.ProcessAccepted is not None:
         if bucketId is not None:
            return lambda : self.getBucketStatus(expId, bucketId)
         else:
            return lambda : self.getExpStatus(expId)
      else:
         return False

   # .........................................
   def randomize(self, expId, bucketId, method='swap', iterations=10000):
      """
      @summary: Requests that a bucket be randomized
      @param expId: The id of the experiment containing the bucket to randomize
      @param bucketId: The id of the bucket to randomize
      @param method: (optional) The randomization method to use (swap | splotch)
      @param iterations: (optional) The number of swap iterations to perform
      """
      postXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<wps:Execute version="1.0.0" service="WPS" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xmlns="http://www.opengis.net/wps/1.0.0" 
             xmlns:wfs="http://www.opengis.net/wfs" 
             xmlns:wps="http://www.opengis.net/wps/1.0.0" 
             xmlns:ows="http://www.opengis.net/ows/1.1" 
             xmlns:xlink="http://www.w3.org/1999/xlink" 
             xmlns:lmRad="http://lifemapper.org"
             xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd">
  <ows:Identifier>randomize</ows:Identifier>
  <wps:DataInputs>
      <wps:Input>
         <ows:Identifier>randomizeMethod</ows:Identifier>
         <wps:Data>
            <wps:LiteralData>%s</wps:LiteralData>
         </wps:Data>
      </wps:Input>
      <wps:Input>
         <ows:Identifier>iterations</ows:Identifier>
         <wps:Data>
            <wps:LiteralData>%s</wps:LiteralData>
         </wps:Data>
      </wps:Input>
  </wps:DataInputs>
  <wps:ResponseForm>
    <wps:RawDataOutput mimeType="application/gml-3.1.1">
      <ows:Identifier>result</ows:Identifier>
    </wps:RawDataOutput>
  </wps:ResponseForm>
</wps:Execute>
""" % (method, iterations)
   
      url = "%s/services/rad/experiments/%s/buckets/%s/randomize" % (
                                                                  WEBSITE_ROOT, 
                                                                  expId, 
                                                                  bucketId)
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=[("request", "Execute")], 
                                body=postXml, 
                                headers={"Content-Type" : "application/xml"},
                                objectify=True)
      if obj.Status.ProcessAccepted is not None:
         if bucketId is not None:
            return lambda : self.getBucketStatus(expId, bucketId)
         else:
            return lambda : self.getExpStatus(expId)
      else:
         return False

   # .........................................
   def countExperiments(self, afterTime=None, beforeTime=None, epsgCode=None):
      """
      @summary: Counts experiments for a user
      @param afterTime: List experiments with creation times after this time 
                           (ISO 8601 format)
      @param beforeTime: List experiments with creation times before this time 
                            (ISO 8601 format)
      @param epsgCode: The epsg code of the experiments to return
      """
      url = "%s/services/rad/experiments/" % WEBSITE_ROOT
      return self.cl.getCount(url, parameters=[("afterTime", afterTime), 
                                              ("beforeTime", beforeTime),
                                              ("epsgCode", epsgCode)])
      
   # .........................................
   def listExperiments(self, afterTime=None, beforeTime=None, epsgCode=None, 
                                                          page=0, perPage=100):
      """
      @summary: Lists experiments for a user
      @param afterTime: List experiments with creation times after this time 
                           (ISO 8601 format)
      @param beforeTime: List experiments with creation times before this time 
                            (ISO 8601 format)
      @param epsgCode: The epsg code of the experiments to return
      @param page: The page of results
      @param perPage: The number of results per page
      """
      url = "%s/services/rad/experiments/" % WEBSITE_ROOT
      return self.cl.getList(url, parameters=[("afterTime", afterTime), 
                                              ("beforeTime", beforeTime),
                                              ("epsgCode", epsgCode),
                                              ("page", page),
                                              ("perPage", perPage)])
      
   # .........................................
   def listBuckets(self, expId, afterTime=None, beforeTime=None, page=0, 
                                                                  perPage=100):
      """
      @summary: Lists buckets for a user
      @param expId: The id of the experiment to list buckets for
      @param afterTime: List buckets with creation times after this time 
                           (ISO 8601 format)
      @param beforeTime: List buckets with creation times before this time 
                            (ISO 8601 format)
      @param page: The page of results
      @param perPage: The number of results per page
      """
      url = "%s/services/rad/experiments/%s/buckets" % (WEBSITE_ROOT, expId)
      bkts = self.cl.getList(url, parameters=[("afterTime", afterTime), 
                                              ("beforeTime", beforeTime),
                                              ("page", page),
                                              ("perPage", perPage)])
      return bkts
   
   # .........................................
   def listPamSums(self, expId, bucketId, afterTime=None, beforeTime=None, 
                                            page=0, perPage=100, randomized=1):
      """
      @summary: Lists pamsums for a user
      @param expId: The id of the experiment
      @param bucketId: The id of the bucket
      @param afterTime: List pamsums with creation times after this time 
                           (ISO 8601 format)
      @param beforeTime: List pamsums with creation times before this time 
                            (ISO 8601 format)
      @param page: The page of results
      @param perPage: The number of results per page
      @param randomized: 1 for random pamsums, 0 for original
      """
      url = "%s/services/rad/experiments/%s/buckets/%s/pamsums/" % \
               (WEBSITE_ROOT, expId, bucketId)
      return self.cl.getList(url, parameters=[("afterTime", afterTime), 
                                              ("beforeTime", beforeTime),
                                              ("page", page),
                                              ("perPage", perPage),
                                              ("randomized", randomized)])

   # .........................................
   def listShapegrids(self, afterTime=None, beforeTime=None, epsgCode=None, 
                            cellSides=None, layerId=None, layerName=None, 
                            page=0, perPage=100):
      """
      @summary: Lists shapegrids for a user
      @param afterTime: List shapegrids with creation times after this time 
                           (ISO 8601 format)
      @param beforeTime: List shapegrids with creation times before this time 
                            (ISO 8601 format)
      @param epsgCode: The epsg code of the shapegrids to return
      @param cellSides: The number of sides for each cell of the shapegrids.
                           Use 4 for square grids and 6 for hexagonal.
      @param layerId: Return shapegrids with this layer id
      @param layerName: Return shapegrids with this layer name
      @param page: The page of results
      @param perPage: The number of results per page
      """
      url = "%s/services/rad/shapegrids/" % WEBSITE_ROOT
      return self.cl.getList(url, parameters=[("afterTime", afterTime), 
                                              ("beforeTime", beforeTime),
                                              ("epsgCode", epsgCode),
                                              ("cellSides", cellSides),
                                              ("layerId", layerId),
                                              ("layerName", layerName),
                                              ("page", page),
                                              ("perPage", perPage)])
      
   # .........................................
   def getAncLayers(self, expId):
      """
      @summary: Get ancillary layers associated with an experiment
      @param expId: The id of the experiment to return layers for
      """
      url = "%s/services/rad/experiments/%s/anc" % (WEBSITE_ROOT, expId)
      try:
         respObj = self.cl.makeRequest(url, method="GET", objectify=True)
         lyrs = respObj.layerset.layers
         if lyrs is not None:
            if not isinstance(lyrs, ListType):
               lyrs = [lyrs]
            return lyrs
      except Exception, _:
         return []
         
   # .........................................
   def getPALayers(self, expId):
      """
      @summary: Get presence / absence layers associated with an experiment
      @param expId: The id of the experiment to return layers for
      """
      url = "%s/services/rad/experiments/%s/pa" % (WEBSITE_ROOT, expId)
      try:
         respObj = self.cl.makeRequest(url, method="GET", objectify=True)
         lyrs = respObj.layerset.layers
         if lyrs is not None:
            if not isinstance(lyrs, ListType):
               lyrs = [lyrs]
            return lyrs
      except Exception, _:
         return []
         
   # .........................................
   def listLayers(self, afterTime=None, beforeTime=None, layerName=None, 
                                                          page=0, perPage=100):
      """
      @summary: Lists buckets for a user
      @param expId: The id of the experiment to list buckets for
      @param afterTime: List buckets with creation times after this time 
                           (ISO 8601 format)
      @param beforeTime: List buckets with creation times before this time 
                            (ISO 8601 format)
      @param layerName: Return layers that match this name
      @param page: The page of results
      @param perPage: The number of results per page
      """
      url = "%s/services/rad/layers" % WEBSITE_ROOT
      lyrs = self.cl.getList(url, parameters=[("afterTime", afterTime), 
                                              ("beforeTime", beforeTime),
                                              ("layerName", layerName),
                                              ("page", page),
                                              ("perPage", perPage)])
      return lyrs
   
   # .........................................
   def postExperiment(self, name, epsgCode, email=None):#, buckets=[], paLayers=[], ancLayers=[]):
      """
      @summary: Posts a new Lifemapper RAD experiment
      @param name: The name of this new experiment
      @param epsgCode: The EPSG code representing the projection used for all
                          spatial data in this experiment
      @param email: (optional) An email address to associate with this 
                       experiment
      """
      postXml = """\
      <lmRad:request xmlns:lmRad="http://lifemapper.org"
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://lifemapper.org 
                                               /schemas/radServiceRequest.xsd">
            <lmRad:experiment>
               <lmRad:name>%s</lmRad:name>
               <lmRad:epsgCode>%s</lmRad:epsgCode>
%s
            </lmRad:experiment>
         </lmRad:request>""" % (name, epsgCode, 
                           "               <lmRad:email>%s</lmRad:email>" % \
                           email if email is not None else "")
      url = "%s/services/rad/experiments/" % WEBSITE_ROOT
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                body=postXml, 
                                headers={"Content-Type": "application/xml"},
                                objectify=True)
      return obj.experiment
   
   # .........................................
   def postRaster(self, name, filename=None, layerUrl=None, epsgCode=4326, 
                  title=None, bbox=None, startDate=None, endDate=None, 
                  mapUnits=None, resolution=None, valUnits=None, 
                  dataFormat="GTiff", valAttribute=None, description=None, 
                  keywords=[]):
      """
      @summary: Uploads a raster layer to Lifemapper to be used in experiments
      @param name: The name of this layer
      @param filename: (optional) The local location of the raster file to 
                          upload (note that this or layerUrl must be specified)
      @param layerUrl: (optional) The remote location (URL) of the raster file
                          to add to Lifemapper (note that this or filename
                          must be specified)
      @param epsgCode: (optional) The EPSG code representing the projection
                          of this layer
      @param title: (optional) The title of this uploaded layer
      @param bbox: (optional) The bounding box for this layer
      @param startDate: (optional) The start date associated with the 
                           measurements of this layer
      @param endDate: (optional) The end date associated with the measurements
                         of this layer
      @param mapUnits: (optional) The units associated with the size of the 
                          cells in the raster
      @param resolution: (optional) The resolution of the cells in this raster
      @param valUnits: (optional) The units associated with the values in this
                          raster
      @param dataFormat: (optional) The format of this raster
      @param valAttribute: (optional) The attribute associated with value
      @param description: (optional) A description of this raster layer
      @param keywords: (optional) A list of keywords associated with this raster
      """
      p = [
           ("name" , name),
           ("title", title),
           ("bbox", bbox),
           ("startDate", startDate),
           ("endDate", endDate),
           ("mapUnits", mapUnits),
           ("resolution", resolution),
           ("epsgCode", epsgCode),
           ("valUnits", valUnits),
           ('raster', 1),
           #("gdalType", gdalType),
           ("dataFormat", dataFormat),
           ("valAttribute", valAttribute),
           ("description", description),
          ]
      for kw in keywords:
         p.append(("keyword", kw))
      url = "%s/services/rad/layers" % WEBSITE_ROOT
      if filename is None:
         if layerUrl is None:
            raise Exception, "Either layerUrl or filename must be specified when posting a raster layer"
         p.append(("layerUrl", layerUrl))
         obj = self.cl.makeRequest(url,
                                   method="POST",
                                   parameters=p,
                                   objectify=True)
      else:
         obj = self.cl.makeRequest(url, 
                                   method="POST", 
                                   parameters=p, 
                                   body=open(filename).read(), 
                                   headers={"Content-Type" : "image/tiff"},
                                   objectify=True)
      return obj.layer

   # .........................................
   def postShapefile(self, shpName, cellShape, cellSize, mapUnits, epsgCode, bbox, cutout=None):
      """
      @summary: Posts a new shapegrid
      @param shpName: The name of this new bucket's shapegrid
      @param cellShape: The shape of the cells for the shapegrid
      @param cellSize: The size of the cells in mapUnits
      @param mapUnits: The units of the cell size (ie: dd for decimal degrees)
      @param epsgCode: The EPSG code representing the projection of the bucket
      @param bbox: The bounding box for the new bucket
      @param cutout: (optional) WKT representing the area to cut out
      """
      postXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<lmRad:request xmlns:lmRad="http://lifemapper.org"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://lifemapper.org 
                                               /schemas/radServiceRequest.xsd">
   <lmRad:shapegrid>
      <lmRad:name>%s</lmRad:name>
      <lmRad:cellShape>%s</lmRad:cellShape>
      <lmRad:cellSize>%s</lmRad:cellSize>
      <lmRad:mapUnits>%s</lmRad:mapUnits>
      <lmRad:epsgCode>%s</lmRad:epsgCode>
      <lmRad:bounds>%s</lmRad:bounds>
%s
   </lmRad:shapegrid>
</lmRad:request> 
""" % (shpName, cellShape, cellSize, mapUnits, epsgCode, bbox, 
       "                 <lmRad:cutout>%s</lmRad:cutout>" % \
                                          cutout if cutout is not None else "")
      
      url = "%s/services/rad/shapegrids"
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                body=postXml, 
                                headers={"Content-Type" : "application/xml"},
                                objectify=True)
      return obj

   # .........................................
   def postVector(self, name, filename=None, layerUrl=None, epsgCode=4326, 
                  title=None, bbox=None, startDate=None, endDate=None, 
                  mapUnits=None, resolution=None, valUnits=None, 
                  dataFormat="ESRI Shapefile", valAttribute=None, 
                  description=None, keywords=[]):
      """
      @summary: Uploads a vector layer to Lifemapper to be used in experiments
      @param name: The name of this layer
      @param filename: (optional) The local location of the vector file to 
                          upload (note that this or layerUrl must be specified)
      @param layerUrl: (optional) The remote location (URL) of the vector file
                          to add to Lifemapper (note that this or filename
                          must be specified)
      @param epsgCode: (optional) The EPSG code representing the projection
                          of this layer
      @param title: (optional) The title of this uploaded layer
      @param bbox: (optional) The bounding box for this layer
      @param startDate: (optional) The start date associated with the 
                           measurements of this layer
      @param endDate: (optional) The end date associated with the measurements
                         of this layer
      @param mapUnits: (optional) The units associated with the distances of 
                          the vector layer
      @param resolution: (optional) The resolution of this vector layer
      @param valUnits: (optional) The units associated with the values in this
                          vector
      @param dataFormat: (optional) The format of this vector
      @param valAttribute: (optional) The attribute associated with value
      @param description: (optional) A description of this vector layer
      @param keywords: (optional) A list of keywords associated with this vector
      """
      p = [
           ("name" , name),
           ("title", title),
           ("bbox", bbox),
           ("startDate", startDate),
           ("endDate", endDate),
           ("mapUnits", mapUnits),
           ("resolution", resolution),
           ("epsgCode", epsgCode),
           ("valUnits", valUnits),
           ('vector', 1),
           #("ogrType", ogrType),
           ("dataFormat", dataFormat),
           ("valAttribute", valAttribute),
           ("description", description),
          ]
      for kw in keywords:
         p.append(("keyword", kw))
      url = "%s/services/rad/layers" % WEBSITE_ROOT
      if filename is None:
         if layerUrl is None:
            raise Exception, "Either layerUrl or filename must be specified when posting a vector layer"
         p.append(("layerUrl", layerUrl))
         obj = self.cl.makeRequest(url,
                                   method="POST",
                                   parameters=p,
                                   objectify=True)
      else:
         if filename.endswith('.zip'):
            content = open(filename).read()
         else:
            content = self.cl.getAutozipShapefileStream(filename)
         obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=p, 
                                body=content, 
                                headers={"Content-Type": "application/x-gzip"},
                                objectify=True)
      return obj.layer
   
   # .........................................
   def getShapegridData(self, filePath, expId, bucketId, intersected=False):
      """
      @summary: Gets a bucket's shapegrid as a shapefile
      @param filePath: The path of the location to save this file
      @param expId: The experiment containing the bucket
      @param bucketId: The id of the bucket to get the shapegrid for
      @param intersected: (optional) If True, returns intersected layers in the
                             shapefile, else returns the shapgrid itself
      """
      url = "%s/services/rad/experiments/%s/buckets/%s/%sshapefile" % \
               (WEBSITE_ROOT, expId, bucketId, 
                "" if intersected else "shapegrid/")
      sf = self.cl.makeRequest(url, method="GET")
      try:
         open(filePath, 'w').write(sf)
      except Exception, e:
         print str(e)
         return False
      return True
   
   # .........................................
   def getExpStatus(self, expId):
      """
      @todo: Remove dummy function
      """
      stage = 1
      status = 35
      return status, stage
   
   # .........................................
   def getBucketStatus(self, experimentId, bucketId):
      """
      @summary: Gets the status of a bucket
      @param experimentId: The id of the experiment containing the bucket
      @param bucketId: The id of the bucket to get the status of
      """
      url = "%s/services/rad/experiments/%s/buckets/%s/status" % (WEBSITE_ROOT, 
                                                        experimentId, bucketId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True)
      
      return obj.status, obj.stage
   
   # .........................................
   def getPamSumShapegrid(self, filePath, experimentId, bucketId, pamsumId):
      """
      @summary: Gets the shapegrid containing pamsum data
      @param filePath: The local location to store the file
      @param experimentId: The id of the experiment container
      @param bucketId: The id of the bucket containing the pamsum
      @param pamsumId: The id of the pamsum to return intersected with the 
                         shapegrid
      """
      url = "%s/services/rad/experiments/%s/buckets/%s/pamsums/%s/shapefile" %\
               (WEBSITE_ROOT, experimentId, bucketId, pamsumId)
      sf = self.cl.makeRequest(url, method="GET")
      try:
         open(filePath, 'w').write(sf)
      except Exception, e:
         print str(e)
         return False
      return True
   
   # .........................................
   def getPamSumStatus(self, experimentId, bucketId, pamsumId):
      """
      @summary: Gets the status of a pamsum
      @param experimentId: The id of the experiment contianer
      @param bucketId: The id of the bucket containing the pamsum
      @param pamsumId: The id of the pamsum to return the status of
      """
      url = "%s/services/rad/experiments/%s/buckets/%s/pamsums/%s/status" % \
               (WEBSITE_ROOT, experimentId, bucketId, pamsumId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True)
      
      return obj.status, obj.stage
   
   # .........................................
   def getExperiment(self, expId):
      """
      @summary: Returns a Lifemapper RAD experiment
      @param expId: The id of the experiment to return
      """
      url = "%s/services/rad/experiments/%s" % (WEBSITE_ROOT, expId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True).experiment
      return obj
   
   # .........................................
   def getPamSum(self, expId, bucketId, pamSumId):
      """
      @summary: Returns a pamsum
      @param expId: The id of the experiment container
      @param bucketId: The id of the bucket containing the pamsum
      @param pamSumId: The id of the pamsum to return
      """
      url = "%s/services/rad/experiments/%s/buckets/%s/pamsums/%s" % \
               (WEBSITE_ROOT, expId, bucketId, pamSumId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True).pamsum
      return obj
   
   # .........................................
   def getShapegrid(self, shpId):
      """
      @summary: Returns a shapegrid's metadata
      @param shpId: The id of the shapegrid to return
      """
      url = "%s/services/rad/shapegrids/%s" % (WEBSITE_ROOT, shpId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True).shapegrid
      return obj

   # .........................................
   def getStatistic(self, expId, bucketId, pamSumId, stat):
      """
      @summary: Gets the available statistics for a pamsum
      @param expId: The id of the RAD experiment
      @param bucketId: The id of the RAD bucket
      @param pamSumId: The id of the pamsum to get statistics for
      @param stat: The key of the statistic to return
      """
      url = "%s/services/rad/experiments/%s/buckets/%s/pamsums/%s/statistics" %\
                        (WEBSITE_ROOT, expId, bucketId, pamSumId)
      statResp = self.cl.makeRequest(url, 
                                     method="GET", 
                                     parameters=[("statistic", stat)]).strip()
      ret = []
      if len(statResp.split('\n')) > 1:
         for line in statResp.split('\n'):
            if len(line.split(' ')) > 1:
               ret.append([i for i in line.split(' ')])
            else:
               ret.append(line)
      else:
         if len(statResp.split(' ')) > 1:
            ret = [i for i in statResp.split(' ')]
         else:
            ret = statResp.strip()
      return ret

   # .........................................
   def getStatisticsKeys(self, expId, bucketId, pamSumId, keys="keys"):
      """
      @summary: Gets the available statistics for a pamsum
      @param expId: The id of the RAD experiment
      @param bucketId: The id of the RAD bucket
      @param pamSumId: The id of the pamsum to get statistics for
      @param keys: (optional) The type of keys to return
                               (keys | specieskeys | siteskeys | diversitykeys)
      """
      url = "%s/services/rad/experiments/%s/buckets/%s/pamsums/%s/statistics" %\
                  (WEBSITE_ROOT, expId, bucketId, pamSumId)
      keyResp = self.cl.makeRequest(url, 
                                    method="GET", 
                                    parameters=[("statistic", keys)]).strip()
      keys = keyResp.split(' ')
      return keys

