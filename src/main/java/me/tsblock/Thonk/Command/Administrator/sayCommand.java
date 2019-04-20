package me.tsblock.Thonk.Command.Administrator;

import me.tsblock.Thonk.Command.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class sayCommand extends Command {
    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getDescription() {
        return "Make the bot say something";
    }

    @Override
    public String getUsage() {
        return "<message>";
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
        return true;
    }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        msg.delete().queue();
        event.getChannel().sendMessage(String.join(" ", args)).queue();
    }
}
