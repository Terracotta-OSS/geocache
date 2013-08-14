package org.terracotta.geospatial.util;

import com.esri.core.geometry.SpatialReference;

public class WorldConstants {

    public static final int SPATIAL_REFERENCE_WGS84_EPSG = 4326;

    public static final SpatialReference SPATIAL_REFERENCE = SpatialReference.create(SPATIAL_REFERENCE_WGS84_EPSG);

    public static final double MIN_LON = -180.0;

    public static final double MAX_LON = 180.0;

    public static final double MIN_LAT = -90.0;

    public static final double MAX_LAT = 90;

    public static final double WIDTH = MAX_LON - MIN_LON;

    public static final double HEIGHT = MAX_LAT - MIN_LAT;

}
