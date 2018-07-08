package me.tsblock.Blinky.Command.Utility;

import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class infoCommand extends Command {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Information about this bot";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        JDA jda = Bot.getJDA();
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long guilds = jda.getGuilds().size();
        long users = jda.getUsers().size();
        long channels = jda.getTextChannels().size() + jda.getVoiceChannels().size();
        long allocatedMemory = runtime.totalMemory() / 1000000;
        long freeMemory = runtime.freeMemory() / 1000000;
        long processCpuLoad = Math.round(osBean.getProcessCpuLoad() * 100);
        long systemCpuLoad = Math.round(osBean.getSystemCpuLoad() * 100);

        String jvm_version = System.getProperty("java.version");
        String jda_version = "3.6.0_376";
        String mongodb_version = "3.8.0";

        StringBuilder statistics = new StringBuilder()
                .append("**Guilds: ** `" + guilds + "`\n")
                .append("**Users: ** `" + users + "`\n")
                .append("**Channels: **`" + channels + "`");
        StringBuilder versions = new StringBuilder()
                .append("**Java:** `" + jvm_version + "`\n")
                .append("**JDA:** `" + jda_version + "`\n")
                .append("**MongoDB Driver:** `" + mongodb_version + "`");
        StringBuilder system = new StringBuilder()
                .append("**Process CPU Usage:** `" + processCpuLoad + "%`\n")
                .append("**System CPU Usage:** `" + systemCpuLoad + "%`\n")
                .append("**Memory Usage:** `" + freeMemory + "mb / " + allocatedMemory + "mb`");
        MessageEmbed info = new EmbedBuilder()
                .setAuthor(jda.getSelfUser().getName(), jda.getSelfUser().getAvatarUrl())
                .setDescription("**A general-purpose Discord bot, written in Java using JDA.**")
                .addField("Bot Statistics", statistics.toString(), true)
                .addField("System Statistics", system.toString(), true)
                .addField("Dependencies Version", versions.toString(), true)
                .build();
        event.getChannel().sendMessage(info).queue();
    }
}
