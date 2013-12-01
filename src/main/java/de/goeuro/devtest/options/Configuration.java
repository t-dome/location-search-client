package de.goeuro.devtest.options;

import java.io.Writer;

/**
 * Holds configuration for the main program.
 *
 * @author Rolf Schuster
 */
public class Configuration {
    private String serviceUrl;
    private String searchParameter;
    private Writer writer;

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public Writer getWriter() {
        return writer;
    }

    public Configuration withServiceUrl(final String serviceUrl) {
        this.serviceUrl = serviceUrl;
        return this;
    }

    public Configuration withSearchParameter(final String searchParameter) {
        this.searchParameter = searchParameter;
        return this;
    }

    public Configuration withWriter(final Writer writer) {
        this.writer = writer;
        return this;
    }

}
