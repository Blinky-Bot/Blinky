package me.tsblock.Thonk.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

public class Embed {
    public static void sendEmbed(String msg, MessageChannel channel) {
        MessageEmbed embed = new EmbedBuilder()
                .setDescription(msg)
                .setColor(Color.CYAN)
                .build();
        channel.sendMessage(embed).queue();
    }
}
