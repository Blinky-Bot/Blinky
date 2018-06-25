package me.tsblock.Blinky.Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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

    public void connect() {
        MongoClient client = new MongoClient(new MongoClientURI(settings.getDataBaseURL()));
        setDatabase(client.getDatabase("plexio"));
        System.out.println("Connected to database");
        setUserStats(database.getCollection("UserStats"));
        setUserLevels(database.getCollection("UserLevels"));
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
}
