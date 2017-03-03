package net.faellr.party.bungee;

import net.faellr.party.api.BungeePartyCoordinator;
import net.faellr.party.api.Party;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyCommand extends Command {

    private BungeePartyCoordinator partyCoordinator = BungeePartyCoordinator.getInstance();

    PartyCommand() {
        super("party", null, "p");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer))
            return;

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.HELP_TOPIC));
            return;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("invite")) {
            ProxiedPlayer invitee = ProxyServer.getInstance().getPlayer(args[1]);

            boolean playerNotOnline = (invitee == null);
            if(validateCondition(playerNotOnline, player, ConfigHolder.Msg.NOT_ONLINE))
                return;

            boolean playerEqualsInvitee = player.getUniqueId().equals(invitee.getUniqueId());
            if(validateCondition(playerEqualsInvitee, player, ConfigHolder.Msg.CAN_NOT_INVITE_YOURSELF))
                return;

            boolean partyRequestsDisabled = !partyCoordinator.hasPartyRequestsEnabled(invitee);
            if(validateCondition(partyRequestsDisabled, player, ConfigHolder.Msg.REQUESTS_DISABLED))
                return;

            boolean inviteeAlreadyInParty = partyCoordinator.isInParty(invitee);
            if(validateCondition(inviteeAlreadyInParty, player, ConfigHolder.Msg.ALREADY_IN_PARTY))
                return;

            Party<ProxiedPlayer> party = partyCoordinator.getParty(player);

            if(party == null) {
                party = partyCoordinator.createParty(player);
                player.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.PARTY_CREATE.replace("%p", player.getName())));
            }

            boolean playerIsNotPartyOwner = !party.getOwner().getUniqueId().equals(player.getUniqueId());
            if(validateCondition(playerIsNotPartyOwner, player, ConfigHolder.Msg.ONLY_ACCESSIBLE_BY_OWNER))
                return;

            boolean alreadyInvited = party.isPending(invitee);
            if(validateCondition(alreadyInvited, player, ConfigHolder.Msg.REQUEST_ALREADY_SENT))
                return;

            party.registerPlayerAsPending(invitee);

            invitee.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.PARTY_INVITE.replace("%p", player.getName())));
            player.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.PARTY_INVITE_SUCCESS));
            return;
        }

        if(args.length == 2 && (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("decline"))) {
            ProxiedPlayer inviter = ProxyServer.getInstance().getPlayer(args[1]);

            boolean playerNotOnline = (inviter == null);
            if(validateCondition(playerNotOnline, player, ConfigHolder.Msg.NOT_ONLINE))
                return;

            Party<ProxiedPlayer> party = partyCoordinator.getParty(inviter);

            boolean playerEqualsInviter = player.getUniqueId().equals(inviter.getUniqueId());
            boolean inviterNotInParty = (party == null);

            if(validateCondition(playerEqualsInviter || inviterNotInParty, player, ConfigHolder.Msg.NOT_REQUESTED))
                return;

            boolean inviterNotOwner = !party.getOwner().getUniqueId().equals(inviter.getUniqueId());
            boolean notInvited = !party.isPending(player);

            if(validateCondition(inviterNotOwner || notInvited, player, ConfigHolder.Msg.NOT_REQUESTED))
                return;

            if(args[0].equalsIgnoreCase("accept")) {
                party.registerPlayerAsActive(player);

                party.getActiveParticipants().forEach(p ->
                        p.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.PARTY_JOIN.replace("%p", player.getName()))));
            } else {
                party.unregisterPlayer(player);
                player.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.DECLINE_REQUEST_SUCCESS));
            }
            return;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("leave")) {
            Party<ProxiedPlayer> party = partyCoordinator.getParty(player);

            boolean playerNotInAParty = (party == null);
            if(validateCondition(playerNotInAParty, player, ConfigHolder.Msg.NOT_IN_A_PARTY))
                return;

            boolean playerIsPartyOwner = player.getUniqueId().equals(party.getOwner().getUniqueId());
            if(validateCondition(playerIsPartyOwner, player, ConfigHolder.Msg.OWNER_CAN_NOT_LEAVE))
                return;

            party.getActiveParticipants().forEach(p ->
                    p.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.PARTY_LEAVE.replace("%p", player.getName()))));

            party.unregisterPlayer(player);
            return;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("owner")) {
            Party<ProxiedPlayer> party = partyCoordinator.getParty(player);
            ProxiedPlayer futureOwner = ProxyServer.getInstance().getPlayer(args[1]);

            boolean playerNotOnline = (futureOwner == null);
            if(validateCondition(playerNotOnline, player, ConfigHolder.Msg.NOT_ONLINE))
                return;

            boolean playerEqualsFutureOwner = player.getUniqueId().equals(futureOwner.getUniqueId());
            if(validateCondition(playerEqualsFutureOwner, player, ConfigHolder.Msg.ALREADY_OWNER))
                return;

            boolean playerNotInAParty = (party == null);
            if(validateCondition(playerNotInAParty, player, ConfigHolder.Msg.NOT_IN_A_PARTY))
                return;

            boolean playerIsNotPartyOwner = !party.getOwner().getUniqueId().equals(player.getUniqueId());
            if(validateCondition(playerIsNotPartyOwner, player, ConfigHolder.Msg.ONLY_ACCESSIBLE_BY_OWNER))
                return;

            boolean futureOwnerIsNotParticipant = !party.isActive(futureOwner);
            if(validateCondition(futureOwnerIsNotParticipant, player, ConfigHolder.Msg.PLAYER_NOT_PARTICIPANT))
                return;

            party.changeOwnership(futureOwner);

            party.getActiveParticipants().forEach(p ->
                    p.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.PARTY_OWNERSHIP_CHANGE.replace("%p", futureOwner.getName()))));
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("disband")) {
            Party<ProxiedPlayer> party = partyCoordinator.getParty(player);

            boolean playerNotInAParty = (party == null);
            if(validateCondition(playerNotInAParty, player, ConfigHolder.Msg.NOT_IN_A_PARTY))
                return;

            boolean playerIsNotPartyOwner = !party.getOwner().getUniqueId().equals(player.getUniqueId());
            if(validateCondition(playerIsNotPartyOwner, player, ConfigHolder.Msg.ONLY_ACCESSIBLE_BY_OWNER))
                return;

            party.getActiveParticipants().forEach(p ->
                    p.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.PARTY_DISBAND.replace("%p", p.getName()))));

            partyCoordinator.disbandParty(player);
            return;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            partyCoordinator.togglePartyRequests(player);
            String toggle = (partyCoordinator.hasPartyRequestsEnabled(player)) ? ConfigHolder.Msg.ON : ConfigHolder.Msg.OFF;
            player.sendMessage(TextComponent.fromLegacyText(ConfigHolder.Msg.TOGGLE_REQUESTS.replace("%toggle", toggle)));
        }
    }

    /*
     * validates condition and sends an error message if true
     */
    private boolean validateCondition(boolean condition, CommandSender sender, String errorMessage) {
        if(condition)
            sender.sendMessage(TextComponent.fromLegacyText(errorMessage));
        return condition;
    }
}
