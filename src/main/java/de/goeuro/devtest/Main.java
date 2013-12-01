package de.goeuro.devtest;

import de.goeuro.devtest.exception.ApplicationException;
import de.goeuro.devtest.mapper.CsvWriter;
import de.goeuro.devtest.mapper.JsonMapper;
import de.goeuro.devtest.model.Results;
import de.goeuro.devtest.options.Configuration;
import de.goeuro.devtest.options.CommandLineOptionsParser;
import de.goeuro.devtest.search.http.HttpLocationSearch;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Usage from the command line:
 * java -jar GoEuroTest.jar {[-u url]|[-l]} [-o filename] search_parameter
 * default URL is http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/
 * URL can be overridden with -u
 * You can also specify -l, then http://localhost:8080/api/v1/suggest/position/en/name/ is used.
 *
 * The query will be done at <URL>/<search_parameter>
 * -o specifies the output file name for the CSV data. If omitted, stdout will be used.
 *
 * Exception stacktraces will be written to GoEuroTest-client.log
 *
 * Example 1:
 * java -jar GoEuroTest.jar London
 * This will be queried:
 * http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/London
 * and the output CSV will be written to stdout.
 *
 * Example 2:
 * java -jar GoEuroTest.jar -l -o out.csv "Frankfurt am Main"
 *
 * This will be queried:
 * http://localhost:8080/api/v1/suggest/position/en/name/Frankfurt+am+Main
 * and the output CSV will be written to out.csv in the current directory.
 *
 * @author Rolf Schuster
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
        try {
            CommandLineOptionsParser cmdParser = new CommandLineOptionsParser();

            // parse the command line and get the configuration data, linek URL and search parameter
            Configuration cfg = cmdParser.parse(args);

            // query the URL
            String jsonResult = new HttpLocationSearch().searchLocation(cfg.getServiceUrl(), cfg.getSearchParameter());

            // map the JSON to POJOs
            Results searchResults = new JsonMapper().fromJson(jsonResult);

            if (searchResults == null || searchResults.getResults() == null || searchResults.getResults().isEmpty()) {
                String msg = "Couldn't find any results. JSON:\n" + jsonResult;
                LOGGER.info(msg);
                System.err.println(msg);
            } else {
                // write the CSV
                new CsvWriter().writeCsv(searchResults, cfg.getWriter());
            }

        } catch (ApplicationException e) {
            switch(e.getCode()) {
                case INTERNAL_ERROR:
                    { // create a new block so that "msg" can be declared again
                        String msg = "There was an internal error.";
                        // log the stacktrace
                        LOGGER.error(msg, e.getCause());
                        // only display the error message on stderr
                        System.err.println(generateErrorMessage(msg, e));
                        break;
                    }
                case WRONG_CMD_ARGUMENTS_ERROR:
                    { // create a new block so that "msg" can be declared again
                        String msg = "Wrong command line arguments.";
                        LOGGER.error(msg, e.getCause());
                        System.err.println(generateErrorMessage(msg, e));
                        break;
                    }
                case JSON_PARSING_ERROR:
                    { // create a new block so that "msg" can be declared again
                        String msg = "Error parsing the received JSON string";
                        LOGGER.error(msg + "\nJSON:\n" + e.getDetailMessage(), e.getCause());
                        System.err.println(generateErrorMessage(msg, e));
                        break;
                    }
            }
        }
    }

    private static String generateErrorMessage(String prefix, Exception e) {
        return prefix +  "Details can be found in GoEuroTest-client.log. Cause: " + e.getMessage();
    }
}
