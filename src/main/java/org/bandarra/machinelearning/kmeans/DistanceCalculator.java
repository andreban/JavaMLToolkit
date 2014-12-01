package org.bandarra.machinelearning.kmeans;

/**
 * Date: 11/26/13
 * Time: 9:52 AM
 *
 * @author andreban
 */
public interface DistanceCalculator<T> {
    public double getDistance(T instance1, T instance2);
}
