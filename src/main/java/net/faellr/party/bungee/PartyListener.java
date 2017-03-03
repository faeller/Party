package net.faellr.party.bungee;

import net.faellr.party.api.BungeePartyCoordinator;
import net.faellr.party.api.Party;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PartyListener implements Listener {
    @EventHandler
    public void on(ServerSwitchEvent e) {
        Party<ProxiedPlayer> party = BungeePartyCoordinator.getInstance().getParty(e.getPlayer());
        if(party != null) {
            if(party.getOwner().getUniqueId().equals(e.getPlayer().getUniqueId())) {
                String server = e.getPlayer().getServer().getInfo().getName();
                party.sendParticipantsToServer(server);
                party.getActiveParticipants().forEach(p -> {
                    p.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.PARTY_SWITCH_SERVER.replace("%s", server)));
                });
            }
        }
    }
}
