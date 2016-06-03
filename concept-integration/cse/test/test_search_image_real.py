#!/usr/bin/env python3

import urllib
import urllib.request
import json
import pprint

url = "http://localhost:9999"


# Example of basic image search


##data = {
##    #'user_id':'robert',
##    'project_id':'project1',
##    'search_param':'project' }
##
##print("Searchin "+str(data))
##data = urllib.parse.urlencode(data)
###GET
##req = urllib.request.Request(url+"/search_basic?"+data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##
##pprint.pprint(r)



# Example of basic search

##query = {
##    'project_id':'project1',
##    'title':'project',
##    "s_type":'exclusive',
##    #'language':'es'
##    }
##data = {
##    #'user_id':'robert',
##    #'project_id':'project1',
##    'search_param':json.dumps(query)
##    }
##
##
###
##print("Searchin "+str(data))
##data = urllib.parse.urlencode(data)
#####GET
##req = urllib.request.Request(url+"/search_advanced?"+data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##
##pprint.pprint(r)
##
##
### Example of category search
##
##data = {
##
##    'search_param':'Modern'
##    }
##
##
###
##print("Searchin category "+str(data))
##data = urllib.parse.urlencode(data)
#####GET
##req = urllib.request.Request(url+"/search_category?"+data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##
##pprint.pprint(r)



data = {
    'project_id':'22',
    'output':"json",
    'id':"034.jpg"
    }


#
print("Searchin keyword "+str(data))
data = urllib.parse.urlencode(data)
###GET
req = urllib.request.Request(url+"/search_image_qbe?"+data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))

pprint.pprint(r)






