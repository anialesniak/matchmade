package clients;

public enum ClientDataType
{
    CLIENT_SELF("clientSelf"),
    CLIENT_SEARCHING("clientSearching");

    private final String typeName;

    ClientDataType(final String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }
}
