#! /usr/bin/env python3
# image search using elastic search 

import base64
import elasticsearch

es = elasticsearch.Elasticsearch(port=9200)

##file_to_search = "KZI9W.jpg"
##
##print("Searching "+file_to_search)
##
##data = open(file_to_search,"rb").read()
# as file is binary, data is bytes

##print ("Encoding...")
##print(data[:100])
##encoded_data = base64.b64encode(data)
##print ("Encoded")
#print(encoded_data)


##query = {
##    "query": {
##        "image": {
##            "my_img": {
##                "feature": "CEDD",
##                "image": encoded_data,
##                "hash": "BIT_SAMPLING",
##                "boost": 2.1,
##                "limit": 10
##            }
##        }
##    }
##}
##
##
##result = es.search(index="test",doc_type="test",body=query)
##
##for i in result['hits']['hits']:
##    print("{} {}".format(i["_id"],i["_score"]))

query = {
    "query": {
        "image": {
            "my_img": {
                "feature": "CEDD",
                "index": "test",
                "type": "test",
                "id": "035.jpg",
                "path": "my_img",
                "hash": "BIT_SAMPLING"
            }
        }
    }
}

result = es.search(index="test",doc_type="test",body=query)

for i in result['hits']['hits']:
    print("{} {}".format(i["_id"],i["_score"]))
    

