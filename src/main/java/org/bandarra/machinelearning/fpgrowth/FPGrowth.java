package org.bandarra.machinelearning.fpgrowth;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by andreban on 08/09/14.
 */
public interface FPGrowth {
    @FunctionalInterface
    interface FrequentItemSetCollector {
        void collect(FrequentItemSet frequentItemSet);
    }

    void fpGrowth(TransactionSet transactionSet, int minSupport, FrequentItemSetCollector collector);

}
