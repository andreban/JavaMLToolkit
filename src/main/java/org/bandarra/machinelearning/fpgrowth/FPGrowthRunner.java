package org.bandarra.machinelearning.fpgrowth;

import java.util.*;

/**
 *
 * http://hareenlaks.blogspot.com.br/2011/06/fp-tree-example-how-to-identify.html
 *
 * Created by andreban on 28/07/14.
 */
public class FPGrowthRunner {

    private static class FPGrowthRunnerCollector implements RegularFPGrowth.FrequentItemSetCollector {
        private List<FrequentItemSet> frequentItemSetList = new ArrayList<>();

        public List<FrequentItemSet> getFrequentItemSetList() {
            return this.frequentItemSetList;
        }
        @Override
        public void collect(FrequentItemSet frequentItemSet) {
            frequentItemSetList.add(frequentItemSet);
//            System.out.println(frequentItemSet);
        }
    }
    private static String[][] transactions = {
            {"E", "A", "D", "B"},
            {"D", "A", "C", "E", "B"},
            {"C", "A", "B", "E"},
            {"B", "A", "D"},
            {"D"},
            {"D", "B"},
            {"A", "D", "E"},
            {"B", "C"}
//            ,
//            {"A", "Z"},            {"A", "Z"},            {"A", "Z"},            {"A", "Z"}
    };

    private static class ItemRecomendationRanking implements Comparable<ItemRecomendationRanking> {
        private FrequentItemSet recommendedFrequentItemSet;
        private FrequentItemSet baseFrequentItemSet;
        private double confidence;


        private ItemRecomendationRanking() {

        }

        public FrequentItemSet getRecommendedFrequentItemSet() {
            return recommendedFrequentItemSet;
        }

        public void setRecommendedFrequentItemSet(FrequentItemSet recommendedFrequentItemSet) {
            this.recommendedFrequentItemSet = recommendedFrequentItemSet;
        }

        public FrequentItemSet getBaseFrequentItemSet() {
            return baseFrequentItemSet;
        }

        public void setBaseFrequentItemSet(FrequentItemSet baseFrequentItemSet) {
            this.baseFrequentItemSet = baseFrequentItemSet;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        @Override
        public int compareTo(ItemRecomendationRanking o) {
            int result;
            result = Double.compare(o.getConfidence(), this.getConfidence());
            if (result != 0) {
                return result;
            }
            result = Integer.compare(
                    o.baseFrequentItemSet.getItems().size(), this.baseFrequentItemSet.getItems().size());
            if (result != 0) {
                return result;
            }

            return Integer.compare(
                    o.recommendedFrequentItemSet.getFrequency(), this.recommendedFrequentItemSet.getFrequency());
        }

        @Override
        public String toString() {
            return "ItemRecomendationRanking{" +
                    "recommendedFrequentItemSet=" + recommendedFrequentItemSet +
                    ", baseFrequentItemSet=" + baseFrequentItemSet +
                    ", confidence=" + confidence +
                    '}';
        }
    }

    private static class ItemConfidence {
        private String item;
        private double confidence;

        private ItemConfidence(String item, double confidence) {
            this.item = item;
            this.confidence = confidence;
        }
    }

    public static void main(String[] args) {
        TransactionSet transactionSet = new TransactionSet();
        for (String[] t: transactions) {
            Transaction transaction = new Transaction(t);
            transactionSet.addTransaction(transaction);
        }

        int minSupport = 1;//(int) Math.ceil(0.3 * transactionSet.getTransactions().size());

//        FPGrowth fpGrowth = new RegularFPGrowth();
        FPGrowth fpGrowth = new ParallelFPGrowth();
        FPGrowthRunnerCollector collector = new FPGrowthRunnerCollector();
        fpGrowth.fpGrowth(transactionSet, minSupport, collector);


        //Build a Map that knows which item sets contains which items.
        Map<String, Set<FrequentItemSet>> itemFrequentItemSetsMap = new HashMap<>();
        for (FrequentItemSet  frequentItemSet: collector.getFrequentItemSetList()) {
            for (String item: frequentItemSet.getItems()) {
                Set<FrequentItemSet> frequentItemSets = itemFrequentItemSetsMap.get(item);
                if (frequentItemSets == null) {
                    frequentItemSets = new HashSet<>();
                    itemFrequentItemSetsMap.put(item, frequentItemSets);
                }
                frequentItemSets.add(frequentItemSet);
            }
        }


        //this is the item set we are researching
        Set<String> keys = new HashSet<>(Arrays.asList("A","D"));

        //build a collection with all item sets that have those items.
        Set<FrequentItemSet> keysFrequentItemsSets = new HashSet<>();
        for (String key:keys) {
            Set<FrequentItemSet> keyFrequentItemSet = itemFrequentItemSetsMap.get(key);
            if (keyFrequentItemSet != null) {
                keysFrequentItemsSets.addAll(keyFrequentItemSet);
            }
        }

        //Separate which item sets will be used to harvest items and which will be used
        //as a base to calculate confidence
        Set<FrequentItemSet> fullFrequentItems = new HashSet<>();
        Set<FrequentItemSet> partialFrequentItems = new HashSet<>();
        for (FrequentItemSet frequentItemSet: keysFrequentItemsSets) {
            if (keys.containsAll(frequentItemSet.getItems())) {
                fullFrequentItems.add(frequentItemSet);
            } else {
                partialFrequentItems.add(frequentItemSet);
            }
        }

        List<ItemRecomendationRanking> itemRecomendationRankingList = new ArrayList<>();
        for (FrequentItemSet partialFrequentItem: partialFrequentItems) {
            double bestConfidence = Double.NaN;
            FrequentItemSet bestFullFrequentItem = null;
            for (FrequentItemSet fullFrequentItem: fullFrequentItems) {
                if (!partialFrequentItem.getItems().containsAll(fullFrequentItem.getItems())) {
                    continue;
                }
                double confidence = partialFrequentItem.getSupport() / fullFrequentItem.getSupport();
                if (Double.isNaN(bestConfidence) || confidence > bestConfidence) {
                    bestConfidence = confidence;
                    bestFullFrequentItem = fullFrequentItem;
                }
            }
            ItemRecomendationRanking itemRecomendationRanking = new ItemRecomendationRanking();
            itemRecomendationRanking.setBaseFrequentItemSet(bestFullFrequentItem);
            itemRecomendationRanking.setRecommendedFrequentItemSet(partialFrequentItem);
            itemRecomendationRanking.setConfidence(bestConfidence);
            itemRecomendationRankingList.add(itemRecomendationRanking);
        }

        Collections.sort(itemRecomendationRankingList);
        Set<String> reviewedItems = new HashSet<>();
        for (ItemRecomendationRanking itemRecomendationRanking: itemRecomendationRankingList) {
            HashSet<String> recommendedItems = new HashSet<>();
            recommendedItems.addAll(itemRecomendationRanking.getRecommendedFrequentItemSet().getItems());
            recommendedItems.removeAll(itemRecomendationRanking.getBaseFrequentItemSet().getItems());
            for (String item: recommendedItems) {
                if (!reviewedItems.contains(item)) {
                    System.out.println(item + ": " + itemRecomendationRanking.getConfidence());
                    reviewedItems.add(item);
                }
            }
        }
    }
}
