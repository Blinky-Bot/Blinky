package me.tsblock.Thonk.Command.Support;

import com.mongodb.client.MongoCollection;
import me.tsblock.Thonk.Bot;
import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.Database.MongoConnect;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class complainCommand extends Command {
    @Override
    public String getName() {
        return "complain";
    }

    @Override
    public String getDescription() {
        return "Send message to bot owner so you can ̶s̶p̶a̶m̶ suggest something or send bug report";
    }

    @Override
    public String getUsage() {
        return "<message>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("suggest", "botreport");
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
    public long cooldown() {
        return 30;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        MongoCollection<Document> complaints = MongoConnect.getComplaints();
        StringBuilder stringBuilder = new StringBuilder()
                .append("**Case #" + MongoConnect.getLength("Complaints") + "**")
                .append("\n**From guild:** `" + guild.getName() + " (" + guild.getId() + ")`")
                .append("\n**User: **`" + user.getName() + "#" + user.getDiscriminator() + " (" + user.getId() + ")`\n")
                .append("**Message:**\n")
                .append("```")
                .append("\n" + String.join(" ", args))
                .append("\n```");
        Bot.getJDA().getGuildById("418382390113861633").getTextChannelsByName("compliants", true).get(0).sendMessage(stringBuilder.toString()).queue(m->{
            Document complaint = new Document("case", MongoConnect.getLength("Complaints") + 1)
                    .append("serverID", guild.getId())
                    .append("userID", user.getId())
                    .append("messageID", m.getId());
            complaints.insertOne(complaint);
            event.getChannel().sendMessage("Sent complain").queue();
        });
    }
}
