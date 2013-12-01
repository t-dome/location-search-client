package de.goeuro.devtest.mapper;

import com.google.gson.Gson;
import de.goeuro.devtest.model.Results;

/**
 * @author Rolf Schuster
 */
public class JsonMapper {

    // don't bother with spring/dependency injection
    // because we want to keep the project simple and the über JAR small
    private Gson gson = new Gson();

    public Results fromJson(String jsonString) {
        return gson.fromJson(jsonString, Results.class);
    }

}
