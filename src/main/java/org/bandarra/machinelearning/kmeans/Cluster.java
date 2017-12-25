package org.bandarra.machinelearning.kmeans;

import java.util.List;

/**
 * Date: 11/26/13
 * Time: 9:50 AM
 *
 * @author andreban
 */
public interface Cluster<T> {
    void addInstance(T i);
    List<T> getInstances();
    double getSSE();
    T getCentroid();
    void calculateCentroid();
    void clear();
    T getFarthestPoint();
    boolean contains(Cluster<T> other);
}
