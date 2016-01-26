#!/usr/bin/env python3

import urllib
import urllib.request
import json

#url = "http://medialab4.atosresearch.eu:9999"
url = "http://localhost:9999"


data = {
"project_id":'22',
"categories": """
[{
 	"label": "Product Category",
 	"id": 1,
 	"children": [{
 		"label": "Kitchenware",
 		"id": 11
 	}, {
 		"label": "Exhibition",
 		"id": 12
 	}, {
 		"label": "Lighting",
 		"id": 13
 	}, {
 		"label": "Furniture",
 		"id": 14
 	}]
 }, {
 	"label": "Product Domain",
 	"id": 2,
 	"children": [{
 		"label": "Medical",
 		"id": 21
 	}, {
 		"label": "Cosumer",
 		"id": 22
 	}, {
 		"label": "Sport",
 		"id": 23
 	}, {
 		"label": "Market Analysis",
 		"id": 24
 	}, {
 		"label": "Technology",
 		"id": 25
 	}, {
 		"label": "Usability",
 		"id": 26
 	}]
 }, {
 	"label": "Product Language",
 	"id": 3,
 	"children": [{
 		"label": "Style",
 		"id": 31,
 		"children": [{
 			"label": "Period Style",
 			"id": 311,
 			"children": [{
 				"label": "Classic",
 				"id": 3112
 			}, {
 				"label": "Chic",
 				"id": 3113
 			}, {
 				"label": "Modern",
 				"id": 3113
 			}, {
 				"label": "Artdeco",
 				"id": 3115
 			}]
 		}, {
 			"label": "Partial Style",
 			"id": 312,
 			"children": [{
 				"label": "National",
 				"id": 3121
 			}, {
 				"label": "Corporate",
 				"id": 3122
 			}, {
 				"label": "Target Style",
 				"id": 3123
 			}]
 		}]
 	}, {
 		"label": "Material",
 		"id": 32,
 		"children": [{
 			"label": "Steel",
 			"id": 321
 		}, {
 			"label": "Stone",
 			"id": 322
 		}]
 	}, {
 		"label": "Associations and Feelings",
 		"id": 33,
 		"children": [{
 			"label": "Cold",
 			"id": 331
 		}, {
 			"label": "Warm",
 			"id": 332
 		}, {
 			"label": "Aggressive",
 			"id": 333
 		}]
 	}]
 }]
"""
}



print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_categories",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)

