#!/usr/bin/env python3
#
# Concept project
__copyright__ = "Copyright Atos ARI 2015"
__author__= "Pablo Salinero, Josemi"

import tornado.web
import tornado.httpclient
import json

import logging

import base64

from constants import boost_list, doc_type_code, doc_type_img
    
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
                    from_=(page-1) * 10, size = 10)
    #print(res)

    
    #print("basic_search_query:hits -> " + str(res['hits']['hits']))
    
    return res
    
    
def advanced_search_query(param,es,index,doc_type, 
                              dpage = 1, size = 10):
    """Create the advanced query for elastic 
    from the params list,
    and returns the results as elastic.
    Low level function.
    """    
    param_list = param.copy() #we don't want the change original parameter           
    
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
        
    exist = param_list.pop("exist","")
    if exist:
        filtered = True
        filter_ = { "exists":{"field":exist}}
        filterList.append(filter_)
    
    rating = param_list.pop("rating","")
    if rating:
        logging.debug(rating)
        match = { "range":{"rating.rating":{"gte":rating}}}
        matchList.append(match)
        
   
    

    if "categories" in param_list and param_list["categories"] and project_id:
        #categories query expansion
        s_tree = getCategoriesTree(project_id,es,index)
        if s_tree:
            #the list of categories is comma separated
            query = param_list["categories"].split(",")
            query = list(map(str.strip,query))

            param_list["categories"] = expandQuery(query,s_tree)
        
    title = param_list.pop("title","")
    if title:
        logging.debug(title)
        match = {"multi_match": {
            "query":  title,
            "fields": [ "title", "title.std^15" ]
        }}
        matchList.append(match) 
        
    content_text = param_list.pop("content-text","")
    if content_text:
        logging.debug(content_text)
        match = {"multi_match": {
            "query":  content_text,
            "fields": [ "content-text", "content-text.std^25" ]
        }}
        matchList.append(match) 


#==============================================================================
#     categories = param_list.pop("categories","")
#     if categories:
#         logging.debug(categories)
#         match =  { "nested": {
#                         "path": "categories",
#                         "score_mode": "sum",
#                         "query": {
#                            "function_score": {
#                               "query": {"match": 
#                                   {"categories.tag":categories 
#                                  }
#                                  },
#                               "script_score": {"script": "doc[\"score\"].value" }
#                            }
#                         }
#                      }
#                      }
#==============================================================================
#        matchList.append(match)

        
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
    
    doc = {}    
    
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
                    from_=(page-1) * size, size = size)
    #print(res)

    
    #print("basic_search_query:hits -> " + str(res['hits']['hits']))
    
    return res
    
    


        

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
            logging.info(search_param)
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
            
        logging.info("search_advanced_handler:search_param -> " 
                        + str(search_param))


        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]
        
        try:
            res = advanced_search_query(search_param, es, index,
                                        doc_type, dpage = page, size = 10)
        except Exception as e:
            logging.exception(e)                
            self.set_status(400,str(e))
            return        
        
        if output == "html":
            total = res["hits"]["total"]
            #max_score = res["hits"]["max_score"]
            list_results = res["hits"]["hits"]

            
            for item in list_results:
                # changing the type code of the document for a long name
                doc_code = item["_source"].get("content-type","")
                item["_source"]["content-type-name"] = doc_type_code.get(doc_code,doc_code)
                # adding an icon according to the file type
                item["_source"]["content-icon"] = doc_type_img.get(doc_code,"/images/fm_generic.png")
                # it is an image?
                if ("image-properties" in item["_source"]
                        and "format" in item["_source"]["image-properties"]
                        and item["_source"]["image-properties"]["format"] =="jpg" ):
                    item["_source"]["content-icon"] = "thumbnail"
                # deleting the [""] present for a empty field 
                if ((len(item["_source"].get("categories",[])) > 0)
                        and (item["_source"]["categories"][0] == "")):
                    item["_source"]["categories"] = []
                if ((len(item["_source"].get("keywords",[])) > 0)
                        and (item["_source"]["keywords"][0] == "")):
                    item["_source"]["keywords"] = []

            #logging.debug(total)
            #logging.debug(max_score)

            self.render('rest_results.html',
                    user = user_id,
                    project = project_id,
                    search_param = json.dumps(search_param),
                    s_type = "search_advanced",
                    page = page,
                    total = total,
                    list_results = list_results)
        else:
            self.set_header("Content-Type", 'application/json')
            self.write(res["hits"])
                    
                    
