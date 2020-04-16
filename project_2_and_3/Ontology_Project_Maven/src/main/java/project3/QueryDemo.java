package project3;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import org.eclipse.rdf4j.query.BindingSet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class stores our demo queries
 */
public class QueryDemo {
    @Getter private static Map<Integer, Question> demoList = new HashMap<>();
    private enum TeamMember {
        LOC("Loc"), VARUN("Varun"), SUCHITRA("Suchitra"), PHUC("Phuc"), LIAM("Liam");

        String name;
        TeamMember(String name) {
            this.name = name;
        }
    }
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
            Question q = new Question(data[0], data[1], data[2], QueryHandler.evaluateQuery(data[2]));
            demoList.put(q.hashCode(), q);
            System.out.println("Added question by " + data[0] + ": " + data[1]);
        }
        System.out.println("=================================");
    }

    /**
     * Helper method to get questions by a team member
     * @param person enum instance of TeamMember
     * @return a list of questions
     */
    public static List<Question> getQuestionBy(TeamMember person) {
        return demoList.values().stream()
                .filter(q -> q.getAuthor().equals(person.name()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        demoList.forEach(($, value) -> {
            System.out.println("****" + value.inEnglish + "****");
            value.getQueryResult().getAllBindings().forEach(System.out::println);
        });
    }
}
