package com.gmail.zhushijie.litecustomstopserver;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
//By主世界
//若要使用本段代码,请遵守CC-BY-NC-ND-4.0协议
public class ConfigReader {
    private File configfile;
    private final JavaPlugin plugin;
    private final String configname;
    private FileConfiguration config;

    public void saveDefaultConfig() {
        if (!configfile.exists()) {
            this.plugin.saveResource(configname,false);
        }
    }

    public FileConfiguration getConfig() {
        if (!configfile.exists()) {
            this.saveDefaultConfig();
        }
        if (config == null) {
            this.reloadConfig();
        }
        return config;
    }

    public void reloadConfig() {
        if (configfile == null) {
            configfile = new File(plugin.getDataFolder(),configname);
        }
        config = YamlConfiguration.loadConfiguration(configfile);
        InputStream defaultStream = plugin.getResource(configname);
        if (defaultStream != null) {
            YamlConfiguration defaultFile = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            config.setDefaults(defaultFile);
        }
    }

    public void saveConfig() {
        try {
            config.save(configfile);
        } catch (Throwable t) {
            Bukkit.getLogger().info("配置保存失败!请重试!");
        }
    }

    public ConfigReader(JavaPlugin plugin, String filename) {
        this.plugin = plugin;
        this.configname = filename;
        this.configfile = new File(plugin.getDataFolder(),filename);
    }
}