package de.goeuro.devtest.mapper;

import com.google.gson.*;
import de.goeuro.devtest.exception.ApplicationException;
import de.goeuro.devtest.model.Results;

import java.io.IOException;

/**
 * Maps a JSON String to a Results object, which contains a List of Result objects.
 *
 * The expected format is like this:
 *
 * {
 *   "results" : [ {
 *     "_type" : "Position",
 *     "_id" : 410978,
 *     "name" : "Potsdam, USA",
 *     "type" : "location",
 *     "geo_position" : {
 *         "latitude" : 44.66978,
 *         "longitude" : -74.98131
 *       }
 *   }, {
 *     "_type" : "Position",
 *     "_id" : 377078,
 *     "name" : "Potsdam, Deutschland",
 *     "type" : "location",
 *     "geo_position" : {
 *         "latitude" : 52.39886,
 *         "longitude" : 13.06566
 *       }
 *   } ]
 * }
 *
 * results can also be null:
 * {
 *   "results" : null
 * }
 *
 * @author Rolf Schuster
 */
public class JsonMapper {

    // don't bother with spring/dependency injection
    // because we want to keep the project simple and the big JAR small
    private Gson parser = new Gson();

    public Results fromJson(String jsonString) throws ApplicationException {
        try {
            return parser.fromJson(jsonString, Results.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationException(ApplicationException.ErrorCode.JSON_PARSING_ERROR,
                    "Error parsing JSON", jsonString, e);

        }
    }
}
