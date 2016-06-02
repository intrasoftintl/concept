#! /usr/bin/env python3
# image search using elastic search 

import base64
import elasticsearch

es = elasticsearch.Elasticsearch(port=9200)


es.indices.delete("test")

es.indices.create("test")
es.cluster.health(wait_for_status = "yellow")

image_mapping = {
    "test": {
        "properties": {
            "my_img": {
                "type": "image",
                "feature": {
                    "CEDD": {
                        "hash": "BIT_SAMPLING"
                    },
                    "JCD": {
                        "hash": ["BIT_SAMPLING", "LSH"]
                    },
                    "FCTH": {}
                },
                "metadata": {
                    "jpeg.image_width": {
                        "type": "string",
                        "store": "yes"
                    },
                    "jpeg.image_height": {
                        "type": "string",
                        "store": "yes"
                    }
                }
            }
        }
    }
}



print (image_mapping)

es.indices.put_mapping(index="test",doc_type="test",body=image_mapping)

file_list = ["034.jpg",
             "035.jpg",
             "2013-10-17 12.21.50.jpg",
             "2013-10-17 15.03.33.jpg",
             "2014-12-05 09.12.24.jpg",
             "2015-04-24 10.11.19.jpg",
             "circulo blanco.png",
             "Copy of IMG_1294.JPG",
             "Copy of IMG_1347.JPG",
             "Copy of IMG_1373.JPG",
             "KZI9W.jpg",
             "viento1.jpg",
             "viento2.jpg"]

for f in file_list:
    print (f)
    data = open(f,"rb").read()
    # as file is binary, data is bytes

    print ("Encoding...")
    #print(data[:300])
    encoded_data = base64.b64encode(data)
    print ("Encoded")
    #print(encoded_data)

    image = { "my_img":encoded_data }

    res = es.index(index="test",doc_type="test",body=image,id=f)
    print(res['created'])

    print("Indexed")
