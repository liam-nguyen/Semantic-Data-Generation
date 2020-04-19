package project3;

import lombok.Getter;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class collects all the output from TupleQueryResult
 */
public class QueryResult {
    private @Getter List<String> variables;
    private @Getter List<BindingSet> allBindings;

    public QueryResult(TupleQueryResult tupleResult) {
        variables = tupleResult.getBindingNames();
        allBindings = new ArrayList<>();
        while(tupleResult.hasNext()) {
            BindingSet line = tupleResult.next();
            this.allBindings.add(line);
        }
    }

    public List<BindingSet> getBindingsOf(String varName) {
        return this.allBindings.stream().filter(bindingSet -> bindingSet.hasBinding(varName)).collect(Collectors.toList());
    }

}
