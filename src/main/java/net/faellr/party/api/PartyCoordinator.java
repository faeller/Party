package net.faellr.party.api;

import net.faellr.party.api.exceptions.NotOwnerException;

/**
 * Singleton coordinating the usage of {@link Party} instances and other related party functionality
 *
 * @param <P> participant type
 */
public interface PartyCoordinator<P> {

    /**
     * Creates and registers a {@link Party<P>}
     *
     * @param owner the party's initiator
     */
    Party<P> createParty(P owner);

    /**
     * Deletes a party by removing all mappings and destroying its instance
     *
     * @param owner the party's initiator
     * @throws NotOwnerException - when {@param owner} is not the owner of the party he is in
     */
    void disbandParty(P owner) throws NotOwnerException;

    /**
     * @return the {@link Party} instance the {@param activeParticipant} is in
     */
    Party<P> getParty(P activeParticipant);

    /**
     * @return whether {@þaram player} is actively registered to a party
     */
    boolean isInParty(P player);

    /**
     * @return whether {@þaram player} has party requests enabled
     */
    boolean hasPartyRequestsEnabled(P player);

    /**
     * Toggles the {@param player} preference on party requests
     */
    void togglePartyRequests(P player);
}
