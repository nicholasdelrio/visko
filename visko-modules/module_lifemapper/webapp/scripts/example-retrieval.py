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

exp = cl.sdm.getExperiment(559920)

print "Reponse structure: %s" % dir(exp)

print "Response algorithm: %s" % exp.algorithm

dir(exp)

print "Posted experiment: %s" % exp.id
