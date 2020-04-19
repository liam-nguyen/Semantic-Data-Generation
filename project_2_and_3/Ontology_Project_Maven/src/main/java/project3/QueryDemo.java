package project3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import org.eclipse.rdf4j.query.BindingSet;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class stores our demo queries
 */
public class QueryDemo {
//    private enum TeamMember {
//        LOC("Loc"), VARUN("Varun"), SUCHITRA("Suchitra"), PHUC("Phuc"), LIAM("Liam");
//
//        String name;
//        TeamMember(String name) {
//            this.name = name;
//        }
//    }

    @Getter private static Map<String, List<Question>> demoList = new HashMap<>();

    static {
        try {
            parseDemoCSV();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inner class is just to store data about a question
     */
    static class Question {
        @Getter private String author = ""; // Who writes the question
        @Getter private String inEnglish = ""; // The actual question
        @Getter private String inSPARQL = ""; // Query in SPARQL language
        @Getter private QueryResult queryResult; // The actual output the query

        public Question(String author, String engLang, String SPARQLLanguage, QueryResult queryResult) {
            this.author = author;
            this.inEnglish = engLang;
            this.inSPARQL = SPARQLLanguage;
            this.queryResult = queryResult;
        }
    }

    /**
     * Parse the queries list in queries.csv in resources folder
     */
    private static void parseDemoCSV() throws IOException, CsvValidationException {
        System.out.println("==== PARSING ALL DEMO QUERIES ====");
        CSVReader openCsvReader = new CSVReaderBuilder(
                new InputStreamReader(
                        Objects.requireNonNull(QueryDemo.class
                                .getClassLoader()
                                .getResourceAsStream("queries.csv"))))
                                .withSkipLines(1)
                                .build();

        String[] data;
        while((data = openCsvReader.readNext()) != null) {
            String person = data[0];
            String question = data[1];
            String query = data[2];

            Question q = new Question(person, question, query, QueryHandler.evaluateQuery(data[2]));
            demoList.merge(person, new ArrayList<>() {{ add(q);}}, (o, n) -> {
                o.addAll(n);
                return o;
            });

            System.out.println("Added question by " + data[0] + ": " + data[1]);
        }
        System.out.println("=================================");
    }

    /**
     * Helper method to get questions by a team member
     * @param personName person's name
     * @return a list of questions of that person
     */
    public static List<Question> getQuestionBy(String personName) {
        return demoList.get(personName);
    }

    public static void main(String[] args) throws IOException {
//        demoList.forEach(($, value) -> {
//            System.out.println("****" + value.inEnglish + "****");
//            value.getQueryResult().getAllBindings().forEach(System.out::println);
//        });

        ObjectMapper mapper = new ObjectMapper();

        File jsonFile = new File(Paths.get(
                System.getProperty("user.dir"))
                .resolve("deliverables")
                .resolve("sample_query.json")
                .toString());

        mapper.writeValue(jsonFile, demoList.get("Liam").get(0).queryResult);
    }
}
