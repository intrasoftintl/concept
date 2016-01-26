#!/usr/bin/env python3
# -*- coding: utf-8 -*-
#
# Concept project
__copyright__ = "Copyright Atos ARI 2015"
__author__= "Pablo Salinero, Josemi"

import tornado.web
import logging

from constants import english_html_analyzer,docs_mapping

class deletion_item_handler(tornado.web.RequestHandler):

    def get(self):
        #Retrieving parameters
        try:
            uuid = self.get_argument('uuid',"")
            uuid = self.get_argument('id',uuid)
        except Exception as e:
            self.set_status(406,str(e))
            return

        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]  

        #Deleting
        try:
            res = es.delete(index=index, doc_type=doc_type, id=uuid)
        except Exception as e:
            logging.exception(e)
            self.set_status(400,str(e))
            return
        
        self.set_status(200,"Item deleted")
        
        return

    def post(self):
        self.get()
        
    def delete(self):
        self.get()

class deletion_category_handler(tornado.web.RequestHandler):

    def post(self):
        #Retrieving parameters
        try:
            name = self.get_argument('name')
        except Exception as e:
            self.set_status(406,str(e))
            return

        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_category"]

        #Deleting
        try:
            res = es.delete(index=index, doc_type = doc_type, id=name)
        except Exception as e:
            logging.exception(e)
            self.set_status(400,str(e))
            return
        
        self.set_status(200,"Category deleted")
        
        return

class deletion_keyword_handler(tornado.web.RequestHandler):
    
    def post(self):
        #Retrieving parameters
        try:
            name = self.get_argument('name')
        except Exception as e:
            self.set_status(406,str(e))
            return

        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_keyword"]
        
        #Deleting
        try:
            res = es.delete(index=index, doc_type=doc_type, id=name)
        except Exception as e:
            logging.exception(e)
            self.set_status(400,str(e))
            return
        
        self.set_status(200,"Keyword deleted")
        
        return

class delete_all_handler(tornado.web.RequestHandler):

    def get(self):
        logging.info("RESET DB RESET DB RESET DB")
        
        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]    
    
        reset_db(es,index,doc_type)


        self.set_status(200,"All deleted")

    def post(self):
        self.get()
        
    def delete(self):
        self.get()



def reset_db(es,index, doc_type):
    if es.indices.exists(index):
        es.indices.delete(index)
    else:
        logging.info("The index doesn't exists")
    
    logging.info("Creating DB")
    
    es.indices.create(index = index, body = english_html_analyzer)
    es.cluster.health(wait_for_status = "yellow")

    
    
#    es.indices.put_mapping(index = index, doc_type = type_category,
#                           body =  categories_mapping
#    )

    es.indices.put_mapping(index = index,doc_type = doc_type,
                           body = docs_mapping
    )                

#    es.indices.put_mapping(index = index,doc_type = type_keyword,
#                           body = keywords_mapping
#    )



class deletion_categories_handler(tornado.web.RequestHandler):

    def get(self):
        #Retrieving parameters
        try:
            project_id = self.get_argument('project_id',"")
        except Exception as e:
            self.set_status(406,str(e))
            return

        es = self.application.es
        index = self.application.config_init["index"]

        #Deleting
        try:
            res = es.delete(index=index, doc_type="categories", id=project_id)
        except Exception as e:
            logging.exception(e)
            self.set_status(400,str(e))
            return
        
        self.set_status(200,"Item deleted")
        
        return

    def post(self):
        self.get()
        
    def delete(self):
        self.get()

