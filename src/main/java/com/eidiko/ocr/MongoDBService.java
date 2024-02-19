package com.eidiko.ocr;


import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

@Service
public class MongoDBService {

    // Method to insert a document into a MongoDB collection
    public void insertDocument(String databaseName, String collectionName, Document document) {
        try (MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"))) {
            // Get the database
            MongoDatabase database = mongoClient.getDatabase(databaseName);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Insert the document
            collection.insertOne(document);

            System.out.println("Document inserted successfully.");
        } catch (Exception e) {
            System.err.println("Error inserting document: " + e.getMessage());
        }
    }
}
