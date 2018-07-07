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
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandHandler {
    private List<Command> registeredCmds = new ArrayList<>();
    private String[] notReplacedArgs;
    private Settings settings = SettingsManager.getInstance().getSettings();
    private Embed embed = new Embed();
    private String prefix = settings.getPrefix();
    private CustomEmotes emotes = new CustomEmotes();
    private Map<Command, Map<User, Date>> cooldownList = new HashMap<>();
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
                if (!cooldownList.containsKey(cmd)) cooldownList.put(cmd, new HashMap<>());
                if (notReplacedArgs[0].equalsIgnoreCase(a) && cmd.enabled()) {
                    String[] args = Arrays.copyOfRange(notReplacedArgs, 1, notReplacedArgs.length);
                    args = StringUtils.stripAll(args); //trim all the space
                    if(cmd.ownerOnly() && !event.getAuthor().getId().equals(settings.getOwnerID())  ) {
                        embed.sendEmbed("Sorry, but you need **Owner** permission to use this command", event.getChannel());
                        return;
                    }
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
                    //cooldown handling
                    if (cmd.cooldown() > 0) {
                        long now = new Date().getTime();
                        long cooldownAmount = cmd.cooldown() * 1000;
                        if (!cooldownList.get(cmd).containsKey(event.getAuthor())) {
                            cooldownList.get(cmd).put(event.getAuthor(), new Date());
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    cooldownList.remove(event.getAuthor());
                                }
                            }, TimeUnit.SECONDS.toMillis(cmd.cooldown()));
                        } else {
                            long expirationTime = cooldownList.get(cmd).get(event.getAuthor()).getTime() + cooldownAmount;
                            if (now < expirationTime) {
                                long timeLeft = TimeUnit.MILLISECONDS.toSeconds(expirationTime - now);
                                event.getChannel().sendMessage(new EmbedBuilder()
                                        .setTitle(CustomEmotes.cross + "Ratelimited!1")
                                        .setDescription("Wait `" + timeLeft + "` more seconds then try again.")
                                        .setColor(Color.red)
                                        .build()
                                ).queue();
                                return;
                            }
                        }
                    }
                    cmd.onExecute(event, event.getMessage(), event.getAuthor(), event.getGuild(), args);
                }
            });
        });
    }
    private void handleXP(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        Random random = new Random();
        int xp = random.nextInt(15) + 5;
        MongoCollection<Document> userdoc = MongoConnect.getUserLevels();
        Document found = userdoc.find(new Document("id", e.getAuthor().getId()).append("guildID", e.getGuild().getId())).first();
        if (found == null) {
            MongoConnect.initLevel(e.getAuthor().getId(), e.getGuild().getId());
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
                            .setDescription("**Congratulation! You have reached to level 100!**")
                            .setColor(Color.YELLOW)
                            .build();
                    e.getChannel().sendMessage(maxlevel).queue();
                    Bson updateflfl = new Document("maxLevel", true)
                            .append("level", 100)
                            .append("xp", 0);
                    Bson operation = new Document("$set", updateflfl);
                    userdoc.updateOne(found, operation);
                } else {
                    Bson updateLevel = new Document("level", nextlvl)
                        .append("xp", 0);
                    Bson updateOperation = new Document("$set", updateLevel);
                    userdoc.updateOne(found, updateOperation);
                    MessageEmbed levelup = new EmbedBuilder()
                            .setAuthor("Level Up!", null, "https://i.imgur.com/RLun06c.png")
                            .setDescription("New Level: " + nextlvl)
                            .setFooter(e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), e.getAuthor().getAvatarUrl())
                            .build();
                    e.getChannel().sendMessage(levelup).queue();
                }
            }
            if (!found.getBoolean("blocked")) {
                Bson updateXp = new Document("xp", newXP)
                        .append("blocked", true);
                Bson update = new Document("$set", updateXp);
                userdoc.updateOne(found, update);
                new Timer().schedule(new TimerTask() {
                @Override
                    public void run() {
                        Bson updatcc = new Document("blocked", false);
                        Bson update = new Document("$set", updatcc);
                        userdoc.updateOne(found, update);
                    }
                }, 60000);
            }
        }
    }


}
