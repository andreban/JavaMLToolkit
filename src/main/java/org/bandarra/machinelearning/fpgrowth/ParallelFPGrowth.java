package org.bandarra.machinelearning.fpgrowth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Created by andreban on 08/09/14.
 */
public class ParallelFPGrowth extends AbstractFPGrowth {
    private static abstract class AbstractFPGrowthTask extends  RecursiveAction {
        private int minSupport;
        protected LinkedList<String> list;
        private FrequentItemSetCollector collector;
        private int parentFrequency;
        private int numTransactions;

        /* package */ AbstractFPGrowthTask(int minSupport, LinkedList<String> list, FrequentItemSetCollector collector,
                int parentFrequency, int numTransactions) {
            this.minSupport = minSupport;
            this.list = list;
            this.collector = collector;
            this.parentFrequency = parentFrequency;
            this.numTransactions = numTransactions;
        }

        /* package */ void doFPComputing(FPTree fpTree, List<ItemFrequency> frequencies ) {
            List<InternalFPGrowthTask> taskList = new ArrayList<>();
            for (ItemFrequency frequency: frequencies) {
                if (frequency.getCount() < minSupport) {
                    continue;
                }

                LinkedList<String> newList = new LinkedList<>(list);
                newList.add(frequency.getName());

                double support = ((double)frequency.getCount() / numTransactions);

                FrequentItemSet frequentItemSet = new FrequentItemSet(new HashSet<>(newList),
                        frequency.getCount(), support);
                collector.collect(frequentItemSet);

                InternalFPGrowthTask fpGrowthTask = new InternalFPGrowthTask(fpTree, minSupport, newList,
                        collector,frequency.getCount(), numTransactions);
                taskList.add(fpGrowthTask);
            }
            invokeAll(taskList);
        }

    }

    private static class InternalFPGrowthTask extends AbstractFPGrowthTask {
        private FPTree parentFPTree;

        private InternalFPGrowthTask(FPTree parentFPTree, int minSupport, LinkedList<String> list,
                FrequentItemSetCollector collector, int parentFrequency, int numTransactions) {
            super(minSupport, list, collector, parentFrequency, numTransactions);
            this.parentFPTree = parentFPTree;
        }

        @Override
        protected void compute() {
            TransactionSet transactionSet = parentFPTree.buildConditionalTransactions(list.getLast());
            List<ItemFrequency> frequencies = transactionSet.calculateFrequencies();
            transactionSet.sortTransactions(frequencies);
            FPTree fpTree = new FPTree();
            transactionSet.getTransactions()
                    .forEach(fpTree::addTransaction);
            doFPComputing(fpTree, frequencies);

        }
    }

    private static class FPGrowthTask extends AbstractFPGrowthTask {
        private FPTree fpTree;
        private List<ItemFrequency> frequencies;
        private FPGrowthTask(FPTree fpTree, List<ItemFrequency> frequencies, int minSupport, LinkedList<String> list,
                             FrequentItemSetCollector collector, int parentFrequency, int numTransactions) {
            super(minSupport,list, collector, parentFrequency, numTransactions);
            this.fpTree = fpTree;
            this.frequencies = frequencies;
        }

        @Override
        protected void compute() {
            doFPComputing(fpTree, frequencies);
        }
    }

    private ForkJoinPool forkJoinPool;

    public ParallelFPGrowth() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    public ParallelFPGrowth(int numThreads) {
        forkJoinPool = new ForkJoinPool(numThreads);
    }

    @Override
    protected void fpGrowth(FPTree fpTree, List<ItemFrequency> frequencies, int minSupport,
                         LinkedList<String> list, FrequentItemSetCollector collector,
                         int parentFrequency, int numTransactions) {
        FPGrowthTask fpGrowthTask = new FPGrowthTask(fpTree, frequencies, minSupport,
                list, collector,parentFrequency, numTransactions);
        forkJoinPool.invoke(fpGrowthTask);
    }

    @Override
    public void fpGrowth(TransactionSet transactionSet, int minSupport, FrequentItemSetCollector collector) {
        fpGrowth(transactionSet, minSupport, new LinkedList<>(), collector,0, transactionSet.size());
    }

}
