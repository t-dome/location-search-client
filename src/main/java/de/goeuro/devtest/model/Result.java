package de.goeuro.devtest.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rolf Schuster
 */
public class Result {
    @SerializedName("_id")
    private String id;
    private String type;
    @SerializedName("_type")
    private String typeFriendlyName;
    private String name;
    @SerializedName("geo_position")
    private GeoPosition geoPosition;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTypeFriendlyName() {
        return typeFriendlyName;
    }

    public String getName() {
        return name;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public Result withId(final String id) {
        this.id = id;
        return this;
    }

    public Result withType(final String type) {
        this.type = type;
        return this;
    }

    public Result withTypeFriendlyName(final String typeFriendlyName) {
        this.typeFriendlyName = typeFriendlyName;
        return this;
    }

    public Result withName(final String name) {
        this.name = name;
        return this;
    }

    public Result withGeoPosition(final GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
        return this;
    }


}
