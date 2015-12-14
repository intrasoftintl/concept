#!/usr/bin/env python3
#
# Concept project
__copyright__ = "Copyright Atos ARI 2015"
__author__= "Pablo Salinero, Josemi"

import tornado.web
import tornado.httpclient
import json

import urllib
import logging

from constants import boost_list
    
def basic_search_query(param,es,index,doc_type, dpage = 0):
    """Create a basic query for elastic 
    from the words in param,
    and returns the results as elastic
    Low level function.
    """
    page = param.pop("page",dpage)
                
    #Building the query
    if not param["project_id"]:
        doc = {
        "query": {
            "multi_match": {
                "query": param["search_param"],
                "lenient": "true",
                "fields": ["uuid",
                           "title^3",
                           "description",
                           "image-properties.height",
                           "image-properties.format",
                           "image-properties.width"]
            }
       }
       }
    else:
        doc = {
            "query": {
            "filtered":
            {
                "filter": { "term":{ "project_id":param["project_id"] } },
                "query": {
                    "multi_match": {
                        "query": param["search_param"],
                        "lenient": "true",
                        "fields": ["uuid",
                           "title^3",
                           "description",
                           "image-properties.height",
                           "image-properties.format",
                           "image-properties.width"]
                    }
                }
            }
            }
       }        
    logging.debug(doc)
    
    #Executing the query
    res = es.search(index=index, doc_type=doc_type, body=doc,
                    from_=page * 10, size = 10)
    #print(res)

    
    #print("basic_search_query:hits -> " + str(res['hits']['hits']))
    
    return res
    
    
def advanced_search_query(param_list,es,index,doc_type, dpage = 0):
    """Create the advanced query for elastic 
    from the params list,
    and returns the results as elastic.
    Low level function.
    """    
    page = param_list.pop("page",dpage)  
    
    #Building the query
    s_type = param_list.pop("s_type","inclusive")
    if s_type == "inclusive":
        action = "should"
    else:
        action = "must"
    
    matchList = []   
    filterList = []
    
    filtered = False    
    
    user_id = param_list.pop("user_id","")
    if user_id:
        filtered = True
        filter_ = { "term":{ "user_id":user_id } }
        filterList.append(filter_)
        
    project_id = param_list.pop("project_id","")
    if project_id:
        filtered = True
        filter_ = { "term":{ "project_id":project_id } }
        filterList.append(filter_)  
        
    content_type= param_list.pop("content-type","")
    if content_type:
        filtered = True
        filter_ = { "term":{ "content-type":content_type } }
        filterList.append(filter_)  

    uuid = param_list.pop("uuid","")
    if uuid:
        filtered = True
        filter_ = { "term":{ "uuid":uuid } }
        filterList.append(filter_) 

    
    rating = param_list.pop("rating","")
    if rating:
        logging.debug(rating)
        match = { "range":{"rating.rating":{"gte":rating}}}
        matchList.append(match)

        
    for k,v in param_list.items():
        if v:
            match = { "match":{k:v} }
            match = { "match":
                            {k:
                                {"query":v,
                                 "boost":boost_list.get(k,1)
                                }
                            }
                    }
            matchList.append(match)
    
    if(not filtered):
        doc = { "query":
            {
                "bool":
                    {
                    action:matchList
                    }
            }
        }        
    elif (not matchList):
        doc = { "query": 
                {
                "filtered": {
                    "filter": {
                        "bool": {
                            "must":filterList
                        }
                    }
                }
                }
                }        
    else:
        doc = { "query": 
                {
                "filtered": {
                    "filter": {
                        "bool": {
                            "must":filterList
                        }
                    },
                    "query": {
                        "bool": {
                            action:matchList
                        }
                    }
                }
                }
                }
    
                
    logging.info(json.dumps(doc,indent=4))  
    
    #Executing the query
    res = es.search(index=index, doc_type=doc_type, body=doc,
                    from_=page * 10, size = 10)
    #print(res)

    
    #print("basic_search_query:hits -> " + str(res['hits']['hits']))
    
    return res
    
    


        
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

#
#  API Rest
#
#
#



#
#
#  CATEGORIES
#
#                    

def category_search_query(param,es,index,doc_type, dpage = 0):
    """ Return the full category from the category name 
    Low level function.
    """    
    #page = param.pop("page",dpage)
                
    #Building the query
    if not param["project_id"]:
        doc = {
            "query": {
            "filtered":
            {
                "filter": { "term":{ "name":param["search_param"] } } 
                }
            }
            }
    else:
        doc = {
            "query": {
            "filtered":
            {
                "filter": [{ "term":{ "project_id":param["project_id"] } },
                           { "term":{ "name":param["search_param"] } } ]
                }
            }
            }
    logging.debug(doc)
    
    #Executing the query
    res = es.search(index=index, doc_type=doc_type, body=doc)
    #print(res)

    
    #print("basic_search_query:hits -> " + str(res['hits']['hits']))
    
    return res

                    
