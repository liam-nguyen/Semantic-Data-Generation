package project3;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle query building
 */
public class QueryBuilder {
    private static final String allNamespace = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX ds: <https://data.medicare.gov/d/nrth-mfg3#>\n";

    private String query = "";
    private List<String> variables; // store all variables in our query

    private QueryBuilder() { // Setup
        query += allNamespace;
        variables = new ArrayList<>();
    }

    /** Building **/
    public static QueryBuilder newQuery() {
        return new QueryBuilder();
    }

    public QueryBuilder select() {
        query += "SELECT ";
        return this;
    }

    public QueryBuilder ofVar(String var) {
        addVar(var);
        query += var + " ";
        return this;
    }

    public QueryBuilder freeStmt(String statement) {
        query += " " + statement + " ";
        return this;
    }

    public QueryBuilder endSelect() {
        query += "\n";
        return this;
    }

    public QueryBuilder where() {
        query += "WHERE { \n";
        return this;
    }

    public QueryBuilder whereStmt(String statement) {
        query += statement + ".\n";
        return this;
    }


    public QueryBuilder endWhere() {
        query += "}\n";
        return this;
    }

    public QueryBuilder orderBy(String variable) {
        query += "ORDER BY " + variable + "\n";
        return this;
    }

    public QueryBuilder limit(String number) {
        query += "LIMIT " + number + "\n";
        return this;
    }

    public QueryBuilder filter() {
        query += "FILTER (";
        return this;
    }

    public QueryBuilder filterStmt(String statement) {
        query += statement;
        return this;
    }

    public QueryBuilder andFilterStmt(String statement) {
        query += "&& " + statement;
        return this;
    }

    public QueryBuilder orFilterStmt(String statement) {
        query += "|| " + statement;
        return this;
    }

    public QueryBuilder endFilter () {
        query += ")\n";
        return this;
    }


    public QueryBuilder addVar(String var) {
        variables.add(var.substring(1));
        return this;
    }

    public QueryBuilder groupBy(String var) {
        query += "GROUP BY (" + var + ")\n";
        return this;
    }

    /** Getters **/
    public String build() {
        return query;
    }
    public List<String> getVars() {return variables;}
}
