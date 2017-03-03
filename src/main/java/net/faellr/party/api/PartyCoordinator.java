package net.faellr.party.api;

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
     */
    void disbandParty(P owner);

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
