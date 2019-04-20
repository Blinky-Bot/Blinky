package me.tsblock.Thonk.Command.Core;

import me.tsblock.Thonk.Bot;
import me.tsblock.Thonk.Command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class pingCommand extends Command {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "PONG";
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
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String[] args) {
        long beforeMillis = System.currentTimeMillis();
        msg.getChannel().sendMessage(".").queue(m -> {
            long currentMillis = System.currentTimeMillis() - beforeMillis;
            EmbedBuilder ping = new EmbedBuilder()
                    .addField(":stopwatch:  Roundtrip", currentMillis + "ms", true)
                    .addField(":heartpulse: Heartbeat", Bot.getJDA().getPing() + "ms", true)
                    .setColor(new Color(50, 54, 59));
            m.editMessage(ping.build()).override(true).queue();
        });
    }
}
