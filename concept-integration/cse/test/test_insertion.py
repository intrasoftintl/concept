#!/usr/bin/env python3

import urllib
import urllib.request
import json

url = "http://localhost:9999"

#deleting all
req = urllib.request.Request(url+"/delete_all")


data = {
"name":'Modern',
"linkedData":'http://dbpedia.org/page/Modern_history',
"category-hierarchy":('{"level-1": "Product language",'
                    '"level-2": "Style",'
                    '"level-3": "Period style" }')
}

print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_category",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)

data = {
"name":'Middle Age',
"linkedData":'http://dbpedia.org/page/Middle_age',
"category-hierarchy":('{"level-1": "Product language",'
                    '"level-2": "Style",'
                    '"level-3": "Period style" }')
}

print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_category",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)

data = {
"name":'Cubism',
"linkedData":'http://dbpedia.org/page/Modern_history',
"category-hierarchy":('{"level-1": "Product language",'
                    '"level-2": "Style",'
                    '"level-3": "Period style" }')
}

print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_category",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)

data = {
"name":'Aggressive',
"linkedData":'http://seco.tkk.fi/onto/iconclass/iconclass#not_94N4',
"category-hierarchy":('{"level-1": "Associations and Feelings",'
                    '"level-2": "Product language" }')
}

print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_category",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)


data = {
"name":'Intellectual giftedness',
"linkedData":'http://dbpedia.org/page/Intellectual_giftedness',
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
"name":'Español',
"linkedData":'http://dbpedia.org/page/Spanish',
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
"name":'Keyword3',
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
"project_id":"project1",
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

data = {
"project_id":"project2",
"uuid":'d5d59649-1911-46b1-a353-4206152376be',
"categories":'["Modern","Cubism"]',
"origin":'web',
"content-type":'moodboard',
"title":'Project test 2',
"keywords":'["Intellectual giftedness"]',
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
"content-text":('Main idea secondary idea Located at 807 Mission Road, Concepción was designated a National '
'Historic Landmark on April 15, 1970 '
'and is part of San Antonio Missions National Historical Park.[3] Restoration of the mission',
'interior was completed in March 2010 after six months of work. Catholic Mass is still held every Sunday.'),
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5a5c649-1998-46b1-a353-4206159376be',
"categories":'["Modern","Aggressive"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project new project',
"keywords":'["Intellectual giftedness", "Another Keyword","Keyword3"]',
"rating":'{"rating": 1.0, "voters": 100 }',
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
"description":('In 2015, Concepción and four other San Antonio missions, including The Alamo, were designated '
'a World Heritage Site by the United Nations Educational, Scientific, and Cultural Organization,'
' the first in Texas and one of twenty-three such establishments in the United States.[4]'),
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-1998-46b1-a353-420ef59376be',
"categories":'["Modern","Middle Age"]',
"origin":'concept',
"content-type":'moodboard',
"title":'Project test 2',
"keywords":'["Intellectual giftedness"]',
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
"status":'Final',
"url":'http://concept/mindmap_1',
"content-text":("Espada was a successful mission because of water wasn't scarce. Originally put "
"in place by Spain as a picket intended to delineate a northern frontier, most of the missions "
"were only temporarily successful as community centers. The Alamo, by the time of the famous "
"battle in 1836, had already been abandoned for three "
"generations because of the fear of attacks by natives and the expulsion of the Jesuits from Spanish "
"possessions. The acequia not only conducted potable water and irrigation, but also powered a mill."),
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-6798-46b1-a353-4206159376be',
"categories":'["Modern","Cubism"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test old',
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
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-1998-46b1-a353-4206154576be',
"categories":'["Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test español',
"keywords":'["Intellectual giftedness", "Español"]',
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
"language":'es',
"domain":'Free word',
"status":'draft',
"url":'http://concept/mindmap_1',
"content-text":('Para mí resultó una gran decepción; estaba enfadado '
'y lo sabía desde hace unas cuantas carreras, pero no esperaba que hiciera algo tan obvio. '
'Me siento decepcionado y preocupado, porque seguramente intente hacerlo de nuevo aquí y en Valencia'),
"description":'Un proyecto en lengua española',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'add59649-1998-46b1-a353-4206159376be',
"categories":'["Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test Espada',
"keywords":'["Intellectual giftedness", "Another Keyword"]',
"rating":'{"rating": 2.0, "voters": 100 }',
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
"content-text":('Mission Espada\'s acequia (irrigation) system '
'can still be seen today. The main ditch, or acequia madre, continues to carry water to the mission '
'and its former farmlands. This water is still used by residents living on these neighboring lands.'),
"description":'Mission Espada',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-1998-46b1-a353-4206150076be',
"categories":'["Modern"]',
"origin":'Web',
"content-type":'mindmap',
"title":'Project test 33',
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
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-1998-46b1-a353-4206112376be',
"categories":'["Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test 45',
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
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d596aa-1998-46b1-a353-4206159ac6be',
"categories":'["Modern","Aggressive"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test 2',
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
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-1998-46b1-a353-4206ef9376be',
"categories":'["Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test 67',
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
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-1998-46b1-a353-4245159376be',
"categories":'["Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test Espada 2',
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
"description":('Mission Espada has survived from its beginnings to the present day as a community center '
'that still supports a Catholic parish and religious education, however a school '
'originally opened by the Sisters of the Incarnate Word and Blessed Sacrament was closed in 1967.'),
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-1998-46b1-a353-4206190376be',
"categories":'["Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test Mission',
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
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-19ad-46b1-a353-4206159376be',
"categories":'["Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test 23',
"keywords":'["Intellectual giftedness", "Another Keyword"]',
"rating":'{"rating": 4.0, "voters": 100 }',
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
"description":'Another description',
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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

data = {
"uuid":'d5d59649-ad98-46b1-a353-4206159376be',
"categories":'["Modern"]',
"origin":'concept',
"content-type":'mindmap',
"title":'Project test español',
"keywords":'["Intellectual giftedness", "Español"]',
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
"language":'es',
"domain":'Free word',
"status":'draft',
"url":'http://concept/mindmap_1',
"content-text":'Según la disposición de las hojas, se clasifica así',
"description":('Un rotafolio, o papelógrafo, es un instrumento usado para la presentación de información '
'en hojas grandes de papel, típicamente del formato A1. Consiste normalmente de un pizarrón blanco '
'montado en un caballete, y '
'sobre el cual se fija un bloc de papel, sujeto al caballete/pizarrón con argollas, cintas o tachuelas.'),
"clients":('[{  "id": "628fa28a-43aa-44ec-8278-eb04cc24f1b6","name": "Graphical Systems Inc.",'
    '"contact-list": "Name1 Surname1 Name2 Surname2" },{"id": "628fa28a-43aa-44ec-8278-eb09cc24f1b6",'
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


