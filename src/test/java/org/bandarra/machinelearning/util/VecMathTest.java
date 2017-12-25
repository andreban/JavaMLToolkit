package org.bandarra.machinelearning.util;

import org.junit.Assert;
import org.junit.Test;

public class VecMathTest {
    @Test
    public void testMult() {
        double v1[] = {1, 2, 3, 4, 5};
        double v2[] = {1, 2, 3, 4, 5};
        double expected[] = {1, 4, 9, 16, 25};
        double[] result = VecMath.mult(v1, v2);
        Assert.assertArrayEquals(expected, result, 0);
    }

    @Test
    public void testLog() {
        double values[] = {1, 2, 3, 4, 5};
        double expected[] = {0, 0.6931471805599453d, 1.0986122886681096d, 1.3862943611198906, 1.6094379124341003};
        double result[] = VecMath.log(values);
        Assert.assertArrayEquals(expected, result, 0.000000000000001);
    }

    @Test
    public void testDiv() {
        int values[] = {2, 4, 6, 8, 10};
        double expected[] = {1, 2, 3, 4, 5};
        double actual[] = VecMath.div(values, 2);
        Assert.assertArrayEquals(expected, actual, 0);
    }

    @Test
    public void testSum() {
        double values[] = {1, 2, 3, 4, 5};
        double expected = 15.0d;
        double actual = VecMath.sum(values);
        Assert.assertEquals(expected, actual,0);
    }
}
