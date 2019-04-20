package me.tsblock.Thonk.Command.Administrator;

import com.mongodb.client.MongoCollection;
import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.Database.MongoConnect;
import me.tsblock.Thonk.utils.Embed;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
/*
This command is for debug purpose
 */

public class forceCreateWalletCommand extends Command {
    private MongoConnect mongoConnect = new MongoConnect();
    @Override
    public String getName() {
        return "forcecreatewallet";
    }

    @Override
    public String getDescription() {
        return "Forcing the bot create a wallet for a user without user do t.balance (this is only for debug purpose)";
    }

    @Override
    public String getUsage() {
        return "<ID>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("fcw");
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
    public boolean needArgs() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        Embed embed = new Embed();
        if (guild.getMemberById(args[0]) == null) {
            embed.sendEmbed("User does not exist.", event.getChannel());
            return;
        }
        MongoCollection<Document> doc = mongoConnect.getUserStats();
        Document found = doc.find(new Document("id", args[0])).first();
        if (found != null) {
            embed.sendEmbed("User wallet already exists", event.getChannel());
            return;
        }
        mongoConnect.initUserStats(args[0]);
        embed.sendEmbed("created wallet for user " + args[0], event.getChannel());
    }
}
