package org.bandarra.machinelearning.fpgrowth;

import java.io.Serializable;
import java.util.*;

/**
 * Created by andreban on 28/07/14.
 */
public class FPTree implements Serializable {

    public static class FPNode implements Serializable {
        private Map<String, FPNode> childNodes = new HashMap<>();
        private int hitcount = 0;
        private String name;

        public void addTransaction(Transaction transaction, int pos) {
            hitcount += transaction.getCount();

            if (pos >= transaction.getItems().size()) {
                return;
            }

            FPNode child = childNodes.get(transaction.getItems().get(pos));
            if (child == null) {
                child = new FPNode();
                child.name = transaction.getItems().get(pos);
                childNodes.put(child.name , child);
            }
            child.addTransaction(transaction, pos + 1);
        }

        public List<Transaction.Builder> buildConditionalTransactions(String name) {
            if (this.name != null && this.name.equals(name)) {
                List<Transaction.Builder> paths = new ArrayList<>();
                paths.add(new Transaction.Builder(this.getHitcount()));
                return paths;
            }

            if (this.childNodes.isEmpty()) {
                Collections.emptyList();
            }

            List<Transaction.Builder> allpaths = new ArrayList<>();
            for (FPNode child: childNodes.values()) {
                List<Transaction.Builder> nodePath = child.buildConditionalTransactions(name);
                for (Transaction.Builder transactionBuilder: nodePath) {
                    if (this.name != null) {
                        transactionBuilder.addItem(this.name);
                    }
                    allpaths.add(transactionBuilder);
                }

            }
            return allpaths;
        }


        public int getHitcount() {
            return hitcount;
        }
    }

    private FPNode rootNode = new FPNode();

    public void addTransaction(Transaction transaction) {
        rootNode.addTransaction(transaction, 0);
    }

    public TransactionSet buildConditionalTransactions(String name) {
        TransactionSet transactionSet = new TransactionSet();
        for (Transaction.Builder builder: rootNode.buildConditionalTransactions(name)){
            transactionSet.addTransaction(builder.build());
        }
        return transactionSet;
    }
}
