package org.terracotta.geospatial.cache.simple;

import net.sf.ehcache.Element;
import net.sf.ehcache.search.Query;
import org.terracotta.geospatial.cache.GeospatialCache;
import org.terracotta.geospatial.coord.Coordinate;
import org.terracotta.geospatial.coord.Distance;

public class SimpleGeospatialCache implements GeospatialCache  {

  @Override
  public void put(Coordinate coord, Element elem) {
  }

  @Override
  public Element get(Object key) {
    return null;
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
