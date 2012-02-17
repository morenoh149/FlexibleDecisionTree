import java.io.*;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * our solution requires the data to be provided in a csv file,
 * this is common practice in Machine Learning so we used the opencsv library available
 * for java
 * NOTE: no attempt is made to parse the values, we treat all attributes as strings,
 * this allows us to handle any type of dataset
 * @author harry moreno
 * @author ben leone
 */
public class Solution {
	static StringBuilder sb;
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
		    System.out.println("Usage: missing filename");
		    return;
		}

		CSVReader parser = new CSVReader(new FileReader(args[0]));
		List<String[]> dataset = parser.readAll();
		String[] header = dataset.get(0);
		int numOfAttributes = header.length;
		dataset = dataset.subList(1, dataset.size());
		
		DTLearning algorithm = new DTLearning(dataset, numOfAttributes);
		System.out.println(sb.toString());
	}
}
