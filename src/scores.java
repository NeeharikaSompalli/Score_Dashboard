import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.zookeeper.data.Stat;

public class scores{
    private List<String> node_ephemeral;
    private List<Node> zNodes;
    private ZooKeeper zk;
    private int display_ct;

    scores (ZooKeeper zk, int ct)
    {
        this.zk=zk;
        this.display_ct = ct;
    }

    public void getList(List<String> nodelist)
    {
        node_ephemeral=new ArrayList<String>();
        List<String> node_persistent  =new ArrayList<String>();
        zNodes = new ArrayList<>();
        String []nodedata;
        String name;
        LocalDateTime time;
        int data;

            for (String node : nodelist) {
                try {

                    Stat stat= zk.exists("/allnodes/"+node, false );
                    if(stat.getEphemeralOwner() !=0)
                        node_ephemeral.add(node);
                    else {
                        nodedata = new String(zk.getData("/allnodes/" + node, false, zk.exists("/allnodes/" + node, false)), "UTF-8").split(" ");
                        node_persistent.add(node);
                        name = nodedata[0];
                        data = Integer.parseInt(nodedata[1]);
                        time = LocalDateTime.parse(nodedata[2]);
                        zNodes.add(new Node(data, time, name));
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException k) {
                    k.printStackTrace();
                } catch (UnsupportedEncodingException u) {
                    u.printStackTrace();
                }
            }
        }




    public void recentscores()
    {
        Collections.sort(zNodes, new TimeComparator());
        System.out.println(" Recent Scores ");
        System.out.println("------------------------------");
        if(zNodes.size() <= display_ct)
        {
            for(Node n : zNodes)
            {
                printdata(n);
            }
        }
        else
        {
            int index =0;
            for(int i=display_ct; i>0;i--)
            {
                Node n = zNodes.get(index++);
                printdata(n);
            }
        }
    }

    public void highscores()
    {
        Collections.sort(zNodes, new DataComparator());
        System.out.println(" Highest Scores ");
        System.out.println("----------------------------");
        if(zNodes.size() <= display_ct)
        {
            for(Node n : zNodes)
            {
                printdata(n);
            }
        }
        else
        {
            int index =0;
            for(int i=display_ct; i>0;i--)
            {
                Node n = zNodes.get(index++);
                printdata(n);
            }
        }
    }

    public void printdata(Node nd)
    {
        String active =node_ephemeral.contains(nd.name)?"**":"";
        System.out.println(nd.name+ "\t" +nd.data + "\t"+ active);


    }


}



