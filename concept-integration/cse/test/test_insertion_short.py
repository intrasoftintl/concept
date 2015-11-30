#!/usr/bin/env python3

import urllib
import urllib.request
import json

url = "http://medialab4.atosresearch.eu:9999"

#deleting all
#req = urllib.request.Request(url+"/delete_all")


data = {
"name":'Modern',
"linkedData":'http://dbpedia.org/page/Modern_history',
"category-hierarchy":('{"level-1": "Product language",'
                    '"level-2": "Style",'
                    '"level-3": "Period style" }')
}
# this python dictionary is like a list of parameters, actually
# so urlencode creates a body for the POST where name, linked data and
# category-hierarchy are independent parameters
# so the body contains 3 parameters, not just 1


print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_category",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)

data = {
"name":'Another Keyword',
"linkedData":'http://dbpedia.org/page/Another Keyword',
"relevancy":0.386072,
"enrichedTags":('[{ "name": "School terminology","linkedData": "http://dbpedia.org/page/Category:School_terminology",'
              '"weight": 0.33 },'
              ' {"name": "Gifted education","linkedData": "http://dbpedia.org/page/Gifted_education", 	"weight": 1.0 },'
              ' {"name": "Giftedness", 	"weight": 0.66 }]')
}

print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_keyword",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)


data = {
"uuid":'d792bbff-236e-419a-b954-a2b86cffe433',
"categories":'[ "Middle Age","Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test 1',
"keywords":'["Intellectual giftedness", "Another Keyword"]',
"rating":'{"rating": 5.0, "voters": 100 }',
"projects":('[{ "id" : "30234a3e-4973-41a6-a35e-ba1feaa4375e","url" : "http://concept/project_1",'
          '"title" : "Project 1",'
          '"authors" : [{"person-id" : "327c7ea0-313d-48f5-b8f8-daa8803ec9a4",'
          ' "person-name" : "Name1 Surname1"},'
          '{"person-id" : "327c7ea0-313d-48f5-b8f8-daa8803ec3f7", "person-name" : "Name2 Surname2"}] },'
          '{"id" : "30234a3e-4973-41a6-a35e-ba1feaa4375e","url" : "http://concept/project_1",'
          '"title" : "Project 1",'
          '"authors" : [{"person-id" : "327c7ea0-313d-48f5-b8f8-daa8803ec9a4", "person-name" : "Name1 Surname1"},'
          '{"person-id" : "327c7ea0-313d-48f5-b8f8-daa8803ec3f7", "person-name" : "Name2 Surname2"}] }]'),
"language":'en',
"domain":'Free word',
"status":'draft',
"url":'http://concept/mindmap_1',
"content-text":'Main idea secondary idea',
"description":'This is the description of the item. There is no limit for the text size',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8275-eb04cc24f1b6",'
    '"name": "Graphical Systems Inc.2", "contact-list": "Name1 Surname1 Name2 Surname2" }]'),
"authors":('[{ "person-id": "00000000-0000-0000-0000-000000000001","person-name": "Name1 Surname1" }'
    ',{ "person-id": "00000000-0000-0000-0000-000000000002","person-name": "Name2 Surname2" }]'),
"references":('[{"id": "d3588eb2-2783-4719-b231-2379fb9b9896",'
            '"url": "https://concept.eu/mindmap?id=d3588eb2-2783-4719-b231-2379fb9b9896",'
            ' "title": "My first item","content-type": "mindmap",'
            '"authors": [{"person-id":"21745378-5076-4298-853d-85c940b7cffc","person-name": "Name3 Surname3"},'
            '{"person-id": "21745378-5076-4298-853d-85c940b7cffc","person-name": "Name4 Surname4"}] },'
            ' { "id": "d3588eb2-2783-4719-b231-2379fb9b9000",'
            '"url": "https://concept.eu/mindmap?id=d3588eb2-2783-4719-b231-2379fb9b9896",'
            '"title": "My first item","content-type": "mindmap",'
            '"authors": [{"person-id": "21745378-5076-4298-853d-85c940b7cffc","person-name": "Name3 Surname3"},'
            '{"person-id": "21745378-5076-4298-853d-85c940b7cffc","person-name": "Name4 Surname4"}]}]')
}

print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_item",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)




