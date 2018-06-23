package me.tsblock.Blinky.Command.Fun;

import me.tsblock.Blinky.Command.Command;
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
            EmbedBuilder meme = new EmbedBuilder()
                    .setTitle(GetBody.getMeme().getTitle())
                    .setImage(GetBody.getMeme().getImage_url())
                    .setFooter(GetBody.getMeme().getUpvotes() + "\uD83D\uDC4D" + " | https://api.ksoft.si/", null)
                    .setColor(Color.GREEN);
            msg.getChannel().sendMessage(meme.build()).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
