package org.bandarra.machinelearning.fpgrowth;

import java.util.Set;

/**
 * Created by andreban on 29/07/14.
 */
public class FrequentItemSet implements Comparable<FrequentItemSet> {
    private Set<String> items;
    private int frequency;
    private double support;

    public FrequentItemSet(Set<String> items, int frequency, double support) {
        this.items = items;
        this.frequency = frequency;
        this.support = support;
    }

    public double getSupport() {
        return support;
    }

    public Set<String> getItems() {
        return items;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public int compareTo(FrequentItemSet o) {
        return Integer.compare(o.getFrequency(), this.getFrequency());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FrequentItemSet that = (FrequentItemSet) o;

        if (items != null ? !items.equals(that.items) : that.items != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return items != null ? items.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FrequentItemSet{" +
                "items=" + items +
                ", frequency=" + frequency +
                ", support=" + support +
                '}';
    }
}
