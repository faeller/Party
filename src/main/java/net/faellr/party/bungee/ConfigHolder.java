package net.faellr.party.bungee;

import net.faellr.party.persistence.ConfigPersistenceAccessor;
import net.md_5.bungee.api.ChatColor;

import java.io.File;

/**
 * Package visibility constant class. Mainly used for Messages.
 */
class ConfigHolder {
    public static class Msg {
        private static final ConfigPersistenceAccessor ACCESSOR = new ConfigPersistenceAccessor(new File(PartyPlugin.getInstance().getDataFolder(), "messages.yml"));

        public static final String HELP_TOPIC = createMessage("HELP_TOPIC", "" +
                "&bCommands:\n" +
                "/p invite <Name> - eine Einladung versenden\n" +
                "/p accept <Name> - eine Einladung akzeptieren\n" +
                "/p decline <Name> - eine Einladung ablehnen\n" +
                "/p leave - eine Party verlassen\n" +
                "/p owner <Name> - ?\n" +
                "/p disband - löst eine Party auf\n" +
                "/p toggle - de-/aktiviert die Möglichkeit Einladungen zu empfangen");

        public static final String NOT_ONLINE = createMessage("NOT_ONLINE", "&cDer angegebene Spieler ist nicht online.");

        public static final String ALREADY_IN_PARTY = createMessage("ALREADY_IN_PARTY", "&cDer angegebene Spieler ist bereits in einer Party.");

        public static final String REQUESTS_DISABLED = createMessage("REQUESTS_DISABLED", "&cDer angegebene Spieler hat Anfragen deaktiviert.");

        public static final String PARTY_CREATE = createMessage("PARTY_CREATE", "&a%p hat eine Party erstellt.");

        public static final String REQUEST_ALREADY_SENT = createMessage("REQUEST_ALREADY_SENT", "&cDu hast diesem Spieler bereits eine Anfrage geschickt");

        public static final String PARTY_INVITE = createMessage("PARTY_INVITE", "&bDu wurdest von %p zu einer Party eingeladen." +
                "Nutze /p accept %p um die Einladung zu akzeptieren. Alternativ kannst du auch die beiden Knöpfe unter dieser Nachricht drücken.");

        public static final String PARTY_INVITE_SUCCESS = createMessage("PARTY_INVITE_SUCCESS", "&aAnfrage wurde versendet.");

        public static final String CAN_NOT_INVITE_YOURSELF = createMessage("CAN_NOT_INVITE_YOURSELF", "&cDu kannst dich nicht selbst in eine Party einladen. Eine Runde Mitleid.");

        public static final String ONLY_ACCESSIBLE_BY_OWNER = createMessage("ONLY_ACCESSIBLE_BY_OWNER", "&cDieser Befehl kann nur vom Besitzer der Party verwendet werden.");

        public static final String NOT_REQUESTED = createMessage("NOT_REQUESTED", "&cDu hast keine Anfrage von diesem Spieler.");

        public static final String DECLINE_REQUEST_SUCCESS = createMessage("DECLINE_REQUEST_SUCCESS", "&aDu hast die Anfrage abgelehnt.");

        public static final String PARTY_JOIN = createMessage("PARTY_JOIN", "&b%p ist der Party beigetreten.");

        public static final String NOT_IN_A_PARTY = createMessage("NOT_IN_A_PARTY", "&cDu bist in keiner Party.");

        public static final String PARTY_LEAVE = createMessage("PARTY_LEAVE", "&b%p hat die Party verlassen.");

        public static final String OWNER_CAN_NOT_LEAVE = createMessage("OWNER_CAN_NOT_LEAVE", "Löse die Party auf um sie zu verlassen.");

        public static final String PARTY_DISBAND = createMessage("PARTY_DISBAND", "&bDie Party wurde durch %p aufgelöst.");

        public static final String ON = createMessage("ON", "an");

        public static final String OFF = createMessage("OFF", "aus");

        public static final String TOGGLE_REQUESTS = createMessage("TOGGLE_REQUESTS", "&aAnfragen sind nun %toggle.");

        private static String createMessage(String key, String message) {
            if(!ACCESSOR.containsString(key)) {
                ACCESSOR.setString(key, message);
                return ChatColor.translateAlternateColorCodes('&', message);
            }
            return ChatColor.translateAlternateColorCodes('&', ACCESSOR.getString(key));
        }
    }
}
