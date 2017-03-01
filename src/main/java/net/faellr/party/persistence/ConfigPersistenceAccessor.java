package net.faellr.party.persistence;


import net.faellr.party.api.exceptions.PartyRuntimeException;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ConfigPersistenceAccessor implements PersistenceAccessor {
    File file;
    Configuration config;

    public ConfigPersistenceAccessor(File file) {
        this.file = file;
        try {

            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new PartyRuntimeException("Exception while doing IO. Please contact the developer with this message", e);
        }
    }

    @Override
    public void setString(String key, String message) {
        config.set(key, message);
        save();
    }

    @Override
    public String readString(String key) {
        return config.getString(key);
    }

    @Override
    public void setPartyRequestsEnabled(UUID player, boolean requestsEnabled) {
        config.set(player+".partyRequestsEnabled", requestsEnabled);
        save();
    }

    @Override
    public boolean hasPartyRequestsEnabled(UUID player) {
        boolean configContainsKey = config.contains(player+".partyRequestsEnabled");
        boolean partyRequestsEnabled = config.getBoolean(player+".partyRequestsEnabled");
        return !configContainsKey || partyRequestsEnabled;
    }

    private void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            throw new PartyRuntimeException("Exception while doing IO. Please contact the developer with this message", e);
        }
    }
}
