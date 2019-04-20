package me.tsblock.Thonk.Command.Fun;

import me.tsblock.Thonk.Command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class lovecalculatorCommand extends Command {
    @Override
    public String getName() {
        return "lovecalculator";
    }

    @Override
    public String getDescription() {
        return "Calculator the love rate";
    }

    @Override
    public String getUsage() {
        return "<first person> <second person>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("lovecalc");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        String first = args[0];
        String second = args[1];
        if (!msg.getMentionedUsers().isEmpty()) {
            first = msg.getMentionedUsers().get(0).getName();
            second = msg.getMentionedUsers().get(1).getName();
        }
        Calendar c = Calendar.getInstance();
        String result_s = first + second + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + String.valueOf(c.get(Calendar.MONTH)) + String.valueOf(c.get(Calendar.YEAR));
        result_s = result_s.toLowerCase();
        result_s = result_s.trim();

        int seed = result_s.hashCode();
        Random r = new Random(seed);
        int rate = r.nextInt(100) + 1;
        if (first.equalsIgnoreCase("pancake") && second.equalsIgnoreCase("sumi")) rate = 100; //for bleach nation, great meme ryan
        int rateDivied = rate / 10;
        StringBuilder stringBuilder = new StringBuilder()
                .append("⏬   `" + first + "`\n")
                .append("⏫   `" + second + "`\n")
                .append("\n**" + rate + "%** ");
        for (int i=0;i<rateDivied;i++) {
            stringBuilder.append("[█](https://google.com)");
        }
        for (int i=0;i<10-rateDivied;i++) {
            stringBuilder.append("█");
        }
        MessageEmbed love = new EmbedBuilder()
                .setTitle("\uD83D\uDC9F Love Calculator \uD83D\uDC9F")
                .setDescription(stringBuilder.toString())
                .setColor(Color.GREEN)
                .build();
        event.getChannel().sendMessage(love).queue();
    }
}
