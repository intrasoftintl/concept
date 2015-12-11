#! /usr/bin/env python3
# image search using elastic search 

import base64
import elasticsearch

es = elasticsearch.Elasticsearch(port=9201)

data = open("KZI9W.jpg","rb").read()
# as file is binary, data is bytes

print ("Encoding...")
print(data[:300])
encoded_data = base64.b64encode(data)
print ("Encoded")
print(encoded_data)


##querySelf = {
##    "query": {
##        "image": {
##            "my_img": {
##                "feature": "CEDD",
##                "id": "image1",
##                "path": "my_image",
##                "hash": "BIT_SAMPLING"
##            }
##        }
##    }
##}
##
query = {
    "query": {
        "image": {
            "my_img": {
                "feature": "CEDD",
                "image": encoded_data,
                "hash": "BIT_SAMPLING",
                "boost": 2.1,
                "limit": 10
            }
        }
    }
}


result = es.search(index="test",doc_type="test",body=query)

print(result['hits']['hits'][0])
