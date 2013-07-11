"""
@summary: Example script for using Lifemapper web services and python client
@author: CJ Grady
"""

from lmClientLib import LMClient
import argparse
import random

# command line arguments
parser = argparse.ArgumentParser('Invoke LifeMapper.')
parser.add_argument('username', type=str, nargs=1, help='LifeMapper user name')
parser.add_argument('password', type=str, nargs=1, help='LifeMapper password')
parser.add_argument('units', type=str, nargs=1, help='Units')
parser.add_argument('algorithm', type=str, nargs=1, help='LifeMapper Algorithm')
parser.add_argument('dumpfile', type=str, nargs=1, help='Location to dump experiment ID')
parser.add_argument('layers', type=str, nargs='+', help='TIF Layers')

args = parser.parse_args()

# User name
uname = args.username[0]
# Password
pword = args.password[0]

cl = LMClient(userId=uname, pwd=pword)

randomID = str(random.randint(1, 10000))
layerName = "testLayer" + randomID
layerTitle = "Test Layer " + randomID
epsgCode = 4326
envLyrType = 1

layers = []
for layerURL in args.layers:
	layers.append({
			"name" : layerName,
			"title" : layerTitle,
			"epsgCode" : epsgCode,
			"envLyrType" : envLyrType,
			"url" : layerURL
	})

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
					units=args.units[0])

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

scenCode = "testScenario" + randomID
scenTitle = "Test Scenario " + randomID

scn = cl.sdm.postScenario(
					layers = scenarioLayerIds,
					code = scenCode,
					title = scenTitle,
					epsgCode = epsgCode,
					units = "dd")

print "Posted scenario %s" % scn.id

# Post an experiment from our posted data
algorithm = args.algorithm[0]
mdlScn = scn.id
prjScns = [scn.id]

# Pick an occurrence set
occSetId = cl.sdm.listOccurrenceSets(perPage=1, minimumNumberOfPoints=100, public=True)[0].id

exp = cl.sdm.postExperiment(algorithm, mdlScn, occSetId, prjScns=prjScns)

print "Posted experiment: %s" % exp.id
f = open(args.dumpfile[0], 'w')
f.write(exp.id + "\n")


