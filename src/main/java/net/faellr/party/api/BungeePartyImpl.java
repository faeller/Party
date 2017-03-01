package net.faellr.party.api;

import com.google.common.base.Preconditions;
import net.faellr.party.api.exceptions.AlreadyInvitedException;
import net.faellr.party.api.exceptions.PartyException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Package only {@link Party} implementation, suited for usage on one bungeecord server
 */
class BungeePartyImpl implements Party<ProxiedPlayer> {

    private UUID owner;
    private Set<UUID> activeParticipants;
    private Set<UUID> pendingParticipants;

    BungeePartyImpl(UUID owner) {
        this.owner = owner;
        this.activeParticipants = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());
        this.pendingParticipants = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public void registerPlayerAsPending(ProxiedPlayer player) throws AlreadyInvitedException {
        this.registerPlayerAsPending(player.getUniqueId());
    }

    @Override
    public void registerPlayerAsActive(ProxiedPlayer player) throws PartyException {
        this.registerPlayerAsActive(player.getUniqueId());
    }

    @Override
    public void registerPlayerAsPending(UUID player) throws AlreadyInvitedException {
        Preconditions.checkNotNull(player);

        if(pendingParticipants.contains(player))
            throw new AlreadyInvitedException("Player with uuid '"+player+"' has already been registered as pending participant");

        pendingParticipants.add(player);
    }

    @Override
    public void registerPlayerAsActive(UUID player) throws PartyException {
        Preconditions.checkNotNull(player);

        if(activeParticipants.contains(player))
            throw new PartyException("Player with uuid '"+player+"' is already registered as active participant");

        pendingParticipants.remove(player);
        activeParticipants.add(player);
    }

    @Override
    public void unregisterPlayer(UUID player) throws PartyException {
        Preconditions.checkNotNull(player);

        if(!activeParticipants.remove(player) || !pendingParticipants.remove(player))
            throw new PartyException("Player with uuid '"+player+"' is neither an active nor pending participant");
    }

    @Override
    public boolean isPending(UUID player) {
        Preconditions.checkNotNull(player);
        return activeParticipants.contains(player);
    }

    @Override
    public boolean isActive(UUID player) {
        Preconditions.checkNotNull(player);
        return activeParticipants.contains(player);
    }

    @Override
    public void sendParticipantsToServer(String serverName) {
        Preconditions.checkNotNull(serverName);
        ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);
        getActiveParticipants().forEach(player -> player.connect(server));
    }

    @Override
    public void sendResourcepackToParticipants(String resourcepackURI) {
        Preconditions.checkNotNull(resourcepackURI);
        // TODO: implement
    }

    @Override
    public Stream<ProxiedPlayer> getActiveParticipants() {
        return activeParticipants.stream()
                .map(ProxyServer.getInstance()::getPlayer);
    }

    @Override
    public Stream<ProxiedPlayer> getPendingParticipants() {
        return pendingParticipants.stream()
                .map(ProxyServer.getInstance()::getPlayer);
    }
}
