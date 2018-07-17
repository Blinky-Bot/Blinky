package me.tsblock.Blinky.Handler;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Database.MongoConnect;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import org.json.JSONObject;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

public class StarboardHandler extends ListenerAdapter {
    MongoCollection<Document> starboard = MongoConnect.getStarboard();
    MongoCollection<Document> guildSettings = MongoConnect.getGuildSettings();
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (!event.getReactionEmote().getName().equals("⭐")) return;
        if (event.getUser().equals(Bot.getJDA().getSelfUser())) return;
        Document foundSettings = guildSettings.find(new Document("id", event.getGuild().getId())).first();
        Document starboardMessage = starboard.find(new Document("messageID", event.getMessageId())).first();
        if (foundSettings != null) {
            if (foundSettings.getString("starboard").isEmpty()) return;
            List<Object> array = new JSONObject(foundSettings.toJson()).getJSONArray("starboardRoles").toList();
            if (starboardMessage != null) return;
            if (new JSONObject(foundSettings.toJson()).getJSONArray("starboardRoles").length() < 1) { //if empty == everyone can star
                sendStarboard(event, foundSettings);
            } else {
                for (Role roles : event.getMember().getRoles()) {
                    if (array.contains(roles.getId())) {
                        sendStarboard(event, foundSettings);
                        break;
                    }
                }
            }

        }
    }

    //idk it does not work
//    @Override
//    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
//        if (!event.getReactionEmote().getName().equals("⭐")) return;
//        Document foundGuildSettings = guildSettings.find(new Document("id", event.getGuild().getId())).first();
//        Document message = starboard.find(new Document("messageID", event.getMessageId())).first();
//        if (message != null) {
//            if (event.getUser().getId().equals(message.getString("reactorID"))) {
//                for (TextChannel channel : event.getGuild().getTextChannels()) {
//                    channel.getMessageById(message.getString("messageID")).queue(m->{
//                        m.delete().queue();
//                        starboard.deleteOne(message);
//                    });
//                }
//            }
//        }
//    }

    private void sendStarboard(GuildMessageReactionAddEvent event, Document foundSettings) {
        event.getChannel().getMessageById(event.getMessageId()).queue(m->{
            EmbedBuilder star = new EmbedBuilder()
                    .setAuthor(m.getMember().getUser().getName() + "#" + m.getMember().getUser().getDiscriminator(), null, m.getMember().getUser().getEffectiveAvatarUrl())
                    .setColor(Color.YELLOW)
                    .setTitle("\uD83C\uDF1F")
                    .setTimestamp(Instant.now())
                    .setFooter("Starred by: " + event.getUser().getName() + "#" + event.getUser().getDiscriminator(), event.getUser().getEffectiveAvatarUrl());
            if (!m.getAttachments().isEmpty())
                star.setImage(m.getAttachments().get(0).getUrl());
            else
                star.setDescription(m.getContentRaw());
            event.getGuild().getTextChannelById(foundSettings.getString("starboard")).sendMessage(star.build()).queue(msg->{
                starboard.insertOne(
                        new Document("messageID", msg.getId())
                                .append("reactorID", event.getUser().getId())
                );
            });
        });
    }
}
