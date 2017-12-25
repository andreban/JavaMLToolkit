package org.bandarra.machinelearning.fpgrowth;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractFPGrowth implements FPGrowth {

    protected abstract void fpGrowth(FPTree fpTree, List<ItemFrequency> frequencies, int minSupport,
                  LinkedList<String> list, FrequentItemSetCollector collector,
                  int parentFrequency, int numTransactions);

    protected void fpGrowth(TransactionSet transactionSet, int minSupport, LinkedList<String> list,
                         FrequentItemSetCollector collector, int parentFrequency, int numTransactions) {
        List<ItemFrequency> frequencies = transactionSet.calculateFrequencies();
        transactionSet.sortTransactions(frequencies);
        FPTree fpTree = new FPTree();
        transactionSet.getTransactions().forEach(fpTree::addTransaction);
        fpGrowth(fpTree, frequencies, minSupport, list, collector, parentFrequency, numTransactions);
    }
}
