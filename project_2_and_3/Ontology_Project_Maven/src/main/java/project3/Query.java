package project3;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private static final String allNamespace = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX ds: <https://data.medicare.gov/d/nrth-mfg3#>\n";
    enum clause {
        SELECT, WHERE, FILTER, AVG
    }

    private String query = "";
    private List<String> variables;

    private Query() {
        query += allNamespace;
        variables = new ArrayList<>();
    }

    public static Query newQuery() {
        return new Query();
    }

    public Query select() {
        query += "SELECT ";
        return this;
    }

    public Query ofVar(String var) {
        addVar(var);
        query += var + " ";
        return this;
    }

    public Query freeStmt(String statement) {
        query += statement;
        return this;
    }

    public Query endSelect() {
        query += "\n";
        return this;
    }

    public Query where() {
        query += "WHERE { \n";
        return this;
    }

    public Query whereStmt(String statement) {
        query += statement + ".\n";
        return this;
    }


    public Query endWhere() {
        query += "}\n";
        return this;
    }

    public Query orderBy(String variable) {
        query += "ORDER BY " + variable + "\n";
        return this;
    }

    public Query limit(String number) {
        query += "LIMIT " + number + "\n";
        return this;
    }

    public Query filter() {
        query += "FILTER (";
        return this;
    }

    public Query filterStmt(String statement) {
        query += statement;
        return this;
    }

    public Query andFilterStmt(String statement) {
        query += "&& " + statement;
        return this;
    }

    public Query orFilterStmt(String statement) {
        query += "|| " + statement;
        return this;
    }

    public Query endFilter () {
        query += ")\n";
        return this;
    }


    public Query addVar(String var) {
        variables.add(var.substring(1));
        return this;
    }

    public Query groupBy(String var) {
        query += "GROUP BY (" + var + ")\n";
        return this;
    }

    public String build() {
        return query;
    }



    public List<String> getVars() {return variables;}
}
