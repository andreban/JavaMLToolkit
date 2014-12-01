package org.bandarra.machinelearning.naivebayes;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author andreban
 */
public class NaiveBayesParameters<T extends Serializable, C extends Serializable>  implements Serializable {
    private Set<T> allTokens;
    private Map<C, double[]> classProbabilities;
    private Map<C, Integer> classCount;
    private int numPosts;

    public NaiveBayesParameters(Set<T> allTokens, Map<C, double[]> classProbabilities, Map<C, Integer> classCount, int numPosts) {
        this.allTokens = allTokens;
        this.classProbabilities = classProbabilities;
        this.classCount = classCount;
        this.numPosts = numPosts;
    }
    
    public Set<T> getAllTokens() {
        return allTokens;
    }

    public void setAllTokens(Set<T> allTokens) {
        this.allTokens = allTokens;
    }

    public Map<C, double[]> getClassProbabilities() {
        return classProbabilities;
    }

    public void setClassProbabilities(Map<C, double[]> classProbabilities) {
        this.classProbabilities = classProbabilities;
    }

    public Map<C, Integer> getClassCount() {
        return classCount;
    }

    public void setClassCount(Map<C, Integer> classCount) {
        this.classCount = classCount;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }         
}
