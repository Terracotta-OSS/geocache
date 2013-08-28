GeoCache
========

Proof of concept: Teracotta Geospatial Cache based on the ESRI geometry library

Design Concepts
===============

The main concept that underlies GeoCache design is that every element has a dedicate field used to store an indexable geospatial value. In this case a geo-hash[2] integer value based on quad-trees[3] and Morton codes[4] is used to encore the geospatial coordinates of the element into the field.

The geo-hash does not replace the key of the cache element, so each element still requires a unique key. This is beneficial as many elements may reside at a single coordinate.

Geo-hashing provides a way to logically index geospatial data without introducing a new data structure into BigMemory. The main downside to this method is sub-optimal performance compared to a baked in geospatial data structure, and is limited to insertion of point data only. However, due to its relative simplicity and the promise of improved performance in some usecases it is worth pursuing.
