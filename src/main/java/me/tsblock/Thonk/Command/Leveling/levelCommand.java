package me.tsblock.Thonk.Command.Leveling;

import com.mongodb.client.MongoCollection;
import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.Database.MongoConnect;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class levelCommand extends Command {
    private MongoConnect mongoConnect = new MongoConnect();
    @Override
    public String getName() {
        return "level";
    }

    @Override
    public String getDescription() {
        return "Gets your current level";
    }

    @Override
    public String getUsage() {
        return null;
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
        return false;
    }

    @Override
    public boolean needArgs() {
        return false;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        MongoCollection<Document> userdoc = mongoConnect.getUserLevels();
        Document toFind = new Document("id", user.getId())
                .append("guildID", guild.getId());
        Document found = userdoc.find(toFind).first();
        if (found != null) {
            int currentlevel = found.getInteger("level");
            int xptillnextlevel = (currentlevel * (300)) - found.getInteger("xp");
            String nextLevel = "\nPoints till next level: " + xptillnextlevel;
            if (currentlevel == 100) nextLevel = "\nMax level";
            MessageEmbed level = new EmbedBuilder()
                    .setColor(Color.cyan)
                    .setDescription("Current Level: " + currentlevel + nextLevel)
                    .setFooter("WIP Feature, expect bugs.", null)
                    .build();
            event.getChannel().sendMessage(level).queue();
        }
    }
}
