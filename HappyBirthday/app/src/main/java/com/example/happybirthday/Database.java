package com.example.happybirthday;

import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
public class Database {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public Database(){
        String connectionString = "mongodb+srv://hdanielqg:rkyde4mRHsWYGzU7@proyecto2.y96zzh2.mongodb.net/?retryWrites=true&w=majority&appName=Proyecto2";
        ConnectionString connString = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("sample_mflix");
    }

    public void insertDocument(String collectionName, Document document) {
        database.getCollection(collectionName).insertOne(document);
    }
}