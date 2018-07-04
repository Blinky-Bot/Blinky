package me.tsblock.Blinky.Command.Fun;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.utils.CustomEmotes;
import me.tsblock.Blinky.utils.FidgetSpinnerWord;
import me.tsblock.Blinky.utils.Utils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.nullschool.util.DigitalRandom;
import org.bson.Document;

import java.util.*;

public class fidgetspinnerCommand extends Command {
    @Override
    public String getName() {
        return "fidgetspinner";
    }

    @Override
    public String getDescription() {
        return "Spin a fidget spinner and see how long does it spin!";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("spin");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        MongoCollection<Document> spinner = MongoConnect.getSpinners();
        Document found = spinner.find(new Document("userID", user.getId())).first();
        if (found == null) {
            int seconds = Utils.randomNumber(5, 140);
            spinner.insertOne(new Document("userID", user.getId()));
            event.getChannel().sendMessage(CustomEmotes.spinner + "You spun your " + FidgetSpinnerWord.words[new Random().nextInt(FidgetSpinnerWord.words.length)] + "! Let's see how long does it spin... ").queue(m-> {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        event.getChannel().sendMessage(user.getAsMention() + "**, your spinner spins for " + seconds + " seconds!**").queue();
                        spinner.deleteOne(new Document("userID", user.getId()));
                    }
                }, seconds * 1000);
            });
        } else {
            event.getChannel().sendMessage("You already have a spinner spinning!").queue();
        }
    }


}
