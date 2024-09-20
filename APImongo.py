import pymongo
from datetime import datetime, timezone
from flask import Flask, request, jsonify, g, current_app, Response, url_for, send_from_directory
from flask_cors import CORS
from flask_mail import Mail, Message
from email.mime.text import MIMEText
# main.py
from estadis1 import generate_user_chart, generar_grafico_pastel

import os
from pymongo.errors import PyMongoError
import sys

import smtplib


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
    db = client.proyecto_ap
    collection = db.projects

except pymongo.errors as e:
    print("Error: ", e)

port = int("8000")


# Inicializa Flask
app = Flask(__name__)
CORS(app)

app.config['MAIL_SERVER'] = 'smtp.office365.com'
app.config['MAIL_PORT'] = 587
app.config['MAIL_USE_TLS'] = True
app.config['MAIL_USE_SSL'] = False
app.config['MAIL_USERNAME'] = os.environ.get('proyectocrowd@outlook.com')  # Your email address
app.config['MAIL_PASSWORD'] = os.environ.get('patata13')  # Your email password
app.config['MAIL_DEFAULT_SENDER'] = os.environ.get('proyectocrowd@outlook.com')  # Default sender email


mail = Mail(app)


# contador de tiempo por request hecho utilizando https://sureshdsk.dev/flask-decorator-to-measure-time-taken-for-a-request
@app.before_request
def logging_before():
    pass

@app.route('/addUser', methods=['POST'])
def addUsuario():
    data = request.get_json()
    usuario = data.get("name")
    email = data.get("email")
    password = data.get("password")
    phone = data.get("phone")
    wallet = data.get("wallet")

    # Guarda la solicitud en MongoDB
    solicitud = {
        "name": usuario,
        "email": email,
        "password": str(password),
        "phone": phone,
        "isActive": 1,
        "wallet": int(wallet),
        "isAdmin": 0
    }
    coleccion = db.users
    coleccion.insert_one(solicitud)
    send_welcome_email(email, usuario)

    return jsonify({"mensaje": "Solicitud guardada exitosamente y correo enviado"}), 200

def send_welcome_email(to_email, username):
    try:
        smtp = smtplib.SMTP('smtp.office365.com', 587)
        smtp.starttls()  # Initiate TLS

        smtp.login('proyectocrowd@outlook.com', 'patata13')  # Login to your email

        # Create the email content
        from_email = "proyectocrowd@outlook.com"
        subject = "Welcome to our crowdfunding service!"
        body = f"Hello {username},\n\nThanks to subscribe to our crowdfunding app!\nThanks for your trust in us.\n\nBest regards,\nCrowdfunding team"

        msg = MIMEText(body)
        msg['From'] = from_email
        msg['To'] = to_email
        msg['Subject'] = subject

        # Send email
        smtp.sendmail(from_email, [to_email], msg.as_string())

        smtp.quit()
        return "Email sent successfully!"
    except Exception as e:
        print(f"Failed to send email: {e}")

@app.route('/addProyecto', methods=['POST'])
def addProyecto():
    data = request.get_json()
    nombre = data.get("name")
    description = data.get("description")
    goal = data.get("goal")
    endDate = data.get("endDate")
    creator = data.get("creator")
    logo = data.get("logo")

    # Guarda la solicitud en MongoDB
    solicitud = {
        "name": nombre,
        "description": description,
        "goal": int(goal),
        "endDate": endDate,
        "gathered": 0,
        "state": 1,
        "creator": creator,
        "logo" : logo
    }
    coleccion = db.projects
    coleccion.insert_one(solicitud)
    
    return jsonify({"mensaje": "Solicitud guardada exitosamente"}), 200

