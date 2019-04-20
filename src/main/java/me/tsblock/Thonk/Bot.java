package me.tsblock.Thonk;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import me.tsblock.Thonk.Command.Administrator.evalCommand;
import me.tsblock.Thonk.Command.Administrator.forceCreateWalletCommand;
import me.tsblock.Thonk.Command.Administrator.sayCommand;
import me.tsblock.Thonk.Command.Administrator.shutdownCommand;
import me.tsblock.Thonk.Command.Core.helpCommand;
import me.tsblock.Thonk.Command.Core.settingsCommand;
import me.tsblock.Thonk.Command.Core.pingCommand;
import me.tsblock.Thonk.Command.Moderation.banCommand;
import me.tsblock.Thonk.Command.Moderation.kickCommand;
import me.tsblock.Thonk.Command.Utility.calcCommand;
import me.tsblock.Thonk.Command.Utility.infoCommand;
import me.tsblock.Thonk.Command.Utility.uptimeCommand;
import me.tsblock.Thonk.Command.Economy.*;
import me.tsblock.Thonk.Command.Fun.*;
import me.tsblock.Thonk.Command.Moderation.pruneCommand;
import me.tsblock.Thonk.Command.Moderation.randombanCommand;
import me.tsblock.Thonk.Command.Tag.*;
import me.tsblock.Thonk.Database.MongoConnect;
import me.tsblock.Thonk.Handler.CommandHandler;
import me.tsblock.Thonk.Handler.EventHandler;
import me.tsblock.Thonk.Handler.StarboardHandler;
import me.tsblock.Thonk.Settings.Settings;
import me.tsblock.Thonk.Settings.SettingsManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    //set logger's level to error only
    private static Logger root = (Logger) LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME);

    static {
        root.setLevel(Level.WARN);
    }

    private static CommandHandler commandHandler = new CommandHandler();
    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }
    public static JDA jda;
    private static Settings settings = SettingsManager.getInstance().getSettings();
    private static MongoConnect mongoConnect = new MongoConnect();
    public static void main(String[] args) {
        mongoConnect.connect();
        try {
            registerCommands();
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(settings.getBotSecret())
                    .setGame(Game.of(GameType.DEFAULT, settings.getPrefix() + "help"))
                    .addEventListener(new EventHandler())
                    .addEventListener(new StarboardHandler())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static JDA getJDA() { return jda; }
    private static void registerCommands() {
        //Administrator
        commandHandler.register(new evalCommand());
        commandHandler.register(new forceCreateWalletCommand());
        commandHandler.register(new sayCommand());
        commandHandler.register(new shutdownCommand());
        //Core
        commandHandler.register(new helpCommand());
        commandHandler.register(new pingCommand());
        commandHandler.register(new settingsCommand());
        //Economy
        commandHandler.register(new balanceCommand());
        commandHandler.register(new dailyCommand());
        commandHandler.register(new gambleCommand());
        commandHandler.register(new hackdollarsCommand());
        commandHandler.register(new payCommand());
        commandHandler.register(new robCommand());
        //Fun
        commandHandler.register(new catCommand());
        commandHandler.register(new dadjokesCommand());
        commandHandler.register(new eightballCommand());
        //commandHandler.register(new insultCommand()); //it's a bit of nsfw
        commandHandler.register(new memeCommand());
        commandHandler.register(new urbanCommand());
        commandHandler.register(new lovecalculatorCommand());
        commandHandler.register(new fidgetspinnerCommand());
        //Leveling
        //commandHandler.register(new levelCommand());
        //Moderation
        commandHandler.register(new banCommand());
        commandHandler.register(new kickCommand());
        commandHandler.register(new randombanCommand());
        commandHandler.register(new pruneCommand());
        //Support
//        commandHandler.register(new complainCommand());
//        commandHandler.register(new replyCommand());
        //Tags
        commandHandler.register(new addCommand());
        commandHandler.register(new removeCommand());
        commandHandler.register(new tagCommand());
        commandHandler.register(new transferCommand());
        commandHandler.register(new listtagCommand());
        //Utility
        commandHandler.register(new infoCommand());
        commandHandler.register(new uptimeCommand());
        commandHandler.register(new calcCommand());
    }
}
