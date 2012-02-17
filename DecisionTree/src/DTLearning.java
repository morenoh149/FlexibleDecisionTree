import java.util.ArrayList;
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
	
	private double entropy(int attribute, List<String[]> dataset){
		double result = 0;
		List<Integer> resul\ts = new ArrayList<Integer>(0);
		List<List<String[]>> sorted = new ArrayList<List<String[]>>(0);
		List<String> category = new ArrayList<String>(0);
		int total = dataset.size(); 
		String temp = null; //holds test stirngs
		int count = 0; //holds indexes
		boolean contanined = false;
		for(String[] select : dataset){ //go through all string arrays
			temp = select[attribute];   // get the attribute we are working on
			if(category.size()==0){     // check if the attribute holder is empty to add stuff
				category.add(select[attribute]);      //adds our atrbuite to the log
				sorted.add(new ArrayList<String[]>(0)); //initialize storage for string arrays
				sorted.get(1).add(select);           // add our string to the stored set by similar attribute
			}else{                       //the attribute holder is not empty so see if our attribute is already logged
				contanined=false;
				count = 1;
				for(String check : category){
					if(check.equals(temp)){
						sorted.get(count).add(select);
						contanined = true;
					}
					count++;
				}
				if(contanined==false){
					category.add(select[attribute]);
					sorted.add(new ArrayList<String[]>(0));
					sorted.get(count).add(select);
				}
			}
		}
		result = 1;
		double sum = 0;
		double hold;
		for(List<String[]> sel : sorted){
			hold = sel.size();
			sum = sum + ((hold/(double)total)*importance(sel));
		}
		result = result - sum;
		return result;
	}
	
	
	public double importance(List<String[]> dataset){
		int total = dataset.size();
		int end;
		int count;
		boolean found;
		List<Integer> results = new ArrayList<Integer>(0);
		List<String> category = new ArrayList<String>(0);
		for(String[] select : dataset){
			end = select.length-1;
			if(category.size()==0){
				category.add(select[end]);
				results.add(1);
			}else{
				found=false;
				count = 1;
				for(String temp : category){
					if(temp.equals(select[end])){
						results.set(count, results.get(count)+1);
						found=true;
					}
				}
				if(found==false){
					results.add(1);
					category.add(select[end]);
				}
			}
		}
		double I = 0;
		for(double k : results){
			I = I - (k/(double)total)*(Math.log(k/(double)total)/Math.log(2));
		}
		return I;
	}
	
//	int entropy(int attribute, List<String[]> dataset){
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		String[] firstrow = dataset.get(0);
//		// count the occurrences of each value
//		for (String sequence : dataset) {
//			if (!map.containsKey(sequence)) {
//				map.put(sequence, 0);
//			}
//			map.put(sequence, map.get(sequence) + 1);
//		}
//
//		// calculate the entropy
//		Double result = 0.0;
//		for (String sequence : map.keySet()) {
//			Double frequency = (double) map.get(sequence) / values.size();
//			result -= frequency * (Math.log(frequency) / Math.log(2));
//		}
//
//		return result;
//	}
	
	/**
	 * given a dataset, return the index of the most important attribute
	 */
//	int importance(List<String[]> dataset){
//		String[] firstrow = dataset.get(0);
//		//index, entropy value
//		HashMap<Integer, Integer> importanceValues = new HashMap<Integer, Integer>();
//		for(int i=0; i<firstrow.length; i++){
////			importanceValues.put(i, entropy(i,dataset));
//		}
//		int max = 0;
//		for(int i=0; i<importanceValues.size(); i++){
//			if(importanceValues.get(i)>max)
//				max = i;
//		}
//		return max;
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
