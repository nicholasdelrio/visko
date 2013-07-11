"""
@summary: Module containing client functions for interacting with Lifemapper
             Species Distribution Modeling services
@author: CJ Grady
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
"""
from constants import WEBSITE_ROOT

# .............................................................................
class SDMClient(object):
   """
   @summary: Lifemapper SDM web service functions
   """
   # .........................................
   def __init__(self, cl):
      """
      @summary: Constructor
      """
      self.cl = cl

   # --------------------------------------------------------------------------
   # ===============
   # = Experiments =
   # ===============
   # .........................................
   def countExperiments(self, afterTime=None, beforeTime=None, 
                              displayName=None, algorithmCode=None, 
                              occurrenceSetId=None, status=None, public=False):
      """
      @summary: Counts the number of experiments that match the specified 
                   criteria.
      @param afterTime: (optional) Count only experiments modified after this 
                           time.  See time formats in the module documentation.
                           [integer or string]
      @param beforeTime: (optional) Count only experiments modified before this 
                           time.  See time formats in the module documentation.
                           [integer or string]
      @param displayName: (optional) Count only experiments with this display
                             name. [string]
      @param algorithmCode: (optional) Count only experiments generated with 
                               this algorithm code.  See available algorithms 
                               in the module documentation. [string]
      @param occurrenceSetId: (optional) Count only experiments generated from
                                 this occurrence set. [integer]
      @param status: (optional) Count only experiments with this model status.
                        More information about status is available in the 
                        module documentation. [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: The total number of experiments that match the given criteria.
                  [integer]
      """
      url = "%s/services/sdm/experiments/" % WEBSITE_ROOT
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("displayName", displayName),
                ("algorithmCode", algorithmCode),
                ("occurrenceSetId", occurrenceSetId),
                ("status", status),
                ("public", int(public))
               ]
      count = self.cl.getCount(url, params)
      return count
   
   # .........................................
   def getExperiment(self, expId):
      """
      @summary: Gets a Lifemapper experiment
      @param expId: The id of the experiment to be returned. [integer]
      @return: A Lifemapper experiment [LMExperiment]
      """
      url = "%s/services/sdm/experiments/%s" % (WEBSITE_ROOT, expId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True).experiment
      return obj
    
   # .........................................
   def listExperiments(self, afterTime=None, beforeTime=None, displayName=None, 
                             perPage=100, page=0, algorithmCode=None, 
                             occurrenceSetId=None, status=None, public=False):
      """
      @summary: Lists experiments that meet the specified criteria.
      @param afterTime: (optional) Return only experiments modified after this 
                           time.  See time formats in the module documentation. 
                           [integer or string]
      @param beforeTime: (optional) Return only experiments modified before 
                            this time.  See time formats in the module 
                            documentation. [integer or string]
      @param displayName: (optional) Return only experiments with this display
                             name. [string]
      @param perPage: (optional) Return this many results per page. [integer]
      @param page: (optional) Return this page of results. [integer]
      @param algorithmCode: (optional) Return only experiments generated from 
                               this algorithm.  See available algorithms in the
                               module documentation. [string]
      @param occurrenceSetId: (optional) Return only experiments generated from
                                 this occurrence set. [integer]
      @param status: (optional) Return only experiments with this status.  More
                        information about status can be found in the module
                        documentation. [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: Experiments that match the specified parameters. [LmAttObj]
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("displayName", displayName),
                ("algorithmCode", algorithmCode),
                ("occurrenceSetId", occurrenceSetId),
                ("status", status),
                ("page", page),
                ("perPage", perPage),
                ("public", int(public))
               ]
      url = "%s/services/sdm/experiments/" % WEBSITE_ROOT
      items = self.cl.getList(url, parameters=params)
      return items
   
   # .........................................
   def postExperiment(self, algorithmCode, mdlScn, occSetId, prjScns=[], 
                            mdlMask=None, prjMask=None, email=None):
      """
      @summary: Post a new Lifemapper experiment
      @param algorithm: An Lifemapper SDM algorithm object [SDMAlgorithm]
      @param mdlScn: The id of the model scenario to use for the experiment
                        [integer]
      @param occSetId: The id of the occurrence set to be used for the 
                          experiment. [integer]
      @param prjScns: (optional) List of projection scenario ids to use for the 
                         experiment [list of integers]
      @return: Experiment
      """
      postXml = """\
      <lm:request xmlns:lm="http://lifemapper.org"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://lifemapper.org 
                                               /schemas/serviceRequest.xsd">
            <lm:experiment>
               <lm:algorithm>
                  <lm:algorithmCode>%s</lm:algorithmCode>
               </lm:algorithm>
               <lm:occurrenceSetId>%s</lm:occurrenceSetId>
               <lm:modelScenario>%s</lm:modelScenario>
