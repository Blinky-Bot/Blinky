package me.tsblock.Blinky.Command.Core;

import me.tsblock.Blinky.Command.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class uptimeCommand extends Command {
    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public String getDescription() {
        return "Get uptime of the bot";
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
    public boolean needArgs() {
        return false;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        long ms = ManagementFactory.getRuntimeMXBean().getUptime();
        String format = String.format("%02d days, %02d hours, %02d minutes, %02d seconds",
                TimeUnit.MILLISECONDS.toDays(ms),
                TimeUnit.MILLISECONDS.toHours(ms),
                TimeUnit.MILLISECONDS.toMinutes(ms) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)),
                TimeUnit.MILLISECONDS.toSeconds(ms) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
        msg.getChannel().sendMessage(format).queue();
    }
}
