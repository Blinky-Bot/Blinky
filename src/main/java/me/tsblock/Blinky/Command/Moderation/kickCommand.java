package me.tsblock.Blinky.Command.Moderation;

import me.tsblock.Blinky.Command.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class kickCommand extends Command {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kick a memeber";
    }

    @Override
    public String getUsage() {
        return super.getUsage();
    }

    @Override
    public List<String> getAliases() {
        return super.getAliases();
    }

    @Override
    public boolean enabled() {
        return super.enabled();
    }

    @Override
    public boolean ownerOnly() {
        return super.ownerOnly();
    }

    @Override
    public boolean needArgs() {
        return super.needArgs();
    }

    @Override
    public long cooldown() {
        return 5;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {

    }
}
