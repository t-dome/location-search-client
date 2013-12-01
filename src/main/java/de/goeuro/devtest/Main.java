package de.goeuro.devtest;

import de.goeuro.devtest.mapper.CsvWriter;
import de.goeuro.devtest.mapper.JsonMapper;
import de.goeuro.devtest.model.Results;
import de.goeuro.devtest.options.Configuration;
import de.goeuro.devtest.options.CommandLineOptionsParser;
import de.goeuro.devtest.search.http.HttpLocationSearch;
import org.apache.commons.cli.*;

import java.io.*;

/**
 * @author Rolf Schuster
 */
public class Main {

    public static void main(String... args) throws IOException, ParseException {
        CommandLineOptionsParser cmdParser = new CommandLineOptionsParser();
        Configuration cfg = cmdParser.parse(args);


        HttpLocationSearch httpSearch = new HttpLocationSearch();

        String result = httpSearch.searchLocation(cfg.getServiceUrl(), cfg.getSearchParameter());

        JsonMapper mapper = new JsonMapper();
        Results searchResults = mapper.fromJson(result);

        CsvWriter csvWriter = new CsvWriter();
        csvWriter.writeCsv(searchResults, cfg.getWriter());
    }
}
