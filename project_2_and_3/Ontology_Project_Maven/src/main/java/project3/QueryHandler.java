package project3;

import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.*;
import java.util.List;

import static Util.Misc.getFileInDeliverables;

public class QueryHandler {
    private static File hospitalOwlFile = new File(getFileInDeliverables("Filtered_Hospital.owl"));


    // Load owl file when this class is loaded
    private static Repository repo;
    static {
        repo = new SailRepository(new MemoryStore()); // Store the owl file in memory, expensive but fast. OK since owl file is pretty small
        try(RepositoryConnection conn = repo.getConnection()) {
            conn.add(hospitalOwlFile, "", RDFFormat.RDFXML); // Load OWL file
        } catch (IOException e) {
            System.out.println("Unable to add owl file to repo");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (RepositoryConnection conn = repo.getConnection()) {
            /**
             * Example:
             * SELECT ?stateName (ROUND(AVG(?score)) AS ?avg)
             * WHERE {
             * 	?hospital ds:hasState ?state.
             *   	?state ds:hasStateName ?stateName.
             *   	?hospital ds:hasScore ?score.
             * }
             * GROUP BY ?stateName
             * ORDER BY ?stateName
             */
            QueryBuilder myQuery = QueryBuilder.newQuery()
                    .select()
                        .ofVar("?stateName") // ofVar automatically add variables into the internal variables's list
                        .freeStmt("(ROUND(AVG(?score)) AS ?avg)") // best use freeStmt for complex statement
                        .addVar("?avg") // Since complex statement won't aware of variables, use addVar to add variable
                    .endSelect()
                    .where()
                        .whereStmt("?hospital ds:hasState ?state") // Each whereStmt will automatically add . at the end
                        .whereStmt("?state ds:hasStateName ?stateName")
                        .whereStmt("?hospital ds:hasScore ?score")
                    .endWhere()
                    .groupBy("?stateName");

            System.out.println(myQuery.build()); // Use build() to get the final query in String
            TupleQuery query = conn.prepareTupleQuery(myQuery.build());
            List<String> vars = myQuery.getVars(); // Retrieve all variables in the query, this is needed to extract info for each var

            // A QueryResult is also an AutoCloseable resource, so make sure it gets
            // closed when done.
            try (TupleQueryResult result = query.evaluate()) {
                // we just iterate over all solutions in the result...
                while (result.hasNext()) {
                    BindingSet st = result.next();
                    vars.forEach(v -> System.out.println(v + ": " + st.getValue(v))); // Extract result for each variable
                }
            }
        }

    }
}
