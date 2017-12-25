package org.bandarra.machinelearning.kmeans;

import java.util.Collection;

/**
 * Date: 11/26/13
 * Time: 9:52 AM
 *
 * @author andreban
 */
@FunctionalInterface
public interface CentroidCalculator<T> {
    T getCentroid(Collection<T> instances);
}
