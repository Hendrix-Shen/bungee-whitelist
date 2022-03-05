package com.hadroncfy.proxywhitelist.bungeecord;

import com.hadroncfy.proxywhitelist.IPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;

import java.util.UUID;

public class PendingPlayer implements IPlayer {
    private final PendingConnection c;

    public PendingPlayer(PendingConnection p) {
        c = p;
    }

    @Override
    public void sendResultMessage(String msg) {
    }

    @Override
    public UUID getUUID() {
        return c.getUniqueId();
    }

    @Override
    public String getName() {
        return c.getName();
    }

    @Override
    public void disconnect(String msg) {
        c.disconnect(new TextComponent(msg));
    }

    @Override
    public String getLabel() {
        return c.getName();
    }

}