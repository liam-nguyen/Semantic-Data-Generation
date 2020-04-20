package p3.query;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import p3.query.util.Misc;

import java.io.File;
import java.io.IOException;

/**
 * This class handles database connection and evaluate the query
 */
public class QueryHandler {
    private static File hospitalOwlFile = new File(Misc.getFileInDeliverables("Filtered_Hospital.owl"));
    private static final String allNamespace = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX ds: <https://data.medicare.gov/d/nrth-mfg3#>\n";

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

    /**
     * Evaluate the actual query. Note query is submitted as is. No namespace included.
     * @param queryStr the SPARQL query
     * @return QueryResult instance since TupleQueryResult is null when the connection is closed.
     */
    public static QueryResult evaluateQuery(String queryStr) {
        try (RepositoryConnection conn = repo.getConnection()) {
            TupleQuery output = conn.prepareTupleQuery(queryStr);
            try (TupleQueryResult tqr = output.evaluate()){
                return new QueryResult(tqr);
            }
        }
    }

    /**
     * Evaluate query with namespace
     * @param queryStr the SPARQL query
     * @return QueryResult instance since TupleQueryResult is null when the connection is closed.
     */
    public static QueryResult evaluateQueryWithNS(String queryStr) {
        StringBuilder sb = new StringBuilder(allNamespace);
        try (RepositoryConnection conn = repo.getConnection()) {
            TupleQuery output = conn.prepareTupleQuery(sb.append(queryStr).toString());
            try (TupleQueryResult tqr = output.evaluate()){
                return new QueryResult(tqr);
            }
        }
    }
}
