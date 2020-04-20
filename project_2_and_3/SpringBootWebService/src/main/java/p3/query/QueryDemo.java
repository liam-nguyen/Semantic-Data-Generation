package p3.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class stores our demo queries
 */
public class QueryDemo {
    /**
     * Inner class is just to store data about a question
     */
    static class Question {
        //@Getter 
        private String author = ""; // Who writes the question
        //@Getter 
        private String inEnglish = ""; // The actual question
        //@Getter 
        private String inSPARQL = ""; // Query in SPARQL language
        //@Getter 
        private QueryResult queryResult; // The actual output the query

        public Question(String author, String engLang, String SPARQLLanguage, QueryResult queryResult) {
            this.author = author;
            this.inEnglish = engLang;
            this.inSPARQL = SPARQLLanguage;
            this.queryResult = queryResult;
        }
        
        public QueryResult getQueryResult() {
        	return this.queryResult;
        }
        
        public String getAuthor() {
        	return this.author;
        }
        public String getInEnglish() {
        	return this.inEnglish;
        }
        public String getInSPARQL() {
        	return this.inSPARQL;
        }
    }
    

    @Getter private static Map<String, List<Question>> demoList = new HashMap<>();
    static {
        try {
            parseDemoCSV();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
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
        QueryResult example = demoList.get("Suchitra").get(0).getQueryResult();
        ObjectMapper objectMapper = new ObjectMapper();

        File sampleJson = new File(Paths.get(
                System.getProperty("user.dir")).resolve("deliverables").resolve("sample_query.json").toString());
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.writeValue(sampleJson, example);
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
            demoList.merge(person, new ArrayList<Question>() {{ add(q);}}, (o, n) -> {
                o.addAll(n);
                return o;
            });

            System.out.println("Added question by " + data[0] + ": " + data[1]);
        }
        System.out.println("=================================");
    }
}
