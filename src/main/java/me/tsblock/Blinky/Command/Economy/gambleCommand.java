package me.tsblock.Blinky.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.utils.Embed;
import me.tsblock.Blinky.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;
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
    public long cooldown() {
        return 10;
    }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        Random random = new Random();
        int p = random.nextInt(100) + 1;
        long betAmount = Long.parseLong(args[0]);
        long winamount = betAmount * 2;
        MongoCollection<Document> userdoc = MongoConnect.getUserStats();
        Document found = userdoc.find(new Document("id", event.getAuthor().getId())).first();
        if (betAmount <= 0) {
            Embed.sendEmbed(betAmount + " is not a number u dumbass", event.getChannel());
            return;
        }
        if (found == null) MongoConnect.initUserStats(user.getId());
        if (found.getLong("balance") < betAmount) {
            Embed.sendEmbed("You bet too much!", event.getChannel());
            return;
        }
        if (p > 50) {
            Bson update = new Document("balance", found.getLong("balance") + winamount);
            Bson updateo = new Document("$set", update);
            userdoc.updateOne(found, updateo);
            MessageEmbed win = new EmbedBuilder()
                    .setDescription("Huzaah! You just won $" + winamount + "\nYou now have: $" + found.getLong("balance"))
                    .setColor(Color.GREEN)
                    .build();
            event.getChannel().sendMessage(win).queue();
        } else {
            Bson updatelose = new Document("balance", found.getLong("balance") - betAmount);
            Bson updateloseo = new Document("$set", updatelose);
            userdoc.updateOne(found, updateloseo);
            MessageEmbed lose = new EmbedBuilder()
                    .setDescription("Uh oh! You just lost $" + betAmount + "\nYou now have: $" + found.getLong("balance"))
                    .setColor(Color.RED)
                    .build();
            event.getChannel().sendMessage(lose).queue();
        }
    }
}
