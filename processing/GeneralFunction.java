/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processing;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * the binomial correlation calculation is under development when use
 * bitset.flip(start_id,end_id) start_id=0, end_id=the end location+1;
 *
 * @author ldn generateRandomSequence(int datasize): return a shuffled sequence
 * with value from 0 to datasize-1 The way to convert an array to an ArrayList:
 * String[] items=itemset.split(","); ArrayList trans=new ArrayList();
 * trans.addAll(Arrays.asList(items)); getBitSet(int[] list): return the BitSet
 * map the list[] information. the int array[i] indicates the i-th position
 * value getBitSet(ArrayList list, int n): the ArrayList only save the position
 * where the value is 1. The object in ArrayList is Integer not String Pause(int
 * mil_sec): the program will stay for mil_sec roundDecimals(double value, int
 * decimal_place): return the double value with the require decimals Class
 * ChineseName.check(String name): check the pinyin is a Chinese name or not
 * Class Progress: showProcessedRecords(int interval): When it processes the a
 * bundle of interval records, show how many records has been process.
 * getCurrentTime(): get the long value of current time showConsumedTime(): show
 * the spend time between the current time and the last stored time
 * showConsumedTime(long his_time): show the time different between the current
 * time and the parameter Class HandleTxt: clearContent(String filename): clear
 * the content in a given file. Used alone. getWriter(String filename): get the
 * writer for a given file appendNewContent(String newline): add newline to the
 * end of file. Must be used together with getWriter(String filename) and
 * closeWriter(). closeWriter(): close the writer for a given file
 * ReadItem(String filename, Boolean getInteger): read each line as an item and
 * saved in ArrayList. getInteger control the object type in ArrayList
 * ReadTransaction(String filename, Boolean getInteger): read each line as a
 * transaction and saved in ArrayList. getInteger control the object type in
 * ArrayList Class getTopKRecords: getTopKSequence(double[] original_value, int
 * k, Boolean get_max_value) get the sequence_id of the top k records. the
 * computation complexity is between n and kn getOriginalVector4TopKPushing():
 * return a ArrayList for push. TopKPushing(ArrayList original, double value,
 * ArrayList items, int k): compare the value with the information in original
 * to push the information Class QuickSort.sort(double[] org_array, int[]
 * breakRank, Boolean ascending): return the sorted sequence of the original
 * array, breakRank is the extra information to break the tie, and ascending
 * control from small to big or from big to small Class Evaluation
 * BinaryRankingEvaluation(BitSet list, double n, int k, String type): for each
 * value in the list, 1 indicates the corresponding item is interesting and 0
 * indicates the corresponding item is not interesting. Return the evaluation
 * value of AP, K-MAP Weighted by Precision, PRC, K-PRC Weighted by Precision,
 * K-Precision TwoByTwoTableEvaluation(double tp, double fn, double fp, double
 * tn, String type): return the value for Accuracy, Precision, Recall,
 * F-measure, Precision Times Recall, Jaccard
 * RelevanceRankingEvaluation(double[] list, double[] ideal_list, String type):
 * list[i] provide the relevance information at the i-th position. return the
 * value of Cumulative Gain, Normalized Cumulative Gain, Discounted Cumulative
 * Gain, Normalized Discounted Cumulative Gain
 * NonoverlappingClusteringEvaluation(ArrayList detected_group, ArrayList
 * predefined_group, double n, String type): the ArrayList contains BitSet data
 * and each BitSet record a group information. return the value of Group Mutual
 * Information, Group Normalized Mutual Information, Group Purity, Group
 * Inversed Purity, Group F Measure, Pair F Measure, Pair Rand Index, Pair
 * Jaccard NonoverlappingClusterMatching(ArrayList detected_group, ArrayList
 * predefined_group, double n, String type): for each detected group, find the
 * matching ground true group related. return the result of Mutual Information,
 * Normalized Mutual Information, F Measure, Precision, Recall Class Statistics
 * getMean(double[] data) getMean(ArrayList data) getVariance(double[] data)
 * getVariance(ArrayList data) getPowerLawNumber(double base, double total,
 * double exponent): we return the number between base and total, the number
 * follow the powerlaw distribution getPowerLawProbability(double base, double
 * total, double exponent): we return the probability between 0 and 1 which
 * follow the power law distribution Class Correlation any_confidence(double tp,
 * double min_pi) all_confidence(double tp, double max_pi) bond(double tp,
 * double ntp) getMeasureType(): all the possible type getItemsetMeasureType():
 * all the possible measure for itemset getCorrelationType(): all the
 * correlation measure getItemsetCorrelationType(): all the correlation measure
 * for itemset getPairCorrelationType(): all the correlation measure for pair
 * correlation_for_itemset(double tp, double ep, double n, double cc, String
 * type) correlation_for_pair(double oa, double ob, double k, double n, String
 * type) Class Binomial getPMF(int n, int occur, double p, Boolean proxi):
 * calculate the probability mass function for binomial distribution. if use the
 * accurate calculation, we use the exact formula. if use the proximate
 * calculation, we use the exact formula when occur is smaller than 50, and we
 * use normal distribution when occur>=50
 *
 */
public class GeneralFunction {

    public Quicksort qs = new Quicksort();
    public getTopKRecords topk = new getTopKRecords();
    public HandleTxt txt = new HandleTxt();
    public Progress prog = new Progress();
    public Statistics stat = new Statistics();
    public Evaluation eva = new Evaluation();
    

    public int[] generateRandomSequence(int datasize) {//generate the non-redundant sequence
        int i, w;
        int rand;
        Random generator = new Random();
        int[] randomSeq = new int[datasize];
        for (i = 0; i < datasize; i++) {
            randomSeq[i] = i;
        }
        for (i = 0; i < datasize; i++) {
            rand = generator.nextInt();
            rand = Math.abs(rand % (datasize - i)) + i;
            w = randomSeq[i];
            randomSeq[i] = randomSeq[rand];
            randomSeq[rand] = w;
        }
        return randomSeq;
    }

    public BitSet getBitSet(int[] list) {//the int array[i] indicates the i-th position value
        BitSet bits = new BitSet(list.length);
        for (int i = 0; i < list.length; i++) {
            // set only if one
            if (list[i] == 1) {
                bits.set(i);
            }
        }
        return bits;
    }

    public BitSet getBitSet(ArrayList list, int n) {
        // the arraylist save the position where the value is 1.
        // it is different from int[] list which code the information in every position.
        BitSet bits = new BitSet(n);
        for (int i = 0; i < list.size(); i++) {
            bits.set((Integer) list.get(i));
        }
        return bits;
    }

    public void Pause(int mil_sec) {
        try {
            Thread.sleep(mil_sec);
        } catch (InterruptedException e) {
        }
    }

    public double roundDecimals(double value, int decimal_place) {
        if (Double.isNaN(value)) {
            return value;
        } else {
            String format = "#.";
            for (int i = 0; i < decimal_place; i++) {
                format += "#";
            }
            DecimalFormat newform = new DecimalFormat(format);
            return Double.valueOf(newform.format(value));
        }
    }

    public class ChineseName {

        HashMap combination = new HashMap();

        ChineseName() {
            File file = new File("../pingyin.txt");
            FileInputStream fis;
            BufferedInputStream bis;
            DataInputStream dis;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String data = dis.readLine();
                    combination.put(data, "");
                }
                fis.close();
                bis.close();
                dis.close();
            } catch (FileNotFoundException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        public Boolean check(String name) {
            String[] part = name.toLowerCase().split(" ");
            Boolean output = true;
            int[] char_num = new int[2];
            if (part.length == 2) {
                Boolean[] satisfied_part = {false, false};
                for (int i = 0; i < 2; i++) {
                    if (combination.containsKey(part[i])) {
                        satisfied_part[i] = true;
                    } else {
                        for (int j = 1; j < part[i].length() - 1; j++) {
                            String fp = part[i].substring(0, j);
                            String sp = part[i].substring(j);
                            if (combination.containsKey(fp) && combination.containsKey(sp)) {
                                satisfied_part[i] = true;
                                char_num[i] = 2;
                            }
                        }
                    }
                }
                if (!satisfied_part[0] || !satisfied_part[1] || char_num[0] + char_num[1] == 4) {
                    output = false;
                }
            } else {
                output = false;
            }
            return output;
        }
    }

