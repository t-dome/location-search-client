package de.goeuro.devtest;

import de.goeuro.devtest.mapper.CsvWriter;
import de.goeuro.devtest.mapper.JsonMapper;
import de.goeuro.devtest.model.Results;
import de.goeuro.devtest.search.http.HttpLocationSearch;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Rolf Schuster
 */
public class Main {

    private static final String LOCALHOST_URL = "http://localhost:8080/api/v1/suggest/position/en/name/Berlin";

    private static final String FIXED_RESULT = "{\n" +
            "  \"results\" : [ {\n" +
            "    \"_type\" : \"Position\",\n" +
            "    \"_id\" : 410978,\n" +
            "    \"name\" : \"Potsdam, USA\",\n" +
            "    \"type\" : \"location\",\n" +
            "    \"geo_position\" : {\n" +
            "        \"latitude\" : 44.66978,\n" +
            "        \"longitude\" : -74.98131\n" +
            "      }\n" +
            "  }, {\n" +
            "    \"_type\" : \"Position\",\n" +
            "    \"_id\" : 377078,\n" +
            "    \"name\" : \"Potsdam, Deutschland\",\n" +
            "    \"type\" : \"location\",\n" +
            "    \"geo_position\" : {\n" +
            "        \"latitude\" : 52.39886,\n" +
            "        \"longitude\" : 13.06566\n" +
            "      }" +
            "   } ]\n" +
            "}";

    public static void main(String... args) throws IOException {
        HttpLocationSearch httpSearch = new HttpLocationSearch();

        String result = httpSearch.searchLocation(LOCALHOST_URL);

        JsonMapper mapper = new JsonMapper();
        Results searchResults = mapper.fromJson(result);

        StringWriter sw = new StringWriter();
        CsvWriter csvWriter = new CsvWriter();
        csvWriter.writeCsv(searchResults, sw);
        System.out.println(sw.toString());
    }
}
