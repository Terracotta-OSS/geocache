package org.terracotta.geospatial.util;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.google.common.collect.Lists;
import org.terracotta.geospatial.cache.geohash.QuadGeoHash;

import java.util.List;

import static com.esri.core.geometry.GeometryEngine.*;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static org.terracotta.geospatial.util.WorldConstants.SPATIAL_REFERENCE;

public class LinearQuadtree {

    private static final double EPSILON = 1.0e-24;

    public static class XY {
        public double x;
        public double y;
    }


    private final Envelope env;
    private final double step;
    private final int steps;
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private final int depth;
    private final int offsT;
    private final int offsU;

    public LinearQuadtree(double minX, double minY, double size, int depth) {
        this.minX = minX;
        this.minY = minY;

        this.maxX = minX + size;
        this.maxY = minY + size;

        this.depth = depth;
        this.env = new Envelope(minX, minY, minX + size, minY + size);

        final double s = Math.pow(2, depth);
        this.step = size / s;
        this.steps = (int) s;

        this.offsT = (int) -floor(minX / step);
        this.offsU = (int) -floor(minY / step);
    }

    public double getMaxX() {
        return maxX;
    }

    public int getDepth() {
        return depth;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getStep() {
        return step;
    }

    public int getSteps() {
        return steps;
    }

    public Envelope getEnv() {
        return env;
    }

    public List<QuadGeoHash> computeQuadrantHashes(Geometry geometry) {
        final List<QuadGeoHash> quads = Lists.newArrayList();
        final Envelope quad = new Envelope();
        quad.setCoords(env.getXMin(), env.getYMin(), env.getXMax(), env.getYMax());
        compute(geometry, quad, quads, 0, depth);
        return quads;
    }

    public void compute(Geometry geom, Envelope quad, List<QuadGeoHash> quads, int depth, int maxDepth) {
        final Envelope tmp = new Envelope();
        geom.queryEnvelope(tmp);

        //if the geom and quad don't interset then do nothing
        if(!tmp.isIntersecting(quad)) return;

        //if we reach a leaf quad or the geometry completely contains the quad then terminate recursion
        if (depth == maxDepth || contains(geom, quad, SPATIAL_REFERENCE)) {
            final int minT = (int) floor(quad.getXMin() / step) + offsT;
            final int minU = (int) floor(quad.getYMin() / step) + offsU;
            //add one to max if not leaf quadrant. if leaf, no need for range query, single equals will do
            final int maxT = (int) floor(quad.getXMax() / step) + offsT;
            final int maxU = (int) floor(quad.getYMax() / step) + offsU;
            quads.add(new QuadGeoHash(depth,
                    minT, minU,
                    maxT, maxU,
                    computeGeoHash(minT, minU),
                    computeGeoHash(maxT, maxU)));
            return;
        }

        final int d = depth + 1;

        //recurse down SW quadrant
        tmp.setCoords(env.getXMin(), env.getYMin(), env.getCenterX(), env.getCenterY());
        compute(geom, tmp, quads, d, maxDepth);

        //recurse down SE quadrant
        tmp.setCoords(env.getCenterX(), env.getYMin(), env.getXMax(), env.getCenterY());
        compute(geom, tmp, quads, d, maxDepth);

        //recurse down NW quadrant
        tmp.setCoords(env.getXMin(), env.getCenterY(), env.getCenterX(), env.getYMax());
        compute(geom, tmp, quads, d, maxDepth);

        //recurse down NE quadrant
        tmp.setCoords(env.getCenterX(), env.getCenterY(), env.getXMax(), env.getYMax());
        compute(geom, tmp, quads, d, maxDepth);
    }


    public long computeGeoHash(double x, double y) {
        checkArgument(x >= minX && x <= maxX, "x out of range: %s <= (%s) < %s", minX, x, maxX);
        checkArgument(y >= minY && y <= maxY, "y out of range: %s <= (%s) < %s", minY, y, maxY);
        final int t = (int) floor(x / step) + offsT;
        final int u = (int) floor(y / step) + offsU;
        return computeGeoHash(t, u);
    }

    public long computeGeoHash(int t, int u) {
        checkArgument(t >= 0 && t <= steps, "t out of range: 0 <= (%s) < %s", t, steps);
        checkArgument(u >= 0 && u <= steps, "u out of range: 0 <= (%s) < %s", u, steps);
        return mortonEncode16((t & 0x000000000000FFFFL), (u & 0x000000000000FFFFL)) |
                (mortonEncode16((t & 0x00000000FFFF0000L) >> 16, (u & 0x00000000FFFF0000L) >> 16) << 16) |
                (mortonEncode16((t & 0x0000FFFF00000000L) >> 32, (u & 0x0000FFFF00000000L) >> 32) << 32) |
                (mortonEncode16((t & 0xFFFF000000000000L) >> 48, (u & 0xFFFF000000000000L) >> 48) << 48);
    }

    private long mortonEncode16(long t, long u) {
        checkArgument(t <= 0xFFFF, "t out of range: 0 <= (%s) <= %s", t, 0xFFFF);
        checkArgument(u <= 0xFFFF, "u out of range: 0 <= (%s) <= %s", u, 0xFFFF);

        t &= 0x0000FFFF;
        t |= (t << 8);
        t &= 0x00FF00FF;
        t |= (t << 4);
        t &= 0x0F0F0F0F;
        t |= (t << 2);
        t &= 0x33333333;
        t |= (t << 1);
        t &= 0x55555555;

        u &= 0x0000FFFF;
        u |= (u << 8);
        u &= 0x00FF00FF;
        u |= (u << 4);
        u &= 0x0F0F0F0F;
        u |= (u << 2);
        u &= 0x33333333;
        u |= (u << 1);
        u &= 0x55555555;

        return (t | (u << 1));
    }

    private void mortonDecode16(int morton, XY xy) {
        int t = morton;
        int u = (morton >> 1);

        t &= 0x55555555;
        t |= (t >> 1);
        t &= 0x33333333;
        t |= (t >> 2);
        t &= 0x0F0F0F0F;
        t |= (t >> 4);
        t &= 0x00FF00FF;
        t |= (t >> 8);
        t &= 0x0000FFFF;

        u &= 0x55555555;
        u |= (u >> 1);
        u &= 0x33333333;
        u |= (u >> 2);
        u &= 0x0F0F0F0F;
        u |= (u >> 4);
        u &= 0x00FF00FF;
        u |= (u >> 8);
        u &= 0x0000FFFF;

        xy.x = t;
        xy.y = u;
    }

}
