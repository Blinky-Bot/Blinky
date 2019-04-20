package me.tsblock.Thonk.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.Database.MongoConnect;
import me.tsblock.Thonk.utils.Embed;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class dailyCommand extends Command {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    String todayDaily = dateFormat.format(new Date());
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
        MongoCollection<Document> userdoc = MongoConnect.getUserStats();
        Document userdocc = userdoc.find(new Document("id", user.getId())).first();
        if (userdocc != null) {
            String lastDaily = userdocc.getString("lastDaily");
//            if (lastDaily.isEmpty() || lastDaily == null) {
//                update(userdocc, userdoc);
//                Embed.sendEmbed("Added $1000 to your wallet", msg.getChannel());
//                return;
//            }
            if (!todayDaily.equalsIgnoreCase(lastDaily)) {
                update(userdocc, userdoc);
                Embed.sendEmbed("Added $1000 to your wallet", msg.getChannel());
            } else {
                Embed.sendEmbed("You have already claimed the rewards. Wait one more day to claim it again.", msg.getChannel());
            }
        }
    }
    private void update(Document userdocc, MongoCollection<Document> userdoc) {
        Bson ok = new Document("balance", userdocc.getLong("balance") + 1000).append("lastDaily", todayDaily);
        Bson update = new Document("$set", ok);
        userdoc.updateOne(userdocc, update);
    }
}