%s
%s
            </lm:experiment>
         </lm:request>""" % (algorithmCode, occSetId, mdlScn, 
                             "            <lm:email>%s</lm:email>" % email if email is not None else "",
                                '\n'.join(([
          "            <lm:projectionScenario>%s</lm:projectionScenario>" % \
                                            scnId for scnId in prjScns])))
      url = "%s/services/sdm/experiments/" % WEBSITE_ROOT
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                body=postXml, 
                                headers={"Content-Type": "application/xml"},
                                objectify=True).experiment
      return obj
   
   # --------------------------------------------------------------------------
   # ==========
   # = Layers =
   # ==========
   # .........................................
   def countLayers(self, afterTime=None, beforeTime=None, 
                         scenarioId=None, public=False):
      """
      @summary: Counts the number of layers that match the specified criteria.
      @param afterTime: (optional) Count only layers modified after this time.  
                           See time formats in the module documentation. 
                           [integer or string]
      @param beforeTime: (optional) Count only layers modified before this 
                            time.  See time formats in the module 
                            documentation. [integer or string]
      @param scenarioId: (optional) Count only layers belonging to this 
                            scenario. [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: The total number of layers that match the given criteria. 
                  [integer]
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("scenarioId", scenarioId),
                ("public", int(public))
               ]
      url = "%s/services/sdm/layers/" % WEBSITE_ROOT
      count = self.cl.getCount(url, params)
      return count
   
   # .........................................
   def getLayer(self, lyrId):
      """
      @summary: Gets a Lifemapper layer
      @param lyrId: The id of the layer to be returned. [integer]
      @return: A Lifemapper layer 
      """
      url = "%s/services/sdm/layers/%s" % (WEBSITE_ROOT, lyrId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True).layer
      return obj

   # .........................................
   def getLayerTiff(self, lyrId, filename):
      """
      @summary: Gets a Lifemapper layer as a tiff file
      @param lyrId: The id of the layer to be returned. [integer]
      @param filename: The location to save the resulting file
      @raise Exception: Raised if write fails
      """
      url = "%s/services/sdm/layers/%s/tiff" % (WEBSITE_ROOT, lyrId)
      f = open(filename, 'w')
      f.write(self.cl.makeRequest(url, method="GET"))
      f.close()

   # .........................................
   def getLayerOgcEndpoint(self, lyrId):
      """
      @summary: Gets the OGC endpoint for a layer
      @param lyrId: The id of the layer to get the endpoint for
      @return: A OGC URL endpoint for the layer
      """
      lyr = self.getLayer(lyrId)
      return lyr.guid
   
   # .........................................
   def listLayers(self, afterTime=None, beforeTime=None, 
                        perPage=100, page=0, scenarioId=None, public=False):
      """
      @summary: Lists layers that meet the specified criteria.
      @param afterTime: (optional) Return only layers modified after this time.  
                           See time formats in the module documentation. 
                           [integer or string]
      @param beforeTime: (optional) Return only layers modified before this 
                            time.  See time formats in the module 
                            documentation. [integer or string]
      @param perPage: (optional) Return this many results per page. [integer]
      @param page: (optional) Return this page of results. [integer]
      @param scenarioId: (optional) Return only layers that belong to this 
                            scenario. [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: Layers that match the specified parameters. [LmAttObj]
      @note: Returned object has metadata included.  Reference items with 
                "items.item" property
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("scenarioId", scenarioId),
                ("page", page),
                ("perPage", perPage),
                ("public", int(public))
               ]
      url = "%s/services/sdm/layers/" % WEBSITE_ROOT
      items = self.cl.getList(url, parameters=params)
      return items
   
   # .........................................
   def postLayer(self, name, epsgCode, envLayerType, fileName=None, 
                       layerUrl=None, title=None, valUnits=None, 
                       startDate=None, endDate=None, units=None, 
                       resolution=None, keywords=[], description=None):
      """
      @summary: Posts an environmental layer
      @param name: The name of the layer
      @param epsgCode: The EPSG code for the layer
      @param envLayerType: Identifier of the layer type
      @param fileName: (optional) The full path to the local file to be 
                          uploaded.  If a file name is not specified then 
                          layerUrl must be used.
      @param layerUrl: (optional) A URL pointing to the raster file to be
                          uploaded.  If the URL is not specified then fileName
                          must be used.
      @param title: (optional) A longer title of the layer
      @param valUnits: (optional) The units for the values of the raster layer
      @param startDate: (optional) The start date for the layer.  See time
                           formats in module documentation
      @param endDate: (optional) The end date for the layer.  See time formats
                         in module documentation
      @param units: (optional) The cell size units
      @param resolution: (optional) The resolution of the cells
      @param keywords: (optional) A list of keywords to associate with the layer
      @param description: (optional) A longer description of what this layer is
      @raise Exception: Raised if neither layerUrl or filename is provided
      """
      params = [
                ("name", name),
                ("title", title),
                ("valUnits", valUnits),
                ("startDate", startDate),
                ("endDate", endDate),
                ("units", units),
                ("resolution", resolution),
                ("epsgcode", epsgCode),
                ("envLayerType", envLayerType),
                ("description", description)
               ]
      for kw in keywords:
         params.append(("keyword", kw))
         
      if fileName is None:
         body = None
         headers = {}
         if layerUrl is not None:
            params.append(("layerUrl", layerUrl))
         else:
            raise Exception, "Must either specify a file to upload or a url to a file when posting a layer"
      else:
         body = open(fileName).read()
         headers={"Content-Type" : "image/tiff"}
         
      url = "%s/services/sdm/layers" % WEBSITE_ROOT
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=params, 
                                body=body, 
                                headers=headers, 
                                objectify=True).layer
      return obj
      
   # --------------------------------------------------------------------------
   # ===================
   # = Occurrence Sets =
   # ===================
   # .........................................
   def countOccurrenceSets(self, afterTime=None, 
                                 beforeTime=None, displayName=None, 
                                 minimumNumberOfPoints=None, public=False):
      """
      @summary: Counts the number of occurrence sets that match the specified
                   criteria.
      @param afterTime: (optional) Count only occurrence sets modified after 
                           this time.  See time formats in the module 
                           documentation. [integer or string]
      @param beforeTime: (optional) Count only occurrence sets modified before 
                            this time.  See time formats in the module 
                            documentation. [integer or string]
      @param displayName: (optional) Count only occurrence sets that have this 
                             display name. [string]
      @param minimumNumberOfPoints: (optional) Count only occurrence sets that 
                                       have at least this many points. 
                                       [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: The total number of occurrence sets that match the given 
                  criteria. [integer]
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("displayName", displayName),
                ("minimumNumberOfPoints", minimumNumberOfPoints),
                ("public", int(public))
               ]
      url = "%s/services/sdm/occurrences/" % WEBSITE_ROOT
      count = self.cl.getCount(url, params)
      return count
   
   # .........................................
   def getOccurrenceSet(self, occId):
      """
      @summary: Gets a Lifemapper occurrence set
      @param occId: The id of the occurrence set to be returned. [integer]
      @return: A Lifemapper occurrence set. 
      """
      url = "%s/services/sdm/occurrences/%s" % (WEBSITE_ROOT, occId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True).occurrence
      return obj
   
   # .........................................
   def getOccurrenceSetShapefile(self, occId, filename):
      """
      @summary: Gets a Lifemapper occurrence set as a shapefile
      @param occId: The id of the occurrence set to get. [integer]
      @param filename: The name of the file location to save the output. [string]
      """
      url = "%s/services/sdm/occurrences/%s/shapefile" % (WEBSITE_ROOT, occId)
      f = open(filename, 'w')
      f.write(self.cl.makeRequest(url, method="GET"))
      f.close()
   
   # .........................................
   def listOccurrenceSets(self, afterTime=None, beforeTime=None, 
                                perPage=100, page=0, displayName=None, 
                                minimumNumberOfPoints=None, public=False):
      """
      @summary: Lists occurrence sets that meet the specified criteria.
      @param afterTime: (optional) Return only occurrence sets modified after 
                           this time.  See time formats in the module 
                           documentation. [integer or string]
      @param beforeTime: (optional) Return only occurrence sets modified before 
                            this time.  See time formats in the module 
                            documentation. [integer or string]
      @param perPage: (optional) Return this many results per page. [integer]
      @param page: (optional) Return this page of results. [integer]
      @param displayName: (optional) Return only occurrence sets that have this 
                             display name. [string]
      @param minimumNumberOfPoints: (optional) Return only occurrence sets that
                                       have at least this many points. 
                                       [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: Occurrence Sets that match the specified parameters. [LmAttObj]
      @note: Returned object has metadata included.  Reference items with 
                "items.item" property
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("displayName", displayName),
                ("minimumNumberOfPoints", minimumNumberOfPoints),
                ("page", page),
                ("perPage", perPage),
                ("public", int(public))
               ]
      url = "%s/services/sdm/occurrences/" % WEBSITE_ROOT
      items = self.cl.getList(url, parameters=params)
      return items
   
   # .........................................
   def postOccurrenceSet(self, displayName, fileType, fileName, epsgCode=4326):
      """
      @summary: Post a new Lifemapper occurrence set
      @param displayName: The display name for the occurrence set
      @param fileType: The type of the file to upload
      @param fileName: The name of the file to upload
      @param epsgCode: (optional) The EPSG code of the occurrence data
      @return: The occurrence set id number. [integer]
      """
      parameters = [("pointsType", fileType),
                    ("displayName", displayName),
                    ("epsgCode", epsgCode)]

      contentType = "application/xml"
      if fileType.lower() == "csv":
         contentType = "text/csv"
      elif fileType.lower() == "shapefile":
         contentType = "application/x-gzip"
      elif fileType.lower() == "xml":
         contentType = "application/xml"
      else:
         raise Exception, "Unknown file type"
      
      f = open(fileName)
      postBody = ''.join(f.readlines())
      f.close()
      
      url = "%s/services/sdm/occurrences" % WEBSITE_ROOT
      obj = self.cl.makeRequest(url, 
                                method="POST", 
                                parameters=parameters, 
                                body=postBody, 
                                headers={"Content-Type": contentType}, 
                                objectify=True).occurrence
      return obj
      
   
   # --------------------------------------------------------------------------
   # ===============
   # = Projections =
   # ===============
   # .........................................
   def countProjections(self, afterTime=None, beforeTime=None,
                              displayName=None, algorithmCode=None, expId=None, 
                              occurrenceSetId=None, scenarioId=None,
                              status=None, public=False):
      """
      @summary: Counts the number of projections that match the specified
                   criteria.
      @param afterTime: (optional) Count only projections modified after this 
                           time.  See time formats in the module documentation.
                           [integer or string]
      @param beforeTime: (optional) Count only projections modified before this 
                            time.  See time formats in the module 
                            documentation. [integer or string]
      @param displayName: (optional) Count only projections with this display 
                             name
      @param algorithmCode: (optional) Count only projections that have this 
                               algorithm code.  See available algorithms in the 
                               module documentation. [string]
      @param expId: (optional) Count only projections generated from this 
                         experiment. [integer]
      @param occurrenceSetId: (optional) Count only experiments generated from 
                                 this occurrence set. [integer]
      @param status: (optional) Count only projections with this status. More
                        information about status can be found in the module 
                        documentation. [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: The total number of projections that match the given criteria.
                  [integer]
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("displayName", displayName),
                ("algorithmCode", algorithmCode),
                ("modelId", expId),
                ("occurrenceSetId", occurrenceSetId),
                ("scenarioId", scenarioId),
                ("status", status),
                ("public", int(public))
               ]
      url = "%s/services/sdm/projections/" % WEBSITE_ROOT
      count = self.cl.getCount(url, params)
      return count
   
   # .........................................
   def getProjection(self, prjId):
      """
      @summary: Gets a Lifemapper projection
      @param prjId: The id of the projection to be returned. [integer]
      @return: A Lifemapper projection.
      """
      url = "%s/services/sdm/projections/%s" % (WEBSITE_ROOT, prjId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True).projection
      return obj
   
   # .........................................
   def getProjectionTiff(self, prjId, filename):
      """
      @summary: Gets a Lifemapper projection as a tiff file
      @param prjId: The id of the projection to be returned. [integer]
      @param filename: The location to save the resulting file
      @raise Exception: Raised if write fails
      """
      url = "%s/services/sdm/projections/%s/tiff" % (WEBSITE_ROOT, prjId)
      f = open(filename, 'w')
      f.write(self.cl.makeRequest(url, method="GET"))
      f.close()

   # .........................................
   def getProjectionUrl(self, prjId, frmt=""):
      """
      @summary: Gets the url for returning a projection in the desired format
      @param prjId: The projection to return
      @param frmt: (optional) The format to return the projection in
      @return: A url pointing to the desired interface for the projection
      @todo: Check that the url is valid
      """
      url = "%s/services/sdm/projections/%s/%s" % (WEBSITE_ROOT, prjId, frmt)
      return url
   
   # .........................................
   def getProjectionOgcEndpoint(self, prjId):
      """
      @summary: Gets the OGC endpoint for a projection
      @param prjId: The id of the projection to get the endpoint for
      @return: A OGC URL endpoint for the projection
      """
      prj = self.getProjection(prjId)
      return prj.guid
   
   # .........................................
   def listProjections(self, afterTime=None, beforeTime=None, displayName=None,
                             perPage=100, page=0, algorithmCode=None, 
                             expId=None, occurrenceSetId=None, scenarioId=None,
                             status=None, public=False):
      """
      @summary: Lists projections that meet the specified criteria.
      @param afterTime: (optional) Return only projections modified after this 
                           time.  See time formats in the module documentation. 
                           [integer or string]
      @param beforeTime: (optional) Return only projections modified before 
                            this time.  See time formats in the module 
                            documentation. [integer or string]
      @param displayName: (optional) Return only projections with this display
                             name. [string]
      @param perPage: (optional) Return this many results per page. [integer]
      @param page: (optional) Return this page of results. [integer]
      @param algorithmCode: (optional) Return only projections that are 
                               generated from models generated from this 
                               algorithm.  See available algorithms in the 
                               module documentation. [string]
      @param expId: (optional) Return only projections generated from this
                         experiment. [integer]
      @param occurrenceSetId: (optional) Return only projections generated from
                                 models that used this occurrence set. 
                                 [integer]
      @param scenarioId: (optional) Only return projections that use this 
                            scenario [integer]
      @param status: (optional) Return only projections that have this status. 
                        More information about status can be found in the 
                        module documentation. [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: Projections that match the specified parameters. [LmAttObj]
      @note: Returned object has metadata included.  Reference items with 
                "items.item" property
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("displayName", displayName),
                ("algorithmCode", algorithmCode),
                ("modelId", expId),
                ("occurrenceSetId", occurrenceSetId),
                ("scenarioId", scenarioId),
                ("status", status),
                ("page", page),
                ("perPage", perPage),
                ("public", int(public))
               ]
      url = "%s/services/sdm/projections/" % WEBSITE_ROOT
      items = self.cl.getList(url, parameters=params)
      return items
   
   # --------------------------------------------------------------------------
   # =============
   # = Scenarios =
   # =============
   # .........................................
   def countScenarios(self, afterTime=None, beforeTime=None,
                         keyword=[], matchingScenario=None, public=False):
      """
      @summary: Counts the number of scenarios that match the specified 
                   criteria.
      @param afterTime: (optional) Count only scenarios modified after this 
                           time.  See time formats in the module documentation.
                           [integer or string]
      @param beforeTime: (optional) Count only scenarios modified before this 
                            time.  See time formats in the module 
                            documentation. [integer or string]
      @param keyword: (optional) Count only scenarios that have the keywords in 
                         this list. [list of strings]
      @param matchingScenario: (optional) Count only scenarios that match this 
                                  scenario. [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: The number of scenarios that match the supplied criteria.
                  [integer]
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("matchingScenario", matchingScenario),
                ("public", int(public))
               ]
      for kw in keyword:
         params.append(("keyword", kw))
      url = "%s/services/sdm/scenarios/" % WEBSITE_ROOT
      count = self.cl.getCount(url, params)
      return count
   
   # .........................................
   def getScenario(self, scnId):
      """
      @summary: Gets a Lifemapper scenario
      @param scnId: The id of the scenario to be returned. [integer]
      @return: A Lifemapper scenario.
      """
      url = "%s/services/sdm/scenarios/%s" % (WEBSITE_ROOT, scnId)
      obj = self.cl.makeRequest(url, method="GET", objectify=True).scenario
      return obj
   
   # .........................................
   def listScenarios(self, afterTime=None, beforeTime=None, 
                           perPage=100, page=0, keyword=[], 
                           matchingScenario=None, public=False):
      """
      @summary: Lists scenarios that meet the specified criteria.
      @param afterTime: (optional) Return only scenarios modified after this 
                           time.  See time formats in the module documentation. 
                           [integer or string]
      @param beforeTime: (optional) Return only scenarios modified before this 
                            time.  See time formats in the module 
                            documentation. [integer or string]
      @param perPage: (optional) Return this many results per page. [integer]
      @param page: (optional) Return this page of results. [integer]
      @param keyword: (optional) Return only scenarios that have the keywords 
                         in this list. [list of strings]
      @param matchingScenario: (optional) Return only scenarios that match this
                                  scenario. [integer]
      @param public: (optional) If True, use the anonymous client if available
      @return: Scenarios that match the specified parameters. [LmAttObj]
      @note: Returned object has metadata included.  Reference items with 
                "items.item" property
      """
      params = [
                ("afterTime", afterTime),
                ("beforeTime", beforeTime),
                ("matchingScenario", matchingScenario),
                ("page", page),
                ("perPage", perPage),
                ("public", int(public))
               ]
      for kw in keyword:
         params.append(("keyword", kw))
      url = "%s/services/sdm/scenarios/" % WEBSITE_ROOT
      items = self.cl.getList(url, parameters=params)
      return items
   
   # .........................................
   def postScenario(self, layers=[], code=None, title=None, 
                          author=None, description=None, startDate=None, 
                          endDate=None, units=None, resolution=None, 
                          keywords=[], epsgCode=None):
      """
      @summary: Posts a climate scenario to the Lifemapper web services
      @param layers: A list of layer ids that should be included in this 
                        scenario
      @param code: (optional) The code to associate with this layer
      @param title: (optional) A title for this scenario
      @param author: (optional) The author of this scenario
      @param description: (optional) A longer description of what this climate 
                             scenario is and what it contains
      @param startDate: (optional) The start date for this scenario.  See more
                           information about time formats in module 
                           documentation.
      @param endDate: (optional) The end date for this scenario.  See more 
                         information about time formats in module documentation
      @param units: (optional) The units for the cell sizes of the scenario
      @param resolution: (optional) The resolution of the cells
      @param keywords: (optional) A list of keywords to associate with the 
                          scenario
      @param epsgCode: (optional) The EPSG code representing the coordinate 
                          system projection of the scenario
      """
      params = [
                ("code", code),
                ("title", title),
                ("author", author),
                ("description", description),
                ("startDate", startDate),
                ("endDate", endDate),
                ("units", units),
                ("resolution", resolution),
                ("epsgcode", epsgCode)
               ]
      for kw in keywords:
         params.append(("keyword", kw))
         
      for lyr in layers:
         params.append(("layer", lyr))
      url = "%s/services/sdm/scenarios" % WEBSITE_ROOT
      
      obj = self.cl.makeRequest(url, 
                                method="post", 
                                parameters=params,
                                objectify=True).scenario
      return obj
   