package me.tsblock.Thonk.Command.Moderation;

import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.utils.CustomEmotes;
import me.tsblock.Thonk.utils.Embed;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class kickCommand extends Command {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kick a member";
    }

    @Override
    public String getUsage() {
        return "<mention or name>";
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
        return true;
    }

    @Override
    public long cooldown() {
        return 5;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        GuildController controller = guild.getController();
        Member target = msg.getMentionedMembers().isEmpty() ? null : msg.getMentionedMembers().get(0);
        if(target == null){
            Embed.sendEmbed(CustomEmotes.cross + " **No member mentioned**", event.getChannel());
            return;
        }

        if(!event.getMember().hasPermission(Permission.KICK_MEMBERS) || !event.getMember().canInteract(target)){
            Embed.sendEmbed(CustomEmotes.cross + " You do not have permission to kick this member", event.getChannel());
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        controller.kick(target, reason.isEmpty() ? "No reason provided" : reason).queue(
                success -> Embed.sendEmbed(CustomEmotes.tick + " **Successfully kicked " + target.getUser().getName() + "#" + target.getUser().getDiscriminator() + ".**", event.getChannel()),
                fail -> Embed.sendEmbed(CustomEmotes.cross + " **Unable to kick " + target.getUser().getName() + "#" + target.getUser().getDiscriminator() + ".**", event.getChannel())
        );
    }
}