    public class Progress {//used to show the program progress by time or iterations
        //showProcessedRecords(int interval):show how many iterations have been processed
        //getCurrentTime()
        //showConsumedTime(): show the time passed between the current time and the last time being recorded, and update the record time
        //showConsumedTime(long his_time): show the time interval between the current time and historical time

        public long begin_time = new java.util.Date().getTime();
        public long current_time = new java.util.Date().getTime();
        public int count = 0;

        public void showProcessedRecords(int interval) {
            count++;
            if (count % interval == 0) {
                System.out.print("We have processed " + count + " records. ");
                showConsumedTime();
            }
        }

        public long getCurrentTime() {
            return current_time = new java.util.Date().getTime();
        }

        public void showConsumedTime() {
            long his_time = current_time;
            current_time = new java.util.Date().getTime();
            System.out.println("It takes " + (current_time - his_time) + " ms!");
        }

        public long showConsumedTime(long his_time) {
            long cur_time = new java.util.Date().getTime();
            System.out.println("It takes " + (cur_time - his_time) + " ms!");
            return cur_time;
        }
    }

    public class HandleTxt {
        //the class can read and write a file
        //the class to return two common types of files
        //type 1: each row is an attribute, return the ArrayList of each row
        //type 2: each row is items separated by comma, return the ArrayList containing ArrayList

        private FileWriter fWriter = null;
        BufferedWriter writer = null;

