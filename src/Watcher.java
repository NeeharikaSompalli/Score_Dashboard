import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.*;

import java.io.*;
import java.util.*;


public class Watcher implements org.apache.zookeeper.Watcher, Runnable {
    private static ZooKeeper zk;
    private static int display_count;
    private static scores score_builder;


    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged || watchedEvent.getState() == Event.KeeperState.SyncConnected)
        {
            getChildren();
        }
    }
    AsyncCallback.ChildrenCallback getChildrenCallBack = new AsyncCallback.ChildrenCallback(){
        public void processResult(int rc, String path, Object ctx, List<String> nodes){
            if(nodes!=null || (!nodes.isEmpty())) {
                score_builder.getList(nodes);
                score_builder.highscores();
                System.out.println();
                score_builder.recentscores();
            }
            else
            {
                System.out.println("Recent Scores:");
                System.out.println("Highest Scores:");
            }
        }
    };

    public void run()
    {   try{
        synchronized (this)
        {
            while(true)
            {
                    wait();
            }
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getChildren()
    {
        zk.getChildren("/allnodes", this, getChildrenCallBack, null);

    }


    public static void main_watcher(String args[]) throws IOException, KeeperException, InterruptedException
    {   if(args.length != 3)
        {
            System.out.println("Wrong Number of arguments");
            System.exit(1);
        }
        if(args.length==3) {
            String address = args[1];
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            Watcher w = new Watcher();
            display_count = Integer.parseInt(args[2]);
            zk = new ZooKeeper(address, 5000, w);
            score_builder = new scores(zk, display_count);

            String data = "/allnodes";
            if (zk.exists(data, false) == null)
                zk.create(data, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            w.getChildren();
            w.run();
        }
    }
}
