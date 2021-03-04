package processing;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class mfciforHeadless {
	public static void main(String[] args) throws IOException{
		FileWriter fw = new FileWriter ("/Users/cmadlock/Documents/mfci.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		
		FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        String output="";
		String item_file = "/Users/cmadlock/Documents/items.txt";
		String trans_file = "/Users/cmadlock/Documents/transactions.txt";
		CorrelationSearch cs = new CorrelationSearch();
		List<String> items =new ArrayList<String>();
		File file = new File(item_file);
        try {
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
        String items_output = "";
        for (int i = 0; i < items.size(); i++) {
            items_output += i + ",";
        }
        String[] items2=items_output.split(",");
		cs.initialData(item_file, trans_file, true);
		
			output=cs.mfci.ShowMFCI(items2, 500, "Likelihood Ratio", 0.5, 0.0, false);
			bw.write(output);
			bw.flush();
			bw.close();
		
		
	}

}
