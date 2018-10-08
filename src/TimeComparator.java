import java.util.Comparator;

public class TimeComparator implements Comparator<Node> {

    public int compare(Node o1, Node o2) {
        if((o1.time).isBefore(o2.time))
            return 1;
        else
            return -1;
    }
}
