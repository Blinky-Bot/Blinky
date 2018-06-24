package me.tsblock.Blinky.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class robCommand extends Command {
    @Override
    public String getName() {
        return "rob";
    }

    @Override
    public String getDescription() {
        return "Robs other people to get ";
    }

    @Override
    public String getUsage() {
        return "<amount> <mention>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("steal");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean ownerOnly() {
        return super.ownerOnly();
    }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        int chance = new Random().nextInt(100) + 1;
        MongoCollection<Document> userdoc = MongoConnect.getUserStats();
        long robAmount = Long.parseLong(args[0]);
        long balance;
    }
}
