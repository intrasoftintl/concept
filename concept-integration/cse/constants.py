#! /usr/bin/env python3
# -*- coding: utf-8 -*-
#
# Concept project
__copyright__ = "Copyright Atos ARI 2015"
__author__= "Pablo Salinero, Josemi"

# Constants

english_html_analyzer = {
  "settings": {
      "number_of_shards" :   1,
      "number_of_replicas" : 0,
    "analysis": {
      "filter": {
        "english_stop": {
          "type":       "stop",
          "stopwords":  "_english_" 
        },
        "english_stemmer": {
          "type":       "stemmer",
          "language":   "english"
        },
        "english_possessive_stemmer": {
          "type":       "stemmer",
          "language":   "possessive_english"
        },
        "english_synonym": {
          "type": "synonym", 
          "format": "wordnet",
          "synonyms_path": "analysis/wn_s.pl"
        }        
      },
      "analyzer": {
        "english-html": {
            "char_filter": "html_strip", #important
          "tokenizer":  "standard",
          "filter": [
            "english_possessive_stemmer",
            "lowercase",
            "english_stop",
            "english_stemmer"
          ]
        },
        "english-html-syno": {
            "char_filter": "html_strip", #important
          "tokenizer":  "standard",
          "filter": [
            "english_possessive_stemmer",
            "lowercase",
            "english_stop",
            "english_synonym",
            "english_stemmer"
          ]
        },        
        "english-syno": {
          "tokenizer":  "standard",
          "filter": [
            "english_possessive_stemmer",
            "lowercase",
            "english_stop",
            "english_synonym",
            "english_stemmer"
          ]
        }
            
      }
    }
  }
}


boost_list = { "title":2,"categories":3,"keyword":3 }


categories_mapping = {
	"category": {
		"properties" : {
			"name" : {"type" : "string",
             "index" : "not_analyzed" },
			"linkedData" : {"type" : "string",
                 "index":"not_analyzed" },
			"category-hierarchy" : {
				"type" : "object",
				"properties" : {
					"level-1" : {"type" : "string"},
					"level-2" : {"type" : "string"},
					"level-3" : {"type" : "string"}
				}
    }
   }
}
}
 
