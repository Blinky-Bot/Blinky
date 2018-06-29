package me.tsblock.Blinky.Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.Tag;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoConnect {
    private Settings settings = SettingsManager.getInstance().getSettings();
    private MongoDatabase database;
    private static MongoCollection GuildSettings;
    private static MongoCollection UserStats;
    private static MongoCollection UserLevels;
    private static MongoCollection<Document> Tags;


    public void connect() {
        MongoClient client = new MongoClient(new MongoClientURI(settings.getDataBaseURL()));
        setDatabase(client.getDatabase("plexio"));
        setUserStats(database.getCollection("UserStats"));
        setUserLevels(database.getCollection("UserLevels"));
        setTags(database.getCollection("Tags"));
        System.out.println("Connected to database");
    }

    public static void initUserStats(String id) {
        Document document = new Document("id", id);
        document.append("balance", 300L);
        document.append("lastDaily", "");
        document.append("donor", false);
        UserStats.insertOne(document);
    }

    public static void initLevel(String id, String GuildID) {
        Document doc = new Document("id", id);
        doc.append("xp", 0);
        doc.append("level", 1);
        doc.append("maxLevel", false);
        doc.append("guildID", GuildID);
        UserLevels.insertOne(doc);
    }

    public static void updateDocument(Document document, MongoCollection<Document> collection, String key, Object value) {
        Document found = collection.find(document).first();
        if (found != null) {
            Bson operation = new Document("$set", new Document(key, value));
            collection.updateOne(found, operation);
        } else {
            throw new NullPointerException();
        }
    }

    public static void addTag(String name, String content, String id) {
        Document doc = new Document("id" ,id)
                .append("name", name)
                .append("content", content);
        Tags.insertOne(doc);
    }

    public static void removeTag(String name, String id) {
        Tags.deleteOne(new Document("name", name).append("id", id));
    }

    public static String getTag(String name) {
        Document found = Tags.find(new Document("name", name)).first();
        return found.getString("content");
    }

    public static void transferTag(String name, String newid) {
        Bson operation = new Document("$set", new Document("id", newid));
        Tags.updateOne(new Document("name", name), operation);
    }

    public static MongoCollection<Document> getUserStats() {
        return UserStats;
    }

    public void setUserStats(MongoCollection userStats) {
        UserStats = userStats;
    }

    public static MongoCollection<Document> getUserLevels() {
        return UserLevels;
    }

    public void setUserLevels(MongoCollection userLevels) {
        UserLevels = userLevels;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public static MongoCollection getTags() {
        return Tags;
    }

    public void setTags(MongoCollection tags) {
        Tags = tags;
    }
}
