package me.tsblock.Blinky;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import me.tsblock.Blinky.Command.Administrator.evalCommand;
import me.tsblock.Blinky.Command.Administrator.forceCreateWalletCommand;
import me.tsblock.Blinky.Command.Administrator.sayCommand;
import me.tsblock.Blinky.Command.Administrator.shutdownCommand;
import me.tsblock.Blinky.Command.Core.helpCommand;
import me.tsblock.Blinky.Command.Core.levelCommand;
import me.tsblock.Blinky.Command.Core.pingCommand;
import me.tsblock.Blinky.Command.Core.uptimeCommand;
import me.tsblock.Blinky.Command.Economy.*;
import me.tsblock.Blinky.Command.Fun.*;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.Handler.MessageHandler;
import me.tsblock.Blinky.Handler.EventHandler;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import ml.duncte123.CleverBot4J.CleverbotAPI;
import ml.duncte123.CleverBot4J.CleverbotBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.UUID;

public class Bot {
    //set logger's level to error only
    static Logger root = (Logger) LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME);

    static {
        root.setLevel(Level.ERROR);
    }

    private static MessageHandler messageHandler = new MessageHandler();
    public static MessageHandler getMessageHandler() {
        return messageHandler;
    }
    public static MongoConnect mongoConnect = new MongoConnect();
    public static JDA jda;


    public static CleverbotAPI cleverbot = new CleverbotBuilder()
            .setKeys("oxXo0mB7KRlNJ8An", "gg7ANyrFCbb52RbxzxcLiOc4BzqJJ3mQ")
            .setNickname(UUID.randomUUID().toString())
            .build();
    private static Settings settings = SettingsManager.getInstance().getSettings();
    public static void main(String[] args) {
        mongoConnect.connect();
        try {
            registerCommands();
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(settings.getBotSecret())
                    .setGame(Game.of(GameType.WATCHING, settings.getPrefix()))
                    .addEventListener(new EventHandler())
                    .buildBlocking();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static JDA getJDA() { return jda; }
    private static void registerCommands() {
        messageHandler.register(new pingCommand());
        messageHandler.register(new evalCommand());
        messageHandler.register(new shutdownCommand());
        messageHandler.register(new balanceCommand());
        messageHandler.register(new hackdollarsCommand());
        messageHandler.register(new payCommand());
        messageHandler.register(new forceCreateWalletCommand());
        messageHandler.register(new sayCommand());
        messageHandler.register(new catCommand());
        messageHandler.register(new levelCommand());
        messageHandler.register(new eightballCommand());
        messageHandler.register(new gambleCommand());
        messageHandler.register(new helpCommand());
        messageHandler.register(new insultCommand());
        messageHandler.register(new dadjokesCommand());
        messageHandler.register(new uptimeCommand());
        messageHandler.register(new dailyCommand());
        messageHandler.register(new memeCommand());
        messageHandler.register(new urbanCommand());
        messageHandler.register(new robCommand());
    }
}
