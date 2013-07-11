"""
@summary: Example script for using Lifemapper web services and python client
@author: CJ Grady
"""

from lmClientLib import LMClient

# User name
uname = "elseweb"
# Password
pword = "elseweb1"
cl = LMClient(userId=uname, pwd=pword)

layers = [
          {
           "name" : "testLayer8010",
           "title" : "Test layer 8010",
           "epsgCode" : 4326,
           "envLyrType" : 1,
           "url" : "http://lifemapper.org/services/sdm/layers/266/tiff"
          },
          {
           "name" : "testLayer8011",
           "title" : "Test layer 8011",
           "epsgCode" : 4326,
           "envLyrType" : 2,
           "url" : "http://lifemapper.org/services/sdm/layers/267/tiff"
          },
          {
           "name" : "testLayer8012",
           "title" : "Test layer 8012",
           "epsgCode" : 4326,
           "envLyrType" : 3,
           "url" : "http://lifemapper.org/services/sdm/layers/268/tiff"
          }]

# Note: At this time, the combination (user, layer name, EPSG code) must be 
#          unique.  If try posting layers with a name that already exists, it
#          will fail.
for layer in layers:
   # Attempt to post
   try:
      lyr = cl.sdm.postLayer(
					layer["name"], 
					layer["epsgCode"],
					layer["envLyrType"],
					layerUrl=layer["url"], 
					title=layer["title"],
					units="dd")

      print "Posted layer %s" % lyr.id
   except Exception, e:
      print str(e)
      print "Most likely the layer failed to post because a layer with that name already exists for this user"
      print "Try changing the name from %s to something else" % layer["name"]


# Get the first 10 layers (max) for the user and build a scenario out of them
userLayers = cl.sdm.listLayers(perPage=10)

# userLayers are small object, pull out the ids for posting a scenario

scenarioLayerIds = [lyr.id for lyr in userLayers] # Gives us integer ids for each layer

# Note that scenario code must be unique for a user
scn = cl.sdm.postScenario(
					layers=scenarioLayerIds,
					code="testScenario8010",
					title="Test scenario 8010",
					epsgCode=4326,
					units="dd")

print "Posted scenario %s" % scn.id


# Post an experiment from our posted data
algorithm = "BIOCLIM"
mdlScn = scn.id
prjScns = [scn.id]

# Pick an occurrence set
occSetId = cl.sdm.listOccurrenceSets(perPage=1, minimumNumberOfPoints=100, public=True)[0].id


exp = cl.sdm.postExperiment(algorithm, mdlScn, occSetId, prjScns=prjScns)

expResponse = cl.sdm.getExperiment(558513)

dir(expResponse)

print "Posted experiment: %s" % exp.id
