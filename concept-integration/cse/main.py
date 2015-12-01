#! /usr/bin/env python3
#
# Concept project
__copyright__ = "Copyright Atos ARI 2015"
__author__= "Pablo Salinero, Josemi"

import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
import tornado.httpclient

import elasticsearch
import elasticsearch.helpers

import logging
import os

from configobj import ConfigObj

from handlers import *
from constants import *


from tornado.options import define, options
define("port", default=9999, help="run on the given port", type=int)

class Application(tornado.web.Application):
    
    def __init__(self, es, config_init):
        settings = {'debug': config_init["debug"],
                    'static_path': os.path.join(os.path.dirname(__file__), 'static'),
                    "template_path": os.path.join(os.path.dirname(__file__), "templates"),
        }
        handlers = [
            (r"/insert_item", insertion.insertion_item_handler),
            (r"/delete_item", deletion.deletion_item_handler),
            
            (r"/insert_category", insertion.insertion_category_handler),
            (r"/delete_category", deletion.deletion_category_handler),
            
            (r"/insert_keyword", insertion.insertion_keyword_handler),
            (r"/delete_keyword", deletion.deletion_keyword_handler),
            
            (r"/search_basic", search.search_basic_handler),
            (r"/search_advanced", search.search_advanced_handler),
            
            (r"/basic_search", search.search_basic_render),
            (r"/basic_search_results", search.search_basic_results_render),
                    
            (r"/advanced_search", search.search_advanced_render),
            (r"/advanced_search_results", search.search_advanced_results_render),
            
            (r"/delete_all",deletion.delete_all_handler),

            (r"/search_category", search.search_category_handler),
            (r"/search_keyword", search.search_keyword_handler),

        ]

        self.es = es
        self.config_init = config_init

        tornado.web.Application.__init__(self, handlers, **settings)


def init_db(es, config_init):
    index = config_init["index"]
    type_item = config_init["type_item"]
    type_category = config_init["type_category"]
    type_keyword = config_init["type_keyword"]

    #delete everything each time that models change
    #es.indices.delete(index)
  
    if es.indices.exists(index):
        logging.info("DB already exists")
        return

    logging.info("Creating DB")
    es.indices.create(index)
    es.cluster.health(wait_for_status = "yellow")

    
    es.indices.put_mapping(index = index, doc_type = type_category,
                           body =  categories_mapping
    )

    es.indices.put_mapping(index = index,doc_type = type_item,
                           body = docs_mapping
    )                

    es.indices.put_mapping(index = index,doc_type = type_keyword,
                           body = keywords_mapping
    )

    #the languages
    actions = []
    for i in range(0,len(languages_list),2):
        action = languages_list[i]["index"]
        action["_source"] = languages_list[i+1]
        #logging.debug(action)
        actions.append(action)
    
    elasticsearch.helpers.bulk(es,actions)



logging.basicConfig(format='%(asctime)s-> %(message)s')

if __name__ == "__main__":
    logging.info("Starting...")

    config_init = ConfigObj('config.cfg')

    tornado.options.parse_command_line()

    #change it from command line
    #logging.basicConfig(level=logging.DEBUG,format='%(asctime)s-> %(message)s')

    es = elasticsearch.Elasticsearch()

    init_db(es,config_init)

    app = Application(es, config_init)

    http_server = tornado.httpserver.HTTPServer(app)
    
    http_server.listen(options.port)
         
    tornado.ioloop.IOLoop.instance().start()
