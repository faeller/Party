package net.faellr.party.api;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.stream.Stream;

/**
 * {@link Party} contract
 * @param <P> participant type (e.g. {@link ProxiedPlayer})
 */
public interface Party<P> {

    /**
     * {@link Party#getOwner()} references the parties initiator.
     * Only he can invite participants or disband the party.
     *
     * @return the owner
     */
    P getOwner();

    /**
     * Changes the {@link Party#getOwner()}
     *
     * @param newOwner future owner
     */
    void changeOwnership(P newOwner);

    /**
     * Registers a player as pending. This state is achieved when the {@link Party#getOwner()}
     * invites a player and the invite hasn't been accepted yet
     *
     * @param player invitee
     */
    void registerPlayerAsPending(P player);

    /**
     * Registers a player as active participant in the party. This state is achieved when the {@link Party#getOwner()}
     * invites a player and the invite is accepted
     *
     * @param player invitee
     */
    void registerPlayerAsActive(P player);

    /**
     * Unregisters any connection a player might have to the party. This state is achieved when
     * the player leaves the party or the party is disbanded by the {@link Party#getOwner()}
     *
     * @param player ex-participant
     */
    void unregisterPlayer(P player);

    /**
     * @param player invitee
     * @return whether the player is registered as pending to this party
     */
    boolean isPending(P player);

    /**
     * @param player participant
     * @return whether the player is registered as active participant in this party
     */
    boolean isActive(P player);

    /**
     * Sends all {@link Party#getActiveParticipants()} to the minecraft server registered as {@param serverName}
     */
    void sendParticipantsToServer(String serverName);

    /**
     * Sends all {@link Party#getActiveParticipants()} the resourcepack provided in {@param resourcepackURI}
     */
    void sendResourcepackToParticipants(String resourcepackURI);

    /**
     * @return {@link Stream<P>} containing all active participants
     */
    Stream<P> getActiveParticipants();

    /**
     * @return {@link Stream<P>} containing all pending participants
     */
    Stream<P> getPendingParticipants();
}
