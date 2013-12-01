package de.goeuro.devtest.model;

import java.util.List;

/**
 * Class to help mapping from JSON. This just holds a List of Result objects.
 *
 * @author Rolf Schuster
 */
public class Results {
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public Results withResults(final List<Result> results) {
        this.results = results;
        return this;
    }


}
