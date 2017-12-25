package org.bandarra.machinelearning.fpgrowth;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by andreban on 29/07/14.
 */

/**
 * http://en.wikipedia.org/wiki/Association_rule_learning
 */
public class RegularFPGrowth extends AbstractFPGrowth {
    @Override
    public void fpGrowth(TransactionSet transactionSet, int minSupport,FrequentItemSetCollector collector) {
        fpGrowth(transactionSet,minSupport, new LinkedList<>(), collector, 0, transactionSet.size());
    }

    @Override
    protected void fpGrowth(FPTree fpTree, List<ItemFrequency> frequencies, int minSupport, LinkedList<String> list,
            FrequentItemSetCollector collector, int parentFrequency, int numTransactions) {
        for (ItemFrequency frequency: frequencies) {
            if (frequency.getCount() < minSupport) {
                continue;
            }
            LinkedList<String> newList = new LinkedList<>(list);
            newList.add(frequency.getName());

            double support = ((double)frequency.getCount() / numTransactions);

            FrequentItemSet frequentItemSet =
                    new FrequentItemSet(new HashSet<>(newList), frequency.getCount(), support);
            collector.collect(frequentItemSet);

            TransactionSet freqTransactionSet = fpTree.buildConditionalTransactions(frequency.getName());
            fpGrowth(freqTransactionSet, minSupport, newList, collector, frequency.getCount(), numTransactions);
        }
    }
}
