package tools;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;


public class DataStorer {
    private List<Pair<Integer, Integer>> list;
    
    public DataStorer() {
        list = new ArrayList<>();
    }
    
    public void append(int a, int b) {
        list.add(new Pair(a,b));
    }

    public List<Pair<Integer, Integer>> getList() {
        return list;
    }

    public void setList(List<Pair<Integer, Integer>> list) {
        this.list = list;
    }
    
    

}
