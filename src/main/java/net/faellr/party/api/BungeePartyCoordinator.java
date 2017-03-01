package net.faellr.party.api;

import com.google.common.base.Preconditions;
import net.faellr.party.api.exceptions.NotOwnerException;
import net.faellr.party.api.exceptions.PartyException;
import net.faellr.party.api.exceptions.PartyRuntimeException;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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

    /**
     * Creates and registers a {@link Party<ProxiedPlayer>}
     *
     * @param owner the party's initiator
     * @throws PartyException when {@param owner} is in a party
     */
    public Party<ProxiedPlayer> createParty(UUID owner) throws PartyException {
        Preconditions.checkNotNull(owner);

        if(participantParty.containsKey(owner))
            throw new PartyException("Player with uuid '"+owner+"' already is in a party");

        UUID partyUUID = UUID.randomUUID();
        Party<ProxiedPlayer> party = new BungeePartyImpl(owner);

        participantParty.put(owner, partyUUID);
        registry.put(partyUUID, party);

        return party;
    }

    /**
     * Deletes a party by removing all mappings and destroying its instance
     *
     * @param owner the party's initiator
     * @throws PartyException - when {@param owner} is not in a party
     * @throws NotOwnerException - when {@param owner} is not the owner of the party he is in
     */
    public void disbandParty(UUID owner) throws PartyException, NotOwnerException {
        Preconditions.checkNotNull(owner);
        Party<ProxiedPlayer> party = getParty(owner);

        if(party == null)
            throw new PartyException("Player with uuid '"+owner+"' is not in a party");

        if(!party.getOwner().equals(owner))
            throw new NotOwnerException("Player with uuid '"+owner+"' is not the owner of the party and can not disband it");

        party.getActiveParticipants().forEach(player -> {
            try {
                party.unregisterPlayer(player.getUniqueId());
            } catch (PartyException e) {
                throw new PartyRuntimeException("Exception while trying to unregister a player. " +
                        "Please contact the developer with this message", e);
            }

            // TODO: implement object destroyal
        });
    }

    /**
     * @return the {@link Party} instance the {@param activeParticipant} is in
     */
    public Party<ProxiedPlayer> getParty(UUID activeParticipant) {
        Preconditions.checkNotNull(activeParticipant);
        UUID partyUUID = participantParty.get(activeParticipant);
        Party<ProxiedPlayer> party = registry.get(partyUUID);
        return party;
    }

    private static BungeePartyCoordinator ourInstance = new BungeePartyCoordinator();

    public static BungeePartyCoordinator getInstance() {
        return ourInstance;
    }

    private BungeePartyCoordinator() {
    }
}
