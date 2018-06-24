package me.tsblock.Blinky.Handler;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import me.tsblock.Blinky.utils.CustomEmotes;
import me.tsblock.Blinky.utils.Embed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageType;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CommandHandler {
    private List<Command> registeredCmds = new ArrayList<>();
    private String[] notReplacedArgs;
    private Settings settings = SettingsManager.getInstance().getSettings();
    private Embed embed = new Embed();
    private String prefix = settings.getPrefix();
    private CustomEmotes emotes = new CustomEmotes();
    private MongoConnect mongoConnect = new MongoConnect();
    public List<Command> getRegisteredCommands() {
        return registeredCmds;
    }
    public void register(Command cmd) {
        registeredCmds.add(cmd);
    }
    public void handle(GuildMessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith(prefix)) handleXP(event);
        if (!event.getMessage().getContentRaw().startsWith(prefix) || !event.getMessage().getType().equals(MessageType.DEFAULT) || event.getAuthor().isBot()) return;
        String[] notReplacedArgs = event.getMessage().getContentRaw().replaceFirst(prefix, "").split(" ");
        registeredCmds.forEach(cmd -> {
            List<String> aliases = new ArrayList<>(cmd.getAliases());
            aliases.add(cmd.getName());
            aliases.forEach(a -> {
                if (notReplacedArgs[0].equalsIgnoreCase(a) && cmd.enabled()) {
                    if(cmd.ownerOnly() && !event.getAuthor().getId().equals(settings.getOwnerID())  ) {
                        embed.sendEmbed("Sorry, but you need **Owner** permission to use this command", event.getChannel());
                        return;
                    }
                    String[] args = Arrays.copyOfRange(notReplacedArgs, 1, notReplacedArgs.length);
                    args = StringUtils.stripAll(args); //trim all the space
                    if (ArrayUtils.isEmpty(args) && cmd.needArgs()) {
                        String usage = "Correct Usage: `" + prefix + cmd.getName() + " " + cmd.getUsage() + "`";
                        if (usage.equals(null)) {
                            usage = "Not available";
                        }
                        MessageEmbed missingArguments = new EmbedBuilder()
                                .setTitle(emotes.cross + " Missing Arguments ")
                                .setDescription(usage)
                                .setColor(Color.red)
                                .build();
                        event.getChannel().sendMessage(missingArguments).queue();
                        return;
                    }
                    try {
                        cmd.onExecute(event, event.getMessage(), event.getAuthor(), event.getGuild(), args);
                    } catch (Error err) {
                        event.getChannel().sendMessage("Something went wrong while executing this command! \n " + "`" + err.getMessage() + "`" + "\nContact tsb#6722 with this error!").queue();
                    }
                }
            });
        });
    }
    private void handleXP(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        Random random = new Random();
        int xp = random.nextInt(10) + 1;
        String id = e.getAuthor().getId();
        MongoCollection<Document> userdoc = mongoConnect.getUserLevels();
        Document toFind = new Document("id", id);
        toFind.append("guildID", e.getGuild().getId());
        Document found = userdoc.find(toFind).first();
        if (found == null) {
            mongoConnect.initLevel(id, e.getGuild().getId());
        } else {
            int currentXP = found.getInteger("xp");
            int newXP = currentXP + xp;
            int nextLevel = found.getInteger("level") * 300;
            if (currentXP >= nextLevel) {
                int nextlvl = found.getInteger("level") + 1;
                if (found.getBoolean("maxLevel")) return;
                if (nextlvl == 100) {
                    MessageEmbed maxlevel = new EmbedBuilder()
                            .setTitle("\uD83C\uDF89")
                            .setDescription("Congratulation! You have reached to level 100!")
                            .setColor(Color.YELLOW)
                            .build();
                    e.getChannel().sendMessage(maxlevel).queue();
                    Bson updateflfl = new Document("maxLevel", true)
                            .append("level", 100)
                            .append("xp", 0);
                    Bson operation = new Document("$set", updateflfl);
                    userdoc.updateOne(found, operation);
                    return;
                }
                Bson updateLevel = new Document("level", nextlvl)
                        .append("xp", 0);
                Bson updateOperation = new Document("$set", updateLevel);
                userdoc.updateOne(found, updateOperation);
                MessageEmbed levelup = new EmbedBuilder()
                        .setAuthor("Level Up!", null, "https://i.imgur.com/RLun06c.png")
                        .setDescription("New Level: " + nextlvl)
                        .setFooter(e.getAuthor().getName() + e.getAuthor().getDiscriminator(), null)
                        .build();
                e.getChannel().sendMessage(levelup).queue();
                return;
            }
            Bson updateXp = new Document("xp", newXP);
            Bson update = new Document("$set", updateXp);
            userdoc.updateOne(found, update);
        }
    }
}
