package me.tsblock.Blinky.Command.Fun;

import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.HTTPRequest.GetBody;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class dadjokesCommand extends Command {
    @Override
    public String getName() {
        return "dadjokes";
    }

    @Override
    public String getDescription() {
        return "Ranmdom dad jokes";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
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
        return false;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        try {
            msg.getChannel().sendMessage(GetBody.getDadJokes()).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
