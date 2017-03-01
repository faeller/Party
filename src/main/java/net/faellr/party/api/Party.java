package net.faellr.party.api;

import net.faellr.party.api.exceptions.AlreadyInvitedException;
import net.faellr.party.api.exceptions.PartyException;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
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
     * Registers a player as pending. This state is achieved when the {@link Party#getOwner()}
     * invites a player and the invite hasn't been accepted yet
     *
     * @param player invitee
     * @throws AlreadyInvitedException - when an invite is sent while the original one hasn't been accepted yet
     */
    void registerPlayerAsPending(P player) throws AlreadyInvitedException;

    /**
     * Registers a player as active participant in the party. This state is achieved when the {@link Party#getOwner()}
     * invites a player and the invite is accepted
     *
     * @param player invitee
     * @throws PartyException - when a player is already registered
     */
    void registerPlayerAsActive(P player) throws PartyException;

    /**
     * Unregisters any connection a player might have to the party. This state is achieved when
     * the player leaves the party or the party is disbanded by the {@link Party#getOwner()}
     *
     * @param player ex-participant
     * @throws PartyException - when the player isn't registered
     */
    void unregisterPlayer(P player) throws PartyException;

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
