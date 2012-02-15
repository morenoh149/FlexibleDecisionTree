import java.io.*;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Solution {
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
		DTLearning algorithm = new DTLearning(dataset, numOfAttributes, null);
		System.out.println(algorithm.toString());
	}
}
