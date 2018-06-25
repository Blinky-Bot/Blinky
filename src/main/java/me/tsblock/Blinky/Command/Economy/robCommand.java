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
        return "Robs other people to get cash fast!\nNote: You have only 7~10% chance to rob other.";
    }

    @Override
    public String getUsage() {
        return "<mention>";
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
    public long cooldown() {
        return 180;
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
        /**
         * DES
         * PAC
         * CITO
         * QUIERO RESPIRAR TU CUELLO DESPACITO
         * DEJA QUE TE DIGA COSAS AL OIDO
         *
         * PARA QUE TE ACUERDES SI NO ESTAS CONMIGO
         *
         * DES
         * PAC
         * CITO
         *
         * QUIERO DESNUDARTE A BESOS DESPACITO
         * FIRMO EN LAS PAREDES DE TU LABERINTO
         * Y HACER DE TU CUERPO TODO UN MANUSCRITO (SUBE, SUBE, SUBE)
         * (SUBE, SUBE)
         */
        int chance = new Random().nextInt(100) + 1;
        int successChance = new Random().nextInt(30) + 7;
        MongoCollection<Document> userdoc = MongoConnect.getUserStats();
        Document selfUser = userdoc.find(new Document("id", user.getId())).first();
        Document targetUser = userdoc.find(new Document("id", msg.getMentionedUsers().get(0).getId())).first();
        if (targetUser == null) {
            MongoConnect.initUserStats(msg.getMentionedUsers().get(0).getId());
        }
        if (selfUser == null) {
            MongoConnect.initUserStats(user.getId());
        }
        if (successChance > chance) {
            long robbedAmount = Math.round(targetUser.getLong("balance") * 0.6);
            MongoConnect.updateDocument(targetUser, userdoc, "balance", targetUser.getLong("balance") - robbedAmount);
            MongoConnect.updateDocument(selfUser, userdoc, "balance", selfUser.getLong("balance") + robbedAmount);
            long currentAmount = selfUser.getLong("balance") + robbedAmount;
            event.getChannel().sendMessage("You just robbed **$" + robbedAmount + "** :money_mouth: \nYou now have **$" +  currentAmount + ".**").queue();
            msg.getMentionedUsers().get(0).openPrivateChannel().queue(c-> c.sendMessage("You just been robbed by **" + user.getName() + "#" + user.getDiscriminator() + "** and lost **$" + robbedAmount + ".**").queue());
        } else {
            long loseAmount = Math.round(selfUser.getLong("balance") * 0.3);
            MongoConnect.updateDocument(selfUser, userdoc, "balance", selfUser.getLong("balance") - loseAmount);
            event.getChannel().sendMessage("Uh oh! You get caught by cops and fined you **$" + loseAmount + ".**").queue();
        }

    }
}
