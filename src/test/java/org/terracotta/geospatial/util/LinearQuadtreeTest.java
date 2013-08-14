package org.terracotta.geospatial.util;

import com.esri.core.geometry.Envelope;
import com.google.common.collect.Lists;
import org.terracotta.geospatial.cache.geohash.QuadGeoHash;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LinearQuadtreeTest {

    @Test
    public void testComputeGeoHash() throws Exception {
        final LinearQuadtree qt = new LinearQuadtree(0.0, -90, 180.0, 4);

        assertThat(qt.getDepth(), equalTo(4));
        assertThat(qt.getSteps(), equalTo(16));
        assertThat(qt.getMinX(), equalTo(0.0));
        assertThat(qt.getMaxX(), equalTo(180.0));
        assertThat(qt.getMinY(), equalTo(-90.0));
        assertThat(qt.getMaxY(), equalTo(90.0));

        List<QuadGeoHash> quads = Lists.newArrayList();

        final Envelope tmp = new Envelope();

        final double s = qt.getStep();
        tmp.setCoords(0.0, 0.0, s, s);
        qt.compute(tmp, qt.getEnv(), quads, 0, qt.getDepth());

        System.out.println(quads);

        assertThat(quads, not(empty()));

    }


}