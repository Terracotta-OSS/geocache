package org.terracotta.geospatial.cache.geohash;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Query;
import org.terracotta.geospatial.coord.Coordinate;
import org.terracotta.geospatial.coord.CoordinateSystem;
import org.terracotta.geospatial.coord.Distance;
import org.terracotta.geospatial.cache.GeospatialCache;

public class HashGeospatialCache implements GeospatialCache {

    private final Cache cache;
    private final CoordinateSystem coordSystem;
    private final GeoHashAdapter geoHashAdapter;

    public HashGeospatialCache(Cache cache,
                               CoordinateSystem coordSystem,
                               GeoHashAdapter geoHashAdapter) {
        this.cache = cache;
        this.coordSystem = coordSystem;
        this.geoHashAdapter = geoHashAdapter;
        //TODO check search attribute for geohash is created
    }

    @Override
    public void put(Coordinate coord, Element elem) {
        final long hash = coordSystem.computeGeoHash(coord);
        final Object o = elem.getObjectValue();
        geoHashAdapter.setGeoHash(o, hash);
        cache.put(elem);
    }

    @Override
    public Element get(Object key) {
        return cache.get(key);
    }

    @Override
    public Query queryWithinRectangle(Coordinate nw, Coordinate se) {


        return null;
    }

    @Override
    public Query queryWithinRadius(Coordinate center, Distance radius) {
        return null;
    }

    @Override
    public Query queryWithinRegion(Coordinate... coords) {
        return null;
    }
}
