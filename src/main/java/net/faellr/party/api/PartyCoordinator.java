package net.faellr.party.api;

import net.faellr.party.api.exceptions.NotOwnerException;
import net.faellr.party.api.exceptions.PartyException;

import java.util.UUID;

/**
 * Singleton coordinating the usage of {@link Party} instances
 *
 * @param <P> participant type
 */
public interface PartyCoordinator<P> {

    /**
     * Creates and registers a {@link Party<P>}
     *
     * @param owner the party's initiator
     * @throws PartyException when {@param owner} is in a party
     */
    Party<P> createParty(UUID owner) throws PartyException;

    /**
     * Deletes a party by removing all mappings and destroying its instance
     *
     * @param owner the party's initiator
     * @throws PartyException - when {@param owner} is not in a party
     * @throws NotOwnerException - when {@param owner} is not the owner of the party he is in
     */
    void disbandParty(UUID owner) throws PartyException, NotOwnerException;

    /**
     * @return the {@link Party} instance the {@param activeParticipant} is in
     */
    Party<P> getParty(UUID activeParticipant);
}
