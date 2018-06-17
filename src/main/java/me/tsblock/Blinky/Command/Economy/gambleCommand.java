package me.tsblock.Blinky.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class gambleCommand extends Command {
    @Override
    public String getName() {
        return "gamble";
    }

    @Override
    public String getDescription() {
        return "Gamble some money!";
    }

    @Override
    public String getUsage() {
        return "<amount to gamble>";
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
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        Random random = new Random();
        int p = random.nextInt(100);
        long amount = Long.parseLong(args[0]);
        MongoCollection<Document> userdoc = MongoConnect.getUserStats();
        Document found = userdoc.find(new Document("id", event.getAuthor().getId())).first();
        if (found != null) {

        } else {

        }
    }
}
