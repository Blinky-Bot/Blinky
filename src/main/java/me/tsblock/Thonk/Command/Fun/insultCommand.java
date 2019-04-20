package me.tsblock.Thonk.Command.Fun;

import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.HTTPRequest.GetBody;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class insultCommand extends Command {
    @Override
    public String getName() {
        return "insult";
    }

    @Override
    public String getDescription() {
        return "Gets a random insult";
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
            event.getChannel().sendMessage(GetBody.run("https://insult.mattbas.org/api/insult")).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
