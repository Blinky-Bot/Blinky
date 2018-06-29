package me.tsblock.Blinky.Command.Moderation;

import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.utils.Embed;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class randombanCommand extends Command {
    @Override
    public String getName() {
        return "randomban";
    }

    @Override
    public String getDescription() {
        return "Randomly select member to ban!";
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
    public long cooldown() {
        return 5;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        List<Member> memberList = guild.getMembers();
        Member lolrekt = memberList.get(new Random().nextInt(memberList.size()));
        if(!guild.getMemberById(Bot.getJDA().getSelfUser().getId()).getPermissions().contains(Permission.BAN_MEMBERS)) {
            Embed.sendEmbed("Looks like I don't have permission to ban **" + lolrekt.getUser().getName() + "**.", msg.getChannel());
            return;
        } else if (!guild.getMember(user).getPermissions().contains(Permission.BAN_MEMBERS)) {
            Embed.sendEmbed("You don't have permission to ban members.", msg.getChannel());
            return;
        }
        try {
            guild.getController().ban(lolrekt, 0).queue();
        } catch (HierarchyException | InsufficientPermissionException e) {
            Embed.sendEmbed("Looks like I don't have permission to ban **" + lolrekt.getUser().getName() + "**.", msg.getChannel());
        }
    }
}
