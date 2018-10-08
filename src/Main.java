import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public class Main {

    public static void main(String args[]) throws KeeperException, InterruptedException,IOException
    {
        if(args[0].equals("watcher"))
        {
            new Watcher().main_watcher(args);
        }
        else if( args[0].equals("player"))
        {
            new Player().main_player(args);
        }
    }
}
