package me.tsblock.Blinky.Command.Fun;

import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.HTTPRequest.APIJSONFormat.ksoft.reddit;
import me.tsblock.Blinky.HTTPRequest.GetBody;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.IOException;

public class memeCommand extends Command {
    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getDescription() {
        return "Get some spicy memes";
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
        try {
            reddit reddit = GetBody.getMeme();
            EmbedBuilder meme = new EmbedBuilder()
                    .setTitle(reddit.getTitle())
                    .setImage(reddit.getImage_url())
                    .setFooter(reddit.getUpvotes() + "\uD83D\uDC4D" + " | " + reddit.getSubreddit() + " | https://api.ksoft.si/", null)
                    .setColor(Color.GREEN);
            msg.getChannel().sendMessage(meme.build()).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}