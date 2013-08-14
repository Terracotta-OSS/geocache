package org.terracotta.geospatial.coord;

public interface CoordinateSystem {

    static enum Name {LAT_LON_DEG, LAT_LON_RAD, MERCATOR}

    long computeGeoHash(Coordinate coord);

    long[] quadrantsForRectangle(Coordinate nw, Coordinate se);

    long[] quadrantsForRadius(Coordinate center, Distance radius);

    long[] quadrantsForRegion(Coordinate[] coords);
}
