#!/usr/bin/env python3
#
# Concept project
__copyright__ = "Copyright Atos ARI 2015"
__author__= "Pablo Salinero, Josemi"

import tornado.web
import logging


class deletion_item_handler(tornado.web.RequestHandler):

    def get(self):
        #Retrieving parameters
        try:
            uuid = self.get_argument('uuid')
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
        print("test post")

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

    def put(self):
        logging.info("Reset_db")
        
        es = self.application.es
        index = self.application.config_init["index"]
        #doc_type = self.application.config_init["type_category"]    
    
        if es.indices.exists(index):
            es.indices.delete(index)
        else:
            logging.info("The index doesn't exists")


        self.set_status(200,"All deleted")
