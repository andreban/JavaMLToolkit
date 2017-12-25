package org.bandarra.machinelearning.fpgrowth;

import java.util.*;
import java.util.stream.Collectors;

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
                frequencies.merge(item, transaction.getCount(), Integer::sum);
            }
        }

        return frequencies.entrySet()
                .stream()
                .map(entry -> new ItemFrequency(entry.getKey(), entry.getValue()))
                .sorted()
                .collect(Collectors.toList());
    }

    public void sortTransactions(List<ItemFrequency> frequencies) {
        Map<String, Integer> nameFrequencyMap =
                frequencies.stream().collect(Collectors.toMap(ItemFrequency::getName, ItemFrequency::getCount));

        transactionList.forEach(t -> t.sortItems(nameFrequencyMap));
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
