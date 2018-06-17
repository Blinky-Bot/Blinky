package me.tsblock.Blinky.Command.Core;

import me.tsblock.Blinky.Command.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class helpCommand extends Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Get a list of available commands";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("commands", "cmds", "h");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
//        HashMap<String, List<Command>> categories = new HashMap<>();
//        Bot.getCommandHandler().getRegisteredCommands().forEach(c -> {
//            Class<? extends Command> _class = c.getClass();
//            String category = _class.getPackage().getName();
//            category = category.split("\\.")[category.split("\\.").length - 1];
//            List<Command> edi
    }
}
