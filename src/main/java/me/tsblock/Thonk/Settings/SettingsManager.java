package me.tsblock.Thonk.Settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.tsblock.Thonk.utils.ExitCodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *The code is directly from Smbarbour's RavenBot
 * https://github.com/MCUpdater/RavenBot/blob/master/src/main/java/org/mcupdater/ravenbot/SettingsManager.java
 * Thanks, I guess
 */

public class SettingsManager {
    private static SettingsManager instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Settings settings;
    private final Path configFile = new File(".").toPath().resolve("config.json");

    public static SettingsManager getInstance(){
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public SettingsManager() {
        if(!configFile.toFile().exists()) {
            System.out.println("Config not found, creating example config...");
            this.settings = getDefaultSettings();
            saveSettings();
            System.exit(ExitCodes.NEW_CONFIG_CREATED);
        }
        try {
            loadSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Config loaded");
    }

    public void loadSettings() throws IOException {
        BufferedReader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8);
        this.settings = gson.fromJson(reader, Settings.class);
        reader.close();
    }

    public Settings getSettings() {
        return settings;
    }

    public void saveSettings() {
        String jsonOut = gson.toJson(this.settings);
        try {
            BufferedWriter writer = Files.newBufferedWriter(configFile, StandardCharsets.UTF_8);
            writer.append(jsonOut);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Settings getDefaultSettings() {
        Settings newSettings = new Settings();
        newSettings.setBotSecret("Discord bot token (Not the secret!)");
        newSettings.setOwnerID("Your Discord ID here");
        newSettings.setDataBaseURL("MongoDB URL");
        newSettings.setPrefix("b.");
        newSettings.setAdminList(new String[]{"id 1", "id 2"});
        newSettings.setAPI(new API("https://api.ksoft.si/ token here"));
        return newSettings;
    }
}
