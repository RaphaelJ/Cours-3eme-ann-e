{
   "_id": "_design/index",
   "_rev": "10-02da49ecc533312c8c9efcb8749102c9",
   "language": "javascript",
   "views": {
       "by_artist": {
           "map": "function (doc) {\n    append_array = function (arr, elems) {\n        if (elems) {\n            if (elems.length)\n                return arr.concat(elems);\n            else {\n                arr.push(elems);\n                return arr;\n            }\n        } else\n            return arr;\n    }\n    \n    artists = new Array();\n    artists = append_array(artists, doc.actors);\n    artists = append_array(artists, doc.directors);\n    artists = append_array(artists, doc.artists);\n    artists = append_array(artists, doc.authors);\n\n    artists.forEach(function (e) {\n        if (e)\n            emit(e, null);\n    });\n}"
       },
       "by_price": {
           "map": "function (doc) {\n    if (doc.price) {\n        emit(doc.price, null);\n    }\n}"
       },
       "by_category": {
           "map": "function (doc) {\n    if (doc.category) {\n        emit(doc.category, null);\n    }\n}"
       },
       "by_language": {
           "map": "\nfunction (doc) {\n    append_array = function (arr, elems) {\n        if (elems) {\n            if (elems.length)\n                return arr.concat(elems);\n            else {\n                arr.push(elems);\n                return arr;\n            }\n        } else\n            return arr;\n    }\n    \n    langues = new Array();\n    langues = append_array(langues, doc.language);\n    langues = append_array(langues, doc.languages);\n    langues = append_array(langues, doc.subtitles);\n\n    langues.forEach(function (e) {\n        if (e)\n            emit(e, null);\n    });\n}"
       },
       "by_asin": {
           "map": "\nfunction (doc) {\n    if (doc.asin)\n            emit(doc.asin, null);\n}"
       }
   }
}