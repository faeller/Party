package net.faellr.party.api;

import com.google.common.base.Preconditions;
import net.faellr.party.bungee.PartyPlugin;
import net.faellr.party.persistence.ConfigPersistenceAccessor;
import net.faellr.party.persistence.PersistenceAccessor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton coordinating the usage of {@link Party} instances
 *
 * The mapping is simple: Everytime a party is created it is mapped to a random UUID.
 * All {@link Party#getActiveParticipants()} are also mapped to this random UUID.
 *
 * The retrieval of a participant's Party is then as simple as using the participant's UUID
 * to get the party's UUID and then using the party's UUID to get the {@link Party} instance
 */
public class BungeePartyCoordinator implements PartyCoordinator<ProxiedPlayer> {
    private final Map<UUID, UUID> participantParty = new ConcurrentHashMap<>(); // <participantUUID, partyUUID>
    private final Map<UUID, Party<ProxiedPlayer>> registry = new ConcurrentHashMap<>(); // <partyUUID, party>
    private final PersistenceAccessor persistenceAccessor = new ConfigPersistenceAccessor(new File(PartyPlugin.getInstance().getDataFolder(), "persistence.yml"));

    /**
     * Creates and registers a {@link Party<ProxiedPlayer>}
     *
     * @param owner the party's initiator
     * @return newly instantiated {@link Party<ProxiedPlayer>}; null if the owner already is in a party
     */
    @Override
    public Party<ProxiedPlayer> createParty(ProxiedPlayer owner) {
        Preconditions.checkNotNull(owner);

        if(participantParty.containsKey(owner.getUniqueId()))
            return null;

        UUID partyUUID = UUID.randomUUID();
        Party<ProxiedPlayer> party = new BungeePartyImpl(owner);

        participantParty.put(owner.getUniqueId(), partyUUID);
        registry.put(partyUUID, party);

        return party;
    }

    /**
     * Deletes a party by removing all mappings and destroying its instance
     *
     * @param owner the party's initiator
     */
    @Override
    public void disbandParty(ProxiedPlayer owner) {
        Preconditions.checkNotNull(owner);
        Party<ProxiedPlayer> party = getParty(owner);

        if(party == null)
            return;

        if(!party.getOwner().getUniqueId().equals(owner.getUniqueId()))
            return;

        party.getActiveParticipants().forEach(party::unregisterPlayer);

        // TODO: implement object destruction
    }

    /**
     * @return whether {@þaram player} is actively registered to a party
     */
    @Override
    public boolean isInParty(ProxiedPlayer player) {
        return participantParty.containsKey(player.getUniqueId());
    }

    /**
     * @return the {@link Party} instance the {@param activeParticipant} is in
     */
    @Override
    public Party<ProxiedPlayer> getParty(ProxiedPlayer activeParticipant) {
        Preconditions.checkNotNull(activeParticipant);
        UUID partyUUID = participantParty.get(activeParticipant.getUniqueId());

        if(partyUUID == null)
            return null;

        Party<ProxiedPlayer> party = registry.get(partyUUID);
        return party;
    }

    // TODO: cache persistenceAccessor values

    /**
     * Toggles the {@param player} preference on party requests
     */
    @Override
    public void togglePartyRequests(ProxiedPlayer player) {
        persistenceAccessor.setPartyRequestsEnabled(player.getUniqueId(), !hasPartyRequestsEnabled(player));
    }

    /**
     * @return whether {@param player} has party requests enabled
     */
    @Override
    public boolean hasPartyRequestsEnabled(ProxiedPlayer player) {
        return persistenceAccessor.hasPartyRequestsEnabled(player.getUniqueId());
    }

    private static BungeePartyCoordinator ourInstance = new BungeePartyCoordinator();

    public static BungeePartyCoordinator getInstance() {
        return ourInstance;
    }

    private BungeePartyCoordinator() {
    }
}
