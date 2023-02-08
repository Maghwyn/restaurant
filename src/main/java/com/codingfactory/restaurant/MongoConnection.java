package com.codingfactory.restaurant;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
public class  MongoConnection{
    private static MongoDatabase database;

    static {
        try {
            Dotenv dotenv = Dotenv.configure().directory("./assets").load();
            String mongo_uri = dotenv.get("MONGODB_URI");
            String mongo_db_name = dotenv.get("MONGODB_DATABASE");

            String uri = mongo_uri;

            MongoClientURI mongoClientUri = new MongoClientURI(uri);

            MongoClient mongoClient = new MongoClient(mongoClientUri) ;

            database = mongoClient.getDatabase(mongo_db_name);
            System.out.println("CONNECTED_TO_DB");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}
