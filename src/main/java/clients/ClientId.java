package clients;

public class ClientId {

    private static int currentID = 0;

    synchronized public static int getNextID(){
        return currentID++;
    }
}
