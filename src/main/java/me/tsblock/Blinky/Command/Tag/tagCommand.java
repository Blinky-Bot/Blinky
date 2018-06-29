package me.tsblock.Blinky.Command.Tag;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

public class tagCommand extends Command {
    @Override
    public String getName() {
        return "tag";
    }

    @Override
    public String getDescription() {
        return "View a tag";
    }

    @Override
    public String getUsage() {
        return "<tag name>";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public long cooldown() {
        return 3;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        String name = args[0];
        MongoCollection<Document> col = MongoConnect.getTags();
        if (col.find(new Document("name", name)).first() == null) {
            event.getChannel().sendMessage("Tag does not exists.").queue();
            return;
        }
        event.getChannel().sendMessage(MongoConnect.getTag(name)).queue();
    }
}
