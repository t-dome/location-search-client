package de.goeuro.devtest.options;

import de.goeuro.devtest.exception.ApplicationException;
import org.apache.commons.cli.*;

import java.io.*;
import java.net.URLEncoder;

/**
 * Parses the command line and returns an appropiately filled Configuration object.
 *
 * @author Rolf Schuster
 */
public class CommandLineOptionsParser {
    private static final String URL_LOCALHOST = "http://localhost:8080/api/v1/suggest/position/en/name/";
    private static final String URL_REMOTE = "http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/";

    private static final String OPTION_URL = "u";
    private static final String OPTION_LOCALHOST = "l";
    private static final String OPTION_FILENAME = "o";

    private Options options;

    // the sertup is done here
    public CommandLineOptionsParser() {
        options = new Options();
        options.addOption(OPTION_URL, true, "(Optional) Service URL to query at. If ommitted and neither -l has been specified, " +
                "the default is used: " + URL_REMOTE);
        options.addOption(OPTION_LOCALHOST, false, "(Optional) Use " + URL_LOCALHOST + " as service URL. Cannot be combined with -u");
        options.addOption(OPTION_FILENAME, true, "(Optional) Filename in which the CSV will be written into." +
                " If omitted, standard output is used.");
    }

    /**
     * Parses the command line and populates a Configuration object with the correct service URL, writer
     * and query parameter.
     *
     * @param cmdArgs the command line args passed to the main method
     *
     * @return a Configuration object with the correct service URL, writer and query parameter
     *
     * @throws ApplicationException thrown when the file could not be created or an error occured
     *                              while parsing the command line
     */
    public Configuration parse(String... cmdArgs) throws ApplicationException {
        try {
            CommandLineParser cmdLineParser = new PosixParser();
            CommandLine cmdLine = cmdLineParser.parse(options, cmdArgs);

            validate(cmdLine);

            // take the parameter to search for
            String searchParam = cmdLine.getArgs()[0];

            // parse the service URL
            String serviceUrl = null;
            if (cmdLine.hasOption(OPTION_LOCALHOST)) {
                serviceUrl = URL_LOCALHOST;
            } else if (cmdLine.hasOption(OPTION_URL)) {
                serviceUrl = cmdLine.getOptionValue(OPTION_URL);
            } else {
                serviceUrl = URL_REMOTE;
            }

            // parse the output writer
            Writer writer = null;
            if (cmdLine.hasOption(OPTION_FILENAME)) {
                writer = new FileWriter(cmdLine.getOptionValue(OPTION_FILENAME));
            } else {
                writer = new PrintWriter(System.out);
            }

            return new Configuration().withSearchParameter(searchParam).withServiceUrl(serviceUrl).withWriter(writer);

        } catch (IOException e) {
            throw new ApplicationException(ApplicationException.ErrorCode.INTERNAL_ERROR,
                    "An error occured while creating the file.", e);
        } catch (ParseException e) {
            throw new ApplicationException(ApplicationException.ErrorCode.INTERNAL_ERROR,
                    "An error occured while parsing the command line.", e);
        }
    }

    // checks that no invalid command arg combinations are used
    private void validate(CommandLine cmdLine) throws UnsupportedEncodingException, ApplicationException {
        if ((cmdLine.hasOption(OPTION_URL) && cmdLine.hasOption(OPTION_LOCALHOST))
                || (cmdLine.getArgs() == null || cmdLine.getArgs().length == 0)) {

            HelpFormatter formatter = new HelpFormatter();
            formatter.setWidth(93);
            String exampleParam = "Frankfurt am Main";
            String msg = String.format("java -jar GoEuroTest.jar {[-%s url]|[-%s]} [-%s filename] search_parameter\n" +
                    "Example:\n" +
                    "java -jar GoEuroTest.jar \"%s\"" +
                    "\nwill query:\n" +
                    "%s%s", OPTION_URL, OPTION_LOCALHOST, OPTION_FILENAME, exampleParam,
                    URL_REMOTE, URLEncoder.encode(exampleParam, "UTF-8"));
            formatter.printHelp(msg, options);

            throw new ApplicationException(ApplicationException.ErrorCode.WRONG_CMD_ARGUMENTS_ERROR,
                    "Invalid command line args", null);
        }
    }
}
