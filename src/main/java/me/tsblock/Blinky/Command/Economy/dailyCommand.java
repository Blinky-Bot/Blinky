package me.tsblock.Blinky.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.utils.Embed;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.List;

public class dailyCommand extends Command {
    @Override
    public String getName() {
        return "daily";
    }

    @Override
    public String getDescription() {
        return "Gives you some daily cash";
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
        //TODO: smh finish the daily command
        MongoCollection<Document> userdoc = MongoConnect.getUserStats();
        Document userdocc = userdoc.find(new Document("id", user.getId())).first();
        if (userdocc != null) {
            long lastDaily = userdocc.getLong("lastDaily");
            if (lastDaily == 0) {
                update(userdocc, userdoc);
                Embed.sendEmbed("Added $1000 to your wallet", msg.getChannel());
                return;
            }
            long todayDaily = lastDaily + 86400000; //a day
            if (lastDaily < todayDaily) {
                Embed.sendEmbed("You have already claimed the rewards.", msg.getChannel());
            } else {
                update(userdocc, userdoc);
                Embed.sendEmbed("Added $1000 to your wallet", msg.getChannel());
            }
        }
    }
    private void update(Document userdocc, MongoCollection<Document> userdoc) {
        Bson update = new Document("$set", new Document("balance", userdocc.getLong("balance") + 1000).append("lastDaily", System.currentTimeMillis()));
        userdoc.updateOne(userdocc, update);
    }
}
