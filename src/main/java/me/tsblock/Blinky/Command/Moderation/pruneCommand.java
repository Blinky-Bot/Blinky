package me.tsblock.Blinky.Command.Moderation;

import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.utils.Embed;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class pruneCommand extends Command {
    @Override
    public String getName() {
        return "prune";
    }

    @Override
    public String getDescription() {
        return "Bulk delete messages";
    }

    @Override
    public String getUsage() {
        return "<number>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("bulkdelete", "purge");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean ownerOnly() {
        return false;
    }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public long cooldown() {
        return 5;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        if (!guild.getMember(user).getPermissions().contains(Permission.MESSAGE_MANAGE)) {
            Embed.sendEmbed("You don't have permission to manage messages", msg.getChannel());
            return;
        }
        TextChannel channel = msg.getTextChannel();
        int toDelete = Integer.parseInt(args[0]);
        if (toDelete < 2) {
            Embed.sendEmbed("Please enter a value that is bigger than 2", msg.getChannel());
            return;
        } else if (toDelete > 10000) {
            Embed.sendEmbed("Value is too big!", msg.getChannel());
            return;
        }
        try {
            if (toDelete > 100) {
                channel.getHistory().retrievePast(100).queue((List<Message> retrieved) -> {
                    int idkhowicallthislol = toDelete % 100;
                    for (int i = 0; i<toDelete / 100;i++) {
                        channel.deleteMessages(retrieved).queue();
                        if (idkhowicallthislol > 1) {
                            channel.getHistory().retrievePast(idkhowicallthislol).queue((List<Message> retrieved2) -> channel.deleteMessages(retrieved2).queue());
                        }

                        System.out.println("ur mum gae " + i);
                    }
                });
                event.getChannel().sendMessage("Successfully deleted `" + toDelete + "` messages").queue(m -> m.delete().queueAfter(3, TimeUnit.SECONDS));
            } else {
                channel.getHistory().retrievePast(toDelete).queue((List<Message> retrieved) -> {
                    channel.deleteMessages(retrieved).queue();
                    event.getChannel().sendMessage("Successfully deleted `" + toDelete + "` messages").queue(m -> m.delete().queueAfter(3, TimeUnit.SECONDS));
                });
            }
        } catch (InsufficientPermissionException e) {
            Embed.sendEmbed("I don't have permission to manage messages", msg.getChannel());
        }
    }
}