keywords_mapping = {    
	"keyword" : {
		"properties" : {
			"name" : {"type" : "string",
              "index":"not_analyzed" },
			"linkedData" : {"type" : "string",
                 "index":"not_analyzed"},
			"relevancy" : {"type" : "float"},
			"enrichedTags" : {
				"properties" : {
					"name" : {"type" : "string",
                 "index":"not_analyzed" },
					"linkedData" : {"type" : "string",
                 "index":"not_analyzed"},
					"weight" : {"type" : "float"}
				}
			}
		}
	}
} 
 
 
docs_mapping = {	"search_item" : {
		"properties" : {
			"uuid" : {"type" : "string",
             "index":"not_analyzed"},
			"url" : {"type" : "string",
            "index":"not_analyzed"},
    "user_id":{ "type" : "string" },
    "project_id": { "type" : "string" },
			"version" : {"type" : "integer"},
			"last-updated" : {"type" : "date" },
			"content-type" : {"type" : "string",
                     "index":"not_analyzed"},
			"title" : {"type" : "string",
              "analyzer":"english-syno",
              "fields": {
                        "std":   { 
                            "type":     "string",
                            "analyzer": "standard"
                        }
              }
              },         
			"origin" : {"type" : "string"},
			"language" : {"type" : "string",
                 "index":"not_analyzed"},
    "language_name" :  {"type" : "string",
                 "index":"not_analyzed"},
			"description" : {"type" : "string"},
			"categories" : {"type" : "string"},
#    "categories": {
#                  "type": "nested",
#                  "fields": {
#                      "tag": {
#                          "type": "string"
#                      },
#                      "score": {
#                          "type": "float"		
#                      }
#                     }
#                     },
    "keywords" : {"type" : "string"},
			"status" : {"type" : "string",
                 "index":"not_analyzed"},
			"domain" : {"type" : "string"},
			"content-text" : {"type" : "string",
                       "analyzer":"english-html-syno",
                      "fields": {
                        "std":   { 
                            "type":     "string",
                            "analyzer": "standard"
                        }
              }  
                       },  
    "content-raw" : {"type" : "string",
                 "index":"no"},
    "content-image":{ "type": "image",
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
    },
			"image-properties" : {
				"type" : "object",
				"properties" : {
					"height" : {"type" : "integer"},
					"width" : {"type" : "integer"},
					"format" : {"type" : "string",
                 "index":"not_analyzed"}
				}
			},
			"thumbnail-url" : {"type" : "string",
                 "index":"not_analyzed"},
			"parent-uuid" : {"type" : "string",
                 "index":"not_analyzed"},
			"authors" : {
                                "type" : "object",
				"properties" : {
					"person-id" : {"type" : "string"},
					"person-name" : {"type" : "string"}
				}
			},
			"uploader" : {
				"type" : "object",
				"properties" : {
					"person-id" : {"type" : "string",
                 "index":"not_analyzed"},
					"person-name" : {"type" : "string"}
				}
			},
			"references" : {
     "type" : "object",
				"properties" : {
					"id" : {"type" : "string",
                 "index":"not_analyzed"},
					"url" : {"type" : "string",
                 "index":"not_analyzed"},
					"title" : {"type" : "string"},
					"content-type" : {"type" : "string"},
					"authors" : {
      "type" : "object",
						"properties" : {
							"person-id" : {"type" : "string",
                 "index":"not_analyzed"},
							"person-name" : {"type" : "string"}
						}
					}
				}
			},
			"ipr-owners" : {
				"properties" : {
					"person-id" : {"type" : "string",
                 "index":"not_analyzed"},
					"person-name" : {"type" : "string"}
				}
			},
			"license-type" : {"type" : "string"},
			"license-url" : {"type" : "string",
                 "index":"not_analyzed"},
			"projects" : {
				"properties" : {
					"id" : {"type" : "string",
                 "index":"not_analyzed"},
					"url" : {"type" : "string",
                 "index":"not_analyzed"},
					"title" : {"type" : "string"},
					"authors" : {
						"properties" : {
							"person-id" : {"type" : "string",
                 "index":"not_analyzed"},
							"person-name" : {"type" : "string"}
						}
					}
				}
			},
			"clients" : {
				"properties" : {
					"id" : {"type" : "string",
                 "index":"not_analyzed"},
					"name" : {"type" : "string"},
					"contact-list" : {"type" : "string"}
				}
			},
			"rating" : {
				"type" : "object",
				"properties" : {
					"rating" : {"type" : "float"},
					"voters" : {"type" : "integer"}
				}
			},
			"evaluators" : {
				"properties" : {
					"person-id" : {"type" : "string",
                 "index":"not_analyzed"},
					"person-name" : {"type" : "string"}
				}
			},
			"creation-timestamp" : {"type" : "date"}
		}
	}
    }




