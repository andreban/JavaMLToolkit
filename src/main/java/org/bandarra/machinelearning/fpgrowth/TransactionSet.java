package org.bandarra.machinelearning.fpgrowth;

import java.util.*;

/**
 * Created by andreban on 28/07/14.
 */
public class TransactionSet {
    private List<Transaction> transactionList = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        this.transactionList.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactionList;
    }

    public List<ItemFrequency> calculateFrequencies() {
        Map<String, Integer> frequencies = new HashMap<>();
        for (Transaction transaction: transactionList) {
            for (String item: transaction.getItems()) {
                Integer frequency = frequencies.get(item);
                if (frequency == null) {
                    frequency = transaction.getCount();
                } else {
                    frequency = frequency + transaction.getCount();
                }
                frequencies.put(item, frequency);
            }
        }
        List<ItemFrequency> frequencyList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: frequencies.entrySet()) {
            frequencyList.add(new ItemFrequency(entry.getKey(), entry.getValue()));
        }
        Collections.sort(frequencyList);
        return frequencyList;
    }

    public void sortTransactions(List<ItemFrequency> frequencies) {
        final Map<String, Integer> nameFrequencyMap = new HashMap<>();
        for (ItemFrequency f: frequencies) {
            nameFrequencyMap.put(f.getName(), f.getCount());
        }

        for (Transaction t: transactionList) {
            t.sortItems(nameFrequencyMap);
        }
    }

    @Override
    public String toString() {
        return "TransactionSet{" +
                "transactionList=" + transactionList +
                '}';
    }

    public int size() {
        return transactionList.size();
    }


}