@app.route('/addDonacion', methods=['POST'])
def addDonacion():
    try:
        data = request.get_json()
        nombreDonator = data.get("name")
        date = data.get("date")
        amount = data.get("amount")
        projectName = data.get("projectName")

        # Guardar la solicitud en la colección de donaciones
        solicitud = {
            "nameDonator": nombreDonator,
            "date": date,
            "amount": int(amount),
            "project": projectName,
        }
        donacion_collection = db.donations
        donacion_collection.insert_one(solicitud)

        # Actualizar el campo gathered en la colección projects
        project_collection = db.projects
        result = project_collection.update_one(
            {"name": projectName},          # Filtro: Proyecto donde coincida el nombre
            {"$inc": {"gathered": int(amount)}}  # Incrementa el campo gathered por el monto de la donación
        )

        if result.matched_count == 0:
            return jsonify({"error": "Proyecto no encontrado"}), 404
        
        proyecto = project_collection.find_one({"name": projectName})

        if proyecto:
            gathered = proyecto.get("gathered", 0)  # Valor actual de gathered
            goal = proyecto.get("goal", 0)          # Valor del goal
            current_state = proyecto.get("state", 1)  # Estado actual, por defecto es 1

            # Verificar si gathered ha alcanzado o superado el goal
            if gathered >= goal:
                # Actualizar el estado a 2 si gathered supera goal
                project_collection.update_one(
                    {"name": projectName},
                    {"$set": {"state": 2}}
                )

        user_collection = db.users
        user = user_collection.find_one({"name": nombreDonator})

        if not user:
            return jsonify({"error": "Donador no encontrado"}), 404

        # Obtener el correo del donador
        to_email_donator = user.get("email")

                # Buscar el correo del creador del proyecto
        creador_name = proyecto.get("creator")  # Suponiendo que el campo 'creator' está en el proyecto
        creador = user_collection.find_one({"name": creador_name})

        if not creador:
            return jsonify({"error": "Creador del proyecto no encontrado"}), 404

        to_email_creador = creador.get("email")

        # Enviar el email de agradecimiento al donador
        send_thanks_email(to_email_donator, nombreDonator, projectName)

        # Enviar el email al creador del proyecto
        send_notification_to_creator(to_email_creador, creador_name, projectName, amount, nombreDonator)
        # Enviar el email de agradecimiento
        send_thanks_email(to_email_creador, nombreDonator, projectName)

    except PyMongoError as e:
        return jsonify({"error": str(e)}), 500

def send_thanks_email(to_email, username, projectName):
    try:
        smtp = smtplib.SMTP('smtp.office365.com', 587)
        smtp.starttls()  # Initiate TLS

        smtp.login('proyectocrowd@outlook.com', 'patata13')  # Login to your email

        # Create the email content
        from_email = "proyectocrowd@outlook.com"
        subject = "Thanks for supporting my project<3!"
        body = f"Hello {username},\n\nThanks for making a donation to {projectName}!\nThanks for your trust in my idea.\n\nBest regards,\n{projectName} team"

        msg = MIMEText(body)
        msg['From'] = from_email
        msg['To'] = to_email
        msg['Subject'] = subject

        # Send email
        smtp.sendmail(from_email, [to_email], msg.as_string())

        smtp.quit()
        return "Email sent successfully!"
    except Exception as e:
        print(f"Failed to send email: {e}")

def send_notification_to_creator(to_email, creador, projectName, amount, nombreDonator):
    try:
        smtp = smtplib.SMTP('smtp.office365.com', 587)
        smtp.starttls()  # Initiate TLS

        smtp.login('proyectocrowd@outlook.com', 'patata13')  # Login to your email

        # Create the email content
        amountStr=str(amount)
        from_email = "proyectocrowd@outlook.com"
        subject = "Someone supports your project {projectName}!"
        body = f"Hello {creador},\n\nThe user {nombreDonator}, JUST MAKE A DONATION TO YOUR PROJECT!\nWith an amount of:{amountStr}\n\nBest regards,\ncrowdfunding team"

        msg = MIMEText(body)
        msg['From'] = from_email
        msg['To'] = to_email
        msg['Subject'] = subject

        # Send email
        smtp.sendmail(from_email, [to_email], msg.as_string())

        smtp.quit()
        return "Email sent successfully!"
    except Exception as e:
        print(f"Failed to send email: {e}")

@app.route('/update_password', methods=['POST'])
def actualizar_password():
    try:
        # Seleccionar la base de datos y la colección
        db = client.proyecto_ap
        collection = db.users

        # Obtener los datos enviados en el cuerpo de la solicitud
        data = request.get_json()
        usuario = data.get('userName') 
        password = data.get('password')  # La nueva contraseña
        password = str(password)

        if not usuario or not password:
            return jsonify({"error": "userId y password son requeridos"}), 400

        # Actualizar el campo password para el usuario con el user_id dado
        result = collection.update_one(
            {"name": usuario},
            {"$set": {"password": password}}  # Actualizar el campo password
        )

        # Verificar si se actualizó algún documento
        if result.modified_count == 1:
            return jsonify({"message": "Contraseña actualizada exitosamente"}), 200
        else:
            return jsonify({"error": "Usuario no encontrado o la contraseña no se actualizó"}), 404

    except PyMongoError as e:
        return jsonify({"error": str(e)}), 500

