package net.faellr.party;

public class PartyCoordinator {



    private static PartyCoordinator ourInstance = new PartyCoordinator();

    public static PartyCoordinator getInstance() {
        return ourInstance;
    }

    private PartyCoordinator() {
    }
}
