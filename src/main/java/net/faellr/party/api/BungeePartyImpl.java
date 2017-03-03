package net.faellr.party.api;

import com.google.common.base.Preconditions;
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

    BungeePartyImpl(ProxiedPlayer owner) {
        this.owner = owner.getUniqueId();
        this.activeParticipants = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());
        this.pendingParticipants = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());
    }

    @Override
    public ProxiedPlayer getOwner() {
        return ProxyServer.getInstance().getPlayer(owner);
    }

    @Override
    public void changeOwnership(ProxiedPlayer newOwner) {
        Preconditions.checkNotNull(newOwner);

        if(!isActive(newOwner))
            throw new PartyException(newOwner.getName()+" is not an active participant to the party");

        this.owner = newOwner.getUniqueId();
    }

    @Override
    public void registerPlayerAsPending(ProxiedPlayer player) {
        Preconditions.checkNotNull(player);

        if(isPending(player))
            return;

        pendingParticipants.add(player.getUniqueId());
    }

    @Override
    public void registerPlayerAsActive(ProxiedPlayer player) {
        Preconditions.checkNotNull(player);

        if(isActive(player))
            return;

        pendingParticipants.remove(player.getUniqueId());
        activeParticipants.add(player.getUniqueId());
    }

    @Override
    public void unregisterPlayer(ProxiedPlayer player) {
        Preconditions.checkNotNull(player);
        pendingParticipants.remove(player.getUniqueId());
        activeParticipants.remove(player.getUniqueId());
    }

    @Override
    public boolean isPending(ProxiedPlayer player) {
        Preconditions.checkNotNull(player);
        return activeParticipants.contains(player.getUniqueId());
    }

    @Override
    public boolean isActive(ProxiedPlayer player) {
        Preconditions.checkNotNull(player);
        return activeParticipants.contains(player.getUniqueId());
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
