package org.terracotta.geospatial.cache;

import net.sf.ehcache.Element;
import net.sf.ehcache.search.Query;
import org.terracotta.geospatial.coord.Coordinate;
import org.terracotta.geospatial.coord.Distance;

public interface GeospatialCache {

    void put(Coordinate coord, Element elem);

    Element get(Object key);

    Query queryWithinRectangle(Coordinate nw, Coordinate se);

    Query queryWithinRadius(Coordinate center, Distance radius);

    Query queryWithinRegion(Coordinate... coords);
}
