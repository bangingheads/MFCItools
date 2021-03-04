/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processing;

import java.io.*;
import java.util.*;

/**
 *
 * @author lianduan
 *
 * 1. In this version, we don't consider the absence of an item is the presence
 * of absence and do the special handling any more. This information can be
 * found by specially load the data. For each item, we can load the record of
 * presence and the presence of absence.
 *
 * initialData(String item_file, String transaction_file, Boolean
 * including_absence): load the data from text file.
 *
 * getAllItemString(): return the comma separated string containing all the item
 * id.
 *
 * getItemsetNameInformation(String[] item_id): get the string to describe items
 * in the String[] item_id
 *
 * getOccurrence(ArrayList item_list): the itemset occurrence. String instead of
 * Integer is saved in item_list.
 *
 * getItemsetOccurrenceInformation(String[] item_id, Boolean item_detail): get
 * the occurrence information
 *
 * getCorrelation(ArrayList item_list, double cc, String type): get the
 * (semi-)correlation value of the item_list
 *
 * getItemsetCorrelationInformation(String[] item_id, double cc, String type,
 * Boolean item_detail): get the (semi-)correlation information
 *
 * Class PairCorrelationSearch
 *
 * generateAllThePairInformation(String file_location): save all the pair
 * measure value in a file
 *
 * getPairCorrelationUpperBound(ArrayList item_list, double cc, String type):
 * get the upper bound of a pair for each measure
 *
 * getPairCandidateCountingInformation(String[] item_id, String
 * pair_search_type, String correlation_type, double cc, double
 * correlation_threshold, Boolean item_detail):
 *
 * pairCandidateCounting(String[] item_id, String pair_search_type, String
 * correlation_type, double cc, double correlation_threshold): get the number of
 * pair (consist from String[] item_id) whose upper bound is greater than the
 * threshold
 *
 * checkCandidate(ArrayList pair, ArrayList satisfied_pairs, String
 * correlation_type, double cc, double correlation_threshold): check whether the
 * actual correlation is greater than the threshold
 *
 * getPairAboveThresholdInformation(String[] item_id, String pair_search_type,
 * String correlation_type, double cc, double correlation_threshold, Boolean
 * item_detail)
 *
 * getPairAboveThreshold(String[] item_id, String pair_search_type, String
 * correlation_type, double cc, double correlation_threshold): we only try to
 * find all the possible pairs related to item_id[] whose correlation value is
 * above the threshold. The final result is (1) int candidate_count (2) int
 * satisfied_pair_count (3) ArrayList satisfied_pairs. and the satisfied_pairs
 * --> ArrayList pair_information, it has (a) (double) correlation (b)
 * (ArrayList) pair
 *
 * getTopKPairInformation(String[] item_id, int topK, String pair_search_type,
 * String correlation_type, double cc, double starting_threshold, Boolean
 * item_detail)
 *
 * getTopKPair(String[] item_id, int topK, String pair_search_type, String
 * correlation_type, double cc, double starting_threshold): we only try to find
 * top k pairs related to item_id[]. The final result is (1) int
 * upper_calculation (2) int num_retrieved_support (3) ArrayList topK pairs, and
 * the topK pairs-->ArrayList pair_information --> (a) double correlation_value
 * (b) ArrayList pair
 *
 * Class MaximalFullyCorrelatedItemset
 *
 *
 */
public class CorrelationSearch {

    ArrayList items;//save the discription for each item which is read from Item.txt
    BitSet[] item_tran;//save the vertical record for each item which is read from the transaction file
    int total_item;
    int total_tran;
    GeneralFunction gf = new GeneralFunction();
    ArrayList itemset_correlation_type = gf.stat.corr.getItemsetCorrelationType();
    ArrayList pair_correlation_type = gf.stat.corr.getPairCorrelationType();
    int[] item_occur;//save the occurrence for each item
    PairCorrelationSearch pcs = new PairCorrelationSearch();
    MaximalFullyCorrelatedItemset mfci = new MaximalFullyCorrelatedItemset();
	
