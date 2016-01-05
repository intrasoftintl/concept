#!/usr/bin/env python3
#
# Concept project
__copyright__ = "Copyright Atos ARI 2015"
__author__= "Pablo Salinero, Josemi"

import tornado.web
import json
import logging
import datetime


class insertion_item_handler(tornado.web.RequestHandler):

    def post(self):
        def getNameLanguage(index,l_code):
            # unnormalize the language
            if l_code == "":
                return ""
        
            type_ = "language"
            fields = ["description"]
            filter_ = { 
                "term":{"_id":l_code}
                }
            doc = { "fields":fields,"filter":filter_ }
        
            res = es.search(index=index, doc_type=type_, 
                                        body=doc)
            if res["hits"]["total"] == 0:
                logging.error("Error: language code "+l_code+" unknown")
                #the language is unkown
                return ""
                
            if res["hits"]["total"] > 1:
                #this condition never should happen, the language 
                #sended is a code invalidad able to "term" several 
                #diferent codes. I think it is impossible, but just in 
                #case
                logging.error("Error: language code "+l_code+" match "
                        "several codes")
                return ""

            #if we are here, we hace one and only one result
            return res["hits"]["hits"][0]["fields"]["description"][0]
   
        
        
        #Retrieving parameters
        logging.debug("Body:")
        logging.debug(self.request.body)
        try:
            uuid = self.get_argument('uuid',"")            
            url = self.get_argument('url',"")
            project_id = self.get_argument('project_id','')
            user_id = self.get_argument('user_id','')
            version = self.get_argument('version',0)
            last_updated = self.get_argument('last-updated',"20150625")
            content_type = self.get_argument('content-type',"")[:2] #only the first 2 characters
            title = self.get_argument('title',"")
            origin  = self.get_argument('origin',"")
            language = self.get_argument('language',"")
            description = self.get_argument('description',"")
            categories = self.get_argument('categories',"[]")
            keywords = self.get_argument('keywords',"[]")
            status = self.get_argument('status',"")
            domain = self.get_argument('domain',"")
            content_raw = self.get_argument('content-text',"")  
            image_properties = json.loads(self.get_argument('image-properties',"{}"))
            thumbnail_url = self.get_argument('thumbnail-url',"")
            parent_uuid = self.get_argument('parent-uuid',"")
            authors = json.loads(self.get_argument('authors',"[]"))
            uploader = json.loads(self.get_argument('uploaders',"{}"))
            references = json.loads(self.get_argument('references',"[]"))
            ipr_owners = json.loads(self.get_argument('ipr-owners',"[]"))
            license_type = self.get_argument('license-type',"")
            license_url = self.get_argument('license-url',"")
            projects = json.loads(self.get_argument('projects',"[]"))
            clients = json.loads(self.get_argument('clients',"[]"))
            rating = json.loads(self.get_argument('rating',"{}"))
            evaluators = json.loads(self.get_argument('evaluators',"[]"))
            
            #Alias
            uuid = self.get_argument('ID',uuid)
            uuid = self.get_argument('id',uuid)
            project_id = self.get_argument('project ID',project_id)
            project_id = self.get_argument('Project ID',project_id)
            project_id = self.get_argument('PROJECT ID',project_id)
            url = self.get_argument('URL',url)
            content_type = self.get_argument('component',content_type)[:2]
            content_type = self.get_argument('COMPONENT',content_type)[:2]
            title = self.get_argument('TITLE',title)
            categories = self.get_argument('CATEGORIES',categories)
            keywords = self.get_argument('KEYWORDS',keywords)
            content_raw = self.get_argument('CONTENT',content_raw)
            content_raw = self.get_argument('content',content_raw)
            
            
            if categories.startswith("["):
                categories = json.loads(categories)
            else:
                # I asume coma separated
                categories = categories.split(",")
                categories = list(map(str.strip,categories))
                
                
            if keywords.startswith("["):
                keywords = json.loads(keywords)
            else:
                # I asume coma separated
                keywords = keywords.split(",")
                keywords = list(map(str.strip,keywords))                
                
        except Exception as e:
            logging.exception(e)
            self.set_status(406,str(e))
            return

        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]
               
        
        #Creating json
        doc = {
            "uuid" : uuid,
            "url" : url,
            "project_id":project_id,
            "user_id":user_id,
            "version" : version,
            "last-updated" : last_updated,
            "content-type" : content_type,
            "title" : title,
            "origin " : origin,
            "language" : language,
            "description" : description,
            "categories" : categories,
            "keywords" : keywords,
            "status" : status,
            "domain" : domain,
            "content-raw" : content_raw,
            "image-properties" : image_properties,
            "thumbnail-url" : thumbnail_url,
            "parent-uuid" : parent_uuid,
            "authors" : authors,
            "uploader" : uploader,
            "references" : references,
            "ipr-owners" : ipr_owners,
            "license-type" : license_type,
            "license-url" : license_url,
            "projects" : projects,
            "clients" : clients,
            "rating" : rating,
            "evaluators" : evaluators,
            "creation-timestamp" : datetime.datetime.now()
        }
        doc["language_name"] = getNameLanguage(index,language)
        
        if content_type in ["BA","MM","SB"]:
            logging.info("Inserting text for "+ content_type)
            doc["content-text"] = doc["content-raw"]  
            
        if content_type in ["FM"]:
            if doc["content-raw"].startswith("data:image/"):
                logging.info("inserting image")
                logging.info(doc["content-raw"][0:30])
                logging.info(doc["content-raw"].partition(",")[0])
                doc["content-image"] = doc["content-raw"].partition(",")[2]
                doc["image-properties"] = {"format":"jpg"}
            else:
                logging.info(doc["content-raw"][0:30])
            
        
        #logging.info(doc)
        #Indexing
        try:
            res = es.index(index=index, doc_type=doc_type, id=uuid, body=doc)
            logging.info(res['created'])
        except Exception as e:
            logging.exception(e)
            self.set_status(400,str(e))
            return

        self.write('{"status":"ok"}')
        self.set_status(201,"Item inserted")
        
        return

    def get(self):
        #the preferred method is post
        self.post()
        
