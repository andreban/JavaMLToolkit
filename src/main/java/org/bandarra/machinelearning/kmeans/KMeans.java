package org.bandarra.machinelearning.kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 11/26/13
 * Time: 10:23 AM
 *
 * @author andreban
 */
public class KMeans<T> {
    private DistanceCalculator<T> distanceCalculator;
    private CentroidCalculator<T> centroidCalculator;

    public KMeans(DistanceCalculator<T> distanceCalculator, CentroidCalculator<T> centroidCalculator) {
        this.distanceCalculator = distanceCalculator;
        this.centroidCalculator = centroidCalculator;
    }

    public List<Cluster<T>> bisectingKMeans(List<T> instances, int numClusters, int maxIterations) {
        List<Cluster<T>> clusters = new ArrayList<>();
        Cluster<T> start = new BaseCluster<T>(distanceCalculator, centroidCalculator);
        for (T instance: instances) {
            start.addInstance(instance);
        }

        clusters.add(start);

        while (clusters.size() < numClusters) {
            System.out.println(clusters.size());
            // find worstSSE
            Cluster<T> worstSSECluster = null;
            for (Cluster<T> cluster: clusters) {
                if (worstSSECluster == null || cluster.getSSE() > worstSSECluster.getSSE()) {
                    worstSSECluster = cluster;
                }
            }
            clusters.remove(worstSSECluster);
            List<Cluster<T>> newClusters = kMeans(worstSSECluster.getInstances(), 2, maxIterations);
            clusters.addAll(newClusters);

        }
        return clusters;
    }

    public List<Cluster<T>> kMeans(List<T> instances, int numClusters, int maxIterations) {
        List<Cluster<T>> clusters = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            Cluster<T> c = new BaseCluster<>(distanceCalculator, centroidCalculator);
            clusters.add(c);
        }

        for (int i = 0; i < instances.size(); i++) {
            int clusterid = (int)(Math.random() * clusters.size());
            clusters.get(clusterid).addInstance(instances.get(i));
//            clusters.get(clusterid).getInstances().add(instances.get(i));
        }

        for (Cluster c: clusters) {
            c.calculateCentroid();
        }

        boolean changed = true;
        int i = 0;
        for (; i < maxIterations && changed; i++) {
            changed = false;
            updateClusters(clusters);
            for (Cluster<T> c: clusters) {
                T oldCentroid = c.getCentroid();
                c.calculateCentroid();
                if (!oldCentroid.equals(c.getCentroid())){
                    changed = true;
                }
            }
        }

        System.out.println(String.format("Stopped at Iteration %d", i));

        System.out.println("Number of Instances: " + instances.size());
        for (i = 0; i <  clusters.size(); i++) {
            System.out.println("Cluster " + i + " has " + clusters.get(i).getInstances().size() + " instances");
        }
        return clusters;
    }

    private void updateClusters(List<Cluster<T>> clusters) {
        List<T> allValues = new ArrayList<>();
        clusters.forEach(c -> {
            allValues.addAll(c.getInstances());
            c.clear();
        });

        for (T value: allValues) {
            Cluster<T> bestCluster = null;
            for (Cluster<T> c: clusters) {
                if (bestCluster == null
                        || (distanceCalculator.getDistance(value, c.getCentroid())
                        < distanceCalculator.getDistance(value, bestCluster.getCentroid()))) {
                    bestCluster = c;
                }
            }
            bestCluster.addInstance(value);
        }
    }
}