class search_image_by_id_handler(tornado.web.RequestHandler):
    """ REST API for image search by id
    """
    def get(self):
        self.post()

    def post(self):
        user_id = self.get_argument('user_id',"")
        project_id = self.get_argument('project_id',"")
        #output = self.get_argument("output","html")
        #page = int(self.get_argument("page","1"))
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
            #search_param["content-text"] = self.get_argument('content',"")
            #search_param["content-type"] = self.get_argument('component',"")
            search_param["uuid"] = self.get_argument("id","")
            #search_param["s_type"] = self.get_argument("s_type","")
            #search_param["keywords"] = self.get_argument("keywords","")
            #search_param["categories"] = self.get_argument("categories","")
            
        if user_id:    
            search_param["user_id"] = user_id
        if project_id:         
            search_param["project_id"] = project_id
        
        search_param["exist"] = "image-properties"        
        
        logging.info("search_image_handler:search_param -> " + str(search_param))


        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]
        
        try:
            res = advanced_search_query(search_param, 
                                        es, 
                                        index,
                                        doc_type,
                                        size = 1)
        except Exception as e:
            logging.exception(e)                
            self.set_status(400,str(e))
            return        
        
        #max_score = res["hits"]["max_score"]
        if res["hits"]["total"] == 1:
            list_results = res["hits"]["hits"]
        
            b = base64.b64decode(list_results[0]["_source"]["content-image"])

            #logging.debug(total)
            #logging.debug(max_score)

            self.set_header("Content-Type", 'image/jpg')
            self.write(b)           
        elif res["hits"]["total"] == 0:
            self.set_status(404, "not found")
            return
        else:
            #something very weird happend
            self.set_status(400,"other error")
            
