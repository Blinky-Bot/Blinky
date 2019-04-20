package me.tsblock.Thonk.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.Database.MongoConnect;
import me.tsblock.Thonk.utils.Embed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class balanceCommand extends Command {
    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Check your wallet\'s balance";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("bal", "money");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean needArgs() {
        return false;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        String toCheckId = user.getId();
        if (!msg.getMentionedUsers().isEmpty()) {
            toCheckId = msg.getMentionedUsers().get(0).getId();
        }
        MongoCollection<Document> userStatsCollection = MongoConnect.getUserStats();
        Document userStats = new Document("id", toCheckId);
        Document found = userStatsCollection.find(userStats).first();
        if (found == null) {
            MongoConnect.initUserStats(toCheckId);
            found = userStatsCollection.find(userStats).first();
            sendBalanceEmbed(event, guild, toCheckId, found);
        } else {
            sendBalanceEmbed(event, guild, toCheckId, found);
        }
    }
    private void sendBalanceEmbed(GuildMessageReceivedEvent event, Guild guild, String toCheckId, Document found) {
        MessageEmbed messageEmbed = new EmbedBuilder()
                .setAuthor(guild.getMemberById(toCheckId).getUser().getName() + "\'s wallet", null, guild.getMemberById(toCheckId).getUser().getAvatarUrl())
                .addField("\uD83D\uDCB0 Balance \uD83D\uDCB0", "$" + String.format("%,d", found.getLong("balance")), false) //ez format ez trick
                .setColor(Color.GREEN)
                .build();
        event.getChannel().sendMessage(messageEmbed).queue();
    }
}
