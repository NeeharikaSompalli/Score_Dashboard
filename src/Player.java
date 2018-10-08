import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooKeeper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class connect_zookeeper {
    private ZooKeeper zoo_keeper;
    private CountDownLatch connectionSignal = new CountDownLatch(1);

    public ZooKeeper connect(String host) throws IllegalStateException, IOException, InterruptedException{
        zoo_keeper = new ZooKeeper(host, 5000, new Watcher(){
            public void process(WatchedEvent we){
                if(we.getState() == Event.KeeperState.SyncConnected){
                    connectionSignal.countDown();
                }
            }
        });
        connectionSignal.await();

        return zoo_keeper;
    }

    public void close() throws InterruptedException{
        zoo_keeper.close();
    }

}

public class Player {

    protected static ZooKeeper zk;
    protected static connect_zookeeper conn;

    public static void main_player(String args[]) throws IOException, InterruptedException, KeeperException {
        if (args.length == 6) {
            String address = args[1];
            String name = args[2];
            int max= Integer.MAX_VALUE;
            int min = Integer.MIN_VALUE;
            if(name.split(" ").length==2)
            {
                String []temp = name.split(" ");
                name = temp[0]+"_"+temp[1];
            }
            int count = Integer.parseInt(args[3]);
            int delay = Integer.parseInt(args[4]);
            int score = Integer.parseInt(args[5]);
            if(count < 0 || delay < 0 || score <0)
            {
                System.out.println(" Enter values greater than zero");
                System.exit(1);
            }
            if(count > max)
                count = Math.min(max, count);
            if(delay > max)
                delay = Math.min(max, delay);
            if(score > max) score = max;
                score = Math.min(max-3, score);

            Random rand = new Random();

            conn = new connect_zookeeper();
            zk = conn.connect(address);
            String data = "/allnodes";


            if (zk.exists(data, false) == null)
                zk.create(data, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            String activen = data + "/" + name;
            if (zk.exists(activen, false) == null) {
                zk.create(activen, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                System.out.println(" Player is active now");
            } else {
                System.out.println(" Player exists");
                System.exit(1);
            }
            for (int k = 1; k <= count; k++) {
                int tempdata = (int)Math.abs(rand.nextGaussian()*3+score);
                if(tempdata >max) tempdata =max;
                if(tempdata < min) tempdata = min;
                String data_score = String.valueOf(tempdata);
                zk.create(data + "/" + name + "_" + LocalDateTime.now(), (name + " " + data_score + " " + LocalDateTime.now()).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                TimeUnit.SECONDS.sleep(delay);
            }

        } else if (args.length == 3) {
            String address = args[1];
            String name = args[2];
            if(name.split(" ").length==2)
            {
                String []temp = name.split(" ");
                name = temp[0]+"_"+temp[1];
            }
            conn = new connect_zookeeper();
            zk = conn.connect(address);

            String data = "/allnodes";

            if (zk.exists(data, false) == null)
                zk.create(data, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);


            String activen = data + "/" + name;
            if (zk.exists(activen, false) == null) {
                zk.create(activen, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                System.out.println(" Player is active now");
            } else {
                System.out.println(" Player exists");
                System.exit(1);

            }
        }
        else
        {
            System.out.println("Wrong number of arguments");
            System.exit(1);

        }
    }
}



