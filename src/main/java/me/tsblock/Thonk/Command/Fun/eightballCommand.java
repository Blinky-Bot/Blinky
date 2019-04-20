package me.tsblock.Thonk.Command.Fun;

import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.HTTPRequest.GetBody;
import me.tsblock.Thonk.utils.Embed;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class eightballCommand extends Command {
    private Embed embed = new Embed();
    @Override
    public String getName() {
        return "8ball";
    }

    @Override
    public String getDescription() {
        return "Ask question to 8ball!";
    }

    @Override
    public String getUsage() {
        return "<question>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("8b", "eightball");
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
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        String question = String.join(" ", args);
        embed.sendEmbed("\uD83C\uDFB1 The 8ball answers you: " + GetBody.get8BallQuestion(question), event.getChannel());
    }
}
