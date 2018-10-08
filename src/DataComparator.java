import java.util.Comparator;

class DataComparator implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return o2.data - o1.data;
    }
}