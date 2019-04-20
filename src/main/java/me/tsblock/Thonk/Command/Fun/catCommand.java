package me.tsblock.Thonk.Command.Fun;

import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.HTTPRequest.GetBody;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class catCommand extends Command {
    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public String getDescription() {
        return "Meow";
    }

    @Override
    public String getUsage() {
        return null;
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
    public boolean ownerOnly() {
        return false;
    }

    @Override
    public boolean needArgs() {
        return false;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        MessageEmbed cat = new EmbedBuilder()
                .setTitle("Meow! \uD83D\uDC31")
                .setImage(GetBody.getCatURL())
                .setFooter("Powered by http://random.cat/", null)
                .setColor(java.awt.Color.cyan)
                .build();
        event.getChannel().sendMessage(cat).queue();
    }
}
