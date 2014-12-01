package org.bandarra.machinelearning.fpgrowth;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by andreban on 01/12/14.
 */
public class RegularFPGrowthTest {
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
        for (String[] t: transactions) {
            Transaction transaction = new Transaction(t);
            transactionSet.addTransaction(transaction);
        }

        int minSupport = 1;//(int) Math.ceil(0.3 * transactionSet.getTransactions().size());

        FPGrowth fpGrowth = new ParallelFPGrowth();
        List<FrequentItemSet> frequentItemSetList = new ArrayList<>();
        FPGrowth.FrequentItemSetCollector collector = (item) -> frequentItemSetList.add(item);
        fpGrowth.fpGrowth(transactionSet, minSupport, collector);
        assertEquals(31, frequentItemSetList.size());
    }

    @Test
    public void testRegularFPGrowth() {
        TransactionSet transactionSet = new TransactionSet();
        for (String[] t: transactions) {
            Transaction transaction = new Transaction(t);
            transactionSet.addTransaction(transaction);
        }

        int minSupport = 1;//(int) Math.ceil(0.3 * transactionSet.getTransactions().size());

        FPGrowth fpGrowth = new RegularFPGrowth();
        List<FrequentItemSet> frequentItemSetList = new ArrayList<>();
        FPGrowth.FrequentItemSetCollector collector = (item) -> frequentItemSetList.add(item);
        fpGrowth.fpGrowth(transactionSet, minSupport, collector);
        assertEquals(31, frequentItemSetList.size());
    }
}
