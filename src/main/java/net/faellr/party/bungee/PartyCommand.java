package net.faellr.party.bungee;

import net.faellr.party.api.BungeePartyCoordinator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class PartyCommand extends Command {

    BungeePartyCoordinator partyCoordinator = BungeePartyCoordinator.getInstance();

    public PartyCommand() {
        super("party", "party.use", "p");
    }

    // TODO: implement configurable messages

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

    }
}
