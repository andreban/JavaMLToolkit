package org.bandarra.machinelearning.fpgrowth;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParallelFPGrowthTest {
    private static String[][] transactions = {
            {"E", "A", "D", "B"},
            {"D", "A", "C", "E", "B"},
            {"C", "A", "B", "E"},
            {"B", "A", "D"},
            {"D"},
            {"D", "B"},
            {"A", "D", "E"},
            {"B", "C"}
    };

    @Test
    public void testParallelFPGrowth() {
        TransactionSet transactionSet = new TransactionSet();
        Arrays.stream(transactions)
                .map(Transaction::new)
                .forEach(transactionSet::addTransaction);

        int minSupport = 1;//(int) Math.ceil(0.3 * transactionSet.getTransactions().size());

        FPGrowth fpGrowth = new ParallelFPGrowth();
        List<FrequentItemSet> frequentItemSetList = new ArrayList<>();
        fpGrowth.fpGrowth(transactionSet, minSupport, frequentItemSetList::add);
        assertEquals(31, frequentItemSetList.size());
    }
}
