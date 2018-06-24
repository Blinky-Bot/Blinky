package me.tsblock.Blinky.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.utils.CustomEmotes;
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
import java.util.Arrays;
import java.util.List;

public class payCommand extends Command {
    private Embed embed = new Embed();
    private MongoConnect mongoConnect = new MongoConnect();
    @Override
    public String getName() {
        return "pay";
    }

    @Override
    public String getDescription() {
        return "Pay an amount of dollars to someone";
    }

    @Override
    public String getUsage() { return "<amounts> <mention>"; }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("share", "give");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean ownerOnly() { return false; }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        if (Utils.isNaN(args[0]) || Long.parseLong(args[0]) <= 0) {
            embed.sendEmbed(args[0] + " is not a number", event.getChannel());
            return;
        }
        long payAmount = Long.parseLong(args[0]);
        String toPayUserID = msg.getMentionedUsers().get(0).getId();
        MongoCollection<Document> userStatsDoc = mongoConnect.getUserStats();
        Document foundToPayUser =  userStatsDoc.find(new Document("id", toPayUserID)).first();
        Document selfUserAmount = userStatsDoc.find(new Document("id", event.getAuthor().getId())).first();
        System.out.println(toPayUserID + " " + event.getAuthor().getId());
        if (toPayUserID.equals(user.getId())) {
            embed.sendEmbed("Why would you trying to pay to yourself? Trying to duplicate money, huh?", event.getChannel());
            return;
        }
        if (foundToPayUser != null && selfUserAmount != null) {
            if (payAmount > selfUserAmount.getLong("balance")) {
                embed.sendEmbed("You don\'t have enough money to pay", event.getChannel());
                return;
            }
            long toPayNewAmount = foundToPayUser.getLong("balance")  + payAmount;
            long selfUserNewAmount = selfUserAmount.getLong("balance") - payAmount;
            Bson updatedTarget = new Document("balance", toPayNewAmount);
            Bson targetOperation = new Document("$set", updatedTarget);
            Bson updatedSelf = new Document("balance", selfUserNewAmount);
            Bson selfOperation = new Document("$set", updatedSelf);
            userStatsDoc.updateOne(foundToPayUser, targetOperation);
            userStatsDoc.updateOne(selfUserAmount, selfOperation);
            MessageEmbed success = new EmbedBuilder()
                    .setTitle(CustomEmotes.tick + " Payment Success")
                    .setDescription("You now have " + "$" + selfUserNewAmount + "\nand they have got " + "$" + toPayNewAmount)
                    .setColor(Color.green)
                    .build();
            event.getChannel().sendMessage(success).queue();
        } else {
            embed.sendEmbed("You or the targeted user did not create a wallet, please run b.balance to create a wallet.", event.getChannel());
        }

    }
}