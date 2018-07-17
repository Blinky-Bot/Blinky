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

import java.util.Arrays;

public class addCommand extends Command {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Add a tag";
    }

    @Override
    public String getUsage() {
        return "<tag name> <description>";
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
        return 10;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        String name = args[0];
        String content = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        MongoCollection<Document> col = MongoConnect.getTags();
        if (col != null) {
            if (col.find(new Document("name", name)).first() != null) {
                Embed.sendEmbed("Tag already exists.", msg.getChannel());
                return;
            }
            if (content.isEmpty()) {
                Embed.sendEmbed("Where is the content?", msg.getChannel());
                return;
            }
            MongoConnect.addTag(name, content, user.getId());
            Embed.sendEmbed("Successfully created tag", msg.getChannel());
        }
    }

}
