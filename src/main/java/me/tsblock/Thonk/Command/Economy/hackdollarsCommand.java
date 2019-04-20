package me.tsblock.Thonk.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.Database.MongoConnect;
import me.tsblock.Thonk.utils.Embed;
import me.tsblock.Thonk.utils.Utils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.List;

public class hackdollarsCommand extends Command {
    private MongoConnect mongoConnect = new MongoConnect();
    private Embed embed = new Embed();
    @Override
    public String getName() {
        return "hackdollars";
    }

    @Override
    public String getDescription() {
        return "become a HACKERMAN";
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
        if (args[0].length() > 15) {
            embed.sendEmbed("HAHAHAH BITCH YOU CANT GetBody THAT THICC MONEY HAHHAAHHAHA", event.getChannel());
            return;
        }
        if (Utils.isNaN(args[0])) {
            embed.sendEmbed(args[0] + " is not a number dumbass", event.getChannel());
            return;
        }
        MongoCollection<Document> userStatsCollection = mongoConnect.getUserStats();
        Document found = (Document) userStatsCollection.find(new Document("id", event.getAuthor().getId())).first();
        if (found != null) {
            long newValue = found.getLong("balance") + Long.parseLong(args[0]);
            Bson updatedvalue = new Document("balance", newValue);
            Bson updateoperation = new Document("$set", updatedvalue);
            userStatsCollection.updateOne(found, updateoperation);
            event.getChannel().sendMessage("***YOU JUST HACCED " + "$" + args[0] + "*** \\<:dab:396368624245407744>").queue();
        } else {
            event.getChannel().sendMessage("you already know what just happened!!!1#!").queue();
        }
    }
}
