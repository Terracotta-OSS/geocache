package org.terracotta.geospatial.cache.geohash;


public interface GeoHashAdapter {

    void setGeoHash(Object value, long hash);

    long getGeoHash(Object value);

}
