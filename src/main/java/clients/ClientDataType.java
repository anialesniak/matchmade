package clients;

public enum ClientDataType
{
    CLIENT_SELF("clientSelf"),
    CLIENT_SEARCHING("clientSearching");

    private final String type;

    ClientDataType(final String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
}
