package p3.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class collects all the output from TupleQueryResult
 */
public class QueryResult {

    private @Getter List<String> variables;
    private @Getter List<Map<String, String>> allBindings;

    public QueryResult(TupleQueryResult tupleResult) {
        variables = tupleResult.getBindingNames();
        allBindings = new ArrayList<>();
        while(tupleResult.hasNext()) {
            BindingSet line = tupleResult.next();
            Map<String, String> binding = new HashMap<>();
            variables.forEach(var -> binding.put(var, line.getValue(var).stringValue()));
            this.allBindings.add(binding);
        }
    }
    
    @Override
    public String toString() {
        return "QueryResult{" +
                "variables=" + variables +
                ", allBindings=" + allBindings +
                '}';
    }
}
