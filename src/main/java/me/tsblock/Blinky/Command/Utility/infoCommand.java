package me.tsblock.Blinky.Command.Utility;

import com.mongodb.Mongo;
import com.mongodb.client.MongoClient;
import jdk.nashorn.internal.runtime.Version;
import me.tsblock.Blinky.Bot;
import me.tsblock.Blinky.Command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

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
        long guilds = jda.getGuilds().size();
        long users = jda.getUsers().size();
        long channels = jda.getTextChannels().size() + jda.getVoiceChannels().size();
        String jvm_version = System.getProperty("java.version");
        String jda_version = jda.getClass().getPackage().getImplementationVersion();
        //String mongodb_version; //broke lmafo

        StringBuilder statistics = new StringBuilder()
                .append("**Guilds: ** `" + guilds + "`\n")
                .append("**Users: ** `" + users + "`\n")
                .append("**Channels: **`" + channels + "`");
        StringBuilder versions = new StringBuilder()
                .append("**Java:** `" + jvm_version + "`\n")
                .append("**JDA:** `" + jda_version + "`\n");
                //.append("**MongoDB Driver:** `" + mongodb_version + "`");
        MessageEmbed info = new EmbedBuilder()
                .setAuthor(jda.getSelfUser().getName(), jda.getSelfUser().getAvatarUrl())
                .setDescription("**A general-purpose Discord bot, written in Java using JDA.**")
                .addField("Statistics", statistics.toString(), true)
                .addField("Dependencies Version", versions.toString(), true)
                .build();

        event.getChannel().sendMessage(info).queue();
    }
}