    public void initialData(String item_file, String transaction_file, Boolean including_absence) {
        File file;
        FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        items = new ArrayList();
        file = new File(item_file);
        try {
			System.out.println(6);

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                items.add(dis.readLine());
            }
            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
		System.out.println(5);

        if (including_absence) {
            //add the absence as the presence of absence
            int size = items.size();
            for (int i = 0; i < size; i++) {
                items.add("Not " + items.get(i));
            }
        }
        total_item = items.size();
        total_tran = 0;
        file = new File(transaction_file);
        try {
			System.out.println(4);

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                dis.readLine();
                total_tran++;
            }
            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
					System.out.println(3);

        item_tran = new BitSet[total_item];
        for (int i = 0; i < total_item; i++) {
            item_tran[i] = new BitSet(total_tran);
        }
        file = new File(transaction_file);
        try {
			System.out.println(2);

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            for (int i = 0; i < total_tran; i++) {
                String data = dis.readLine();
                String[] item_ids = data.split(",");
                for (int j = 0; j < item_ids.length; j++) {
                    item_tran[Integer.parseInt(item_ids[j])].set(i);
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
				System.out.println(1);

        if (including_absence) {
            //get the fliped records
            int size = total_item / 2;
            for (int i = 0; i < size; i++) {
                BitSet temp = (BitSet) item_tran[i].clone();
                temp.flip(0, total_tran);
                item_tran[size + i] = temp;
            }
        }
        item_occur = new int[items.size()];
		System.out.println(item_occur.length);
        for (int i = 0; i < item_occur.length; i++) {
            item_occur[i] = item_tran[i].cardinality();
        }
    }

    public String getAllItemString() {
        String output = "";
        for (int i = 0; i < items.size(); i++) {
            output += i + ",";
        }
        if (!output.isEmpty()) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

    private String getItemsetNameInformation(String[] item_id) {
        String output = "The itemset contains:\n";
        for (int i = 0; i < item_id.length; i++) {
            output += items.get(Integer.parseInt(item_id[i])) + "\n";
        }
        return output;
    }

    private int getOccurrence(ArrayList item_list) {
        int occur = 0;
        if (!item_list.isEmpty()) {
            BitSet intersection = (BitSet) item_tran[Integer.parseInt((String) item_list.get(0))].clone();
            for (int i = 1; i < item_list.size(); i++) {
                intersection.and(item_tran[Integer.parseInt((String) item_list.get(i))]);
            }
            occur = intersection.cardinality();
        }
        return occur;
    }

    public String getItemsetOccurrenceInformation(String[] item_id, Boolean item_detail) {
        ArrayList item_list = new ArrayList();
        item_list.addAll(Arrays.asList(item_id));
        String output = "The occurrrence is " + getOccurrence(item_list) + " out of " + total_tran + ".\n\n";
        if (item_detail) {
            output += getItemsetNameInformation(item_id);
        }
        return output;
    }

    private double getCorrelation(ArrayList item_list, double cc, String type) {
        double correlation = 0;
        if (itemset_correlation_type.contains(type)) {
            double tp = (double) getOccurrence(item_list) / total_tran;
            double ep = 1;
            for (int i = 0; i < item_list.size(); i++) {
                ep *= (double) item_occur[Integer.parseInt((String) item_list.get(i))] / total_tran;
            }
            correlation = gf.stat.corr.correlation_for_itemset(tp, ep, total_tran, cc, type);
        } else if (pair_correlation_type.contains(type)) {
            if (item_list.size() == 2) {
                double oa = item_occur[Integer.parseInt((String) item_list.get(0))];
                double ob = item_occur[Integer.parseInt((String) item_list.get(1))];
                double k = getOccurrence(item_list);
                correlation = gf.stat.corr.correlation_for_pair(oa, ob, k, total_tran, type);
            } else {
                System.out.println("Error! This type is only for pairs!");
            }
        } else if (type.equals("Support")) {
            correlation = (double) getOccurrence(item_list) / total_tran;
        } else if (type.equals("Any-confidence")) {
            double tp = (double) getOccurrence(item_list) / total_tran;
            double min_pi = 1;
            for (int i = 0; i < item_list.size(); i++) {
                min_pi = Math.min((double) item_occur[Integer.parseInt((String) item_list.get(i))] / total_tran, min_pi);
            }
            correlation = gf.stat.corr.any_confidence(tp, min_pi);
        } else if (type.equals("All-confidence")) {
            double tp = (double) getOccurrence(item_list) / total_tran;
            double max_pi = 0;
            for (int i = 0; i < item_list.size(); i++) {
                max_pi = Math.max((double) item_occur[Integer.parseInt((String) item_list.get(i))] / total_tran, max_pi);
            }
            correlation = gf.stat.corr.any_confidence(tp, max_pi);
        } else if (type.equals("Bond")) {
            double tp = (double) getOccurrence(item_list) / total_tran;
            BitSet start = new BitSet(total_tran);
            for (int i = 0; i < item_list.size(); i++) {
                start.or(item_tran[Integer.parseInt((String) item_list.get(i))]);
            }
            double ntp = 1 - (double) start.cardinality() / total_tran;
            correlation = gf.stat.corr.bond(tp, ntp);
        }
        return correlation;
    }

    public String getItemsetCorrelationInformation(String[] item_id, double cc, String type, Boolean item_detail) {
        ArrayList item_list = new ArrayList();
        item_list.addAll(Arrays.asList(item_id));
        String output = "The " + type + " is " + getCorrelation(item_list, cc, type) + ".\n\n";
        if (item_detail) {
            output += getItemsetNameInformation(item_id);
        }
        return output;
    }

    public class PairCorrelationSearch {

        public void generateAllThePairInformation(String file_location) {
            ArrayList measure_type = gf.stat.corr.getMeasureType();
            //remove the last type because the current calculation needs change
            measure_type.remove(measure_type.size() - 1);
            gf.txt.getWriter(file_location);
            gf.txt.clearContent(file_location);
            String output = "id1,id2,";
            for (int i = 0; i < measure_type.size(); i++) {
                output += measure_type.get(i) + ",";
            }
            output = output.substring(0, output.length() - 1) + "\n";
            gf.txt.appendNewContent(output);
            for (int i = 0; i < items.size(); i++) {
                gf.prog.showProcessedRecords(100);
                for (int j = i + 1; j < items.size(); j++) {
                    ArrayList pair = new ArrayList();
                    pair.add(i + "");
                    pair.add(j + "");
                    output = i + "," + j + ",";
                    for (int k = 0; k < measure_type.size(); k++) {
                        String type = (String) measure_type.get(k);
                        output += getCorrelation(pair, 0.5, type) + ",";
                    }
                    output = output.substring(0, output.length() - 1) + "\n";
                    gf.txt.appendNewContent(output);
                }
            }
            gf.txt.closeWriter();
        }

        private double getPairCorrelationUpperBound(ArrayList item_list, double cc, String type) {
            double correlation = 0;
            if (itemset_correlation_type.contains(type)) {
                double tp = 1;
                double ep = 1;
                for (int i = 0; i < item_list.size(); i++) {
                    double value = (double) item_occur[Integer.parseInt((String) item_list.get(i))] / total_tran;
                    ep *= value;
                    tp = Math.min(tp, value);
                }
                correlation = gf.stat.corr.correlation_for_itemset(tp, ep, total_tran, cc, type);
            } else if (pair_correlation_type.contains(type)) {
                if (item_list.size() == 2) {
                    double oa = item_occur[Integer.parseInt((String) item_list.get(0))];
                    double ob = item_occur[Integer.parseInt((String) item_list.get(1))];
                    double k = Math.min(oa, ob);
                    correlation = gf.stat.corr.correlation_for_pair(oa, ob, k, total_tran, type);
                } else {
                    System.out.println("Error! This type is only for pairs!");
                }
            } else if (type.equals("Support")) {
                double tp = 1;
                for (int i = 0; i < item_list.size(); i++) {
                    tp = Math.min((double) item_occur[Integer.parseInt((String) item_list.get(i))] / total_tran, tp);
                }
                correlation = tp;
            } else if (type.equals("Any-confidence")) {
                correlation = 1;
            } else if (type.equals("All-confidence")) {
                double tp = 1;
                double max_pi = 0;
                for (int i = 0; i < item_list.size(); i++) {
                    double value = (double) item_occur[Integer.parseInt((String) item_list.get(i))] / total_tran;
                    max_pi = Math.max(value, max_pi);
                    tp = Math.min(tp, value);
                }
                correlation = gf.stat.corr.any_confidence(tp, max_pi);
            } else if (type.equals("Bond")) {
                double tp = 1;
                double max_pi = 0;
                for (int i = 0; i < item_list.size(); i++) {
                    double value = (double) item_occur[Integer.parseInt((String) item_list.get(i))] / total_tran;
                    max_pi = Math.max(value, max_pi);
                    tp = Math.min(tp, value);
                }
                double ntp = 1 - max_pi;
                correlation = gf.stat.corr.bond(tp, ntp);
            }
            return correlation;
        }

        public String getPairCandidateCountingInformation(String[] item_id, String pair_search_type, String correlation_type, double cc, double correlation_threshold, Boolean item_detail) {
            long history = gf.prog.getCurrentTime();
            int count = pairCandidateCounting(item_id, pair_search_type, correlation_type, cc, correlation_threshold);
            long consume = (gf.prog.getCurrentTime() - history);
            String output = "When the pair search type is " + pair_search_type + ", correlation type is " + correlation_type + ", and the threshold is " + correlation_threshold + ", it takes " + consume + " milliseconds and the count is " + count + ".\n\n";
            if (item_detail) {
                output += getItemsetNameInformation(item_id);
            }
            return output;
        }

        public int pairCandidateCounting(String[] item_id, String pair_search_type, String correlation_type, double cc, double correlation_threshold) {
            //we only try to find all the possible pairs related to item_id[]
            int candidate_count = 0;
            double[] item_id_occur = new double[item_id.length];
            for (int i = 0; i < item_id.length; i++) {
                item_id_occur[i] = item_occur[Integer.parseInt(item_id[i])];
            }
            int[] break_ranking = new int[item_id.length];
            int[] sequence = gf.qs.sort(item_id_occur, break_ranking, true);
            if (pair_search_type.equals("Two-dimensional Search") | pair_search_type.equals("Token-ring Search")) {
                Boolean endtoexist = false;
                for (int i = 0; i < item_id.length - 1; i++) {
                    if (endtoexist) {
                        break;
                    }
                    String first_item = item_id[sequence[i]];
                    for (int j = i + 1; j < item_id.length; j++) {
                        String second_item = item_id[sequence[j]];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        if (correlation_upper_bound >= correlation_threshold) {
                            candidate_count += (j - i);
                            if (j == item_id.length - 1) {
                                endtoexist = Boolean.TRUE;
                            }
                        } else {
                            if (i < j - 1) {
                                i++;
                                j--;
                            } else {
                                i++;
                            }
                            first_item = item_id[sequence[i]];
                        }
                    }
                }
            } else if (pair_search_type.equals("One-dimensional Search")) {
                for (int i = 0; i < item_id.length - 1; i++) {
                    String first_item = item_id[sequence[i]];
                    for (int j = i + 1; j < item_id.length; j++) {
                        String second_item = item_id[sequence[j]];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        if (correlation_upper_bound >= correlation_threshold) {
                            candidate_count++;
                        } else {
                            break;
                        }
                    }
                }
            } else if (pair_search_type.equals("Brute-force Search")) {
                for (int i = 0; i < item_id.length - 1; i++) {
                    String first_item = item_id[sequence[i]];
                    for (int j = i + 1; j < item_id.length; j++) {
                        String second_item = item_id[sequence[j]];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        if (correlation_upper_bound >= correlation_threshold) {
                            candidate_count++;
                        }
                    }
                }
            }
            return candidate_count;
        }

        private int checkCandidate(ArrayList pair, ArrayList satisfied_pairs, String correlation_type, double cc, double correlation_threshold) {
            //the parameter satisfied_pairs save the pair information, it has (1) (double) correlation (2) (ArrayList) pair
            double correlation = getCorrelation(pair, cc, correlation_type);
            if (correlation >= correlation_threshold) {
                ArrayList object = new ArrayList();
                object.add(correlation);
                object.add(pair);
                satisfied_pairs.add(object);
                return 1;
            } else {
                return 0;
            }
        }

        public String getPairAboveThresholdInformation(String[] item_id, String pair_search_type, String correlation_type, double cc, double correlation_threshold, Boolean item_detail) {
            long history = gf.prog.getCurrentTime();
            ArrayList result = getPairAboveThreshold(item_id, pair_search_type, correlation_type, cc, correlation_threshold);
            long consume = (gf.prog.getCurrentTime() - history);
            String output = "When the pair search type is " + pair_search_type + ", correlation type is " + correlation_type + ", and the threshold is " + correlation_threshold + ", it takes " + consume + " milliseconds, the number of candiates is " + result.get(0) + ", and the number of satisfied pairs is " + result.get(1) + ".\n\n";
            ArrayList pair_information = (ArrayList) result.get(2);
            for (int i = 0; i < pair_information.size(); i++) {
                ArrayList each_pair = (ArrayList) pair_information.get(i);
                if (item_detail) {
                    ArrayList pair_id = (ArrayList) each_pair.get(1);
                    System.out.println("The pair is (" + items.get(Integer.parseInt((String) pair_id.get(0))) + "; " + items.get(Integer.parseInt((String) pair_id.get(1))) + "), and its correlation is " + each_pair.get(0));
                } else {
                    System.out.println("The pair is " + each_pair.get(1) + ", and its correlation is " + each_pair.get(0));
                }
            }
            System.out.println();
            return output;
        }

        //we only try to find all the possible pairs related to item_id[]
        //the final result is (1) int candidate_count (2) int satisfied_pair_count (3) ArrayList satisfied_pairs
        //satisfied_pairs --> ArrayList pair_information, it has (1) (double) correlation (2) (ArrayList) pair
        public ArrayList getPairAboveThreshold(String[] item_id, String pair_search_type, String correlation_type, double cc, double correlation_threshold) {
            int candidate_count = 0;
            int satisfied_pair_count = 0;
            ArrayList satisfied_pairs = new ArrayList();
            double[] item_id_occur = new double[item_id.length];
            for (int i = 0; i < item_id.length; i++) {
                item_id_occur[i] = item_occur[Integer.parseInt(item_id[i])];
            }
            int[] break_ranking = new int[item_id.length];
            int[] sequence = gf.qs.sort(item_id_occur, break_ranking, true);
            if (pair_search_type.equals("Two-dimensional Search") | pair_search_type.equals("Token-ring Search")) {
                Boolean endtoexist = false;
                for (int i = 0; i < item_id.length - 1; i++) {
                    if (endtoexist) {
                        break;
                    }
                    String first_item = item_id[sequence[i]];
                    for (int j = i + 1; j < item_id.length; j++) {
                        String second_item = item_id[sequence[j]];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        if (correlation_upper_bound >= correlation_threshold) {
                            //check real correlation from (i,j) to (j-1,j)
                            for (int k = i; k < j; k++) {
                                String temp_item = item_id[sequence[k]];
                                ArrayList temp_pair = new ArrayList();
                                temp_pair.add(temp_item);
                                temp_pair.add(second_item);
                                satisfied_pair_count += checkCandidate(temp_pair, satisfied_pairs, correlation_type, cc, correlation_threshold);
                            }
                            candidate_count += (j - i);
                            if (j == item_id.length - 1) {
                                endtoexist = Boolean.TRUE;
                            }
                        } else {
                            if (i < j - 1) {
                                i++;
                                j--;
                            } else {
                                i++;
                            }
                            first_item = item_id[sequence[i]];
                        }
                    }
                }
            } else if (pair_search_type.equals("One-dimensional Search")) {
                for (int i = 0; i < item_id.length - 1; i++) {
                    String first_item = item_id[sequence[i]];
                    for (int j = i + 1; j < item_id.length; j++) {
                        String second_item = item_id[sequence[j]];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        if (correlation_upper_bound >= correlation_threshold) {
                            candidate_count++;
                            satisfied_pair_count += checkCandidate(pair, satisfied_pairs, correlation_type, cc, correlation_threshold);
                        } else {
                            break;
                        }
                    }
                }
            } else if (pair_search_type.equals("Brute-force Search")) {
                for (int i = 0; i < item_id.length - 1; i++) {
                    String first_item = item_id[sequence[i]];
                    for (int j = i + 1; j < item_id.length; j++) {
                        String second_item = item_id[sequence[j]];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        if (correlation_upper_bound >= correlation_threshold) {
                            candidate_count++;
                            satisfied_pair_count += checkCandidate(pair, satisfied_pairs, correlation_type, cc, correlation_threshold);
                        }
                    }
                }
            }
            ArrayList result = new ArrayList();
            result.add(candidate_count);
            result.add(satisfied_pair_count);
            result.add(satisfied_pairs);
            return result;
        }

        public String getTopKPairInformation(String[] item_id, int topK, String pair_search_type, String correlation_type, double cc, double starting_threshold, Boolean item_detail) {
            long history = gf.prog.getCurrentTime();
            ArrayList result = getTopKPair(item_id, topK, pair_search_type, correlation_type, cc, starting_threshold);
            long consume = (gf.prog.getCurrentTime() - history);
            String output = "When the pair search type is " + pair_search_type + ", correlation type is " + correlation_type
                    + ", and the starting threshold is " + starting_threshold + ", it takes " + consume
                    + " milliseconds, the number of upperbound calculation is " + result.get(0) + ", and the number of retrieved support is "
                    + result.get(1) + ".\n\n";
            ArrayList pair_information = (ArrayList) result.get(2);
            for (int i = 0; i < pair_information.size(); i++) {
                ArrayList each_pair = (ArrayList) pair_information.get(i);
                if (item_detail) {
                    ArrayList pair_id = (ArrayList) each_pair.get(1);
                    output += "The pair is (" + items.get(Integer.parseInt((String) pair_id.get(0))) + ";" + items.get(Integer.parseInt((String) pair_id.get(1))) + "), and its correlation is " + each_pair.get(0) + ".\n";
                } else {
                    output += "The pair is (" + each_pair.get(1) + "), and its correlation is " + each_pair.get(0) + ".\n";
                }
            }
            return output;
        }

        //we only try to find top k pairs related to item_id[]
        //the final result is (1) int upper_calculation (2) int num_retrieved_support (3) ArrayList topK pairs 
        //topK pairs-->ArrayList pair_information --> (a) double correlation_value (b) ArrayList pair
        public ArrayList getTopKPair(String[] item_id, int topK, String pair_search_type, String correlation_type, double cc, double starting_threshold) {
            ArrayList result = gf.topk.getOriginalVector4TopKPushing();
            double[] item_id_occur = new double[item_id.length];
            for (int i = 0; i < item_id.length; i++) {
                item_id_occur[i] = item_occur[Integer.parseInt(item_id[i])];
            }
            int[] break_ranking = new int[item_id.length];
            int[] sequence = gf.qs.sort(item_id_occur, break_ranking, true);
            int upper_calculation = 0;
            int num_retrieved_support = 0;
            double cur_threshold = starting_threshold;
            if (pair_search_type.equals("Two-dimensional Search") || pair_search_type.equals("One-dimensional Search")) {
                for (int i = 0; i < item_id.length - 1; i++) {
                    //first loop contains (#_item - 1) steps
                    double max_upperbound = 0;
                    for (int j = 0; j < item_id.length - i - 1; j++) {
                        //second loop contains (#_item - i - 1) steps
                        String first_item = item_id[sequence[j]];
                        String second_item = item_id[sequence[i + j + 1]];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        upper_calculation++;
                        if (correlation_upper_bound > max_upperbound) {
                            max_upperbound = correlation_upper_bound;
                        }
                        if (correlation_upper_bound > cur_threshold) {
                            num_retrieved_support++;
                            double true_correlation = getCorrelation(pair, cc, correlation_type);
                            if (true_correlation > cur_threshold) {
                                gf.topk.TopKPushing(result, true_correlation, pair, topK);
                                double k_value = (Double) ((ArrayList) result.get(result.size() - 1)).get(0);
                                if (k_value > cur_threshold) {
                                    cur_threshold = k_value;
                                }
                            }
                        }
                    }
                    if (cur_threshold > max_upperbound) {
                        break;
                    }
                }
            } else if (pair_search_type.equals("Brute-force Search")) {
                for (int i = 0; i < item_id.length - 1; i++) {
                    String first_item = item_id[i];
                    for (int j = i + 1; j < item_id.length; j++) {
                        String second_item = item_id[j];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        upper_calculation++;
                        if (correlation_upper_bound > cur_threshold) {
                            num_retrieved_support++;
                            double true_correlation = getCorrelation(pair, cc, correlation_type);
                            if (true_correlation > cur_threshold) {
                                gf.topk.TopKPushing(result, true_correlation, pair, topK);
                                double k_value = (Double) ((ArrayList) result.get(result.size() - 1)).get(0);
                                if (k_value > cur_threshold) {
                                    cur_threshold = k_value;
                                }
                            }
                        }
                    }
                }
            } else if (pair_search_type.equals("Token-ring Search")) {
                int[] branch_start_point = new int[item_id.length - 1];
                ArrayList living_branch = new ArrayList();
                for (int i = 0; i < item_id.length - 1; i++) {
                    branch_start_point[i] = i + 1;
                    living_branch.add(i + "");
                }
                for (int i = 0; i < item_id.length - 1; i++) {
                    //first loop contains #_item-1 steps
                    double max_upperbound = 0;
                    for (int j = 0; j < living_branch.size(); j++) {
                        int branch_num = Integer.parseInt((String) living_branch.get(j));
                        String first_item = item_id[sequence[branch_num]];
                        String second_item = item_id[sequence[branch_start_point[branch_num]]];
                        ArrayList pair = new ArrayList();
                        pair.add(first_item);
                        pair.add(second_item);
                        double correlation_upper_bound = getPairCorrelationUpperBound(pair, cc, correlation_type);
                        upper_calculation++;
                        if (correlation_upper_bound > max_upperbound) {
                            //get the maximal upper bound for this diagonal
                            max_upperbound = correlation_upper_bound;
                        }
                        branch_start_point[branch_num]++;
                        if (correlation_upper_bound > cur_threshold) {
                            if (branch_start_point[branch_num] == item_id.length) {
                                living_branch.remove(j);
                            }
                            num_retrieved_support++;
                            double true_correlation = getCorrelation(pair, cc, correlation_type);
                            if (true_correlation > cur_threshold) {
                                gf.topk.TopKPushing(result, true_correlation, pair, topK);
                                double k_value = (Double) ((ArrayList) result.get(result.size() - 1)).get(0);
                                if (k_value > cur_threshold) {
                                    cur_threshold = k_value;
                                }
                            }
                        } else {
                            living_branch.remove(j);
                            j--;
                        }
                    }
                    if (living_branch.isEmpty()) {
                        break;
                    }
                }
            }
            gf.topk.correctTopKPush(result);
            ArrayList final_result = new ArrayList();
            final_result.add(upper_calculation);
            final_result.add(num_retrieved_support);
            final_result.add(result);
            return final_result;
        }
    }
    public String ShowMFCI(String[] item_id, Integer topK, String corr_type, double continuity_correction, double starting_threshold, Boolean singleton) {
		return mfci.ShowMFCI(item_id, topK, corr_type, continuity_correction, starting_threshold, singleton);
	}
	
	public String getTopKPairInformation(String[] item_id, int topK, String pair_search_type, String correlation_type, double cc, double starting_threshold, Boolean item_detail) {
		return pcs.getTopKPairInformation(item_id,topK,pair_search_type,correlation_type,cc,starting_threshold, item_detail);
		
	}

	
	
	
	
    public class MaximalFullyCorrelatedItemset {

        private prefixTree current_layer_tree;
        private prefixTree previous_layer_tree;
        private double correlation_threshold;
        private double cc;
        private String correlation_type;
        private ArrayList mfcis;

        public String ShowMFCI(String[] item_id, int topK, String corr_type, double continuity_correction, double starting_threshold, Boolean singleton) {
            GenerateMFCI(item_id, topK, corr_type, continuity_correction, starting_threshold, singleton);
            String output = "The current maximal fully-correlated itemsets contain:\n\n";
            for (int i = 0; i < mfcis.size(); i++) {
                ArrayList itemset = (ArrayList) mfcis.get(i);
                String[] item = new String[itemset.size()];
                for(int j=0;j<itemset.size();j++) {
                    item[j]=(String)itemset.get(j);
                }
                output += (i + 1) + ". " + getItemsetNameInformation(item) + "\n";
            }
            return output;
        }

        public ArrayList GenerateMFCI(String[] item_id, int topK, String corr_type, double continuity_correction, double starting_threshold, Boolean singleton) {
            correlation_threshold = starting_threshold;
            mfcis = new ArrayList();
            current_layer_tree = new prefixTree();
            previous_layer_tree = new prefixTree();
            cc = continuity_correction;
            correlation_type = corr_type;
            GenerateInitialPairs(item_id, topK, correlation_type, cc, starting_threshold);
            if (singleton) {
                GetUnmergedItemset();
            }
            while (!current_layer_tree.root.child.isEmpty()) {
                GenerateNextLayerTree();
                GetUnmergedItemset();
            }
            return mfcis;
        }

        //find the topK pairs in the given item_id list
        private void GenerateInitialPairs(String[] item_id, int topK, String correlation_type, double cc, double starting_threshold) {
            ArrayList result = pcs.getTopKPair(item_id, topK, "Token-ring Search", correlation_type, cc, starting_threshold);
            ArrayList topKpairs = (ArrayList) result.get(2);
            for (int i = 0; i < topKpairs.size(); i++) {
                ArrayList pair = (ArrayList) topKpairs.get(i);
                double correlation = (Double) pair.get(0);
                ArrayList pair_id = (ArrayList) pair.get(1);
                String first_id = (String) pair_id.get(0);
                String second_id = (String) pair_id.get(1);
                ArrayList temp = new ArrayList();
                if (Integer.parseInt(first_id) > Integer.parseInt(second_id)) {
                    temp.add(second_id);
                    temp.add(first_id);
                } else {
                    temp.add(first_id);
                    temp.add(second_id);
                }
                correlation_threshold = correlation;
                current_layer_tree.BuildCurrentLayerTree(temp);
            }
            for (int i = 0; i < item_id.length; i++) {
                ArrayList temp = new ArrayList();
                temp.add(item_id[i]);
                previous_layer_tree.BuildCurrentLayerTree(temp);
            }
        }

        private void GetUnmergedItemset() {
            TravelCurrentLayerTree(current_layer_tree.root, new ArrayList());
            TravelPreviousLayerTree(previous_layer_tree.root, new ArrayList());
        }

        private void TravelCurrentLayerTree(prefixTree.Node cur_node, ArrayList prefix) {
            Object[] objs = cur_node.child.keySet().toArray();
            if (objs.length != 0) {//go deeper
                for (int i = 0; i < objs.length; i++) {
                    ArrayList temp_prefix = new ArrayList();
                    for (int j = 0; j < prefix.size(); j++) {
                        temp_prefix.add(prefix.get(j));
                    }
                    temp_prefix.add(objs[i]);
                    prefixTree.Node child_node = (prefixTree.Node) cur_node.child.get(objs[i]);
                    TravelCurrentLayerTree(child_node, temp_prefix);
                }
            } else {// check the layer, try to mark existing itemset in the previous layer tree
                for (int i = 0; i < prefix.size(); i++) {
                    ArrayList subset = new ArrayList();
                    for (int j = 0; j < prefix.size(); j++) {
                        if (i != j) {
                            subset.add(prefix.get(j));
                        }
                    }
                    MarkPreviousLayerTree(subset);
                }
            }
        }

        private void MarkPreviousLayerTree(ArrayList itemset) {
            prefixTree.Node cur_node = previous_layer_tree.root;
            for (int i = 0; i < itemset.size(); i++) {
                cur_node = (prefixTree.Node) cur_node.child.get(itemset.get(i));
            }
            cur_node.mark = Boolean.FALSE;
        }

        private void TravelPreviousLayerTree(prefixTree.Node cur_node, ArrayList prefix) {
            Object[] objs = cur_node.child.keySet().toArray();
            if (objs.length != 0) {//go deeper
                for (int i = 0; i < objs.length; i++) {
                    ArrayList temp_prefix = new ArrayList();
                    for (int j = 0; j < prefix.size(); j++) {
                        temp_prefix.add(prefix.get(j));
                    }
                    temp_prefix.add(objs[i]);
                    prefixTree.Node child_node = (prefixTree.Node) cur_node.child.get(objs[i]);
                    TravelPreviousLayerTree(child_node, temp_prefix);
                }
            } else {
                if (cur_node.mark) {
                    mfcis.add(prefix);
                }
            }
        }

        private void GenerateNextLayerTree() {
            previous_layer_tree = current_layer_tree;
            current_layer_tree = new prefixTree();
            //generate a new tree the unmarked itemset in the previous layer tree
            GenerateCandidateItemset(previous_layer_tree.root, new ArrayList());
        }

        private void GenerateCandidateItemset(prefixTree.Node cur_node, ArrayList prefix) {
            Object[] objs1 = cur_node.child.keySet().toArray();
            ArrayList leaf_item = new ArrayList();// to save all the leaf nodes
            for (int i = 0; i < objs1.length; i++) {
                ArrayList temp_prefix = new ArrayList();
                for (int j = 0; j < prefix.size(); j++) {
                    temp_prefix.add(prefix.get(j));
                }
                temp_prefix.add(objs1[i]);
                prefixTree.Node child_node = (prefixTree.Node) cur_node.child.get(objs1[i]);
                Object[] objs2 = child_node.child.keySet().toArray();
                if (objs2.length != 0) {//if not leaf node, go deeper
                    GenerateCandidateItemset(child_node, temp_prefix);
                } else {//the leaf node
                    leaf_item.add(objs1[i]);
                }
            }
            for (int i = 0; i < leaf_item.size() - 1; i++) {
                for (int j = i + 1; j < leaf_item.size(); j++) {
                    ArrayList candidate = (ArrayList) prefix.clone();
                    if (Integer.parseInt((String) leaf_item.get(i)) < Integer.parseInt((String) leaf_item.get(j))) {
                        candidate.add(leaf_item.get(i));
                        candidate.add(leaf_item.get(j));
                    } else {
                        candidate.add(leaf_item.get(j));
                        candidate.add(leaf_item.get(i));
                    }
                    ProcessCandidate(candidate);
                }
            }
        }

        private void ProcessCandidate(ArrayList itemset) {
            Boolean satisfied = Boolean.TRUE;
            for (int i = 0; i < itemset.size(); i++) {
                ArrayList subset = new ArrayList();
                for (int j = 0; j < itemset.size(); j++) {
                    if (i != j) {
                        subset.add(itemset.get(j));
                    }
                }
                //////////////check subset
                if (!CheckExistence(subset, previous_layer_tree.root)) {
                    satisfied = Boolean.FALSE;
                }
            }
            if (satisfied) {
                double corr_value = getCorrelation(itemset, cc, correlation_type);
                if (corr_value >= correlation_threshold) {
                    current_layer_tree.BuildCurrentLayerTree(itemset);
                }
            }
        }

        private Boolean CheckExistence(ArrayList itemset, prefixTree.Node tree_root) {
            Boolean output = Boolean.TRUE;
            prefixTree.Node cur_node = tree_root;
            for (int i = 0; i < itemset.size(); i++) {
                if (!cur_node.child.containsKey(itemset.get(i))) {
                    output = Boolean.FALSE;
                    break;
                } else {
                    cur_node = (prefixTree.Node) cur_node.child.get(itemset.get(i));
                }
            }
            return output;
        }
    }

    /*
     public class MaximalFullyCorrelatedItemset1 {

     private ArrayList transaction = new ArrayList();
     private Boolean getSingleton;
     private String correlation_type = "";
     private String pair_search_type = "";
     private Boolean general_format = Boolean.FALSE;
     private double correlation_threshold = Double.POSITIVE_INFINITY;
     private double cc = 0;
     private Boolean the_same_configuration = Boolean.FALSE;
     private ArrayList previous_transaction = new ArrayList();
     private String previous_correlation_type = "";
     private Boolean previous_general_format = Boolean.FALSE;
     private double previous_correlation_threshold = Double.POSITIVE_INFINITY;
     private double previous_cc = 0;
     public Node enu_tree = new Node();
     private ArrayList mfcis = new ArrayList();
     private Node mfci_tree = new Node();
     private Node pre_layer_tree_root = new Node();
     private Node cur_layer_tree_root = new Node();
     private int topK = 0;

     public String showMFCIResult(ArrayList result) {
     String output = "";
     for (int i = 0; i < result.size() / 2; i++) {
     ArrayList itemset = (ArrayList) result.get(i * 2);
     double fcv = (Double) result.get(i * 2 + 1);
     output += itemset + ", " + gf.roundDecimals(fcv, 4) + ", [";
     for (int j = 0; j < itemset.size(); j++) {
     String item = (String) itemset.get(j);
     output += "(" + items.get(Integer.parseInt(item)) + "), ";
     }
     output = output.substring(0, output.length() - 2) + "]\n";
     }
     return output;
     }

     public ArrayList (ArrayList trans, ArrayList corr_para, ArrayList corr_threshold, int K, String pstype, String ctype, Boolean singleton, Boolean geneal_form) {
     transaction = trans;
     getSingleton = singleton;
     correlation_type = ctype;
     pair_search_type = pstype;
     correlation_threshold = Double.parseDouble((String) corr_threshold.get(0));
     cc = Double.parseDouble((String) corr_para.get(0));
     topK = K;
     general_format = geneal_form;
     judgeConfiguration();
     mfcis = new ArrayList();
     pre_layer_tree_root = new Node();
     cur_layer_tree_root = new Node();
     if (!the_same_configuration) {
     enu_tree = new Node();
     enu_tree.correlation = Double.POSITIVE_INFINITY;
     }
     if (correlation_threshold < enu_tree.correlation) {//the enumeration tree needs to be incremented.
     GenerateInitialPairs();
     if (getSingleton) {
     GetUnmergedItemset();
     }
     while (!cur_layer_tree_root.child.isEmpty()) {
     GenerateNextLayerTree();
     GetUnmergedItemset();
     }
     enu_tree.correlation = correlation_threshold;
     } else {//generate the satisfied mfci from the current enumeration tree
     GenerateMFCIFromEnumerationTree();
     }
     previous_transaction = transaction;
     previous_correlation_type = correlation_type;
     previous_general_format = general_format;
     previous_cc = cc;
     if (!the_same_configuration || correlation_threshold < previous_correlation_threshold) {
     previous_correlation_threshold = correlation_threshold;
     }
     return mfcis;
     }

     private void judgeConfiguration() {
     if (previous_transaction.equals(transaction) && previous_correlation_type.equals(correlation_type) && previous_general_format == general_format && previous_cc == cc) {
     the_same_configuration = Boolean.TRUE;
     } else {
     the_same_configuration = Boolean.FALSE;
     }
     }

     private void GenerateMFCIFromEnumerationTree() {
     mfci_tree = new Node();
     TravelEnumerationTree(enu_tree, new ArrayList());
     TravelMFCITree(mfci_tree, new ArrayList());
     }

     private void TravelEnumerationTree(Node cur_node, ArrayList prefix) {
     Enumeration e = (Enumeration) cur_node.child.keySet();
     Boolean leaf = Boolean.TRUE;
     while (e.hasMoreElements()) {//
     Object cur_name = e.nextElement();
     ArrayList temp_prefix = new ArrayList();
     for (int i = 0; i < prefix.size(); i++) {
     temp_prefix.add(prefix.get(i));
     }
     temp_prefix.add(cur_name);
     Node child_node = (Node) cur_node.child.get(cur_name);
     if (prefix.size() < 2) {//cur_node is related to root or 1-itemset
     TravelEnumerationTree(child_node, temp_prefix);
     } else {
     if (child_node.correlation >= correlation_threshold) {
     TravelEnumerationTree(child_node, temp_prefix);
     leaf = Boolean.FALSE;
     }
     }
     }
     if (leaf && cur_node.correlation >= correlation_threshold) {
     //the current node(prefix) is a candidate, try to build a MFCI tree.
     BuildMFCITree(prefix, Boolean.FALSE);
     }
     }

     private void BuildMFCITree(ArrayList itemset, Boolean isSubset) {
     Node cur_node = mfci_tree;
     Boolean new_node = Boolean.FALSE;
     for (int i = 0; i < itemset.size(); i++) {
     new_node = Boolean.FALSE;
     if (!cur_node.child.containsKey(itemset.get(i))) {
     cur_node.child.put(itemset.get(i), new Node());
     new_node = Boolean.TRUE;
     }
     cur_node = (Node) cur_node.child.get(itemset.get(i));
     cur_node.mark = Boolean.FALSE;
     }
     if (new_node && !isSubset) {
     cur_node.mark = Boolean.TRUE;
     }
     for (int i = 0; i < itemset.size(); i++) {
     ArrayList subset = new ArrayList();
     for (int j = 0; j < itemset.size(); j++) {
     if (i != j) {
     subset.add(itemset.get(j));
     }
     }
     BuildMFCITree(subset, Boolean.TRUE);
     }
     }

     private void TravelMFCITree(Node cur_node, ArrayList prefix) {
     Enumeration e = (Enumeration) cur_node.child.keySet();
     while (e.hasMoreElements()) {
     Object cur_name = e.nextElement();
     ArrayList temp_prefix = new ArrayList();
     for (int i = 0; i < prefix.size(); i++) {
     temp_prefix.add(prefix.get(i));
     }
     temp_prefix.add(cur_name);
     Node child_node = (Node) cur_node.child.get(cur_name);
     if (child_node.mark) {
     mfcis.add(temp_prefix);
     mfcis.add(GetEnumerationTreeCorrelationUpperbound(temp_prefix));
     }
     TravelMFCITree(child_node, temp_prefix);
     }
     }

     private double GetEnumerationTreeCorrelationUpperbound(ArrayList itemset) {
     Node cur_node = enu_tree;
     for (int i = 0; i < itemset.size(); i++) {
     if (!cur_node.child.containsKey(itemset.get(i))) {
     return Double.POSITIVE_INFINITY;
     }
     cur_node = (Node) cur_node.child.get(itemset.get(i));
     }
     return cur_node.correlation;
     }

     private void BuildEnumerationTree(ArrayList itemset, double correlation_upperbound) {
     Node cur_node = enu_tree;
     for (int i = 0; i < itemset.size(); i++) {
     if (!cur_node.child.containsKey(itemset.get(i))) {
     cur_node.child.put(itemset.get(i), new Node());
     }
     cur_node = (Node) cur_node.child.get(itemset.get(i));
     }
     cur_node.correlation = correlation_upperbound;
     }

     private void BuildCurrentLayerTree(ArrayList itemset) {
     Node cur_node = cur_layer_tree_root;
     for (int i = 0; i < itemset.size(); i++) {
     if (!cur_node.child.containsKey(itemset.get(i))) {
     cur_node.child.put(itemset.get(i), new Node());
     }
     cur_node = (Node) cur_node.child.get(itemset.get(i));
     }
     }

     private void GenerateInitialPairs() {
     //we only try to find top k pairs related to item_id[]
     //the final result is (1) int upper_calculation (2) int num_retrieved_support (3) ArrayList topK pairs 
     //topK pairs-->ArrayList pair_information --> (a) double correlation_value (b) ArrayList pair
     String[] item_id = new String[transaction.size()];
     for (int i = 0; i < item_id.length; i++) {
     item_id[i] = (String) transaction.get(i);
     }
     ArrayList result = pcs.getTopKPair(item_id, topK, pair_search_type, correlation_type, cc, correlation_threshold);
     ArrayList topKpairs = (ArrayList) result.get(2);
     for (int i = 0; i < topKpairs.size(); i++) {
     ArrayList pair = (ArrayList) topKpairs.get(i);
     double correlation = (Double) pair.get(0);
     ArrayList pair_id = (ArrayList) pair.get(1);
     String first_id = (String) pair_id.get(0);
     String second_id = (String) pair_id.get(1);
     ArrayList temp = new ArrayList();
     if (Integer.parseInt(first_id) > Integer.parseInt(second_id)) {
     temp.add(second_id);
     temp.add(first_id);
     } else {
     temp.add(first_id);
     temp.add(second_id);
     }
     correlation_threshold = correlation;
     BuildCurrentLayerTree(temp);
     }
     }

     private void GetUnmergedItemset() {
     TravelCurrentLayerTree(cur_layer_tree_root, new ArrayList());
     TravelPreviousLayerTree(pre_layer_tree_root, new ArrayList());
     //rd.showItemsetsInformation(mfcis);
     }

     private void TravelCurrentLayerTree(Node cur_node, ArrayList prefix) {
     Enumeration e = (Enumeration) cur_node.child.keySet();
     if (e.hasMoreElements()) { // go deeper
     while (e.hasMoreElements()) {
     Object cur_name = e.nextElement();
     ArrayList temp_prefix = new ArrayList();
     for (int i = 0; i < prefix.size(); i++) {
     temp_prefix.add(prefix.get(i));
     }
     temp_prefix.add(cur_name);
     Node child_node = (Node) cur_node.child.get(cur_name);
     TravelCurrentLayerTree(child_node, temp_prefix);
     }
     } else { // check the layer, try to mark existing itemset in the previous layer tree
     for (int i = 0; i < prefix.size(); i++) {
     ArrayList subset = new ArrayList();
     for (int j = 0; j < prefix.size(); j++) {
     if (i != j) {
     subset.add(prefix.get(j));
     }
     }
     MarkPreviousLayerTree(subset);
     }
     }
     }

     private void MarkPreviousLayerTree(ArrayList itemset) {
     Node cur_node = pre_layer_tree_root;
     for (int i = 0; i < itemset.size(); i++) {
     cur_node = (Node) cur_node.child.get(itemset.get(i));
     }
     cur_node.mark = Boolean.FALSE;
     }

     private void TravelPreviousLayerTree(Node cur_node, ArrayList prefix) {
     Enumeration e = (Enumeration) cur_node.child.keySet();
     if (e.hasMoreElements()) { // go deeper
     while (e.hasMoreElements()) {
     Object cur_name = e.nextElement();
     ArrayList temp_prefix = new ArrayList();
     for (int i = 0; i < prefix.size(); i++) {
     temp_prefix.add(prefix.get(i));
     }
     temp_prefix.add(cur_name);
     Node child_node = (Node) cur_node.child.get(cur_name);
     TravelPreviousLayerTree(child_node, temp_prefix);
     }
     } else {
     if (cur_node.mark) {
     mfcis.add(prefix);
     mfcis.add(GetEnumerationTreeCorrelationUpperbound(prefix));
     }
     }
     }

     private void GenerateNextLayerTree() {
     pre_layer_tree_root = cur_layer_tree_root;
     cur_layer_tree_root = new Node();
     //generate a new tree the unmarked itemset in the previous layer tree
     GenerateCandidateItemset(pre_layer_tree_root, new ArrayList());
     }

     private void GenerateCandidateItemset(Node cur_node, ArrayList prefix) {
     Enumeration e1 = (Enumeration) cur_node.child.keySet();
     ArrayList temp_item = new ArrayList();
     while (e1.hasMoreElements()) {
     Object cur_name = e1.nextElement();
     ArrayList temp_prefix = new ArrayList();
     for (int i = 0; i < prefix.size(); i++) {
     temp_prefix.add(prefix.get(i));
     }
     temp_prefix.add(cur_name);
     Node child_node = (Node) cur_node.child.get(cur_name);
     Enumeration e2 = (Enumeration) child_node.child.keySet();
     //if have child, go deeper; else merge
     if (e2.hasMoreElements()) {
     GenerateCandidateItemset(child_node, temp_prefix);
     } else {
     temp_item.add(cur_name);
     }
     }
     ArrayList sorted_item = new ArrayList();
     for (int i = 0; i < transaction.size(); i++) {

     sorted_item.add(transaction.get(i));
     }
     for (int i = 0; i < sorted_item.size() - 1; i++) {
     for (int j = i + 1; j < sorted_item.size(); j++) {
     ArrayList candidate = new ArrayList();
     for (int k = 0; k < prefix.size(); k++) {
     candidate.add(prefix.get(k));
     }
     candidate.add(sorted_item.get(i));
     candidate.add(sorted_item.get(j));
     ProcessCandidate(candidate);
     }
     }
     }

     private void ProcessCandidate(ArrayList itemset) {
     double enu_upperbound = GetEnumerationTreeCorrelationUpperbound(itemset);
     if (enu_upperbound == Double.POSITIVE_INFINITY) {//if not in the enumeration tree
     Boolean satisfied = Boolean.TRUE;
     double temp_min_value = Double.POSITIVE_INFINITY;
     for (int i = 0; i < itemset.size(); i++) {
     ArrayList subset = new ArrayList();
     for (int j = 0; j < itemset.size(); j++) {
     if (i != j) {
     subset.add(itemset.get(j));
     }
     }
     temp_min_value = Math.min(temp_min_value, GetEnumerationTreeCorrelationUpperbound(subset));
     //////////////check subset
     if (!CheckExistence(subset, pre_layer_tree_root)) {
     satisfied = Boolean.FALSE;
     }
     }
     if (satisfied) {
     temp_min_value = Math.min(temp_min_value, getCorrelation(itemset, cc, correlation_type));
     BuildEnumerationTree(itemset, temp_min_value);
     if (temp_min_value >= correlation_threshold) {
     BuildCurrentLayerTree(itemset);
     }
     }
     } else {//if it is in the enumeration tree
     if (enu_upperbound >= correlation_threshold) {
     BuildCurrentLayerTree(itemset);
     }
     }
     }

     private Boolean CheckExistence(ArrayList itemset, Node tree_root) {
     Boolean output = Boolean.TRUE;
     Node cur_node = tree_root;
     for (int i = 0; i < itemset.size(); i++) {
     if (!cur_node.child.containsKey(itemset.get(i))) {
     output = Boolean.FALSE;
     break;
     } else {
     cur_node = (Node) cur_node.child.get(itemset.get(i));
     }
     }
     return output;
     }
     }
     //*/
}