@app.route('/updateProject', methods=['POST'])
def actualizar_proyecto():
    try:
        # Obtener los datos enviados en el cuerpo de la solicitud
        data = request.get_json()
        nombre = data.get("name")
        description = data.get("description")
        goal = data.get("goal")
        endDate = data.get("endDate")
        logo = data.get("logo")
        # Verificar que se envíe el nombre del proyecto
        if not nombre:
            return jsonify({"error": "El nombre del proyecto es requerido"}), 400

        # Actualizar el proyecto con los campos proporcionados
        update_fields = {}

        if description:
            update_fields["description"] = description
        if goal is not None:
            update_fields["goal"] = int(goal)
        if endDate:
            update_fields["endDate"] = endDate
        if logo:
            update_fields["logo"] = logo

        # Seleccionar la base de datos y la colección
        project_collection = db.projects

        # Actualizar el proyecto con los datos proporcionados
        result = project_collection.update_one(
            {"name": nombre},  # Filtro: Proyecto donde coincida el nombre
            {"$set": update_fields}  # Actualizar los campos proporcionados
        )

        # Verificar si se actualizó algún proyecto
        if result.matched_count == 0:
            return jsonify({"error": "Proyecto no encontrado"}), 404

        return jsonify({"mensaje": "Proyecto actualizado exitosamente"}), 200

    except PyMongoError as e:
        return jsonify({"error": str(e)}), 500


@app.route('/user/<userName>', methods=['GET'])
def getUser(userName):
    try:
        # Seleccionar la base de datos y la colección
        db = client.proyecto_ap
        collection = db.users

        # Realizar la búsqueda en el campo deseado
        regex = f"\\b{userName}\\b"       # Expresión para buscar la palabra exacta
        resultado = collection.find({"name": { "$regex": regex, "$options": "i" }})

        resultados_json = [documento for documento in resultado]

        # Convertir la lista de diccionarios a JSON y luego a bytes
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        return resultados_json

    except PyMongoError as e:
        return jsonify({"error": str(e)})
    
    
    
@app.route('/verifyUser', methods=['POST'])
def verifyUser():
    try:
        data = request.get_json()
        usuario = data.get("username")
        password = data.get("password")

        # Seleccionar la colección
        coleccion = db.users

        # Verificar si el usuario con ese nombre y contraseña existe
        user = coleccion.find_one({"name": usuario, "password": str(password)})

        if user:
            # Si existe, retornar si es admin y que existe
            return jsonify({"existe": 1, "isAdmin": user.get("isAdmin", 0)}), 200
        else:
            # Si no existe, retornar que no existe
            return jsonify({"existe": 0}), 200

    except PyMongoError as e:
        return jsonify({"error": str(e)}), 500


@app.route('/donation/<donatorName>', methods=['GET'])
def getDonations(donatorName):
    try:
        # Seleccionar la base de datos y la colección
        db = client.proyecto_ap
        collection = db.donations

        # Realizar la búsqueda en el campo deseado
        regex = f"\\b{donatorName}\\b"       # Expresión para buscar la palabra exacta
        resultado = collection.find({"nameDonator": { "$regex": regex, "$options": "i" }})

        resultados_json = [documento for documento in resultado]

        # Convertir la lista de diccionarios a JSON y luego a bytes
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        return resultados_json

    except PyMongoError as e:
        return jsonify({"error": str(e)})
    
@app.route('/projects', methods=['GET'])
def getAllProjects():
    try:
        # Seleccionar la base de datos y la colección
        db = client.proyecto_ap
        collection = db.projects

        # Realizar la búsqueda de todos los proyectos (sin filtro)
        resultado = collection.find({})

        # Convertir el cursor a una lista de diccionarios (JSON)
        resultados_json = [documento for documento in resultado]

        # Convertir ObjectId a cadena para cada documento
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        # Retornar los proyectos como JSON
        return jsonify(resultados_json), 200

    except PyMongoError as e:
        return jsonify({"error": str(e)}), 500
    
@app.route('/users', methods=['GET'])
def getAllUsers():
    try:
        # Seleccionar la base de datos y la colección
        db = client.proyecto_ap
        collection = db.users

        # Realizar la búsqueda de todos los proyectos (sin filtro)
        resultado = collection.find({})

        # Convertir el cursor a una lista de diccionarios (JSON)
        resultados_json = [documento for documento in resultado]

        # Convertir ObjectId a cadena para cada documento
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        # Retornar los proyectos como JSON
        return jsonify(resultados_json), 200

    except PyMongoError as e:
        return jsonify({"error": str(e)}), 500

@app.route('/donations', methods=['GET'])
def getAllDonations():
    try:
        # Seleccionar la base de datos y la colección
        db = client.proyecto_ap
        collection = db.donations

        # Realizar la búsqueda de todos los proyectos (sin filtro)
        resultado = collection.find({})

        # Convertir el cursor a una lista de diccionarios (JSON)
        resultados_json = [documento for documento in resultado]

        # Convertir ObjectId a cadena para cada documento
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        # Retornar los proyectos como JSON
        return jsonify(resultados_json), 200

    except PyMongoError as e:
        return jsonify({"error": str(e)}), 500
    


