package de.goeuro.devtest.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.goeuro.devtest.exception.ApplicationException;
import de.goeuro.devtest.model.Results;

/**
 * @author Rolf Schuster
 */
public class JsonMapper {

    // don't bother with spring/dependency injection
    // because we want to keep the project simple and the über JAR small
    private Gson gson = new Gson();

    public Results fromJson(String jsonString) throws ApplicationException {
        try {
        return gson.fromJson(jsonString, Results.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationException(ApplicationException.ErrorCode.JSON_PARSING_ERROR,
                    "Error parsing JSON", jsonString, e);
        }
    }

}
