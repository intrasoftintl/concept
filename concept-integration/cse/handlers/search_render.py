#!/usr/bin/env python3
# -*- coding: utf-8 -*-
#
# Concept project
__copyright__ = "Copyright Atos ARI 2015"
__author__= "Pablo Salinero, Josemi"

import tornado.web
import tornado.httpclient
import json

import urllib
import logging

from .search import basic_search_query,advanced_search_query

class search_basic_render(tornado.web.RequestHandler):
    """ Render the form for basic render
    """
    def get(self):
        user_id = self.get_argument('user_id',"")
        project_id = self.get_argument('project_id',"")
        self.render('basic_search.html', user = user_id, project = 
        project_id)
    
    def post(self):
        self.get()


class search_advanced_render(tornado.web.RequestHandler):
    """ Render the form for advanced render
    """
    def get(self):
        def getAllValues(es,index,type_):
        
            fields = ["_id"]
            query = { 
                "match_all":{}
                }
            doc = { "fields":fields,"query":query }
        
            res = es.search(index=index, doc_type=type_, 
                                        body=doc, 
                                        size=50)

            list_res = [ j["_id"] for j in res["hits"]["hits"]]

            logging.debug(list_res)   
            
            return list_res
        
        def languageValues(es,index,type_):
            query = { 
                    "size": 0,
                    "aggs" : {
                        "language" : {
                            "terms" : { "field" : "language_name" }
                            }
                    }
                    }

        
            res = es.search(index=index, doc_type=type_, 
                                        body=query, 
                                        size=200)
                                        
                                        

            list_res = [ j["key"]
                for j in res["aggregations"]["language"]["buckets"]]

            logging.debug(list_res)   
            
            return list_res    
        
        user_id = self.get_argument('user_id',"")
        project_id = self.get_argument('project_id',"")
        
        # the list of categories
        es = self.application.es
        index = self.application.config_init["index"]
        category_type = self.application.config_init["type_category"]
        
        
        listCategories = getAllValues(es,index,category_type)
        
        keyword_type = self.application.config_init["type_keyword"]
        
        listKeywords = getAllValues(es,index, keyword_type)
        
        doc_type =  self.application.config_init["type_item"]       
        
        listLanguages = languageValues(es,index,doc_type)
    
        self.render('advanced_search.html', user = user_id,
                                            project = project_id,
                                            listCategories = listCategories,
                                            listKeywords = listKeywords,
                                            listLanguages = listLanguages)



class search_basic_results_render(tornado.web.RequestHandler):
    """ Render the results from simple render
    """
    def post(self):
        user_id = self.get_argument('user_id',"")
        project_id = self.get_argument('project_id',"")
        search_param = self.get_argument('search_param')
        search_param = { "user_id":user_id, "project_id":project_id, 
                            "search_param":search_param}        
        
        if self.get_argument('page',""):
            page = int(self.get_argument("page"))
            new_search = False
        else:
            page = 1
            new_search = True
        
        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]
        
        res = basic_search_query(search_param, es, index,doc_type, page - 1) 



        total = res["hits"]["total"]
        #max_score = res["hits"]["max_score"]
        list_results = res["hits"]["hits"]

        logging.debug(search_param)
        #logging.debug(total)
        #logging.debug(max_score)

        self.render('search_results.html',
                    user = user_id,
                    project = project_id,
                    search_param=search_param,
                    s_type = "basic_search_results",
                    page = page,
                    total = total,
                    list_results = list_results)

        if (new_search
                and self.application.config_init["notification_url"]):
            logging.debug("Sending log")
            client = tornado.httpclient.HTTPClient()
            response = client.fetch(
                self.application.config_init["notification_url"] +
                urllib.urlencode(
                    { 
                        "user_id":user_id,
                        "project_id":project_id,
                        "type":"simple",
                      "search_param": search_param
                      } )
                      )
            logging.debug(response.body) 
        


class search_advanced_results_render(tornado.web.RequestHandler):
    """ Render the results from advanced render
    """
    def post(self):
        user_id = self.get_argument('user_id',"")
        project_id = self.get_argument('project_id',"")
        #the advanced search query words object
        p = {}
        p["user_id"] = user_id
        p["project_id"] = project_id
        p["title"] = self.get_argument("title","") 
        p["origin"] = self.get_argument("origin","")     
        p["description"] = self.get_argument("description","") 
        p["content-text"] = self.get_argument("content-text","") 
        #p["type"] = self.get_argument("title","")
        p["language_name"] = self.get_argument("language_name","")
        p["categories"] = self.get_argument("categories","")
        p["keywords"] = self.get_argument("keywords","")
        p["status"] = self.get_argument("status","")
        p["rating"] = self.get_argument("rating","")
        p["s_type"] = self.get_argument("s_type","")
        if self.get_argument('page',""):
            page = int(self.get_argument("page"))
            new_search = False
        else:
            page = 1
            new_search = True
                
        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]
        
        logging.debug(p)

        res = advanced_search_query(p, es, index,doc_type, page - 1) 

        total = res["hits"]["total"]
        #max_score = res["hits"]["max_score"]
        list_results = res["hits"]["hits"]


        #logging.debug(total)
        #logging.debug(max_score)

        self.render('search_results.html',
                    user = user_id,
                    project = project_id,
                    search_param = json.dumps(p),
                    s_type = "/search_advanced",
                    page = page,
                    total = total,
                    list_results = list_results)
                    
        if (new_search 
                and self.application.config_init["notification_url"]):
            logging.debug("Sending log")
            client = tornado.httpclient.HTTPClient()
            response = client.fetch(
                self.application.config_init["notification_url"] +
                urllib.urlencode(
                    { 
                        "user_id":user_id,
                        "project_id":project_id,
                        "type":"advanced",
                      "search_param": json.dumps(p)
                      } )
                      )
            logging.debug(response.body) 
