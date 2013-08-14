package org.terracotta.geospatial.cache.geohash;

import com.google.common.base.Objects;

public class QuadGeoHash {
    public final int depth;

    public final long minT;
    public final long minU;

    public final long maxT;
    public final long maxU;


    public final long minHash;
    public final long maxHash;

    public QuadGeoHash(int depth, long minT, long minU, long maxT, long maxU, long minHash, long maxHash) {
        this.depth = depth;
        this.minT = minT;
        this.minU = minU;
        this.maxT = maxT;
        this.maxU = maxU;
        this.minHash = minHash;
        this.maxHash = maxHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuadGeoHash that = (QuadGeoHash) o;

        if (depth != that.depth) return false;
        if (maxHash != that.maxHash) return false;
        if (maxT != that.maxT) return false;
        if (maxU != that.maxU) return false;
        if (minHash != that.minHash) return false;
        if (minT != that.minT) return false;
        if (minU != that.minU) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = depth;
        result = 31 * result + (int) (minT ^ (minT >>> 32));
        result = 31 * result + (int) (minU ^ (minU >>> 32));
        result = 31 * result + (int) (maxT ^ (maxT >>> 32));
        result = 31 * result + (int) (maxU ^ (maxU >>> 32));
        result = 31 * result + (int) (minHash ^ (minHash >>> 32));
        result = 31 * result + (int) (maxHash ^ (maxHash >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("depth", depth)
                .add("minT", minT)
                .add("minU", minU)
                .add("maxT", maxT)
                .add("maxU", maxU)
                .add("minHash", minHash)
                .add("maxHash", maxHash)
                .toString();
    }
}