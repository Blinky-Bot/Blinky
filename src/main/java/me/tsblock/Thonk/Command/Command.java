package me.tsblock.Thonk.Command;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class Command {
    public String getName() {
        return null;
    }
    public String getDescription() {
        return "Not available";
    }
    public String getUsage() { return null; }
    public List<String> getAliases() {
        return Collections.emptyList();
    }
    public boolean enabled() { return false; }
    public boolean ownerOnly() { return false; }
    public boolean needArgs() { return false; }
    public long cooldown() { return 0; }
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) { event.getChannel().sendMessage("DUDE WHAT'S WRONG WITH YOU HOW CAN YOU FORGOT TO REMOVE THE LINE SMH").queue(); }
}
