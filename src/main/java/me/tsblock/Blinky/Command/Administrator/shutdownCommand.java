package me.tsblock.Blinky.Command.Administrator;

import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.utils.Embed;
import me.tsblock.Blinky.utils.ExitCodes;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class shutdownCommand extends Command {
    private Embed embed = new Embed();
    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public String getDescription() {
        return "Shutdown the bot";
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
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        embed.sendEmbed("***Shutting down...***", event.getChannel());
        System.exit(ExitCodes.NORMAL_SHUTDOWN);
    }
}
