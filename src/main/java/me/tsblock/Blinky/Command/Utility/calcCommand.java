package me.tsblock.Blinky.Command.Utility;

import me.tsblock.Blinky.Command.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class calcCommand extends Command {
    @Override
    public String getName() {
        return "calc";
    }

    @Override
    public String getDescription() {
        return "Evaluate maths expressions";
    }

    @Override
    public String getUsage() {
        return "<expressions>";
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
        String expression = String.join(" ", args);
        try {
            Expression e = new ExpressionBuilder(expression).build();
            event.getChannel().sendMessage("```" + e.evaluate() + "```").queue();
        } catch (IllegalArgumentException e) {
            event.getChannel().sendMessage("```" + e.getMessage() + "```").queue();
        }
    }
}
