package tools;

import javafx.util.Pair;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class PairBuilder {
    public JsonObject createJsonPair(Pair pair){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("x", (int) pair.getKey())
            .add("y",(int) pair.getValue());
        return job.build();
    }
}