@app.route('/projectFull/<projectName>', methods=['GET'])
def getProjectFull(projectName):
    try:
        # Seleccionar la base de datos y la colección
        db = client.proyecto_ap
        collection = db.projects

        # Realizar la búsqueda en el campo deseado
        regex = f"\\b{projectName}\\b"       # Expresión para buscar la palabra exacta
        resultado = collection.find({"name": { "$regex": regex, "$options": "i" }})
        resultados_json = [documento for documento in resultado]

        # Convertir la lista de diccionarios a JSON y luego a bytes
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        return resultados_json

    except PyMongoError as e:
        return jsonify({"error": str(e)})
    
@app.route('/projectByName/<creatorName>', methods=['GET'])
def getProjectByName(creatorName):
    try:
        # Seleccionar la base de datos y la colección
        db = client.proyecto_ap
        collection = db.projects

        # Realizar la búsqueda en el campo deseado
        regex = f"\\b{creatorName}\\b"       # Expresión para buscar la palabra exacta
        resultado = collection.find({"creator": { "$regex": regex, "$options": "i" }})
        resultados_json = [documento for documento in resultado]

        # Convertir la lista de diccionarios a JSON y luego a bytes
        for documento in resultados_json:
            documento['_id'] = str(documento['_id'])

        return resultados_json
    except PyMongoError as e:
        return jsonify({"error": str(e)})

# @app.route('/countUsers', methods=['GET'])
# def count_users():
#     try:
#         # Contar usuarios activos (isActive = 1)
#         active_users = users_collection.count_documents({"isActive": 1})
        
#         # Contar usuarios inactivos (isActive = 0)
#         inactive_users = users_collection.count_documents({"isActive": 0})
        
#         # Devolver los resultados como JSON
#         generate_user_chart(active_users, inactive_users)
    
#     except Exception as e:
#         return jsonify({"error": str(e)}), 500

@app.route('/getImageLink', methods=['GET'])
def get_image_link():
    IMAGE_FOLDER = r"C:\Users\hdani\OneDrive\Escritorio\Harlen\Semestre 2 2024\AP\App repo"
    try:
        users_collection = db.users
        # Contar usuarios activos (isActive = 1)
        active_users = users_collection.count_documents({"isActive": 1})
        
        # Contar usuarios inactivos (isActive = 0)
        inactive_users = users_collection.count_documents({"isActive": 0})
        
        projects_collection = db.projects
        state_0_count = projects_collection.count_documents({"state": 0})
        state_1_count = projects_collection.count_documents({"state": 1})
        state_2_count = projects_collection.count_documents({"state": 2})

        # Devolver los resultados como JSON
        generate_user_chart(active_users, inactive_users)
        generar_grafico_pastel(state_2_count, state_1_count, state_0_count)

        user_activity_chart_path = os.path.join(IMAGE_FOLDER, 'user_activity_chart.jpg')
        grafico_pastel_path = os.path.join(IMAGE_FOLDER, 'grafico_pastel_proyectos.jpg')
        image_urls = {}

        if os.path.exists(user_activity_chart_path):
            image_urls['user_activity_chart'] = url_for('serve_image', image_name='user_activity_chart.jpg', _external=True)
        else:
            image_urls['user_activity_chart'] = 'Image not found'

        if os.path.exists(grafico_pastel_path):
            image_urls['grafico_pastel_proyectos'] = url_for('serve_image', image_name='grafico_pastel_proyectos.jpg', _external=True)
        else:
            image_urls['grafico_pastel_proyectos'] = 'Image not found'

        return jsonify(image_urls), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/images/<image_name>', methods=['GET'])
def serve_image(image_name):
    IMAGE_FOLDER = r"C:\Users\hdani\OneDrive\Escritorio\Harlen\Semestre 2 2024\AP\App repo"
    try:
        # Serve the image from the 'images' folder
        return send_from_directory(IMAGE_FOLDER, image_name)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/toggleUserActive', methods=['POST'])
def toggle_user_active():
    try:
        # Get the data from the request
        data = request.get_json()
        user_name = data.get("name")
        action = data.get("action")  # "activate" or "deactivate"
        
        if not user_name or not action:
            return jsonify({"error": "Name and action are required"}), 400

        # Determine the value for `isActive` based on action
        is_active = 1 if action == "activate" else 0
        users_collection = db.users 
        # Update the user's `isActive` status based on the action
        result = users_collection.update_one(
            {"name": user_name},
            {"$set": {"isActive": is_active}}
        )

        # Check if any user was updated
        if result.matched_count == 0:
            return jsonify({"error": "User not found"}), 404

        action_status = "activated" if is_active == 1 else "deactivated"
        return jsonify({"message": f"User '{user_name}' has been {action_status}"}), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

#probarlo
#https://oyster-robust-ghost.ngrok-free.app/getImageLink/user_activity_chart.jpg

if __name__ == '__main__':
    app.run(host='0.0.0.0')