class search_image_by_example_handler(tornado.web.RequestHandler):
    """ REST API for image search by example, sending a example image
    or using the id of a stored image
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
            logging.info(search_param)
            try:
                search_param = json.loads(search_param)
            except Exception as e:
                logging.exception(e)
                self.set_status(400,str(e))
                return            
        else:
            search_param = {}
            search_param["uuid"] = self.get_argument('id',"")
                    
        if user_id:    
            search_param["user_id"] = user_id
        if project_id:         
            search_param["project_id"] = project_id

        size = 10


        es = self.application.es
        index = self.application.config_init["index"]
        doc_type = self.application.config_init["type_item"]               
                
        queryImage =  {
                     "image": {
                     "content-image": {
                         "feature": "CEDD",
                         "index": index,
                         "type": doc_type,
                         "id": search_param["uuid"],
                         "path": "content-image",
                         "hash": "BIT_SAMPLING"
                     }
                     }
                    }
        
        if project_id:
            doc = { "query": 
                    {
                        "filtered": {
                            "filter": {
                                "bool": {
                                "must":[ {"term": { "project_id":project_id }}],
                                "must_not":[ { "term": {"uuid":search_param["uuid"] }}]
                                }
                            },
                            "query":queryImage
                        }
                    }
                }
        else:
             doc = { "query":queryImage }
        
             #Executing the query
        try:
             res = es.search(index=index, doc_type=doc_type, body=doc,
                              from_=(page-1) * size, size = size)
        except Exception as e:
            logging.exception(e)                
            self.set_status(400,str(e))
            return    

        logging.info(res["hits"]["total"])

        for i in res['hits']['hits']:
            logging.info("{} {}".format(i["_id"],i["_score"]))
    


        if output == "html":
            total = res["hits"]["total"]
            #max_score = res["hits"]["max_score"]
            list_results = res["hits"]["hits"]

            for item in list_results:
                # changing the type code of the document for a long name
                doc_code = item["_source"].get("content-type","")
                item["_source"]["content-type-name"] = doc_type_code.get(doc_code,doc_code)
                # adding an icon according to the file type
                item["_source"]["content-icon"] = doc_type_img.get(doc_code,"/images/fm_generic.png")
                # it is an image?
                if ("image-properties" in item["_source"]
                        and "format" in item["_source"]["image-properties"]
                        and item["_source"]["image-properties"]["format"] =="jpg" ):
                    item["_source"]["content-icon"] = "thumbnail"
                # deleting the [""] present for a empty field 
                if ((len(item["_source"].get("categories",[])) > 0)
                        and (item["_source"]["categories"][0] == "")):
                    item["_source"]["categories"] = []
                if ((len(item["_source"].get("keywords",[])) > 0)
                        and (item["_source"]["keywords"][0] == "")):
                    item["_source"]["keywords"] = []


            #logging.debug(total)
            #logging.debug(max_score)

            self.render('rest_results.html',
                    user = user_id,
                    project = project_id,
                    search_param = json.dumps(search_param),
                    s_type = "search_image_qbe",
                    page = page,
                    total = total,
                    list_results = list_results)
        else:
            self.set_header("Content-Type", 'application/json')
            self.write(res["hits"]["hits"])
            

# This set of functions expands the fathers from the children
#
##def expandTerm(term,s_tree,resultList,listFathers):
##    for item in s_tree:
##        #loggin.info((item["label"],listFathers)
##        if item["label"].lower()==term.lower():
##            return resultList.extend(listFathers)
##        else:
##            if "children" in item:
##                fathers = list(listFathers)
##                fathers.append(item["label"])
##                expandTerm(term,item["children"],
##                           resultList,fathers)
##            
##            
##def expandQuery(query,s_tree):
##    logging.info("expandQuery")
##    resultList=list(query)
##    for term in query:
##        expandTerm(term,s_tree,resultList,[])
##
##    logging.info(resultList)
##    lt =' OR '.join(resultList)
##    logging.info(lt)
##    return lt

# this set of funcitions give the children of a father node
def expandTerm(term,s_tree,resultList):
    for item in s_tree:
        #print(item)
        if item["label"].lower()==term.lower():
            getChildren(item,resultList)
        else:
            if "children" in item:
                expandTerm(term,item["children"],resultList)
            
def getChildren(item,resultList):
    #print(item)
    if "children" in item:
        for i in item["children"]:
            resultList.append(i["label"])
            getChildren(i,resultList)
            
def expandQuery(categories,s_tree):
    logging.info("expandQuery")
    resultList=list(categories)
    for term in categories:
        expandTerm(term,s_tree,resultList)

    logging.info(resultList)
    lt =' OR '.join(resultList)
    logging.info(lt)
    return lt


def getCategoriesTree(project,es,index):
    logging.info("Searching for categories for "+project)
    
    doc = { "query": 
                {
                "filtered": {
                    "filter": {
                    "term":{ "project_id":project} 
                    }
                }
                }
                }
    try:
        res = es.search(index=index, doc_type="categories", body=doc,size = 1)
    except Exception as e:
        logging.exception(e)                
        return None
        
    if res["hits"]["total"] == 1:
        list_results = res["hits"]["hits"]
            
        data = list_results[0]["_source"]["categories"]            
        #print("Data:"+str(list_results))

        try:
            tree = json.loads(data)
            logging.info("Found categories tree for "+project)
            return tree
        except:
            logging.exception("Bad format "+project)  
            return None              
    logging.info("No results for "+project)
    return None
                  
                  
