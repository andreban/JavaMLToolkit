package org.bandarra.machinelearning.kmeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date: 11/26/13
 * Time: 9:55 AM
 *
 * @author andreban
 */
public class BaseCluster<T> implements Cluster<T> {
    private List<T> instances = new ArrayList<>();
    private T centroid = null;

    private DistanceCalculator<T> distanceCalculator;
    private CentroidCalculator<T> centroidCalculator;

    public BaseCluster(DistanceCalculator<T> distanceCalculator, CentroidCalculator<T> centroidCalculator) {
        this.distanceCalculator = distanceCalculator;
        this.centroidCalculator = centroidCalculator;
    }

    @Override
    public void addInstance(T i) {
        instances.add(i);
    }

    @Override
    public List<T> getInstances() {
        return Collections.unmodifiableList(instances);
    }

    @Override
    public double getSSE() {
        double total = 0;
        for (T instance: instances) {
            total += Math.pow(distanceCalculator.getDistance(instance, getCentroid()), 2);
        }
        return total;
    }

    @Override
    public T getCentroid() {
        return  centroid;
    }

    @Override
    public void calculateCentroid() {
        centroid = centroidCalculator.getCentroid(instances);
    }

    @Override
    public void clear() {
        this.instances.clear();
    }

    @Override
    public T getFarthestPoint() {
        T farthest = null;
        T center = getCentroid();
        double maxDistance = Double.NaN;
        for (T point: instances) {
            double distance = distanceCalculator.getDistance(center, point);
            if (Double.isNaN(maxDistance) || distance > maxDistance) {
                maxDistance = distance;
                farthest = point;
            }
        }
        return farthest;
    }

    @Override
    public boolean contains(Cluster<T> other) {
        double r1 = distanceCalculator.getDistance(this.getCentroid(), this.getFarthestPoint());
        double r2 = distanceCalculator.getDistance(other.getCentroid(), other.getFarthestPoint());
        double distance = distanceCalculator.getDistance(this.getCentroid(), other.getCentroid());
        return r1 >= distance + r2;
    }
}
