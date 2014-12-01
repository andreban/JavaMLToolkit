package org.bandarra.machinelearning.fpgrowth;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by andreban on 08/09/14.
 */
public interface FPGrowth {
    public interface FrequentItemSetCollector {
        public void collect(FrequentItemSet frequentItemSet);
    }

    public void fpGrowth(FPTree fpTree, List<ItemFrequency> frequencies, int minSupport,
                         LinkedList<String> list, FrequentItemSetCollector collector,
                         int parentFrequency, int numTransactions);

    public void fpGrowth(TransactionSet transactionSet, int minSupport, FrequentItemSetCollector collector);
    public void fpGrowth(TransactionSet transactionSet, int minSupport, LinkedList<String> list,
                         FrequentItemSetCollector collector, int parentFrequency, int numTransactions);
}
