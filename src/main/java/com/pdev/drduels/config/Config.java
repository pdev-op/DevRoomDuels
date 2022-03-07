package com.pdev.drduels.config;

import java.io.File;

import com.pdev.drduels.Main;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    private File file;
    private Main plugin;
    private YamlConfiguration config;

    public Config(Main plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        save();

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    public YamlConfiguration getConfiguration() {
        return config;
    }

    public String getSQLHost() {
        return config.getString("host");
    }

    public String getSQLPort() {
        return config.getString("port");
    }

    public String getSQLUsername() {
        return config.getString("username");
    }

    public String getSQLPassword() {
        return config.getString("password");
    }

    public String getSQLDatabase() {
        return config.getString("database");
    }

    public String getSQLPrefix() {
        return config.getString("table-prefix");
    }

    public String getPrefix() {
        return config.getString("prefix");
    }
}