        public void getWriter(String filename) {
            try {
                fWriter = new FileWriter(filename, true);
                writer = new BufferedWriter(fWriter);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        public void closeWriter() {
            try {
                writer.close();
                fWriter.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        public void appendNewContent(String newline) {//it must be used together with getWriter and closeWriter
            try {
                writer.write(newline);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        public void clearContent(String filename) {//it is used alone, and doesn't need getWriter or closeWriter.
            try {
                FileWriter tfWriter = new FileWriter(filename);
                BufferedWriter twriter = new BufferedWriter(tfWriter);
                twriter.write("");
                twriter.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        public ArrayList ReadItem(String filename, Boolean getInteger) {
            /* the sample file:
             * 1
             * 2
             * 3
             */
            File file = new File(filename);
            FileInputStream fis;
            BufferedInputStream bis;
            DataInputStream dis;
            ArrayList itemsCode = new ArrayList();
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String data = dis.readLine();
                    if (getInteger) {
                        itemsCode.add(Integer.parseInt(data));
                    } else {
                        itemsCode.add(data);
                    }
                }
                fis.close();
                bis.close();
                dis.close();
            } catch (FileNotFoundException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
            return itemsCode;
        }

        public ArrayList ReadTransaction(String filename, Boolean getInteger) {
            /* the sample file:
             * 23, 56, 79
             * 1, 90, 195
             * 54, 67, 93
             */
            File file = new File(filename);
            FileInputStream fis;
            BufferedInputStream bis;
            DataInputStream dis;
            ArrayList transactions = new ArrayList();
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String data = dis.readLine();
                    ArrayList transaction = new ArrayList();
                    String[] items = data.split(",");
                    for (int i = 0; i < items.length; i++) {
                        if (getInteger) {
                            transaction.add(Integer.parseInt(items[i]));
                        } else {
                            transaction.add(items[i]);
                        }
                    }
                    transactions.add(transaction);
                }
                fis.close();
                bis.close();
                dis.close();
            } catch (FileNotFoundException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
            return transactions;
        }
    }

    public class getTopKRecords {//get the top k value from a given array

        public int[] getTopKSequence(double[] original_value, int k, Boolean get_max_value) {//the computation complexity is between n and kn
            int total_length = original_value.length;
            int[] top_k_sequence = new int[k];
            double[] top_k_value = new double[k];
            if (k == 1) {
                double cur_value = original_value[0];
                int cur_seq = 0;
                if (get_max_value) {
                    for (int i = 0; i < total_length; i++) {
                        if (cur_value < original_value[i]) {
                            cur_value = original_value[i];
                            cur_seq = i;
                        }
                    }
                } else {
                    for (int i = 0; i < total_length; i++) {
                        if (cur_value > original_value[i]) {
                            cur_value = original_value[i];
                            cur_seq = i;
                        }
                    }
                }
                top_k_sequence[0] = cur_seq;
            } else if (k <= total_length) {
                if (get_max_value) {
                    for (int i = 0; i < k; i++) {
                        top_k_value[i] = Double.NEGATIVE_INFINITY;
                    }
                    for (int i = 0; i < total_length; i++) {
                        int position = k;
                        for (int j = 0; j < k - 1; j++) {//try to compare the current value to the jth value counted from back to forth
                            if (top_k_value[k - 1 - j] < original_value[i]) {
                                top_k_value[k - 1 - j] = top_k_value[k - 2 - j];
                                top_k_sequence[k - 1 - j] = top_k_sequence[k - 2 - j];
                                position--;
                            } else {
                                break;
                            }
                        }
                        if (position < k) {//we push the current value to the array
                            if (position == 1 & top_k_value[0] < original_value[i]) {//if position is on the second the place, we will compare it with the first one
                                position--;
                            }
                            top_k_value[position] = original_value[i];
                            top_k_sequence[position] = i;
                        }
                    }
                } else {
                    for (int i = 0; i < k; i++) {
                        top_k_value[i] = Double.POSITIVE_INFINITY;
                    }
                    for (int i = 0; i < total_length; i++) {
                        int position = k;
                        for (int j = 0; j < k - 1; j++) {//try to compare the current value to the jth value counted from back to forth
                            if (top_k_value[k - 1 - j] > original_value[i]) {
                                top_k_value[k - 1 - j] = top_k_value[k - 2 - j];
                                top_k_sequence[k - 1 - j] = top_k_sequence[k - 2 - j];
                                position--;
                            } else {
                                break;
                            }
                        }
                        if (position < k) {//we push the current value to the array
                            if (position == 1 & top_k_value[0] > original_value[i]) {//if position is on the second the place, we will compare it with the first one
                                position--;
                            }
                            top_k_value[position] = original_value[i];
                            top_k_sequence[position] = i;
                        }
                    }
                }
            } else {
                System.out.println("Error: the k is greater than the array length!");
            }
            return top_k_sequence;
        }

        public int[] getTopKSequence_SlowVersion(double[] original_value, int k, Boolean get_max_value) {//It takes longer time for a large dataset. Computational complexity is kn.
            int total_length = original_value.length;
            int[] sequence = new int[total_length];
            double[] copy_value = new double[total_length];
            for (int i = 0; i < total_length; i++) {
                sequence[i] = i;
                copy_value[i] = original_value[i];
            }
            int[] top_k_sequence = new int[k];
            if (k <= total_length) {
                if (get_max_value) {
                    for (int i = 0; i < k; i++) {
                        double cur_max = copy_value[i];
                        int cur_seq = i;
                        for (int j = i + 1; j < total_length; j++) {
                            if (copy_value[j] > cur_max) {
                                cur_max = copy_value[j];
                                cur_seq = j;
                            }
                        }
                        int tmp_seq = sequence[i];
                        sequence[i] = sequence[cur_seq];
                        top_k_sequence[i] = sequence[cur_seq];
                        sequence[cur_seq] = tmp_seq;
                        copy_value[cur_seq] = copy_value[i];
                        copy_value[i] = cur_max;
                    }
                } else {
                    for (int i = 0; i < k; i++) {
                        double cur_max = copy_value[i];
                        int cur_seq = i;
                        for (int j = i + 1; j < total_length; j++) {
                            if (copy_value[j] < cur_max) {
                                cur_max = copy_value[j];
                                cur_seq = j;
                            }
                        }
                        int tmp_seq = sequence[i];
                        sequence[i] = sequence[cur_seq];
                        top_k_sequence[i] = sequence[cur_seq];
                        sequence[cur_seq] = tmp_seq;
                        copy_value[cur_seq] = copy_value[i];
                        copy_value[i] = cur_max;
                    }
                }
            } else {
                System.out.println("Error: the k is greater than the array length!");
            }
            return top_k_sequence;
        }

        public void TopKPushing(ArrayList original, double value, ArrayList items, int k) {
            //it needs an original vector for pushing which can be got from getOriginalVector4TopKPushing().
            //for a given vector, we push the new object (value, description) in to the vector.
            int position = original.size();
            int totalsize = original.size();
            for (int i = 0; i < totalsize; i++) {
                if ((Double) ((ArrayList) original.get(totalsize - 1 - i)).get(0) < value) {
                    position--;
                } else {
                    break;
                }
            }
            if (position < k) {
                ArrayList temp = new ArrayList();
                temp.add(value);
                temp.add(items);
                original.add(position, temp);
                if (original.size() > k) {
                    original.remove(k);
                }
            }
        }

        public ArrayList correctTopKPush(ArrayList original) {
            if (((Double) ((ArrayList) original.get(original.size() - 1)).get(0)).isInfinite()) {
                original.remove(original.size() - 1);
            }
            return original;
        }

        public ArrayList getOriginalVector4TopKPushing() {
            ArrayList original = new ArrayList();
            ArrayList firstv = new ArrayList();
            firstv.add(Double.NEGATIVE_INFINITY);
            firstv.add("");
            original.add(firstv);
            return original;
        }
    }

    public class Quicksort {//the class to get the sequence array for the sorted array

        public Random RND = new Random();

        private void swap(double[] array, int[] sequence, int[] breakRank, int i, int j) {
            double tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            int tmp_seq = sequence[i];
            sequence[i] = sequence[j];
            sequence[j] = tmp_seq;
            tmp_seq = breakRank[i];
            breakRank[i] = breakRank[j];
            breakRank[j] = tmp_seq;
        }

        private int partition(double[] array, int[] sequence, int[] breakRank, int begin, int end) {
            int index = begin + RND.nextInt(end - begin + 1);
            double pivot = array[index];
            int pivotrank = breakRank[index];
            swap(array, sequence, breakRank, index, end);
            for (int i = index = begin; i < end; ++i) {
                if (array[i] > pivot || (array[i] == pivot && breakRank[i] < pivotrank)) {
                    swap(array, sequence, breakRank, index++, i);
                }
            }
            swap(array, sequence, breakRank, index, end);
            return (index);
        }

        private void qsort(double[] array, int[] sequence, int[] breakRank, int begin, int end) {
        	int MAX_INSERTION_SORT = 25;
        		int[] stack = new int[42289];
        		int top =-1;
        		int n1, n2;
        		stack[++top]=begin;
        		stack[++top]=end;
        		while (top>=0){
        			end=stack[top--];
        			begin=stack[top--];
        			int index = partition(array, sequence, breakRank, begin, end);
        			  n1 = index - begin;
        		      n2 = end - index;
        		      if(n2 < n1) {
        		          if(n1 > MAX_INSERTION_SORT) {
        		             stack[++top] = begin;
        		             stack[++top] = index-1;
        		          }
        		          if(n2 > MAX_INSERTION_SORT) {
        		             stack[++top] = index+1;
        		             stack[++top] = end;
        		          }
        		        }
        		        else {
        		          if(n2 > MAX_INSERTION_SORT){
        		            stack[++top] = index+1;
        		            stack[++top] = end;
        		          }
        		          if(n1 > MAX_INSERTION_SORT){
        		             stack[++top] = begin;
        		             stack[++top] = index - 1;
        		           } 
        		        } // end push depending on size
        		      } 
        		
        		 
        	
        }

        public int[] sort(double[] org_array, int[] breakRank, Boolean ascending) {
            int[] sequence = new int[org_array.length];
            int[] reverse_sequence = new int[org_array.length];
            double[] array = new double[org_array.length];
            for (int i = 0; i < org_array.length; i++) {
                sequence[i] = i;
                array[i] = org_array[i];
            }
            qsort(array, sequence, breakRank, 0, array.length - 1);
            for (int i = 0; i < org_array.length; i++) {
                reverse_sequence[i] = sequence[org_array.length - 1 - i];
            }
            if (ascending) {
                return reverse_sequence;
            } else {
                return sequence;
            }
        }
    }

    public class Evaluation {

        public double BinaryRankingEvaluation(BitSet list, double n, int k, String type) {
            //for each value in the list, 1 indicates the corresponding item is interesting and 0 indicates the corresponding item is not interesting.
            //type: AP, K-MAP Weighted by Precision, PRC, K-PRC Weighted by Precision, K-Precision
            double score = 0;
            if (type.equals("AP")) {//emphasis more on the top ranking
                int positive_number = 0;
                for (int i = list.nextSetBit(0); i >= 0; i = list.nextSetBit(i + 1)) {
                    positive_number++;
                    score += (double) positive_number / (i + 1);
                }
                score = score / positive_number;
            } else if (type.equals("K-AP Weighted by Precision")) {//emphasis more on the top ranking
                int positive_number = 0;
                for (int i = list.nextSetBit(0); i >= 0 & i < k; i = list.nextSetBit(i + 1)) {
                    positive_number++;
                    score += (double) positive_number / (i + 1);
                }
                score = score / k;
            } else if (type.equals("PRC")) {
                int total_positive_number = list.cardinality();
                int positive_number = 0;
                int interval = 0;
                double previous_precision = 0;
                for (int i = list.nextSetBit(0); i >= 0; i = list.nextSetBit(i + 1)) {
                    positive_number++;
                    double precision = (double) positive_number / (i + 1);
                    if (precision >= previous_precision) {
                        interval++;
                    } else {
                        score += previous_precision * interval / total_positive_number;
                        interval = 1;
                    }
                    previous_precision = precision;
                }
                score += previous_precision * interval / total_positive_number;
            } else if (type.equals("K-PRC Weighted by Precision")) {
                int total_positive_number = 0;
                for (int i = list.nextSetBit(0); i >= 0 & i < k; i = list.nextSetBit(i + 1)) {
                    total_positive_number++;
                }
                int positive_number = 0;
                int interval = 0;
                double previous_precision = 0;
                for (int i = list.nextSetBit(0); i >= 0 & i < k; i = list.nextSetBit(i + 1)) {
                    positive_number++;
                    double precision = (double) positive_number / (i + 1);
                    if (precision >= previous_precision) {
                        interval++;
                    } else {
                        score += previous_precision * interval / total_positive_number;
                        interval = 1;
                    }
                    previous_precision = precision;
                }
                score += previous_precision * interval / total_positive_number;
                score = score * (double) positive_number / k;
            } else if (type.equals("K-Precision")) {
                int positive_number = 0;
                for (int i = list.nextSetBit(0); i >= 0 & i < k; i = list.nextSetBit(i + 1)) {
                    positive_number++;
                }
                score = (double) positive_number / k;
            }
            return score;
        }

        private void testBinaryRanking() {
            int n = 10;
            //int[][] array={{1,1,1,0,0,0,0,0,0,0},{1,1,0,1,0,0,0,0,0,0},{1,0,1,1,0,0,0,0,0,0},{0,1,1,1,0,0,0,0,0,0},{0,0,0,0,0,0,0,1,1,1},{0,1,1,0,0,1,1,0,0,0},{1,0,1,0,0,0,0,0,1,1},{1,0,1,0,0,1,1,0,0,0},{0,1,0,0,1,1,1,0,0,0}};
            int[][] array = {{1, 1, 1, 0, 1, 1, 1, 0, 0, 1}, {1, 1, 0, 1, 1, 1, 1, 0, 0, 1}, {1, 0, 1, 1, 1, 1, 1, 0, 0, 1}};
            BitSet[] bs = new BitSet[array.length];
            int k = 3;
            for (int i = 0; i < array.length; i++) {
                bs[i] = getBitSet(array[i]);
                System.out.println(bs[i] + "," + BinaryRankingEvaluation(bs[i], n, k, "AP") + "," + BinaryRankingEvaluation(bs[i], n, k, "PRC")
                        + "," + BinaryRankingEvaluation(bs[i], n, k, "K-AP Weighted by Precision") + "," + BinaryRankingEvaluation(bs[i], n, k, "K-PRC Weighted by Precision")
                        + "," + BinaryRankingEvaluation(bs[i], n, k, "K-Precision"));
            }
        }

        public double TwoByTwoTableEvaluation(double tp, double fn, double fp, double tn, String type) {
            //type: Accuracy, Precision, Recall, F-measure, Precision Times Recall, Jaccard
            double score = 0;
            double n = tp + fn + fp + tn;
            double precision;
            double recall;
            if (tp == 0) {
                precision = 0;
                recall = 0;
            } else {
                precision = tp / (tp + fp);
                recall = tp / (tp + fn);
            }
            if (type.equals("Accuracy")) {
                score = (tp + tn) / n;
            } else if (type.equals("Precision")) {
                score = precision;
            } else if (type.equals("Recall")) {
                score = recall;
            } else if (type.equals("F-measure")) {
                if (precision + recall == 0) {
                    score = 0;
                } else {
                    score = 2 * precision * recall / (precision + recall);
                }
            } else if (type.equals("Precision Times Recall")) {
                score = precision * recall;
            } else if (type.equals("Jaccard")) {
                if (tp == 0) {
                    score = 0;
                } else {
                    score = tp / (tp + fn + fp);
                }
            }
            return score;
        }

        public double RelevanceRankingEvaluation(double[] list, double[] ideal_list, String type) {
            //list[i] provide the relevance information at the i-th position
            //type: Cumulative Gain, Normalized Cumulative Gain, Discounted Cumulative Gain, Normalized Discounted Cumulative Gain
            double score = 0;
            if (type.equals("Cumulative Gain")) {
                for (int i = 0; i < list.length; i++) {
                    score += list[i];
                }
            } else if (type.equals("Normalized Cumulative Gain")) {
                double list_score = 0;
                double ideal_list_score = 0;
                for (int i = 0; i < list.length; i++) {
                    list_score += list[i];
                    ideal_list_score += ideal_list[i];
                }
                score = list_score / ideal_list_score;
            } else if (type.equals("Discounted Cumulative Gain")) {
                score += list[0];
                for (int i = 1; i < list.length; i++) {
                    score += list[i] * Math.log(2) / Math.log(i + 1);
                }
            } else if (type.equals("Normalized Discounted Cumulative Gain")) {
                double list_score = RelevanceRankingEvaluation(list, list, "Discounted Cumulative Gain");
                double ideal_list_score = RelevanceRankingEvaluation(ideal_list, ideal_list, "Discounted Cumulative Gain");
                score = list_score / ideal_list_score;
            }
            return score;
        }

        private void testRelevanceRanking() {
            double[] list = {3, 2, 3, 0, 1, 2};
            double[] list1 = {3, 3, 2, 2, 1, 0};
            double[] list2 = {3, 3, 3, 2, 2, 2};
            System.out.println(RelevanceRankingEvaluation(list, list1, "Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list, list1, "Normalized Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list, list1, "Discounted Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list, list1, "Normalized Discounted Cumulative Gain"));
            System.out.println(RelevanceRankingEvaluation(list, list2, "Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list, list2, "Normalized Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list, list2, "Discounted Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list, list2, "Normalized Discounted Cumulative Gain"));
            System.out.println(RelevanceRankingEvaluation(list1, list2, "Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list1, list2, "Normalized Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list1, list2, "Discounted Cumulative Gain")
                    + "," + RelevanceRankingEvaluation(list1, list2, "Normalized Discounted Cumulative Gain"));
        }

        public double NonoverlappingClusteringEvaluation(ArrayList detected_group, ArrayList predefined_group, double n, String type) {
            //the ArrayList contains BitSet data and each BitSet record a group information
            //for BitSet like a=new BitSet(10);a.set(5);      a.cardinality()=1,a.length()=6,a.size()=64 
            //type: Group Mutual Information, Group Normalized Mutual Information, Group Purity, Group Inversed Purity, Group F Measure, Pair F Measure, Pair Rand Index, Pair Jaccard
            double score = 0;
            if (type.startsWith("Group")) {
                double[] pro_det = new double[detected_group.size()];
                double[] pro_pre = new double[predefined_group.size()];
                for (int i = 0; i < detected_group.size(); i++) {
                    BitSet group_member = (BitSet) detected_group.get(i);
                    pro_det[i] = group_member.cardinality() / n;
                }
                for (int i = 0; i < predefined_group.size(); i++) {
                    BitSet group_member = (BitSet) predefined_group.get(i);
                    pro_pre[i] = group_member.cardinality() / n;
                }
                if (type.equals("Group Normalized Mutual Information")) {
                    double hc = 0;
                    double hg = 0;
                    double icg = 0;
                    for (int i = 0; i < detected_group.size(); i++) {
                        hc -= pro_det[i] * Math.log(pro_det[i]);
                    }
                    for (int i = 0; i < predefined_group.size(); i++) {
                        hg -= pro_pre[i] * Math.log(pro_pre[i]);
                    }
                    for (int i = 0; i < detected_group.size(); i++) {
                        BitSet dg = (BitSet) detected_group.get(i);
                        for (int j = 0; j < predefined_group.size(); j++) {
                            BitSet pg = (BitSet) predefined_group.get(j);
                            BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                            union.and(pg);
                            if (union.cardinality() != 0) {
                                icg += (union.cardinality() / n) * Math.log((union.cardinality() * n) / (dg.cardinality() * pg.cardinality()));
                            }
                        }
                    }
                    score = 2 * icg / (hc + hg);
                } else if (type.equals("Group Mutual Information")) {
                    for (int i = 0; i < detected_group.size(); i++) {
                        BitSet dg = (BitSet) detected_group.get(i);
                        for (int j = 0; j < predefined_group.size(); j++) {
                            BitSet pg = (BitSet) predefined_group.get(j);
                            BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                            union.and(pg);
                            if (union.cardinality() != 0) {
                                score += (union.cardinality() / n) * Math.log((union.cardinality() * n) / (dg.cardinality() * pg.cardinality()));
                            }
                        }
                    }
                } else if (type.equals("Group Purity")) {
                    for (int i = 0; i < detected_group.size(); i++) {
                        BitSet dg = (BitSet) detected_group.get(i);
                        double max = 0;
                        for (int j = 0; j < predefined_group.size(); j++) {
                            BitSet pg = (BitSet) predefined_group.get(j);
                            BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                            union.and(pg);
                            double temp_precision = (double) union.cardinality() / dg.cardinality();
                            if (temp_precision > max) {
                                max = temp_precision;
                            }
                        }
                        score += dg.cardinality() / n * max;
                    }
                } else if (type.equals("Group Inversed Purity")) {
                    for (int i = 0; i < predefined_group.size(); i++) {
                        BitSet pg = (BitSet) predefined_group.get(i);
                        double max = 0;
                        for (int j = 0; j < detected_group.size(); j++) {
                            BitSet dg = (BitSet) detected_group.get(j);
                            BitSet union = (BitSet) ((BitSet) predefined_group.get(i)).clone();
                            union.and(dg);
                            double temp_precision = (double) union.cardinality() / pg.cardinality();
                            if (temp_precision > max) {
                                max = temp_precision;
                            }
                        }
                        score += pg.cardinality() / n * max;
                    }
                } else if (type.equals("Group F Measure")) {
                    for (int i = 0; i < detected_group.size(); i++) {
                        double max = 0;
                        for (int j = 0; j < predefined_group.size(); j++) {
                            BitSet pg = (BitSet) predefined_group.get(j);
                            BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                            union.and(pg);
                            double pro_union = union.cardinality() / n;
                            double temp = 2 * pro_union / pro_det[i] * pro_union / pro_pre[j] / (pro_union / pro_det[i] + pro_union / pro_pre[j]);
                            if (temp > max) {
                                max = temp;
                            }
                        }
                        score += max * pro_det[i];
                    }
                }
            } else if (type.startsWith("Pair")) {
                int[] clusteredID = new int[(int) n];
                int[] groundID = new int[(int) n];
                for (int i = 0; i < detected_group.size(); i++) {
                    BitSet group_member = (BitSet) detected_group.get(i);
                    for (int j = group_member.nextSetBit(0); j >= 0; j = group_member.nextSetBit(j + 1)) {
                        // operate on index i here
                        clusteredID[j] = i;
                    }
                }
                for (int i = 0; i < predefined_group.size(); i++) {
                    BitSet group_member = (BitSet) predefined_group.get(i);
                    for (int j = group_member.nextSetBit(0); j >= 0; j = group_member.nextSetBit(j + 1)) {
                        // operate on index i here
                        groundID[j] = i;
                    }
                }
                int size = (int) n;
                double[][] contingency = new double[2][2];
                int x;
                int y;
                for (int i = 0; i < size; i++) {
                    for (int j = i + 1; j < size; j++) {
                        if (clusteredID[i] == clusteredID[j]) {
                            x = 0;
                        } else {
                            x = 1;
                        }
                        if (groundID[i] == groundID[j]) {
                            y = 0;
                        } else {
                            y = 1;
                        }
                        contingency[x][y]++;
                    }
                }
                if (type.equals("Pair F Measure")) {
                    if (contingency[0][0] == 0) {
                        score = 0;
                    } else {
                        double pre = contingency[0][0] / (contingency[0][0] + contingency[0][1]);
                        double rec = contingency[0][0] / (contingency[0][0] + contingency[1][0]);
                        score = 2 * pre * rec / (pre + rec);
                    }
                } else if (type.equals("Pair Rand Index")) {
                    score = (contingency[0][0] + contingency[1][1]) / (contingency[0][0] + contingency[1][1] + contingency[1][0] + contingency[0][1]);
                } else if (type.equals("Pair Jaccard")) {
                    score = (contingency[0][0]) / (contingency[0][0] + contingency[1][0] + contingency[0][1]);
                }
            }
            return score;
        }

        private void testNonoverlappingClusteringEvaluation() {
            int[][] X = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 1, 1, 1, 2, 2},
                {0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, {0, 0, 0, 1, 1, 0, 0, 1, 1, 1}};
            int[] xm = {10, 1, 3, 2, 2};
            int[] Y = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
            int ym = 2;
            ArrayList[] det = new ArrayList[X.length];
            for (int j = 0; j < X.length; j++) {
                det[j] = new ArrayList();
                BitSet[] xb = new BitSet[xm[j]];
                for (int i = 0; i < xm[j]; i++) {
                    xb[i] = new BitSet(10);
                }
                for (int i = 0; i < 10; i++) {
                    xb[X[j][i]].set(i);
                }
                for (int i = 0; i < xm[j]; i++) {
                    det[j].add(xb[i]);
                }
            }
            BitSet[] yb = new BitSet[ym];
            for (int i = 0; i < ym; i++) {
                yb[i] = new BitSet(10);
            }
            for (int i = 0; i < 10; i++) {
                yb[Y[i]].set(i);
            }
            ArrayList pre = new ArrayList();
            for (int i = 0; i < ym; i++) {
                pre.add(yb[i]);
            }
            for (int i = 0; i < X.length; i++) {
                System.out.println(i + ", predefined:" + pre + ", detected:" + det[i]);
            }
            System.out.println("id, Group Normalized Mutual Information, Group Mutual Information, Group F Measure, Group Purity, Group Inversed Purity, Pair F Measure, Pair Rand Index, Pair Jaccard");
            for (int i = 0; i < X.length; i++) {
                System.out.println(i + "," + roundDecimals(NonoverlappingClusteringEvaluation(det[i], pre, 10, "Group Normalized Mutual Information"), 4)
                        + "," + roundDecimals(NonoverlappingClusteringEvaluation(det[i], pre, 10, "Group Mutual Information"), 4)
                        + "," + roundDecimals(NonoverlappingClusteringEvaluation(det[i], pre, 10, "Group F Measure"), 4)
                        + "," + roundDecimals(NonoverlappingClusteringEvaluation(det[i], pre, 10, "Group Purity"), 4)
                        + "," + roundDecimals(NonoverlappingClusteringEvaluation(det[i], pre, 10, "Group Inversed Purity"), 4)
                        + "," + roundDecimals(NonoverlappingClusteringEvaluation(det[i], pre, 10, "Pair F Measure"), 4)
                        + "," + roundDecimals(NonoverlappingClusteringEvaluation(det[i], pre, 10, "Pair Rand Index"), 4)
                        + "," + roundDecimals(NonoverlappingClusteringEvaluation(det[i], pre, 10, "Pair Jaccard"), 4));
            }
        }

        public int[] NonoverlappingClusterMatching(ArrayList detected_group, ArrayList predefined_group, double n, String type) {
            //Mutual Information, Normalized Mutual Information, F Measure, Precision, Recall
            int[] matching = new int[detected_group.size()];
            double[] pro_det = new double[detected_group.size()];
            double[] pro_pre = new double[predefined_group.size()];
            for (int i = 0; i < detected_group.size(); i++) {
                BitSet group_member = (BitSet) detected_group.get(i);
                pro_det[i] = group_member.cardinality() / n;
            }
            for (int i = 0; i < predefined_group.size(); i++) {
                BitSet group_member = (BitSet) predefined_group.get(i);
                pro_pre[i] = group_member.cardinality() / n;
            }
            if (type.equals("Mutual Information")) {
                for (int i = 0; i < detected_group.size(); i++) {
                    BitSet dg = (BitSet) detected_group.get(i);
                    int temp_index = -1;
                    double temp_value = Double.NEGATIVE_INFINITY;
                    for (int j = 0; j < predefined_group.size(); j++) {
                        BitSet pg = (BitSet) predefined_group.get(j);
                        BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                        union.and(pg);
                        if (union.cardinality() != 0) {
                            double icg = (union.cardinality() / n) * Math.log((union.cardinality() * n) / (dg.cardinality() * pg.cardinality()));
                            if (icg > temp_value) {
                                temp_index = j;
                                temp_value = icg;
                            }
                        }
                    }
                    matching[i] = temp_index;
                }
            } else if (type.equals("Normalized Mutual Information")) {
                double hc = 0;
                double hg = 0;
                double icg = 0;
                for (int i = 0; i < detected_group.size(); i++) {
                    BitSet dg = (BitSet) detected_group.get(i);
                    int temp_index = -1;
                    double temp_value = Double.NEGATIVE_INFINITY;
                    for (int j = 0; j < predefined_group.size(); j++) {
                        BitSet pg = (BitSet) predefined_group.get(j);
                        BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                        union.and(pg);
                        if (union.cardinality() != 0) {
                            icg = (union.cardinality() / n) * Math.log((union.cardinality() * n) / (dg.cardinality() * pg.cardinality()));
                            hc = -pro_det[i] * Math.log(pro_det[i]);
                            hg = -pro_pre[j] * Math.log(pro_pre[j]);
                            double nmi = 2 * icg / (hc + hg);
                            if (nmi > temp_value) {
                                temp_index = j;
                                temp_value = nmi;
                            }
                        }
                    }
                    matching[i] = temp_index;
                }
            } else if (type.equals("F Measure")) {
                for (int i = 0; i < detected_group.size(); i++) {
                    int temp_index = -1;
                    double temp_value = Double.NEGATIVE_INFINITY;
                    for (int j = 0; j < predefined_group.size(); j++) {
                        BitSet pg = (BitSet) predefined_group.get(j);
                        BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                        union.and(pg);
                        double pro_union = union.cardinality() / n;
                        double temp = 2 * pro_union / pro_det[i] * pro_union / pro_pre[j] / (pro_union / pro_det[i] + pro_union / pro_pre[j]);
                        if (temp > temp_value) {
                            temp_value = temp;
                            temp_index = j;
                        }
                    }
                    matching[i] = temp_index;
                }
            } else if (type.equals("Precision")) {
                for (int i = 0; i < detected_group.size(); i++) {
                    BitSet dg = (BitSet) detected_group.get(i);
                    int temp_index = -1;
                    double temp_value = Double.NEGATIVE_INFINITY;
                    for (int j = 0; j < predefined_group.size(); j++) {
                        BitSet pg = (BitSet) predefined_group.get(j);
                        BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                        union.and(pg);
                        if (union.cardinality() != 0) {
                            double precision = (double) union.cardinality() / dg.cardinality();
                            if (precision > temp_value) {
                                temp_index = j;
                                temp_value = precision;
                            }
                        }
                    }
                    matching[i] = temp_index;
                }
            } else if (type.equals("Recall")) {
                for (int i = 0; i < detected_group.size(); i++) {
                    BitSet dg = (BitSet) detected_group.get(i);
                    int temp_index = -1;
                    double temp_value = Double.NEGATIVE_INFINITY;
                    for (int j = 0; j < predefined_group.size(); j++) {
                        BitSet pg = (BitSet) predefined_group.get(j);
                        BitSet union = (BitSet) ((BitSet) detected_group.get(i)).clone();
                        union.and(pg);
                        if (union.cardinality() != 0) {
                            double recall = (double) union.cardinality() / pg.cardinality();
                            if (recall > temp_value) {
                                temp_index = j;
                                temp_value = recall;
                            }
                        }
                    }
                    matching[i] = temp_index;
                }
            }
            return matching;
        }

        private void testNonoverlappingClusterMatching() {
            int[][] X = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 1, 1, 1, 2, 2},
                {0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, {0, 0, 0, 1, 1, 0, 0, 1, 1, 1}};
            int[] xm = {10, 1, 3, 2, 2};
            int[] Y = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
            int ym = 2;
            ArrayList[] det = new ArrayList[X.length];
            for (int j = 0; j < X.length; j++) {
                det[j] = new ArrayList();
                BitSet[] xb = new BitSet[xm[j]];
                for (int i = 0; i < xm[j]; i++) {
                    xb[i] = new BitSet(10);
                }
                for (int i = 0; i < 10; i++) {
                    xb[X[j][i]].set(i);
                }
                for (int i = 0; i < xm[j]; i++) {
                    det[j].add(xb[i]);
                }
            }
            BitSet[] yb = new BitSet[ym];
            for (int i = 0; i < ym; i++) {
                yb[i] = new BitSet(10);
            }
            for (int i = 0; i < 10; i++) {
                yb[Y[i]].set(i);
            }
            ArrayList pre = new ArrayList();
            for (int i = 0; i < ym; i++) {
                pre.add(yb[i]);
            }
            for (int i = 0; i < X.length; i++) {
                System.out.println(i + ", predefined:" + pre + ", detected:" + det[i]);
            }
            System.out.println("id, Mutual Information, Normalized Mutual Information, F Measure, Precision, Recall");
            for (int i = 0; i < X.length; i++) {
                /*
                System.out.println(i + "," + convertArrayIntoList(NonoverlappingClusterMatching(det[i], pre, 10, "Mutual Information"))
                + "," + convertArrayIntoList(NonoverlappingClusterMatching(det[i], pre, 10, "Normalized Mutual Information"))
                + "," + convertArrayIntoList(NonoverlappingClusterMatching(det[i], pre, 10, "F Measure"))
                + "," + convertArrayIntoList(NonoverlappingClusterMatching(det[i], pre, 10, "Precision"))
                + "," + convertArrayIntoList(NonoverlappingClusterMatching(det[i], pre, 10, "Recall")));
                 */
                new ArrayList().addAll(Arrays.asList(NonoverlappingClusterMatching(pre, det[i], 10, "Mutual Information")));
                System.out.println(i + "," + new ArrayList().addAll(Arrays.asList(NonoverlappingClusterMatching(pre, det[i], 10, "Mutual Information")))
                        + "," + new ArrayList().addAll(Arrays.asList(NonoverlappingClusterMatching(pre, det[i], 10, "Normalized Mutual Information")))
                        + "," + new ArrayList().addAll(Arrays.asList(NonoverlappingClusterMatching(pre, det[i], 10, "F Measure")))
                        + "," + new ArrayList().addAll(Arrays.asList(NonoverlappingClusterMatching(pre, det[i], 10, "Precision")))
                        + "," + new ArrayList().addAll(Arrays.asList(NonoverlappingClusterMatching(pre, det[i], 10, "Recall"))));
            }
        }
    }

    public class Statistics {

        public Statistics.Correlation corr = new Statistics.Correlation();
        public Statistics.Binomial binom = new Statistics.Binomial();

        public double getMean(double[] data) {
            double value = 0;
            for (int i = 0; i < data.length; i++) {
                value += data[i];
            }
            return value / data.length;
        }

        public double getMean(ArrayList data) {
            double value = 0;
            for (int i = 0; i < data.size(); i++) {
                value += (Double) data.get(i);
            }
            return value / data.size();
        }

        public double getVariance(double[] data) {
            double value = 0;
            double mean = getMean(data);
            for (int i = 0; i < data.length; i++) {
                value += (data[i] - mean) * (data[i] - mean);
            }
            return value / data.length;
        }

        public double getVariance(ArrayList data) {
            double value = 0;
            double mean = getMean(data);
            for (int i = 0; i < data.size(); i++) {
                value += ((Double) data.get(i) - mean) * ((Double) data.get(i) - mean);
            }
            return value / data.size();
        }

        public double getPowerLawNumber(double base, double total, double exponent) {
            //we return the number between base and total, the number follow the powerlaw distribution
            double size = base * Math.pow(1 - new Random().nextDouble(), -1 / (exponent - 1));
            while (size > total) {//if the generated size is bigger than total, we generate it again.
                size = base * Math.pow(1 - new Random().nextDouble(), -1 / (exponent - 1));
            }
            return size;
        }

        public double getPowerLawProbability(double base, double total, double exponent) {
            //we return the probability between 0 and 1 which follow the power law distribution
            return getPowerLawNumber(base, total, exponent) / total;
        }

        public class Correlation {

            public double any_confidence(double tp, double min_pi) {
                return tp / min_pi;
            }

            public double all_confidence(double tp, double max_pi) {
                return tp / max_pi;
            }

            public double bond(double tp, double ntp) {
                return tp / (1 - ntp);
            }

            public ArrayList getMeasureType() {
                ArrayList list = new ArrayList();
                list.add("Support");
                list.add("Any-confidence");
                list.add("All-confidence");
                list.add("Bond");
                list.add("The Simplified Chi-square");
                list.add("Probability Ratio");
                list.add("Leverage");
                list.add("Likelihood Ratio");
                list.add("The Simplified Chi-square with Continuity Correction");
                list.add("BCPNN");
                list.add("IS");
                list.add("Two-way Support");
                list.add("The Simplified Chi-square with Support");
                list.add("Phi-Coefficient");
                list.add("Relative Risk");
                list.add("Odds Ratio");
                list.add("Conviction");
                list.add("Added Value");
                list.add("Uniform Distirbution Inference");
                return list;
            }

            public ArrayList getItemsetMeasureType() {
                ArrayList list = new ArrayList();
                list.add("Support");
                list.add("Any-confidence");
                list.add("All-confidence");
                list.add("Bond");
                list.add("The Simplified Chi-square");
                list.add("Probability Ratio");
                list.add("Leverage");
                list.add("Likelihood Ratio");
                list.add("The Simplified Chi-square with Continuity Correction");
                list.add("BCPNN");
                list.add("IS");
                list.add("Two-way Support");
                list.add("The Simplified Chi-square with Support");
                return list;
            }

            public ArrayList getCorrelationType() {
                ArrayList list = new ArrayList();
                list.add("The Simplified Chi-square");
                list.add("Probability Ratio");
                list.add("Leverage");
                list.add("Likelihood Ratio");
                list.add("The Simplified Chi-square with Continuity Correction");
                list.add("BCPNN");
                list.add("IS");
                list.add("Two-way Support");
                list.add("The Simplified Chi-square with Support");
                list.add("Phi-Coefficient");
                list.add("Relative Risk");
                list.add("Odds Ratio");
                list.add("Conviction");
                list.add("Added Value");
                list.add("Uniform Distirbution Inference");
                return list;
            }

            public ArrayList getItemsetCorrelationType() {
                ArrayList list = new ArrayList();
                list.add("The Simplified Chi-square");
                list.add("Probability Ratio");
                list.add("Leverage");
                list.add("Likelihood Ratio");
                list.add("The Simplified Chi-square with Continuity Correction");
                list.add("BCPNN");
                list.add("IS");
                list.add("Two-way Support");
                list.add("The Simplified Chi-square with Support");
                return list;
            }

            public double correlation_for_itemset(double tp, double ep, double n, double cc, String type) {
                double correlation = 0;
                if (type.equals("The Simplified Chi-square")) {
                    //chi=(tp-ep)^2/ep
                    if (ep == 0 || ep == 1) {
                        correlation = 0;
                    } else {
                        if (tp >= ep) {
                            correlation = (tp - ep) * (tp - ep) / ep;
                        } else {
                            correlation = -(tp - ep) * (tp - ep) / ep;
                        }
                    }
                } else if (type.equals("Probability Ratio")) {
                    //pr=log(tp/ep)
                    if (ep == 0 || ep == 1) {
                        correlation = 0;
                    } else {
                        correlation = Math.log(tp / ep);
                    }
                } else if (type.equals("Leverage")) {
                    correlation = tp - ep;
                } else if (type.equals("Likelihood Ratio")) {
                    //lr=log(Pr(n,k,tp)/Pr(n,k,ep))
                    if (ep == 0 || ep == 1) {
                        correlation = 0;
                    } else {
                        if (tp == 0) {
                            correlation = -Math.log(1 / (1 - ep));
                        } else {
                            if (tp >= ep) {
                                correlation = tp * Math.log(tp / ep) + (1 - tp) * Math.log((1 - tp) / (1 - ep));
                            } else {
                                correlation = -tp * Math.log(tp / ep) - (1 - tp) * Math.log((1 - tp) / (1 - ep));
                            }
                        }
                    }
                } else if (type.equals("The Simplified Chi-square with Continuity Correction")) {
                    //scscc=(tp-ep)^2/(ep+cc)
                    if (tp >= ep) {
                        correlation = (tp - ep) * (tp - ep) / (ep + cc / n);
                    } else {
                        correlation = -(tp - ep) * (tp - ep) / (ep + cc / n);
                    }
                } else if (type.equals("BCPNN")) {
                    //BCPNN=log((tp+cc)/(ep+cc))
                    if (ep == 0 || ep == 1) {
                        correlation = 0;
                    } else {
                        correlation = Math.log((tp * n + cc) / (ep * n + cc));
                    }
                } else if (type.equals("IS")) {
                    //IS=tp/sqrt(ep)
                    if (ep == 0 || ep == 1) {
                        correlation = 1;
                    } else {
                        correlation = tp / Math.sqrt(ep);
                    }
                } else if (type.equals("Two-way Support")) {
                    //tws=tp*log(tp/ep)
                    if (ep == 0 || ep == 1) {
                        correlation = 0;
                    } else {
                        correlation = tp * Math.log(tp / ep);
                    }
                } else if (type.equals("The Simplified Chi-square with Support")) {
                    //=tp*chi-square
                    if (ep == 0 || ep == 1) {
                        correlation = 0;
                    } else {
                        if (tp >= ep) {
                            correlation = tp * (tp - ep) * (tp - ep) / ep;
                        } else {
                            correlation = -tp * (tp - ep) * (tp - ep) / ep;
                        }
                    }
                }
                return correlation;
            }

            public ArrayList getPairCorrelationType() {
                ArrayList list = new ArrayList();
                list.add("Phi-Coefficient");
                list.add("Relative Risk");
                list.add("Odds Ratio");
                list.add("Conviction");
                list.add("Added Value");
                list.add("Uniform Distirbution Inference");
                return list;
            }

            public double correlation_for_pair(double oa, double ob, double k, double n, String type) {
                double correlation = 0;
                double ep = oa / n * ob / n;
                double f11 = k;
                double f10 = oa - k;
                double f01 = ob - k;
                double f00 = n - f11 - f10 - f01;
                if (type.equals("Phi-Coefficient")) {
                    if (ep == 0 || ep == 1) {
                        correlation = 0;
                    } else {
                        correlation = (f11 * f00 - f10 * f01) / Math.sqrt(oa * ob * (n - oa) * (n - ob));
                    }
                } else if (type.equals("Relative Risk")) {
                    if (f01 == 0 || oa == 0) {
                        correlation = 1;
                    } else {
                        correlation = f11 * (n - oa) / oa * f01;
                    }
                } else if (type.equals("Odds Ratio")) {
                    if (f10 == 0 || f01 == 0) {
                        correlation = 1;
                    } else {
                        correlation = f11 * f00 / (f10 * f01);
                    }
                } else if (type.equals("Conviction")) {
                    if (f10 == 0) {
                        correlation = 1;
                    } else {
                        correlation = oa * (n - ob) / (n * f10);
                    }
                } else if (type.equals("Added Value")) {
                    if (oa == 0) {
                        correlation = 0;
                    } else {
                        correlation = (n * f11 - oa * ob) / (n * oa);
                    }
                } 
                return correlation;
            }
        }

        public class Binomial {

            public double getPMF(int n, int occur, double p, Boolean proxi) {
                //calculate the probability mass function for binomial distribution
                //if use the accurate calculation, we use the exact formula
                //if use the proximated calculation, we use the exact formula when occur< 50, and we use normal distribution when occur>=50
                double pro;
                if (2 * occur > n) {
                    pro = getPMF(n, n - occur, 1 - p, proxi);
                } else {
                    if (p == 0) {
                        if (occur == 0) {
                            pro = 1;
                        } else {
                            pro = 0;
                        }
                    } else if (p == 1) {
                        //we are sure pro=0 because occur<n/2
                        pro = 0;
                    } else {
                        if (proxi) {
                            if (occur < 50) {
                                double intermediate = occur * Math.log10(p) + (n - occur) * Math.log10(1 - p);
                                for (int i = 0; i < occur; i++) {
                                    intermediate += Math.log10(n - i) - Math.log10(occur - i);
                                }
                                pro = Math.pow(10, intermediate);
                            } else {
                                double mean = n * p;
                                double var = n * p * (1 - p);
                                pro = Math.pow(Math.E, -(occur - mean) * (occur - mean) / (2 * var)) / Math.sqrt(2 * Math.PI * var);
                            }
                        } else {
                            double intermediate = occur * Math.log10(p) + (n - occur) * Math.log10(1 - p);
                            for (int i = 0; i < occur; i++) {
                                intermediate += Math.log10(n - i) - Math.log10(occur - i);
                            }
                            pro = Math.pow(10, intermediate);
                        }
                    }
                }
                return pro;
            }

            public double getWeight(int n, int occur, double p) {
                double weight;
                if (p == 0) {
                    if (occur == 0) {
                        weight = 1;
                    } else {
                        weight = 0;
                    }
                } else if (p == 1) {
                    if (occur == n) {
                        weight = 1;
                    } else {
                        weight = 0;
                    }
                } else {
                    weight = Math.pow(10, occur * Math.log10(p) + (n - occur) * Math.log10(1 - p));
                }
                return weight;
            }

            public double[] searchDirectionInterval(double n, double occur, int number_of_interval, int cutoff_index, Boolean proxi) {
                //try to find the different interval for different direction
                //we want to find the interval for each direction that more than number_of_interval have the probability greater than the cutoff.
                double[] interval = new double[2];
                double start_point, end_point, middle_point, probability_difference;
                //search the left side
                start_point = 0;
                end_point = occur / n;
                probability_difference = 1;
                while (probability_difference > Math.pow(10, -cutoff_index)) {
                    middle_point = (start_point + end_point) / 2;
                    if (getPMF((int) n, (int) occur, middle_point, proxi) > Math.pow(10, -cutoff_index)) {
                        end_point = middle_point;
                    } else {
                        start_point = middle_point;
                    }
                    probability_difference = getPMF((int) n, (int) occur, end_point, proxi) - getPMF((int) n, (int) occur, start_point, proxi);
                }
                middle_point = (start_point + end_point) / 2;
                interval[0] = (occur / n - middle_point) / number_of_interval;
                //search the right side
                start_point = occur / n;
                end_point = 1;
                probability_difference = 1;
                while (probability_difference > Math.pow(10, -cutoff_index)) {
                    middle_point = (start_point + end_point) / 2;
                    if (getPMF((int) n, (int) occur, middle_point, proxi) > Math.pow(10, -cutoff_index)) {
                        start_point = middle_point;
                    } else {
                        end_point = middle_point;
                    }
                    probability_difference = getPMF((int) n, (int) occur, start_point, proxi) - getPMF((int) n, (int) occur, end_point, proxi);
                }
                middle_point = (start_point + end_point) / 2;
                interval[1] = (middle_point - occur / n) / number_of_interval;
                return interval;
            }

            public double searchInterval(double n, double occur, double granularity) {
                //we want to find the interval which is smalller than 1/(10^granularity) of deviation
                //variance=n*p*(1-p). given the occur and n, the possible deviation is sqrt(n*p*(1-p)). 
                //convert the deviation to probability: sqrt(n*p*(1-p))/n
                //we get the deviation baseline by using continuity correction
                //here, we find the first 10^-power that is smaller than the deviation
                if (occur * 2 > n) {
                    occur = n - occur;
                }
                double baseline = Math.sqrt((occur + 0.5) * (n - occur - 0.5) / n) / n;
                int power = 0;
                for (int i = 0; i < 20; i++) {
                    if (Math.pow(10, -i) < baseline) {
                        power = i;
                        break;
                    }
                }
                //ratio indicates how many times the deviation is bigger than 10^-power
                double ratio = baseline / Math.pow(10, -power);
                int coefficient = 0;
                //we hope to find the interval that is smaller than 1/(10^granularity) of deviation.
                for (int i = 0; i < 30; i++) {
                    if (ratio * Math.pow(2, i) > Math.pow(10, granularity)) {
                        coefficient = i - 1;
                        break;
                    }
                }
                double interval = Math.pow(10, -power) * Math.pow(2, -coefficient);
                //System.out.println(granularity+",coefficient:"+coefficient+",baseline:"+baseline+",ratio:"+ratio+",interval:"+interval);                
                return interval;
            }

            private void checkgetPMF() {
                // test 1: fix occur 3, change n and p=occur/n, get the probability as follows:
                // 3,10,0.2668279319999995                 
                // 3,100,0.22747412748216278
                // 3,1000,0.2243785721398652
                // 3,10000,0.22407542092955504
                // 3,100000,0.22404516835308907
                // 3,1000000,0.2240421437243462
                // 3,10000000,0.22404184136670705
                // 3,100000000,0.22404181012611185
                // 3,1000000000,0.22404180212681804
                // get the conclusion that for large n, we need to use the finer gralunarity.
                // test 2: fix the large n and small occur, change p according to the granularity as follows
                int occur = 10;
                int[] list = {0, 1, 2, 3, 5, 8};
                int zz = 9;
                int n = (int) Math.pow(10, zz);
                for (int x = 0; x < zz; x++) {
                    for (int y = 0; y < list.length; y++) {
                        occur = (int) (list[y] * Math.pow(10, x));
                        double interval = searchInterval(n, occur, 1) * n;
                        for (double j = occur; j < n; j = j + interval) {
                            double p = (double) j / n;
                            double pro = getPMF(n, occur, p, true);
                            if (pro > Math.pow(10, -50)) {
                                //System.out.println(j + "," + n + "," + pro);
                            } else {
                                System.out.println(occur + "," + j + "," + n + "," + interval + "," + ((j - occur) / interval));
                                break;
                            }
                        }
                    }
                }

            }
        }
    }

    private void checkSortAlgorithm() {
        int k = 100;
        int n = 1000000;
        double[] tmp = new double[n];
        int[] ranking = new int[n];
        Random generator = new Random();
        for (int i = 0; i < n; i++) {
            tmp[i] = (double) generator.nextInt(n);
            ranking[i] = i;
        }
        prog.showConsumedTime();
        int[] sequence1 = topk.getTopKSequence(tmp, k, true);
        prog.showConsumedTime();
        int[] sequence2 = topk.getTopKSequence_SlowVersion(tmp, k, Boolean.TRUE);
        prog.showConsumedTime();
        int[] sequence3 = qs.sort(tmp, ranking, false);
        prog.showConsumedTime();
        System.out.println("************");

        int[] nextsequence = qs.sort(tmp, ranking, Boolean.FALSE);
        for (int i = 0; i < nextsequence.length; i++) {
            //System.out.println(tmp[nextsequence[i]]);
        }
        System.out.println("************");
        prog.showConsumedTime();
        ArrayList original = topk.getOriginalVector4TopKPushing();
        for (int i = 0; i < tmp.length; i++) {
            //prog.showProcessedRecords(2);
            ArrayList items = new ArrayList();
            items.add(i + "");
            topk.TopKPushing(original, tmp[i], items, k);
        }
        prog.showConsumedTime();
        for (int i = 0; i < original.size(); i++) {
            System.out.println(((ArrayList) original.get(i)).get(0) + "," + ((ArrayList) original.get(i)).get(1));
        }

    }

    public static void main(String[] args) {
        //double[] tmp = {2345, 2, 4563, 324, 435, 23, 4234, 546, 573, 767, 2, 423, 9};
        //int[] ranking = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        GeneralFunction gf = new GeneralFunction();
        //gf.eva.testNonoverlappingClusteringEvaluation();
        //gf.eva.testNonoverlappingClusterMatching();
        //gf.eva.testBinaryRanking();
        //gf.eva.testRelevanceRanking();    
        //gf.checkSortAlgorithm();
    }
}
