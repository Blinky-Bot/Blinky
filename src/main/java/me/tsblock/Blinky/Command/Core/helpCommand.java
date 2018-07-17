package me.tsblock.Blinky.Command.Core;

import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import me.tsblock.Blinky.utils.CustomEmotes;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

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
        return "[command]";
    }

    @Override
    public String getDescription() {
        return "Seriously? You need to get information for a help command?!?!? What's wrong with you?";
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
        if (args.length > 0) {
            String toCheckCmd = args[0];
            for (Command cmd : Bot.getCommandHandler().getRegisteredCommands()) {
                if (cmd.getName().equalsIgnoreCase(toCheckCmd)) {
                    String name = cmd.getName();
                    String description = cmd.getDescription();
                    String usage = cmd.getUsage();
                    EmbedBuilder asdf = new EmbedBuilder()
                            .setTitle(setting.getPrefix() + name)
                            .setColor(Color.CYAN)
                            .addField("Description", "```" + description + "```", false);
                    if (usage != null) asdf.addField("Usage", "```" + setting.getPrefix() + name + " " + usage + "```", false);
                    if (!cmd.getAliases().isEmpty()) asdf.addField("Aliases", "```" + StringUtils.join(cmd.getAliases() , ", ") + "```", false);
                    if (cmd.cooldown() > 0) asdf.addField("Cooldown", cmd.cooldown() + " seconds", true);
                    event.getChannel().sendMessage(asdf.build()).queue();
                    return;
                }
            }
            return;
        }
        EmbedBuilder b = new EmbedBuilder()
                .setTitle("Help menu")
                .setColor(Color.CYAN)
                .setThumbnail("https://i.imgur.com/mG1JiGT.png")
                .setDescription("Type b.help [command name] to get more information about a command.\nUsage syntax: <required argument> [optional argument]");
        categories.forEach((cat, cmds) -> {
            StringBuilder line = new StringBuilder();
            for (Command cmd : cmds)
                line.append("`").append(cmd.getName()).append("`\n");

                b.addField(cat, line.toString(), true)
                        .setFooter("", null);
        });
        event.getAuthor().openPrivateChannel().queue(c-> {
            c.sendMessage(b.build()).queue(s-> event.getChannel().sendMessage("\uD83D\uDCE3 ***Sent to DM!***").queue(), err->{
                event.getChannel().sendMessage(CustomEmotes.cross + " **Cannot send DMs to you! Did you disable your DMs?**").queue();
            });
        });
    }
}
