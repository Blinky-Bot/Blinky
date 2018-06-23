package me.tsblock.Blinky.Command.Core;

import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.util.*;
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
        List<Command> commands = new ArrayList<>();
            Bot.getCommandHandler().getRegisteredCommands().forEach(c -> {
                commands.add(c);
                Class<? extends Command> clazz = c.getClass();
                String category = clazz.getPackage().getName();
                category = category.split("\\.")[category.split("\\.").length-1];
                List<Command> edit = categories.get(category);
                if (edit == null)
                    edit = new ArrayList<>();
                edit.add(c);
                categories.put(category, edit);
            });
        System.out.println(commands.size());
        if (args.length != 0) {
            String toCheckCmd = args[0];
            for (Command cmd : commands) {
                if (cmd.getAliases().contains(args[0])) {
                    String name = cmd.getName();
                    String description = cmd.getDescription();
                    String usage = cmd.getUsage();
                    String aliases = cmd.getAliases().toArray().toString();
                    EmbedBuilder asdf = new EmbedBuilder()
                            .setTitle(setting.getPrefix() + name)
                            .addField("Description", "```" + description, false);
                    if (cmd.needArgs()) asdf.addField("Usage", usage, false);
                    if (!cmd.getAliases().isEmpty()) asdf.addField("Aliases", aliases, false);
                    return;
                } else {
                    event.getChannel().sendMessage("Command not found, use b.help to list all commands").queue();
                    return;
                }
            }
        }
        EmbedBuilder b = new EmbedBuilder()
                .setTitle("Help menu")
                .setColor(Color.CYAN)
                .setDescription("Type b.help [command name] to get more information about a command.\nUsage syntax: {optional argument}, [required argument]");
        categories.forEach((cat, cmds) -> {
            StringBuilder line = new StringBuilder();
            for (Command cmd : cmds)
                line.append("`").append(cmd.getName()).append("`\n");

                b.addField(cat, line.toString(), true)
                        .setFooter("This is a temporary help menu, it will be much better in future", null);
        });
        event.getChannel().sendMessage(b.build()).queue();
    }
}
