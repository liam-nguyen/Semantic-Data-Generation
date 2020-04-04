package project3;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.core.query.SelectQuery;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QUERY {
    public static void main(String[] args) throws IOException, URISyntaxException {
//        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
//        Path deliverables_dir = root.resolve("deliverables");
//        String filename = "Hospital.owl";
//
//        File file = new File(deliverables_dir.resolve(filename).toString());
//        InputStream input = new FileInputStream(file);
//
//        Model model = Rio.parse(input, "", RDFFormat.RDFXML);
//        System.out.println(model);
//
//        Repository db = new SailRepository((new MemoryStore()));
//        db.init();
//
//        try(RepositoryConnection conn = db.getConnection()) {
//            conn.add(model);
//
////            try(RepositoryResult<Statement> result = conn.getStatements(null, null, null);) {
////                while(result.hasNext()) {
////                    Statement st = result.next();
////                    System.out.println("db contains: " + st);
////                }
////            }
//
//            SelectQuery select = Queries.SELECT();
//            Variable id;
////            select.prefix(OntModel.source).select()
//        }
//        finally {
//            db.shutDown();
//        }
    }
}
