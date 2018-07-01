package me.tsblock.Blinky.Command.Administrator;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class evalCommand extends Command {
    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public String getDescription() {
        return "Evaluate codes";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean ownerOnly() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        MongoCollection<Document> ReactionMessages = MongoConnect.getReactionMessages();
        engine.put("event", event);
        engine.put("jda", Bot.getJDA());
        try {
            Object result = engine.eval(String.join(" ", args));
            MessageEmbed evaled = new EmbedBuilder()
                    .setDescription("```xl\n" + result + "\n```")
                    .setFooter("React ⛔ to remove", null)
                    .build();
            event.getChannel().sendMessage(evaled).queue(m -> {
                m.addReaction("⛔").queue();
                Document doc = new Document("messageID", m.getId())
                        .append("emote", "⛔")
                        .append("userID", user.getId())
                        .append("type", "evalDelete");
                ReactionMessages.insertOne(doc);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        MongoConnect.getReactionMessages().deleteOne(doc);
                    }
                }, 10000);
                m.delete().queueAfter(5, TimeUnit.SECONDS);
            });
        } catch (ScriptException e) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("<:redTick:438696459894784000> Error!")
                    .setDescription("```xl" + e.toString() + "\n```")
                    .setColor(Color.red)
                    .build();
            event.getChannel().sendMessage(embed).queue();
        }
    }
}
