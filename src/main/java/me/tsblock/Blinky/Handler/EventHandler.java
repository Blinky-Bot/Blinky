package me.tsblock.Blinky.Handler;

import me.tsblock.Blinky.Bot;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Bot.getCommandHandler().handle(event);
    }

}
