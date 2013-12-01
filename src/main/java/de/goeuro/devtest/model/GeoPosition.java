package de.goeuro.devtest.model;

/**
 * @author Rolf Schuster
 */
public class GeoPosition {
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public GeoPosition withLatitude(final String latitude) {
        this.latitude = latitude;
        return this;
    }

    public GeoPosition withLongitude(final String longitude) {
        this.longitude = longitude;
        return this;
    }


}
