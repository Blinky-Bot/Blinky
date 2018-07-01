package me.tsblock.Blinky.Handler;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Database.MongoConnect;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler extends ListenerAdapter {
    static MongoCollection<Document> messages = MongoConnect.getReactionMessages();
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Bot.getCommandHandler().handle(event);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser().getId().equals(Bot.getJDA().getSelfUser().getId())) return;
        Document message = messages.find(new Document("messageID", event.getMessageId())).first();
        if (message != null) {
            deleteEval(message, event);
            switchPageUrban(message, event);

        }
    }

    public void deleteEval(Document message, MessageReactionAddEvent event) {
        if (message.getString("messageID").equals(event.getMessageId()) && message.getString("userID").equals(event.getUser().getId()) && message.getString("type").equals("evalDelete") && message.getString("emote").equals("⛔")) {
            event.getChannel().getMessageById(message.getString("messageID")).queue(m -> {
                m.delete().queue();
            });
        }
    }

    public static void switchPageUrban(Document message, MessageReactionAddEvent event) {
        if (message.getString("messageID").equals(event.getMessageId()) && message.getString("userID").equals(event.getUser().getId()) && message.getString("type").equals("urban")) {
            int currentPage = message.getInteger("page");
            switch (event.getReactionEmote().getName()) {
                case ("◀"):
                    currentPage -= 1;
                    if (currentPage < 0) currentPage = message.getInteger("maxPage");
                    editUrban(currentPage, event, message);
                    break;
                case ("▶"):
                    currentPage += 1;
                    if (currentPage > message.getInteger("maxPage")) currentPage = 0;
                    editUrban(currentPage, event, message);
                    break;
            }
        }
    }

    public static void editUrban(int currentPage, MessageReactionAddEvent event, Document message) {
        JSONObject list = new JSONObject(message.toJson()).getJSONObject("object").getJSONArray("list").getJSONObject(currentPage);
        String word = list.getString("word");
        String definition = list.getString("definition");
        String example = list.getString("example");
        String link = list.getString("permalink");
        int upvote = list.getInt("thumbs_up");
        int downvote = list.getInt("thumbs_down");
        int maxPage = message.getInteger("maxPage");
        Bson operation = new Document("$set", new Document("page", currentPage));
        messages.updateOne(message, operation);
        event.getChannel().getMessageById(message.getString("messageID")).queue(msg -> {
            event.getReaction().removeReaction(event.getUser()).queue();
            msg.editMessage(urbanEmbed(word, definition, example, link, upvote, downvote, currentPage, maxPage)).override(true).queue();
        });
    }

    public static MessageEmbed urbanEmbed(String word, String definition, String example, String link, int upvote, int downvote, int currentPage, int maxPage) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(word, link)
                .setColor(Color.YELLOW)
                .addField("Definition", definition, false)
                .addField("Example", example, false)
                .setFooter(upvote + "\uD83D\uDC4D" + " | " + downvote + "\uD83D\uDC4E" + " | " + (currentPage + 1) + " / " + (maxPage + 1) + " | Menu will be deactivated after 1 minute.", null)
                .build();
        return embed;
    }


}

