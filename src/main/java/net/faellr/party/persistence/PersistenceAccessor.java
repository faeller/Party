package net.faellr.party.persistence;

import java.util.UUID;

/**
 * PersistenceAccessors are used as a facade for decoupling different
 * ways of persisting data.
 *
 * Examples are:
 * @see ConfigPersistenceAccessor
 * @see MySqlDatabasePersistenceAccessor
 */
public interface PersistenceAccessor {
    void setString(String key, String message);
    String readString(String key);

    void setPartyRequestsEnabled(UUID uuid, boolean toggle);
    boolean hasPartyRequestsEnabled(UUID uuid);
}
