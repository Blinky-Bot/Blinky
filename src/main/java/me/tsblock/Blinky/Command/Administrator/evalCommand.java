package me.tsblock.Blinky.Command.Administrator;

import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.utils.Embed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class evalCommand extends Command {
    private Embed embed = new Embed();
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
        engine.put("event", event);
        engine.put("jda", Bot.getJDA());
        engine.put("userstats", MongoConnect.getUserStats());
        engine.put("userlevels", MongoConnect.getUserLevels());
        try {
            Object result = engine.eval(String.join(",", args));
            embed.sendEmbed("```java\n" + result + "\n```", event.getChannel());
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