class insertion_category_handler(tornado.web.RequestHandler):

    def post(self):
        #Retrieving parameters
        try:
            name = self.get_argument('name')
            linkedData = self.get_argument('linkedData')
            category_hierarchy = json.loads(self.get_argument('category-hierarchy'))
        except Exception as e:
            self.set_status(406,str(e))
            return

        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_category"]
        
        #Creating json
        doc = {
            "name" : name,
            "linkedData" : linkedData,
            "category-hierarchy" : category_hierarchy
        }
        
        #Indexing
        try:
            res = es.index(index=index, doc_type=doc_type, id=name, body=doc)
            logging.info(res['created'])
        except Exception as e:
            logging.exception(e)
            self.set_status(400,str(e))
            return

        self.write('{"status":"ok"}')
        self.set_status(201,"Category inserted")
        
        return


class insertion_keyword_handler(tornado.web.RequestHandler):


    def post(self):
        #Retrieving parameters
        try:
            name = self.get_argument('name')
            linkedData = self.get_argument('linkedData')
            relevancy = self.get_argument('relevancy')
            enriched_tags = json.loads(self.get_argument('enrichedTags'))
        except Exception as e:
            logging.exception(e)
            self.set_status(406,str(e))
            return

        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_keyword"]
        #Creating json
        doc = {
            "name" : name,
            "linkedData" : linkedData,
            "relevancy" : relevancy,
            "enrichedTags" : enriched_tags
        }
        
        #Indexing
        try:
            res = es.index(index=index, doc_type=doc_type, id=name, body=doc)
            logging.info(res['created'])
        except Exception as e:
            self.set_status(400,str(e))
            return


        self.write('{"status":"ok"}')
        self.set_status(201,"Keyword inserted")
        
        return

