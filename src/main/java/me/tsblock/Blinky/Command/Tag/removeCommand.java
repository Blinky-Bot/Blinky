package me.tsblock.Blinky.Command.Tag;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.utils.Embed;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

public class removeCommand extends Command {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove a tag you own.";
    }

    @Override
    public String getUsage() {
        return "<tag to remove>";
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
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        String name = args[0];
        MongoCollection<Document> col = MongoConnect.getTags();
        if (col.find(new Document("name", name)).first() == null) {
            Embed.sendEmbed("Tag does not exists.", msg.getChannel());
            return;
        }
        if (!user.getId().equals(col.find(new Document("name", name)).first().getString("id"))) {
            Embed.sendEmbed("You don't own the tag", msg.getChannel());
            return;
        }
        MongoConnect.removeTag(name, user.getId());
        Embed.sendEmbed("Successfully removed tag.", msg.getChannel());
    }
}
