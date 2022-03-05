package com.hadroncfy.proxywhitelist.bungeecord;

import com.hadroncfy.proxywhitelist.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;

public class WhitelistPlugin extends Plugin implements IPlugin, Listener {

    private File dataPath;
    private final Whitelist whitelist = new Whitelist(this);
    private final ILogger loggerImpl = new ILogger() {

        @Override
        public void info(String msg) {
            getLogger().info(msg);
        }

        @Override
        public void error(String msg) {
            getLogger().severe(msg);
        }
    };

    @Override
    public void onEnable() {
        dataPath = getDataFolder();
        whitelist.init();

        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new WhitelistCommand(whitelist));
    }

    @Override
    public void broadcast(ICommandSender sender, String msg) {
        msg = "[" + sender.getLabel() + ": " + msg + "]";
        getLogger().info(msg);
        TextComponent ret = new TextComponent(msg);
        ret.setItalic(true);
        ret.setColor(ChatColor.DARK_RED);
        getProxy().broadcast(ret);
    }

    @EventHandler
    public void onNetworkJoin(LoginEvent e) {
        whitelist.checkPlayerJoin(new PendingPlayer(e.getConnection()));
    }

    @Override
    public boolean isOnlineMode() {
        return getProxy().getConfig().isOnlineMode();
    }

    @Override
    public File getDataDirectory() {
        return getDataFolder();
    }

    @Override
    public void loadConfig(Config config) {
        File configFile = new File(dataPath, "config.yml");
        if (!configFile.exists()) {
            saveConfig(config);
            return;
        }

        try {
            Configuration cfg = YamlConfiguration.getProvider(YamlConfiguration.class).load(configFile);
            config.enabled = cfg.getBoolean("enabled");
            config.kickMessage = cfg.getString("kick-message");
        } catch (Exception e) {
            getLogger().severe("Failed to load configuration!");
        }
    }

    @Override
    public void saveConfig(Config config) {
        File configFile = new File(dataPath, "config.yml");
        try {
            Configuration cfg = new Configuration();
            cfg.set("enabled", config.enabled);
            cfg.set("kick-message", config.kickMessage);
            YamlConfiguration.getProvider(YamlConfiguration.class).save(cfg, configFile);
        } catch (Exception e) {
            getLogger().severe("Failed to save configuration!");
        }
    }

    @Override
    public ILogger logger() {
        return loggerImpl;
    }
}