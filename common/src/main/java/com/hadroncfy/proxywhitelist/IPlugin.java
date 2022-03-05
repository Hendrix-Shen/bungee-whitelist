package com.hadroncfy.proxywhitelist;

import java.io.File;

public interface IPlugin {
    boolean isOnlineMode();

    File getDataDirectory();

    void broadcast(ICommandSender sender, String msg);

    ILogger logger();

    void loadConfig(Config config);

    void saveConfig(Config config);
}