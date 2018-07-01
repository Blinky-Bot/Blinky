package me.tsblock.Blinky.Command.Tag;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class listtagCommand extends Command {
    @Override
    public String getName() {
        return "listtag";
    }

    @Override
    public String getDescription() {
        return "Get a list of your tag";
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
        MongoCollection<Document> tags = MongoConnect.getTags();
        List<Document> list = tags.find(new Document("id", user.getId())).into(new ArrayList<>());
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(document -> stringBuilder.append(document.getString("name")));
        String result = stringBuilder.toString();
        if (list.isEmpty()) result = "None";
        event.getChannel().sendMessage("```" + result + "```").queue();
    }
}
