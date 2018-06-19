package me.tsblock.Blinky.Command.Core;

import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class helpCommand extends Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getUsage() {
        return "[category or command]";
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
        Settings setting = SettingsManager.getInstance().getSettings();
        HashMap<String, List<Command>> categories = new HashMap<>();
            Bot.getCommandHandler().getRegisteredCommands().forEach(c -> {
                Class<? extends Command> clazz = c.getClass();
                String category = clazz.getPackage().getName();
                category = category.split("\\.")[category.split("\\.").length-1];
                List<Command> edit = categories.get(category);
                if (edit == null)
                    edit = new ArrayList<>();
                edit.add(c);
                categories.put(category, edit);
            });
        EmbedBuilder b = new EmbedBuilder()
                .setTitle("Help menu")
                .setColor(Color.CYAN);
        categories.forEach((cat, cmds) -> {
            StringBuilder line = new StringBuilder();
            for (Command cmd : cmds)
                line.append("`").append(cmd.getName()).append("`\n");
                b.addField(cat, line.toString(), true)
                        .setFooter("This is a tempory help menu, it will be much better in future", null);
        });
        event.getChannel().sendMessage(b.build()).queue();
    }
}
