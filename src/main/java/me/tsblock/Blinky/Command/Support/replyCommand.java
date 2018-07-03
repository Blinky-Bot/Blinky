package me.tsblock.Blinky.Command.Support;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;

public class replyCommand extends Command {
    @Override
    public String getName() {
        return "reply";
    }

    @Override
    public String getDescription() {
        return "replies a user suggestion";
    }

    @Override
    public String getUsage() {
        return "<case number> <message>";
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
        int _case = Integer.parseInt(args[0]);
        String message = String.join(" ", ArrayUtils.remove(args, 0));
        MongoCollection<Document> com = MongoConnect.getComplaints();
        Document found = com.find(new Document("case", _case)).first();
        if (found != null) {
            Bot.getJDA().getUserById(found.getString("userID")).openPrivateChannel().queue(c-> c.sendMessage("owner just replied you yay!!!1\n```" + message + "```").queue(), e-> event.getChannel().sendMessage("cant dm").queue());
            Bot.getJDA().getGuildById("418382390113861633").getTextChannelsByName("compliants", true).get(0).addReactionById(found.getString("messageID"), "✅").queue();
        } else {
            event.getChannel().sendMessage("case not found").queue();
        }
    }
}
