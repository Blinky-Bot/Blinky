package me.tsblock.Blinky.Command.Fun;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.HTTPRequest.GetBody;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.BSONObject;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class urbanCommand extends Command {
    @Override
    public String getName() {
        return "urban";
    }

    @Override
    public String getDescription() {
        return "Get definition from urban dictionary";
    }

    @Override
    public String getUsage() {
        return "<word>";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        try {
            JSONObject urban = GetBody.getDefine(String.join(",", args));
            JSONArray list = urban.getJSONArray("list");
            if (list.isNull(0)) {
                event.getChannel().sendMessage("Definition not found!").queue();
                return;
            }
            JSONObject icantunderstand = list.getJSONObject(0);
            String word = icantunderstand.getString("word").replace("[word]", "").replace("[/word]", "").replace("[", "").replace("]", "");
            String definition = icantunderstand.getString("definition");
            String example = icantunderstand.getString("example");
            int thumbsup = icantunderstand.getInt("thumbs_up");
            int thumbsdown = icantunderstand.getInt("thumbs_down");
            EmbedBuilder urbane = new EmbedBuilder()
                    .setTitle(word, icantunderstand.getString("permalink"))
                    .addField("Definition", definition, false)
                    .addField("Example", example, false)
                    .setFooter(thumbsup + "\uD83D\uDC4D" + " | " + thumbsdown + "\uD83D\uDC4E" + " | " + "1 / " + icantunderstand.length() + " | Menu will be deactivated after 1 minute.", null)
                    .setColor(Color.ORANGE);
            event.getChannel().sendMessage(urbane.build()).queue(m->{
                MongoCollection<Document> messages = MongoConnect.getReactionMessages();
                Document doc = new Document("messageID", m.getId())
                        .append("userID", user.getId())
                        .append("page", 0)
                        .append("maxPage", list.length() - 1)
                        .append("object", urban.toMap())
                        .append("type", "urban");
                messages.insertOne(doc);
                m.addReaction("◀").queue();
                m.addReaction("▶").queue();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        messages.deleteOne(doc);
                        event.getChannel().removeReactionById(m.getId(), "◀").queue();
                        event.getChannel().removeReactionById(m.getId(), "▶").queue();
                        m.addReaction("❌").queue();
                    }
                }, 60000);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
