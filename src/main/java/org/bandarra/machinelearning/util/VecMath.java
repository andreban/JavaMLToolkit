package org.bandarra.machinelearning.util;

/**
 *
 * @author andreban
 */
public class VecMath {
   private VecMath() {}
   
    public static double[] mult(double[] v1, double[] v2) {
       double[] result = new double[v1.length];
       for (int i = 0; i < result.length; i++) {
           result[i] = v1[i] * v2[i];
       }
       return result;
    }
    
    public static double[] log(double[] input) {
        double[] result = new double[input.length];        
        for (int i = 0; i < input.length; i++) {
            result[i] = Math.log(input[i]);
        }
        return result;
    }
    public static double[] div(int[] input, double value) {
        double[] result = new double[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = input[i] / value;
        }
        return result;
    }    
    
    public static double sum(double[] input) {
        double result = 0.0d;
        for (int i = 0; i < input.length; i++) {
            result += input[i];
        }
        return result;
    }   
}
