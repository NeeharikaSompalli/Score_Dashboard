import java.time.LocalDateTime;
import java.util.*;

public class Node{
    String name;
    LocalDateTime time;
    int data;

    Node(int d, LocalDateTime t, String n)
    {
        this.name =n;
        this.data =d;
        this.time =t;
    }
}

