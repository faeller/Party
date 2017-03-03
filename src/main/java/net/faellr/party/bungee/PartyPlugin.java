package net.faellr.party.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class PartyPlugin extends Plugin {
    private static PartyPlugin instance;

    @Override
    public void onEnable() {
        PartyPlugin.instance = this;
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PartyCommand());
    }

    public static PartyPlugin getInstance() {
        return instance;
    }
}
