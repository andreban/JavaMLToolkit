package org.bandarra.machinelearning.util;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author andreban
 */
public class VecMath {
   private VecMath() {}
   
    public static double[] mult(double[] v1, double[] v2) {
//       return Stream.iterate(0, i -> i + 1)
//               .limit(v1.length)
//               .parallel()
//               .mapToDouble(i -> v1[i] * v2[i])
//               .toArray();

       double[] result = new double[v1.length];
       for (int i = 0; i < result.length; i++) {
           result[i] = v1[i] * v2[i];
       }
       return result;
    }
    
    public static double[] log(double[] input) {
//       return Arrays.stream(input)
//               .parallel()
//               .map(Math::log)
//               .toArray();
        double[] result = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = Math.log(input[i]);
        }
        return result;
    }
    public static double[] div(int[] input, double value) {
//       return Arrays.stream(input)
//               .parallel()
//               .mapToDouble(i -> i / value)
//               .toArray();
        double[] result = new double[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = input[i] / value;
        }
        return result;
    }    
    
    public static double sum(double[] input) {
//       return Arrays.stream(input)
//              .parallel()
//              .sum();
        double result = 0.0d;
        for (int i = 0; i < input.length; i++) {
            result += input[i];
        }
        return result;
    }   
}
