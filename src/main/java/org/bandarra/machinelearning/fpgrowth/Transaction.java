package org.bandarra.machinelearning.fpgrowth;

import java.util.*;

/**
 * Created by andreban on 28/07/14.
 */
public class Transaction {
    public static class Builder {
        private int count;
        private LinkedList<String> items = new LinkedList<>();
        public Builder(int count) {
            this.count = count;
        }

        public Builder addItem(String item) {
            items.addFirst(item);
            return this;
        }

        public Transaction build() {
            return new Transaction(new ArrayList<>(items), count);
        }
    }
    private ArrayList<String> items;
    private int count;

    public Transaction(ArrayList<String> items, int count) {
        this.items = items;
        this.count = count;
    }

    public Transaction(int count) {
        this.items = new ArrayList<>();
        this.count = count;
    }

    public Transaction() {

    }

    public Transaction(List<String> items) {
        this(new ArrayList<>(items), 1);
    }

    public Transaction(String[] items) {
        this(new ArrayList<>(Arrays.asList(items)), 1);
    }

    public List<String> getItems() {
        return items;
    }

    public int getCount() {
        return count;
    }

    public void sortItems(final Map<String, Integer> itemPriorities) {
        items.sort((o1, o2) -> {
            int result = Integer.compare(itemPriorities.get(o2), itemPriorities.get(o1));
            if (result != 0) {
                return result;
            }
            return o1.compareTo(o2);
        });
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "items=" + items +
                ", count=" + count +
                '}';
    }
}
