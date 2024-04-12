package fr.angelsky.angelskyproxy.discord;

public enum IDS {

    ACCOUNT_LINKED_ROLE("1228290991568588862"),

    ;

    private final String id;

    IDS(String id)
    {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
