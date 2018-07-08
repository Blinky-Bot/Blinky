package me.tsblock.Blinky.Database;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.Tag;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import net.dv8tion.jda.core.entities.Message;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoConnect {
    private Settings settings = SettingsManager.getInstance().getSettings();
    private static MongoDatabase database;
    private static MongoCollection GuildSettings;
    private static MongoCollection UserStats;
    private static MongoCollection UserLevels;
    private static MongoCollection ReactionMessages;
    private static MongoCollection Complaints;
    private static MongoCollection Spinners;
    private static MongoCollection<Document> Tags;


    public void connect() {
        MongoClient client = new MongoClient(new MongoClientURI(settings.getDataBaseURL()));
        setDatabase(client.getDatabase("plexio"));
        setUserStats(database.getCollection("UserStats"));
        setUserLevels(database.getCollection("UserLevels"));
        setTags(database.getCollection("Tags"));
        setComplaints(database.getCollection("Complaints"));
        setReactionMessages(database.getCollection("ReactionMessages"));
        setSpinners(database.getCollection("Spinners"));
        resetCollection();
        System.out.println("Connected to database");
    }

    public static void resetCollection() {
        Spinners.deleteMany(new Document());
        ReactionMessages.deleteMany(new Document());
        Bson filter = new Document("blocked", true);
        Bson uOperation = new Document("$set", new Document("blocked", false));
        UserLevels.updateMany(filter, uOperation);
    }

    public static void initUserStats(String id) {
        Document document = new Document("id", id);
        document.append("balance", "300");
        document.append("lastDaily", "");
        document.append("donor", false);
        UserStats.insertOne(document);
    }

    public static void initLevel(String id, String GuildID) {
        Document doc = new Document("id", id);
        doc.append("xp", 0);
        doc.append("level", 1);
        doc.append("maxLevel", false);
        doc.append("blocked", false);
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

    public static int getLength(String collectionName) {
        Document stats = database.runCommand(new Document("collStats", collectionName));
        return stats.getInteger("count");
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
    public static MongoCollection getComplaints() {
        return Complaints;
    }

    public static void setComplaints(MongoCollection complaints) {
        Complaints = complaints;
    }

    public static MongoCollection getSpinners() {
        return Spinners;
    }

    public static void setSpinners(MongoCollection spinners) {
        Spinners = spinners;
    }

    public static MongoCollection getReactionMessages() {
        return ReactionMessages;
    }

    public static void setReactionMessages(MongoCollection reactionMessages) {
        ReactionMessages = reactionMessages;
    }
}
