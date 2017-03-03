package net.faellr.party.persistence;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.UUID;

/**
 * WARNING: Not implemented; Only for demonstration purposes
 */
public class MySqlDatabasePersistenceAccessor implements PersistenceAccessor {
    @Override
    public void setString(String key, String message) {
        throw new NotImplementedException();
    }

    @Override
    public String getString(String key) {
        throw new NotImplementedException();
    }

    @Override
    public boolean containsString(String key) {
        throw new NotImplementedException();
    }

    @Override
    public void setPartyRequestsEnabled(UUID uuid, boolean toggle) {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasPartyRequestsEnabled(UUID uuid) {
        throw new NotImplementedException();
    }
}
