package org.bandarra.machinelearning.naivebayes;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import static org.bandarra.machinelearning.util.VecMath.*;

/**
 *
 * @author andreban
 */
public class NaiveBayesClassifier<T extends Serializable, C extends Serializable> {
    public static class ClassificationResult<C extends Serializable> implements Comparable<ClassificationResult> {
        private C clazz;
        private double probability;

        public ClassificationResult(C clazz, double probability) {
            this.clazz = clazz;
            this.probability = probability;
        }

        public C getClazz() {
            return clazz;
        }

        public void setClazz(C clazz) {
            this.clazz = clazz;
        }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }        
                
        @Override
        public int compareTo(ClassificationResult o) {
            return Double.compare(o.probability, this.probability);
        }
        
    }

    public TreeSet<ClassificationResult<C>> classify(NaiveBayesParameters<T, C> parameters, Set<T> document) {
        double[] tokens = getWords(parameters.getAllTokens(), document);
        
        TreeSet<ClassificationResult<C>> results = new TreeSet<>();
        for (C clazz:  parameters.getClassCount().keySet()) {
            double classProb = (double)parameters.getClassCount().get(clazz) / parameters.getNumPosts();
            double[] pclass = parameters.getClassProbabilities().get(clazz);
            double p = sum(mult(tokens, pclass)) + Math.log(classProb);
            results.add(new ClassificationResult<>(clazz, p));
        }
        return results;
    }
    
    private double[] getWords(Set<T> allTokens, Set<T> tokenSet) {
        double[] contains = new double[allTokens.size()];
        int i = 0;
        for (T token: allTokens) {
            contains[i++] = tokenSet.contains(token) ? 1: 0;
        }    
        return contains;
    }    
}
