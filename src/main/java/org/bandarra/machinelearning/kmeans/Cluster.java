package org.bandarra.machinelearning.kmeans;

import java.util.List;

/**
 * Date: 11/26/13
 * Time: 9:50 AM
 *
 * @author andreban
 */
public interface Cluster<T> {
    public void addInstance(T i);
    public List<T> getInstances();
    public double getSSE();
    public T getCentroid();
    public void calculateCentroid();
    public void clear();
    public T getFarthestPoint();
    public boolean contains(Cluster<T> other);
}
