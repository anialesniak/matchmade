package clients;

public class ClientID {

    private static int currentID = 0;

    synchronized public static int getNextID(){
        return currentID++;
    }
}
