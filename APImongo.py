import pymongo
from datetime import datetime, timezone
from flask import Flask, request, jsonify, g, current_app, Response
from flask_cors import CORS
from pymongo.errors import PyMongoError
import logging
import json
from json import dumps 
from typing import cast
import os 
import sys

# Basado en: https://github.com/mongodb-university/atlas_starter_python/blob/master/atlas-starter.py

# Configura la conexión a MongoDB Atlas
try:
    conexion= "mongodb+srv://hdanielqg:rkyde4mRHsWYGzU7@proyecto2.y96zzh2.mongodb.net/?retryWrites=true&w=majority&appName=Proyecto2"
    client = pymongo.MongoClient(conexion)

# return a friendly error if a URI error is thrown 
except pymongo.errors.ConfigurationError:
  print("An Invalid URI host error was received. Is your Atlas host name correct in your connection string?")
  sys.exit(1)

try:
    # Seleccionar la base de datos y la colección
    db = client.sample_mflix
    collection = db.movies

except pymongo.errors as e:
    print("Error: ", e)

port = int("8000")


# Inicializa Flask
app = Flask(__name__)
CORS(app)


#Funciones incluidas para busqueda por pelicula 
def serialize_movie(movie):
    return {
        "id": movie["id"],
        "title": movie["title"],
        "summary": movie["summary"],
        "released": movie["released"],
        "duration": movie["duration"],
        "rated": movie["rated"],
        "tagline": movie["tagline"],
        "votes": movie.get("votes", 0)
    }

# "   "
def serialize_cast(cast):
    return {
        "name": cast[0],
        "job": cast[1],
        "role": cast[2]
    }

# contador de tiempo por request hecho utilizando https://sureshdsk.dev/flask-decorator-to-measure-time-taken-for-a-request
@app.before_request
def logging_before():
    pass

@app.route('/endpoint', methods=['POST'])
def endpoint():
    data = request.get_json()
    usuario = data.get("userId")
    busqueda = data.get("busqueda")

    # Guarda la solicitud en MongoDB
    solicitud = {
        "body": request.get_data(as_text=True),
        "request": busqueda,
        "timestamp": datetime.now(timezone.utc),
        "usuario": usuario
    }
    coleccion = db.registros
    coleccion.insert_one(solicitud)
    
    return jsonify({"mensaje": "Solicitud guardada exitosamente"}), 200


@app.route('/titulo/<palabra>', methods=['GET'])
def buscar_pelicula(palabra):
    try:
        # Seleccionar la base de datos y la colección
        db = client.sample_mflix
        collection = db.movies

        # Realizar la búsqueda en el campo deseado
        regex = f"\\b{palabra}\\b"       # Expresión para buscar la palabra exacta
        resultado = collection.find({"title": { "$regex": regex, "$options": "i" }})

        resultados_json = [documento for documento in resultado]

        # Convertir la lista de diccionarios a JSON y luego a bytes
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        return resultados_json

    except PyMongoError as e:
        return jsonify({"error": str(e)})


@app.route('/director/<director>', methods=['GET'])
def buscar_directors(director):
    try:
        # Seleccionar la base de datos y la colección
        db = client.sample_mflix
        collection = db.movies

        # Realizar la búsqueda en el campo deseado
        agg =  [{"$search": {
            "index": "directores", 
            "wildcard": {
                "query": director,
                "path": "directors",
                "allowAnalyzedField": True
            }
        }}]

        resultado = collection.aggregate(agg)

        
        resultados_json = [documento for documento in resultado]

        # Convertir la lista de diccionarios a JSON y luego a bytes
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        return resultados_json

    except PyMongoError as e:
        return jsonify({"error": str(e)})
    
@app.route('/elenco/<cast>', methods=['GET'])
def buscar_cast(cast):
    try:
        # Seleccionar la base de datos y la colección
        db = client.sample_mflix
        collection = db.movies

        # Realizar la búsqueda en el campo deseado
        agg =  [{"$search": {
            "index": "elenco", 
            "wildcard": {
                "query": cast,
                "path": "cast",
                "allowAnalyzedField": True
            }
        }}]

        resultado = collection.aggregate(agg)

        
        resultados_json = [documento for documento in resultado]

        # Convertir la lista de diccionarios a JSON y luego a bytes
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        return resultados_json

    except PyMongoError as e:
        return jsonify({"error": str(e)})
    

@app.route('/trama/<plot>', methods=['GET'])
def buscar_plot(plot):
    try:
        # Seleccionar la base de datos y la colección
        db = client.sample_mflix
        collection = db.movies

        # Realizar la búsqueda en el campo deseado
        agg =  [{"$search": {
            "index": "trama", 
            "wildcard": {
                "query": plot,
                "path": "plot",
                "allowAnalyzedField": True
            }
        }}]

        resultado = collection.aggregate(agg)

        resultados_json = [documento for documento in resultado]

        # Convertir la lista de diccionarios a JSON y luego a bytes
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        return resultados_json

    except PyMongoError as e:
        return jsonify({"error": str(e)})


if __name__ == '__main__':
    app.run(host='0.0.0.0')


