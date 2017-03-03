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
     * Registers a player as active participant in the party. This state is achieved when the {@link Party#getOwner()}
     * invites a player and the invite is accepted
     *
     * @param party
     * @param futureParticipant
     */
    void registerPlayerToParty(Party<P> party, P futureParticipant);

    /**
     * Unregisters any connection a player might have to a party. This state is achieved when
     * the player leaves the party or the party is disbanded by the {@link Party#getOwner()}
     *
     * @param player ex-participant
     */
    void unregisterPlayer(P player);

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
