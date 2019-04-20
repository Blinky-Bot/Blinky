package me.tsblock.Thonk.Command.Core;

import com.mongodb.client.MongoCollection;
import me.tsblock.Thonk.Command.Command;
import me.tsblock.Thonk.Database.MongoConnect;
import me.tsblock.Thonk.Settings.Settings;
import me.tsblock.Thonk.Settings.SettingsManager;
import me.tsblock.Thonk.utils.CustomEmotes;
import me.tsblock.Thonk.utils.Embed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class settingsCommand extends Command {
    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public String getDescription() {
        return "Change settings for server you currently in.";
    }

    @Override
    public String getUsage() {
        return "<setting> <value> \n";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("config", "cfg");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean needArgs() {
        return false;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        Settings settings = SettingsManager.getInstance().getSettings();
        if (!guild.getMember(user).getPermissions().contains(Permission.ADMINISTRATOR)) {
            Embed.sendEmbed("Sorry, you need Administrator permission to use this command", event.getChannel());
            return;
        }
        String key = "";
        String stringValue = "";
        if (args.length > 0) {
            key = args[0];
            stringValue = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }
        MongoCollection<Document> guildSettings = MongoConnect.getGuildSettings();
        Document found = guildSettings.find(new Document("id", guild.getId())).first();
        String prefix = found.getString("prefix") == null ? settings.getPrefix() : found.getString("prefix");
        String prefixDisplay = "▶ `" + prefix + "`";
        String welcomeChannel = found.getString("welcomeChannel") == null ? "▶ **None**" : "▶ " + guild.getTextChannelById(found.getString("welcomeChannel")).getAsMention();
        String welcomeMessage = found.getString("joinMessage") == null ? "▶ **None**" : "▶ `" + found.getString("welcomeMessage" + "`");
        String leaveMessage = found.getString("leaveMessage") == null ? "▶ **None**" : "▶ `" + found.getString("leaveMessage" + "`");
        String autoRole = found.getString("autoRole") == null ? "▶ **None**" : "▶ `" + found.getString("autoRole");
        String starboard = found.getString("starboard") == null ? "▶ **None**": "▶ " + guild.getTextChannelById(found.getString("starboard")).getAsMention();
        JSONArray starboardRoles = new JSONObject(found.toJson()).getJSONArray("starboardRoles");
        switch (key) {
            case "prefix":
                if (args[1].isEmpty()) {
                    Embed.sendEmbed("What is the prefix?", event.getChannel());
                    return;
                }
                if (args[1].length() > 3) {
                    Embed.sendEmbed("Prefix can not be more than 3 characters long.", event.getChannel());
                    return;
                }
                MongoConnect.updateDocument(found, guildSettings, "prefix", args[1]);
                Embed.sendEmbed("Chaned prefix to " + args[1], event.getChannel());
                break;
            case "starboard":
                if (!msg.getMentionedChannels().isEmpty()) {
                    MongoConnect.updateDocument(found, guildSettings, "starboard", msg.getMentionedChannels().get(0).getId());
                    Embed.sendEmbed("Changed starboard channel to " + msg.getMentionedChannels().get(0).getAsMention(), event.getChannel());
                } else if (stringValue.equalsIgnoreCase("none")) {
                    MongoConnect.updateDocument(found, guildSettings, "starboard", null);
                    Embed.sendEmbed("Successfully turn off starboard.", event.getChannel());
                } else {
                    Embed.sendEmbed("Please mention a channel", event.getChannel());
                }
                break;
            case "starboardRoles":
                switch (args[1]) {
                    case "add":
                        if (!args[2].isEmpty()) {
                            if (!guild.getRolesByName(args[2], true).isEmpty()) {
                                if (JSONArrayContains(starboardRoles, guild.getRolesByName(args[2], true).get(0).getId())) {
                                    Embed.sendEmbed("Role already in list!", event.getChannel());
                                    return;
                                }
                                MongoConnect.updateDocument(found, guildSettings, "starboardRoles", starboardRoles.put(guild.getRolesByName(args[2], true).get(0).getId()));
                                Embed.sendEmbed("Added role " + args[2], event.getChannel());
                            } else {
                                Embed.sendEmbed("Role not found.", event.getChannel());
                            }
                        } else {
                            Embed.sendEmbed("What role?", event.getChannel());
                        }
                        break;
                    case "remove":
                        if (!args[2].isEmpty()) {
                            if (JSONArrayContains(starboardRoles, guild.getRolesByName(args[2], true).get(0).getId())) {
                                //SORRY I KNOW THIS IS A BAD CODE PLEASE FORGIVE ME
                                for (int i=0;i<starboardRoles.length();i++) {
                                    if (starboardRoles.getString(i).equals(guild.getRolesByName(args[2], true).get(0).getId())) starboardRoles.remove(i);
                                }
                                MongoConnect.updateDocument(found, guildSettings, "starboardRoles", starboardRoles);
                                Embed.sendEmbed("Removed role " + args[2], event.getChannel());
                            } else {
                                Embed.sendEmbed("Role not found.", event.getChannel());
                            }
                        } else {
                            Embed.sendEmbed("What role?", event.getChannel());
                        }
                }
                break;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                for (int i=0;i<starboardRoles.length();i++) stringBuilder.append(guild.getRoleById(starboardRoles.getString(i)).getAsMention() + " ");
                if (stringBuilder.length() < 1) stringBuilder.append("None (anyone can star message)");
                EmbedBuilder _default = new EmbedBuilder()
                        .setAuthor("Settings of " + guild.getName(), null,"https://i.imgur.com/gJNLEXQ.png")
                        .setColor(Color.cyan)
                        .setDescription("Use `b.settings <setting> none` to disable the setting")
                        .addField("Prefix", "`b.settings prefix <prefix>`\n" + prefixDisplay, false)
//                        .addField("Join/Leave Text Channel `b.settings welcomeChannel <channel>` " + getStatus(welcomeChannel), welcomeChannel, false)
//                        .addField("Join Message `b.settings joinMessage <message>` " + getStatus(welcomeMessage), welcomeMessage, false)
//                        .addField("Leave Message `b.settings leaveMessage <message>` " + getStatus(leaveMessage), leaveMessage, false)
//                        .addField("Auto role on join `b.settings autoRole <role name>` " + getStatus(autoRole), autoRole, false)
                        .addField("Starboard Channel `b.settings starboard <channel>` " + getStatus(starboard), starboard, false);
                if (!starboard.equals("▶ **None**")) _default.addField("Starboard Roles", "\n`b.settings starboardRoles add <role>`\n`b.settings starboardRoles remove <role>`\n" + "▶ " + stringBuilder.toString(), false);
                event.getChannel().sendMessage(_default.build()).queue();
        }
    }

    private String getStatus(String key) {
        return key == "▶ **None**" ? CustomEmotes.off : CustomEmotes.on;
    }

    private boolean JSONArrayContains(JSONArray array, String value) {
        return array.toString().contains("\"" + value + "\"");
    }

}
