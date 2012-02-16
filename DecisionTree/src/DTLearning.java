import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * decision tree learning takes place here,
 * @author harry
 *
 */
public class DTLearning extends Node{
	String finaltree;

	DTLearning(List<String[]> dataset, int attributes){
		this.parent = null;
		classify(dataset,attributes);
	}

	/**
	 * runs the decision-tree-learning algorithm
	 * @param dataset
	 * @param attributes
	 */
	void classify(List<String[]> dataset, int attributes){
		if(dataset.isEmpty()){
			this.pluralityValue = parent.pluralityValue;
		}
		else if(sameClass(dataset)){
			String[] first = dataset.get(0);
			this.pluralityValue = first[first.length];
		}
		else{
			int indexOfImportantAttribute = importance(dataset);
		}
	}
	/**
	 * given a dataset and an attribute index, calculate the entropy value of splitting
	 * on that attribute
	 */
	int entropy(int attribute, List<String[]> dataset){
		Map<String, Integer> map = new HashMap<String, Integer>();
		String[] firstrow = dataset.get(0);
		// count the occurrences of each value
		for (String sequence : dataset) {
			if (!map.containsKey(sequence)) {
				map.put(sequence, 0);
			}
			map.put(sequence, map.get(sequence) + 1);
		}

		// calculate the entropy
		Double result = 0.0;
		for (String sequence : map.keySet()) {
			Double frequency = (double) map.get(sequence) / values.size();
			result -= frequency * (Math.log(frequency) / Math.log(2));
		}

		return result;
	}
	
	/**
	 * given a dataset, return the index of the most important attribute
	 */
	int importance(List<String[]> dataset){
		String[] firstrow = dataset.get(0);
		//index, entropy value
		HashMap<Integer, Integer> importanceValues = new HashMap<Integer, Integer>();
		for(int i=0; i<firstrow.length; i++){
			importanceValues.put(i, entropy(i,dataset));
		}
		int max = 0;
		for(int i=0; i<importanceValues.size(); i++){
			if(importanceValues.get(i)>max)
				max = i;
		}
		return max;
	}

	/**
	 * returns true if the given list is all of the same class,
	 * assumes target classification is the final column
	 */
	boolean sameClass(List<String[]> dataset){
		String[] first = dataset.get(0);
		int lastindex = first.length;
		String target = first[lastindex];
		for(String[] row : dataset){
			if(row[lastindex]!= target)
				return false;
		}
		return true;
	}
}
