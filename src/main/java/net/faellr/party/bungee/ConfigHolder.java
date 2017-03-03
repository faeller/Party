package net.faellr.party.bungee;

import net.faellr.party.persistence.ConfigPersistenceAccessor;
import net.md_5.bungee.api.ChatColor;

import java.io.File;

/**
 * Package visibility constant class. Mainly used for Messages.
 */
class ConfigHolder {
    static class Msg {
        private static final ConfigPersistenceAccessor ACCESSOR = new ConfigPersistenceAccessor(new File(PartyPlugin.getInstance().getDataFolder(), "messages.yml"));

        static final String HELP_TOPIC = createMessage("HELP_TOPIC", "" +
                "&bCommands:\n" +
                "/p invite <Name> - eine Einladung versenden\n" +
                "/p accept <Name> - eine Einladung akzeptieren\n" +
                "/p decline <Name> - eine Einladung ablehnen\n" +
                "/p leave - eine Party verlassen\n" +
                "/p owner <Name> - ?\n" +
                "/p disband - loest eine Party auf\n" +
                "/p toggle - de-/aktiviert die Moeglichkeit Einladungen zu empfangen");

         static final String NOT_ONLINE = createMessage("NOT_ONLINE", "&cDer angegebene Spieler ist nicht online.");

         static final String ALREADY_IN_PARTY = createMessage("ALREADY_IN_PARTY", "&cDer angegebene Spieler ist bereits in einer Party.");

         static final String REQUESTS_DISABLED = createMessage("REQUESTS_DISABLED", "&cDer angegebene Spieler hat Anfragen deaktiviert.");

         static final String PARTY_CREATE = createMessage("PARTY_CREATE", "&a%p hat eine Party erstellt.");

         static final String CAN_NOT_INVITE_YOURSELF = createMessage("CAN_NOT_INVITE_YOURSELF", "&cDu kannst dich nicht selbst in eine Party einladen. Eine Runde Mitleid.");

         static final String REQUEST_ALREADY_SENT = createMessage("REQUEST_ALREADY_SENT", "&cDu hast diesem Spieler bereits eine Anfrage geschickt");

         static final String PARTY_INVITE = createMessage("PARTY_INVITE", "&bDu wurdest von %p zu einer Party eingeladen." +
                "Nutze /p accept %p um die Einladung zu akzeptieren. Alternativ kannst du auch die beiden Knoepfe unter dieser Nachricht druecken.");

         static final String PARTY_INVITE_CLICK_ACCEPT = createMessage("PARTY_INVITE_CLICK_ACCEPT", "&a[Akzeptieren]");

         static final String PARTY_INVITE_CLICK_DENY = createMessage("PARTY_INVITE_CLICK_DENY", "&c[Ablehnen]");

         static final String PARTY_INVITE_SUCCESS = createMessage("PARTY_INVITE_SUCCESS", "&aAnfrage wurde versendet.");

         static final String ONLY_ACCESSIBLE_BY_OWNER = createMessage("ONLY_ACCESSIBLE_BY_OWNER", "&cDieser Befehl kann nur vom Besitzer der Party verwendet werden.");

         static final String NOT_REQUESTED = createMessage("NOT_REQUESTED", "&cDu hast keine Anfrage von diesem Spieler.");

         static final String DECLINE_REQUEST_SUCCESS = createMessage("DECLINE_REQUEST_SUCCESS", "&aDu hast die Anfrage abgelehnt.");

         static final String PARTY_JOIN = createMessage("PARTY_JOIN", "&b%p ist der Party beigetreten.");

         static final String NOT_IN_A_PARTY = createMessage("NOT_IN_A_PARTY", "&cDu bist in keiner Party.");

         static final String PARTY_LEAVE = createMessage("PARTY_LEAVE", "&b%p hat die Party verlassen.");

         static final String OWNER_CAN_NOT_LEAVE = createMessage("OWNER_CAN_NOT_LEAVE", "Loese die Party auf um sie zu verlassen.");

         static final String ALREADY_OWNER = createMessage("ALREADY_OWNER", "&cDu kannst diesen Befehl nicht auf dich ausfuehren.");

         static final String PLAYER_NOT_PARTICIPANT = createMessage("PLAYER_NOT_PARTICIPANT", "&cDer angegebene Spieler ist nicht Teil deiner Party.");

         static final String PARTY_OWNERSHIP_CHANGE = createMessage("PARTY_OWNERSHIP_CHANGE", "&b%p ist der neue Besitzer der Party.");

         static final String PARTY_DISBAND = createMessage("PARTY_DISBAND", "&bDie Party wurde durch %p aufgeloest.");

         static final String ON = createMessage("ON", "an");

         static final String OFF = createMessage("OFF", "aus");

         static final String TOGGLE_REQUESTS = createMessage("TOGGLE_REQUESTS", "&aAnfragen sind nun %toggle.");

         static final String PARTY_SWITCH_SERVER = createMessage("PARTY_SWITCH_SERVER", "&bDie Party betritt Server %s.");


        private static String createMessage(String key, String message) {
            if(!ACCESSOR.containsString(key)) {
                ACCESSOR.setString(key, message);
                return ChatColor.translateAlternateColorCodes('&', message);
            }
            return ChatColor.translateAlternateColorCodes('&', ACCESSOR.getString(key));
        }
    }
}
