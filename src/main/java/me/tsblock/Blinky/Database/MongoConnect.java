package me.tsblock.Blinky.Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import org.bson.Document;

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

    public void initUserStats(String id) {
        Document document = new Document("id", id);
        document.append("balance", 300L);
        document.append("lastDaily", 0);
        document.append("donor", false);
        UserStats.insertOne(document);
    }

    public void initLevel(String id, String GuildID) {
        Document doc = new Document("id", id);
        doc.append("xp", 0);
        doc.append("level", 1);
        doc.append("maxLevel", false);
        doc.append("guildID", GuildID);
        UserLevels.insertOne(doc);
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
