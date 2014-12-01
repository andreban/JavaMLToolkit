package org.bandarra.machinelearning.fpgrowth;

/**
 * Created by andreban on 28/07/14.
 */
public class ItemFrequency implements Comparable<ItemFrequency> {
    private String name;
    private int count;

    public ItemFrequency(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(ItemFrequency o) {
        return Integer.compare(o.count, this.count);
    }

    @Override
    public String toString() {
        return "ItemFrequency{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