languages_list = [
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ab"}},
{"description" : "Abkhaz", "code": "ab"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"aa"}},
{"description" : "Afar", "code": "aa"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"af"}},
{"description" : "Afrikaans", "code": "af"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ak"}},
{"description" : "Akan", "code": "ak"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sq"}},
{"description" : "Albanian", "code": "sq"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"am"}},
{"description" : "Amharic", "code": "am"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ar"}},
{"description" : "Arabic", "code": "ar"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"an"}},
{"description" : "Aragonese", "code": "an"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"hy"}},
{"description" : "Armenian", "code": "hy"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"as"}},
{"description" : "Assamese", "code": "as"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"av"}},
{"description" : "Avaric", "code": "av"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ae"}},
{"description" : "Avestan", "code": "ae"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ay"}},
{"description" : "Aymara", "code": "ay"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"az"}},
{"description" : "Azerbaijani", "code": "az"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"bm"}},
{"description" : "Bambara", "code": "bm"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ba"}},
{"description" : "Bashkir", "code": "ba"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"eu"}},
{"description" : "Basque", "code": "eu"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"be"}},
{"description" : "Belarusian", "code": "be"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"bn"}},
{"description" : "Bengali", "code": "bn"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"bh"}},
{"description" : "Bihari", "code": "bh"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"bi"}},
{"description" : "Bislama", "code": "bi"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"bs"}},
{"description" : "Bosnian", "code": "bs"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"br"}},
{"description" : "Breton", "code": "br"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"bg"}},
{"description" : "Bulgarian", "code": "bg"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"my"}},
{"description" : "Burmese", "code": "my"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ca"}},
{"description" : "Catalan", "code": "ca"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ch"}},
{"description" : "Chamorro", "code": "ch"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ce"}},
{"description" : "Chechen", "code": "ce"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ny"}},
{"description" : "Chichewa", "code": "ny"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"zh"}},
{"description" : "Chinese", "code": "zh"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"cv"}},
{"description" : "Chuvash", "code": "cv"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"kw"}},
{"description" : "Cornish", "code": "kw"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"co"}},
{"description" : "Corsican", "code": "co"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"cr"}},
{"description" : "Cree", "code": "cr"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"hr"}},
{"description" : "Croatian", "code": "hr"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"cs"}},
{"description" : "Czech", "code": "cs"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"da"}},
{"description" : "Danish", "code": "da"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"dv"}},
{"description" : "Divehi", "code": "dv"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"nl"}},
{"description" : "Dutch", "code": "nl"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"dz"}},
{"description" : "Dzongkha", "code": "dz"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"en"}},
{"description" : "English", "code": "en"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"eo"}},
{"description" : "Esperanto", "code": "eo"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"et"}},
{"description" : "Estonian", "code": "et"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ee"}},
{"description" : "Ewe", "code": "ee"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"fo"}},
{"description" : "Faroese", "code": "fo"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"fj"}},
{"description" : "Fijian", "code": "fj"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"fi"}},
{"description" : "Finnish", "code": "fi"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"fr"}},
{"description" : "French", "code": "fr"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ff"}},
{"description" : "Fula", "code": "ff"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"gl"}},
{"description" : "Galician", "code": "gl"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ka"}},
{"description" : "Georgian", "code": "ka"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"de"}},
{"description" : "German", "code": "de"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"el"}},
{"description" : "Greek (modern)", "code": "el"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"gn"}},
{"description" : "Guaraní", "code": "gn"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"gu"}},
{"description" : "Gujarati", "code": "gu"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ha"}},
{"description" : "Hausa", "code": "ha"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"he"}},
{"description" : "Hebrew (modern)", "code": "he"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"hz"}},
{"description" : "Herero", "code": "hz"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"hi"}},
{"description" : "Hindi", "code": "hi"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ho"}},
{"description" : "Hiri Motu", "code": "ho"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"hu"}},
{"description" : "Hungarian", "code": "hu"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ia"}},
{"description" : "Interlingua", "code": "ia"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"id"}},
{"description" : "Indonesian", "code": "id"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ie"}},
{"description" : "Interlingue", "code": "ie"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ga"}},
{"description" : "Irish", "code": "ga"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ig"}},
{"description" : "Igbo", "code": "ig"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ik"}},
{"description" : "Inupiaq", "code": "ik"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"io"}},
{"description" : "Ido", "code": "io"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"is"}},
{"description" : "Icelandic", "code": "is"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"it"}},
{"description" : "Italian", "code": "it"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"iu"}},
{"description" : "Inuktitut", "code": "iu"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ja"}},
{"description" : "Japanese", "code": "ja"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"jv"}},
{"description" : "Javanese", "code": "jv"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"kl"}},
{"description" : "Kalaallisut", "code": "kl"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"kn"}},
{"description" : "Kannada", "code": "kn"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"kr"}},
{"description" : "Kanuri", "code": "kr"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ks"}},
{"description" : "Kashmiri", "code": "ks"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"kk"}},
{"description" : "Kazakh", "code": "kk"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"km"}},
{"description" : "Khmer", "code": "km"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ki"}},
{"description" : "Kikuyu, Gikuyu", "code": "ki"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"rw"}},
{"description" : "Kinyarwanda", "code": "rw"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ky"}},
{"description" : "Kyrgyz", "code": "ky"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"kv"}},
{"description" : "Komi", "code": "kv"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"kg"}},
{"description" : "Kongo", "code": "kg"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ko"}},
{"description" : "Korean", "code": "ko"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ku"}},
{"description" : "Kurdish", "code": "ku"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"kj"}},
{"description" : "Kwanyama", "code": "kj"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"la"}},
{"description" : "Latin", "code": "la"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"lb"}},
{"description" : "Luxembourgish", "code": "lb"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"lg"}},
{"description" : "Ganda", "code": "lg"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"li"}},
{"description" : "Limburgish", "code": "li"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ln"}},
{"description" : "Lingala", "code": "ln"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"lo"}},
{"description" : "Lao", "code": "lo"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"lt"}},
{"description" : "Lithuanian", "code": "lt"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"lu"}},
{"description" : "Luba-Katanga", "code": "lu"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"lv"}},
{"description" : "Latvian", "code": "lv"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"gv"}},
{"description" : "Manx", "code": "gv"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"mk"}},
{"description" : "Macedonian", "code": "mk"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"mg"}},
{"description" : "Malagasy", "code": "mg"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ms"}},
{"description" : "Malay", "code": "ms"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ml"}},
{"description" : "Malayalam", "code": "ml"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"mt"}},
{"description" : "Maltese", "code": "mt"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"mi"}},
{"description" : "Māori", "code": "mi"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"mr"}},
{"description" : "Marathi", "code": "mr"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"mh"}},
{"description" : "Marshallese", "code": "mh"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"mn"}},
{"description" : "Mongolian", "code": "mn"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"na"}},
{"description" : "Nauru", "code": "na"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"nv"}},
{"description" : "Navajo", "code": "nv"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"nd"}},
{"description" : "Northern Ndebele", "code": "nd"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ne"}},
{"description" : "Nepali", "code": "ne"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ng"}},
{"description" : "Ndonga", "code": "ng"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"nb"}},
{"description" : "Norwegian Bokmål", "code": "nb"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"nn"}},
{"description" : "Norwegian Nynorsk", "code": "nn"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"no"}},
{"description" : "Norwegian", "code": "no"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ii"}},
{"description" : "Nuosu", "code": "ii"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"nr"}},
{"description" : "Southern Ndebele", "code": "nr"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"oc"}},
{"description" : "Occitan", "code": "oc"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"oj"}},
{"description" : "Ojibwe", "code": "oj"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"cu"}},
{"description" : "Old Church Slavonic", "code": "cu"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"om"}},
{"description" : "Oromo", "code": "om"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"or"}},
{"description" : "Oriya", "code": "or"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"os"}},
{"description" : "Ossetian", "code": "os"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"pa"}},
{"description" : "Panjabi", "code": "pa"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"pi"}},
{"description" : "Pāli", "code": "pi"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"fa"}},
{"description" : "Persian (Farsi)", "code": "fa"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"pl"}},
{"description" : "Polish", "code": "pl"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ps"}},
{"description" : "Pashto, Pushto", "code": "ps"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"pt"}},
{"description" : "Portuguese", "code": "pt"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"qu"}},
{"description" : "Quechua", "code": "qu"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"rm"}},
{"description" : "Romansh", "code": "rm"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"rn"}},
{"description" : "Kirundi", "code": "rn"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ro"}},
{"description" : "Romanian", "code": "ro"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ru"}},
{"description" : "Russian", "code": "ru"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sa"}},
{"description" : "Sanskrit", "code": "sa"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sc"}},
{"description" : "Sardinian", "code": "sc"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sd"}},
{"description" : "Sindhi", "code": "sd"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"se"}},
{"description" : "Northern Sami", "code": "se"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sm"}},
{"description" : "Samoan", "code": "sm"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sg"}},
{"description" : "Sango", "code": "sg"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sr"}},
{"description" : "Serbian", "code": "sr"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"gd"}},
{"description" : "Scottish Gaelic", "code": "gd"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sn"}},
{"description" : "Shona", "code": "sn"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"si"}},
{"description" : "Sinhala", "code": "si"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sk"}},
{"description" : "Slovak", "code": "sk"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sl"}},
{"description" : "Slovene", "code": "sl"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"so"}},
{"description" : "Somali", "code": "so"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"st"}},
{"description" : "Southern Sotho", "code": "st"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"es"}},
{"description" : "Spanish", "code": "es"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"su"}},
{"description" : "Sundanese", "code": "su"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sw"}},
{"description" : "Swahili", "code": "sw"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ss"}},
{"description" : "Swati", "code": "ss"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"sv"}},
{"description" : "Swedish", "code": "sv"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ta"}},
{"description" : "Tamil", "code": "ta"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"te"}},
{"description" : "Telugu", "code": "te"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"tg"}},
{"description" : "Tajik", "code": "tg"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"th"}},
{"description" : "Thai", "code": "th"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ti"}},
{"description" : "Tigrinya", "code": "ti"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"bo"}},
{"description" : "Tibetan", "code": "bo"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"tk"}},
{"description" : "Turkmen", "code": "tk"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"tl"}},
{"description" : "Tagalog", "code": "tl"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"tn"}},
{"description" : "Tswana", "code": "tn"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"to"}},
{"description" : "Tonga", "code": "to"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"tr"}},
{"description" : "Turkish", "code": "tr"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ts"}},
{"description" : "Tsonga", "code": "ts"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"tt"}},
{"description" : "Tatar", "code": "tt"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"tw"}},
{"description" : "Twi", "code": "tw"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ty"}},
{"description" : "Tahitian", "code": "ty"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ug"}},
{"description" : "Uyghur", "code": "ug"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"uk"}},
{"description" : "Ukrainian", "code": "uk"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ur"}},
{"description" : "Urdu", "code": "ur"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"uz"}},
{"description" : "Uzbek", "code": "uz"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"ve"}},
{"description" : "Venda", "code": "ve"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"vi"}},
{"description" : "Vietnamese", "code": "vi"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"vo"}},
{"description" : "Volapük", "code": "vo"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"wa"}},
{"description" : "Walloon", "code": "wa"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"cy"}},
{"description" : "Welsh", "code": "cy"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"wo"}},
{"description" : "Wolof", "code": "wo"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"fy"}},
{"description" : "Western Frisian", "code": "fy"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"xh"}},
{"description" : "Xhosa", "code": "xh"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"yi"}},
{"description" : "Yiddish", "code": "yi"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"yo"}},
{"description" : "Yoruba", "code": "yo"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"za"}},
{"description" : "Zhuang", "code": "za"},
{"index":{"_index":"concept_search_engine","_type":"language","_id":"zu"}},
{"description" : "Zulu", "code": "zu"}
]

doc_type_code = {
"MB":"MoodBoard",
"FM":"File",
"BA":"Document",
"MM":"Mindmap",
"SB":"StoryBoard",
"SK":"Sketch"
}

doc_type_img = {
"MB":"/images/fm_generic.png",
"FM":"/images/fm_generic.png",
"BA":"/images/fm_generic.png",
"MM":"/images/fm_generic_mm.png",
"SB":"/images/fm_generic.png",
"SK":"/images/fm_generic.png"
}

 
