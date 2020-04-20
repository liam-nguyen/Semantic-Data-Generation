package p3.web;


import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import lombok.Getter;
import p3.query.QueryDemo;
import p3.query.QueryHandler;
import p3.query.QueryResult;

@RestController
public class MainController {
	
	static class Question {
	    private String author = ""; // Who writes the question
	    private String inEnglish = ""; // The actual question
	    private String inSPARQL = ""; // Query in SPARQL language
	    private QueryResult queryResult; // The actual output the query

	    public Question(String author, String engLang, String SPARQLLanguage, QueryResult queryResult) {
	        this.author = author; this.inEnglish = engLang;
	        this.inSPARQL = SPARQLLanguage; this.queryResult = queryResult;
	    }
	    
	    //Getters
	    public QueryResult getQueryResult() {return this.queryResult;}
	    public String getAuthor() {return this.author;}
	    public String getInEnglish() {return this.inEnglish;}
	    public String getInSPARQL() {return this.inSPARQL;}
	}
	
	@Getter private static Map<String, List<Question>> demoList = new HashMap<>();
    static {
        try {
            parseDemoCSV();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
    
    //This method will run an sample query that's been hardcoded, and refers to files
    // in the file system. Only used 
    //
    @RequestMapping(method=RequestMethod.GET)
	public void handleQuery() throws IOException {
		QueryResult example = demoList.get("Suchitra").get(0).getQueryResult();
        ObjectMapper objectMapper = new ObjectMapper();

        File sampleJson = new File(Paths.get(
                System.getProperty("user.dir")).resolve("deliverables").resolve("sample_query.json").toString());
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.writeValue(sampleJson, example);
	}
	
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

	@GetMapping("/greeting")
	public MainController greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new MainController();
	}
}

