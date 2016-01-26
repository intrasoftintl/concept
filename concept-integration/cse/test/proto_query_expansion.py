#!/usr/bin/env python3

import codecs
import json

def expandTerm(term,s_tree,resultList,listFathers):
    for item in s_tree:
        print(item["label"],listFathers)
        if item["label"].lower()==term.lower():
            return resultList.extend(listFathers)
        else:
            if "children" in item:
                fathers = list(listFathers)
                fathers.append(item["label"])
                expandTerm(term,item["children"],
                           resultList,fathers)
            
            
def expandQuery(query,s_tree):
    resultList=list(query)
    for term in query:
        expandTerm(term,s_tree,resultList,[])
    return " ".join(resultList)



f = codecs.open("sample_hierarchy.json","r","UTF-8")
sim_file = f.read()
f.close()

s_tree = json.loads(sim_file)

query = "corporate stone pepe"

query = query.split()
query = list(map(str.strip,query))

query = expandQuery(query,s_tree)

print(query)
