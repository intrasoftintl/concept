#!/usr/bin/env python3

import codecs
import json

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
            
def expandTerms(categories,s_tree):
    resultList=list(categories)
    for term in categories:
        expandTerm(term,s_tree,resultList)
    return resultList



f = codecs.open("sample_hierarchy.json","r","UTF-8")
sim_file = f.read()
f.close()

s_tree = json.loads(sim_file)

categories = "Furniture,Consumer,Market Analysis,Product Language"

categories = categories.split(",")
categories = list(map(str.strip,categories))

categories = expandTerms(categories,s_tree)

print(categories)