class search_category_handler(tornado.web.RequestHandler):
    """ REST API for category search
    """
    def get(self):
        self.post()

    def post(self):
        user_id = self.get_argument('user_id',"")
        project_id = self.get_argument('project_id',"")
        search_param = self.get_argument('search_param')
        search_param = { "user_id":user_id, 
                        "project_id":project_id, 
                        "search_param":search_param}        
        
        logging.info("search_category_handler:search_param -> " + str(search_param))

        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_category"]
        
        try:
            res = category_search_query(search_param, es, index,doc_type)
        except Exception as e:
            logging.exception(e)
                
            self.set_status(400,str(e))
            return        

            
        
        self.set_header("Content-Type", 'application/json')
        self.write(res["hits"])                    
                    
#
#
#  Keywords
#
#                    
#==============================================================================
 
def keyword_search_query(param,es,index,doc_type, dpage = 0):
     """ Return the full keyword from the keyword name 
     Low level function.
     """    
     #page = param.pop("page",dpage)
                 
     #Building the query
     if not param["project_id"]:
         doc = {
             "query": {
             "filtered":
             {
                 "filter": { "term":{ "name":param["search_param"] } } 
                 }
             }
             }
     else:
         doc = {
             "query": {
             "filtered":
             {
                 "filter": [{ "term":{ "project_id":param["project_id"] } },
                            { "term":{ "name":param["search_param"] } } ]
                 }
             }
             }
     logging.debug(doc)
     
     #Executing the query
     res = es.search(index=index, doc_type=doc_type, body=doc)
     #print(res)
 
     
     #print("basic_search_query:hits -> " + str(res['hits']['hits']))
     
     return res
 
                     
class search_keyword_handler(tornado.web.RequestHandler):
     """ REST API for keyword search
     """
     def get(self):
         self.post()

     def post(self):
         user_id = self.get_argument('user_id',"")
         project_id = self.get_argument('project_id',"")
         search_param = self.get_argument('search_param')
         search_param = { "user_id":user_id, 
                         "project_id":project_id, 
                         "search_param":search_param}        
         
         logging.info("search_keyword_handler:search_param -> " + str(search_param))
 
         es = self.application.es
         index = self.application.config_init["index"]
         doc_type = self.application.config_init["type_keyword"]
         
         try:
             res = keyword_search_query(search_param, es, index,doc_type)
         except Exception as e:
             logging.exception(e)
                 
             self.set_status(400,str(e))
             return        

       
         self.set_header("Content-Type", 'application/json')
         self.write(res)                    
 

#    
#
# REST API for basic search
#
#                                   

class search_basic_handler(tornado.web.RequestHandler):
    """ REST API for basic handler
    """
    def get(self):
        self.post()

    def post(self):
        user_id = self.get_argument('user_id',"")
        project_id = self.get_argument('project_id',"")
        search_param = self.get_argument('search_param')
        search_param = { "user_id":user_id, 
                        "project_id":project_id, 
                        "search_param":search_param}        
        
        logging.info("search_basic_handler:search_param -> " + str(search_param))

        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]
        
        try:
            res = basic_search_query(search_param, es, index,doc_type)
        except Exception as e:
            logging.exception(e)
                
            self.set_status(400,str(e))
            return        
        
        self.set_header("Content-Type", 'application/json')
        self.write(res["hits"])


#    
#
# REST API for advanced search
#
# 


class search_advanced_handler(tornado.web.RequestHandler):
    """ REST API for advanced handler
    """
    def get(self):
        self.post()

    def post(self):
        user_id = self.get_argument('user_id',"")
        project_id = self.get_argument('project_id',"")
        output = self.get_argument("output","html")
        page = int(self.get_argument("page","1"))
        search_param = self.get_argument('search_param',"")
        if search_param:        
            logging.debug(search_param)
            try:
                search_param = json.loads(search_param)
            except Exception as e:
                logging.exception(e)
                self.set_status(400,str(e))
                return            
        else:
            search_param = {}
            search_param["title"] = self.get_argument('title',"")
            search_param["content-text"] = self.get_argument('content',"")
            search_param["content-type"] = self.get_argument('component',"")
            search_param["uuid"] = self.get_argument("id","")
            search_param["s_type"] = self.get_argument("s_type","")
            search_param["keywords"] = self.get_argument("keywords","")
            search_param["categories"] = self.get_argument("categories","")
            
        if user_id:    
            search_param["user_id"] = user_id
        if project_id:         
            search_param["project_id"] = project_id
            
        logging.info("search_advanced_handler:search_param -> " + str(search_param))


        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]
        
        try:
            res = advanced_search_query(search_param, es, index,doc_type)
        except Exception as e:
            logging.exception(e)                
            self.set_status(400,str(e))
            return        
        
        if output == "html":
            total = res["hits"]["total"]
            #max_score = res["hits"]["max_score"]
            list_results = res["hits"]["hits"]


            #logging.debug(total)
            #logging.debug(max_score)

            self.render('rest_results.html',
                    user = user_id,
                    project = project_id,
                    search_param = json.dumps(search_param),
                    s_type = "advanced_search_results",
                    page = page,
                    total = total,
                    list_results = list_results)
        else:
            self.set_header("Content-Type", 'application/json')
            self.write(res["hits"])
                    