/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processing;

import java.util.*;

/**
 *
 * @author lianduan
 *
 * buildDatabaseTree(ArrayList transactions): make use of
 * buildTransactionTree(ArrayList transaction) and buildSubTree(Node
 * cur_node,ArrayList sub_trans) to build the prefix tree
 *
 * buildTwoLayerTree(ArrayList transaction): this function is used to save the
 * item and pair support information
 *
 * BuildCurrentLayerTree(ArrayList itemset): build the tree branch for the given itemset
 * 
 * updateCorrelationTree(ArrayList itemset, double correlation): this function
 * is used to update the correlation saved in the tree.
 *
 * getTotalOccurrence()
 *
 * getOccurrence(ArrayList itemset)
 *
 * getCorrelationFromTree(ArrayList itemset)
 *
 */
public final class prefixTree {

    public Node root = new Node();
    private GeneralFunction gf = new GeneralFunction();

    public class Node {

        int occurrence = 0;
        double correlation = 0;
        Boolean mark = true;
        HashMap child = new HashMap();
    }

    prefixTree() {
    }

    prefixTree(String transaction_file) {
    	//the gf gts  the list of terms for each document
        buildDatabaseTree(gf.txt.ReadTransaction(transaction_file, Boolean.TRUE));
    }

    public void buildDatabaseTree(ArrayList transactions) {
        //transactions contain many individual transaction. Each transaction contains many items.
        for (int i = 0; i < transactions.size(); i++) {
            gf.prog.showProcessedRecords(50000);
            ArrayList transaction = (ArrayList) transactions.get(i);
            buildTransactionTree(transaction);
        }
    }

    public void buildTransactionTree(ArrayList transaction) {
        //We add the node according to the sequence given in transaction. We don't reorder it.
        buildSubTree(root, transaction);
        root.occurrence++;
    }
//the following code does all combinations. We will want to do only the combitations to 3 
    private void buildSubTree(Node cur_node, ArrayList sub_trans) {
        Node next_node = new Node();
        for (int i = 0; i < sub_trans.size(); i++) {
        	//the following lines check if the node has a term from sub_trans as a child node 
            if (!cur_node.child.containsKey(sub_trans.get(i))) {
                cur_node.child.put(sub_trans.get(i), new Node());
            }
            next_node = (Node) cur_node.child.get(sub_trans.get(i));
            next_node.occurrence++;
            ArrayList transaction = new ArrayList();
            for (int j = i + 1; j < sub_trans.size(); j++) {
                transaction.add(sub_trans.get(j));
            }
            buildSubTree(next_node, transaction);
        }
    }

    public void buildTwoLayerTree(ArrayList transaction) {
        root.occurrence++;
        for (int i = 0; i < transaction.size(); i++) {
            Node first_node = new Node();
            if (!root.child.containsKey(transaction.get(i))) {
                root.child.put(transaction.get(i), new Node());
            }
            first_node = (Node) root.child.get(transaction.get(i));
            first_node.occurrence++;
            for (int j = i + 1; j < transaction.size(); j++) {
                Node second_node = new Node();
                if (!first_node.child.containsKey(transaction.get(j))) {
                    first_node.child.put(transaction.get(j), new Node());
                }
                second_node = (Node) first_node.child.get(transaction.get(j));
                second_node.occurrence++;
            }
        }
    }

    public void BuildCurrentLayerTree(ArrayList itemset) {
        Node cur_node = root;
        for (int i = 0; i < itemset.size(); i++) {
            if (!cur_node.child.containsKey(itemset.get(i))) {
                cur_node.child.put(itemset.get(i), new Node());
            }
            cur_node = (Node) cur_node.child.get(itemset.get(i));
        }
    }

    public void updateCorrelationTree(ArrayList itemset, double correlation) {
        Node cur_node = new Node();
        Node next_node = new Node();
        Object cur_path;
        cur_node = root;
        for (int i = 0; i < itemset.size(); i++) {
            cur_path = itemset.get(i);
            if (!cur_node.child.containsKey(cur_path)) {
                cur_node.child.put(cur_path, new Node());
            }
            next_node = (Node) cur_node.child.get(cur_path);
            cur_node = next_node;
        }
        cur_node.correlation = correlation;
    }

    public double getTotalOccurrence() {
        return root.occurrence;
    }

    public double getOccurrence(ArrayList itemset) {
        Node cur_node = new Node();
        Node next_node = new Node();
        Object cur_path;
        cur_node = root;
        for (int i = 0; i < itemset.size(); i++) {
            cur_path = itemset.get(i);
            if (!cur_node.child.containsKey(cur_path)) {
                return 0;
            }
            next_node = (Node) cur_node.child.get(cur_path);
            cur_node = next_node;
        }
        return cur_node.occurrence;
    }

    public double getCorrelationFromTree(ArrayList itemset) {
        Node cur_node = new Node();
        Node next_node = new Node();
        Object cur_path;
        cur_node = root;
        for (int i = 0; i < itemset.size(); i++) {
            cur_path = itemset.get(i);
            if (!cur_node.child.containsKey(cur_path)) {
                return 0;
            }
            next_node = (Node) cur_node.child.get(cur_path);
            cur_node = next_node;
        }
        return cur_node.correlation;
    }

    /*
     public void travelForTopKCorrelatedItemset(Node cur_node, ArrayList prefix, ArrayList array, int k, double cc, String type) {
     Node child_node = new Node();
     ArrayList full_path = new ArrayList();
     Object cur_name;
     for (int i = 0; i < prefix.size(); i++) {
     full_path.add(prefix.get(i));
     }
     for (Enumeration e = (Enumeration) cur_node.child.keySet(); e.hasMoreElements();) {
     cur_name = (Object) e.nextElement();
     ArrayList temp_prefix = new ArrayList();
     for (int i = 0; i < full_path.size(); i++) {
     temp_prefix.add(full_path.get(i));
     }
     temp_prefix.add(cur_name);
     child_node = (Node) cur_node.child.get(cur_name);
     gf.topk.TopKPushing(array, getCorrelation(temp_prefix, cc, type), new ArrayList(), k);
     travelForTopKCorrelatedItemset(child_node, temp_prefix, array, k, cc, type);
     }
     }
    
     public String showTopKCorrelatedItemset(ArrayList result, Boolean show_details) {
     String output = "";
     for (int i = 0; i < result.size(); i++) {
     String show = "";
     double value = (Double) ((ArrayList) result.get(i)).get(0);
     ArrayList itemset = (ArrayList) ((ArrayList) result.get(i)).get(1);
     show += itemset + ", " + gf.roundDecimals(value, 4) + ", ";
     for (int j = 0; j < itemset.size(); j++) {
     Object item = itemset.get(j);
     if (show_details) {
     show += "(" + items.get(j) + "), ";
     }
     }
     output += show.substring(0, show.length() - 2) + ".\n";
     }
     return output;
     }

     public ArrayList getTopKCorrelatedItemset(Node root_node, int k, double cc, String type) {
     ArrayList array = gf.topk.getOriginalVector4TopKPushing();
     ArrayList prefix = new ArrayList();
     travelForTopKCorrelatedItemset(root_node, prefix, array, k, cc, type);
     return array;
     }
     */
    public static void main(String[] args) {
        // TODO code application logic here
        prefixTree tmp = new prefixTree("D:/test.txt");
        System.out.println();



    }
}
