package net.faellr.party.api;

import com.google.common.base.Preconditions;
import net.faellr.party.api.exceptions.PartyException;
import net.faellr.party.bungee.PartyPlugin;
import net.faellr.party.persistence.ConfigPersistenceAccessor;
import net.faellr.party.persistence.PersistenceAccessor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton coordinating the usage of {@link Party<ProxiedPlayer>} instances
 *
 * The mapping is simple: Everytime a party is created it is mapped to a random UUID.
 * All {@link Party#getActiveParticipants()} are also mapped to this random UUID.
 * The retrieval of a participant's Party is then as simple as using the participant's UUID
 * to get the party's UUID and then using the party's UUID to get the {@link Party} instance
 *
 * @see PartyCoordinator
 */
public class BungeePartyCoordinator implements PartyCoordinator<ProxiedPlayer> {
    private final Map<UUID, UUID> participants = new ConcurrentHashMap<>(); // <participantUUID, partyUUID>
    private final Map<UUID, Party<ProxiedPlayer>> registry = new ConcurrentHashMap<>(); // <partyUUID, party>
    private final PersistenceAccessor persistenceAccessor = new ConfigPersistenceAccessor(new File(PartyPlugin.getInstance().getDataFolder(), "persistence.yml"));

    /**
     * @see PartyCoordinator#createParty(Object)
     */
    @Override
    public Party<ProxiedPlayer> createParty(ProxiedPlayer owner) {
        Preconditions.checkNotNull(owner);

        if(participants.containsKey(owner.getUniqueId()))
            throw new PartyException(owner.getName()+" already is in a party");

        UUID partyUUID = UUID.randomUUID();
        Party<ProxiedPlayer> party = new BungeePartyImpl(owner);

        participants.put(owner.getUniqueId(), partyUUID);
        registry.put(partyUUID, party);

        return party;
    }

    /**
     * @see PartyCoordinator#disbandParty(Object)
     */
    @Override
    public void disbandParty(ProxiedPlayer owner) {
        Preconditions.checkNotNull(owner);
        Party<ProxiedPlayer> party = getParty(owner);

        if(party == null)
            throw new PartyException(owner.getName()+" is not in a party");

        if(!party.getOwner().getUniqueId().equals(owner.getUniqueId()))
            throw new PartyException(owner.getName()+" is not owner of the party");

        // TODO: implement object destruction
        party.getActiveParticipants().forEach(party::unregisterPlayer);
    }

    /**
     * @see PartyCoordinator#isInParty(Object)
     */
    @Override
    public boolean isInParty(ProxiedPlayer player) {
        return participants.containsKey(player.getUniqueId());
    }

    /**
     * @see PartyCoordinator#registerPlayerToParty(Party, Object)
     */
    @Override
    public void registerPlayerToParty(Party<ProxiedPlayer> party, ProxiedPlayer futureParticipant) {
        Preconditions.checkNotNull(party);
        Preconditions.checkNotNull(futureParticipant);

        if(participants.containsKey(futureParticipant.getUniqueId()))
            return;

        party.registerPlayerAsActive(futureParticipant);
        UUID partyUUID = participants.get(party.getOwner().getUniqueId());
        participants.put(futureParticipant.getUniqueId(), partyUUID);
    }

    /**
     * @see PartyCoordinator#unregisterPlayer(Object)
     */
    @Override
    public void unregisterPlayer(ProxiedPlayer player) {
        Preconditions.checkNotNull(player);

        Party<ProxiedPlayer> party = getParty(player);
        if(party == null)
            return;

        party.unregisterPlayer(player);
        participants.remove(player.getUniqueId());
    }

    /**
     * @see PartyCoordinator#getParty(Object)
     */
    @Override
    public Party<ProxiedPlayer> getParty(ProxiedPlayer activeParticipant) {
        Preconditions.checkNotNull(activeParticipant);
        UUID partyUUID = participants.get(activeParticipant.getUniqueId());

        if(partyUUID == null)
            return null;

        return registry.get(partyUUID);
    }

    // TODO: cache persistenceAccessor values

    /**
     * @see PartyCoordinator#togglePartyRequests(Object)
     */
    @Override
    public void togglePartyRequests(ProxiedPlayer player) {
        persistenceAccessor.setPartyRequestsEnabled(player.getUniqueId(), !hasPartyRequestsEnabled(player));
    }

    /**
     * @see PartyCoordinator#hasPartyRequestsEnabled(Object)
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
