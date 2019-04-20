package me.tsblock.Thonk.Command.Utility;

import me.tsblock.Thonk.Command.Command;
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
        long days = TimeUnit.MILLISECONDS.toDays(ms);
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms));
        StringBuilder stringBuilder = new StringBuilder()
                .append(days + " days, ")
                .append(hours + " hours, ")
                .append(minutes + " minutes, ")
                .append(seconds + " seconds.");
        for (int i=0;i<days;i++) {
            stringBuilder.append("<:lol:429593322902913024>");
        }
        for (int i=0;i<((int)hours/10);i++) {
            stringBuilder.append("\uD83D\uDE02");
        }
        for (int i=0;i<((int)minutes/10);i++) {
            stringBuilder.append("\uD83D\uDE24");
        }
        for (int i=0;i<((int)seconds/10);i++) {
            stringBuilder.append("\uD83D\uDE0F");
        }
        msg.getChannel().sendMessage(stringBuilder.toString()).queue();
    }
}
