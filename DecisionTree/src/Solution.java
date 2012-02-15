import java.io.*;
import au.com.bytecode.opencsv.CSVReader;

public class Solution {
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
		    System.out.println("Usage: missing filename");
		    return;
		}

		CSVReader parser = new CSVReader(new FileReader(args[0]));
		String[] values = parser.readNext();
		int lineNumber = 1;
		while (values != null) {
		    printValues(lineNumber, values);
		    values = parser.readNext();
		    lineNumber++;
		}
	    }

	    private static void printValues(int lineNumber, String[] as) {
		System.out.println("Line " + lineNumber + " has " + as.length + " values:");
		for (String s: as) {
		    System.out.println("\t|" + s + "|");
		}
		System.out.println();
	    }
}
