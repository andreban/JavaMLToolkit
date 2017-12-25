package org.bandarra.machinelearning.naivebayes;

import java.io.Serializable;
import java.util.*;
import static org.bandarra.machinelearning.util.VecMath.*;

/**
 *
 * @author andreban
 */
public class NaiveBayesTrainer<T extends Serializable, C extends Serializable> {
    public NaiveBayesParameters<T, C> train(List<Set<T>> documents, List<C> classes) {
        Set<T> allTokens;
        Map<C, double[]> classProbabilities;
        Map<C, Integer> classCount;
        int numPosts;        
        
        Map<Integer, Set<T>> postsTokens = new HashMap<>();
        numPosts = documents.size();
        allTokens = new TreeSet<>();
        
        //Tokenize and count the Input
        for (int i = 0; i < documents.size(); i++) {
            Set<T> uniqueTokens = documents.get(i);
            postsTokens.put(i, uniqueTokens);
            allTokens.addAll(uniqueTokens);
        }        
                
        Map<Integer, boolean[]> postContains = new HashMap<>();
        for (Map.Entry<Integer, Set<T>> post: postsTokens.entrySet()) {
            boolean[] contains = new boolean[allTokens.size()];
            int i = 0;
            for (T token: allTokens) {
                contains[i++] = post.getValue().contains(token);
            }
            postContains.put(post.getKey(), contains);
        }
        
        //How Many post in each class?
        classCount = new HashMap<>();
        for (C b: classes) {
            Integer count = classCount.get(b);
            if (count == null) {
                count = 0;                
            }
            count++;
            classCount.put(b, count);
        }
        
        Map<C, int[]> pNums = new HashMap<>();
        Map<C, Double> pDenoms = new HashMap<>();
        for (C clazz: classCount.keySet()) {
            int[] pNum = new int[allTokens.size()];
            Arrays.fill(pNum, 1);
            pNums.put(clazz, pNum);
            pDenoms.put(clazz, 2.0d);
        }
                
        for (Map.Entry<Integer, boolean[]> documentEntry: postContains.entrySet()) {
            C documentClass = classes.get(documentEntry.getKey());
            double pDenom = pDenoms.get(documentClass);
            int[] pnum = pNums.get(documentClass);
            for (int i = 0; i < pnum.length; i++) {
                if (documentEntry.getValue()[i]) {
                    pnum[i] += 1;
                    pDenom += 1;
                }
            }
            pNums.put(documentClass, pnum);
            pDenoms.put(documentClass, pDenom);                              
        }
        
        classProbabilities = new HashMap<>();
        for (C clazz: classCount.keySet()) {
            int[] pNum = pNums.get(clazz);
            double pDenom = pDenoms.get(clazz);
            double[] p = log(div(pNum, pDenom));            
            classProbabilities.put(clazz, p);
        }         
        
        return new NaiveBayesParameters<>(allTokens, classProbabilities, classCount, numPosts);
    }
}
