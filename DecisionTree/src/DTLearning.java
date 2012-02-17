import java.util.*;

/**
 * decision tree learning takes place here,
 * @author harry moreno
 *
 */
public class DTLearning extends Node{
	int numOfAttributes;
	StringBuilder sb;

	DTLearning(List<String[]> dataset, int attributes){
		this.parent = null;
		this.numOfAttributes = attributes;
		this.children = new ArrayList<Node>();
		this.depth = 1;
		sb = new StringBuilder("begin tree \n");
		classify(dataset,attributes);
	}

	DTLearning(List<String[]> dataset, int attributes, DTLearning parent){
		this.parent = parent;
		this.numOfAttributes = attributes;
		this.children = new ArrayList<Node>();
		this.depth = parent.depth + 1;
		//System.out.println("created child!");
	}

	/**
	 * runs the decision-tree-learning algorithm
	 * @param dataset
	 * @param attributes
	 */
	void classify(List<String[]> dataset, int attributes){
		if(dataset.isEmpty()){
			this.pluralityValue = parent.pluralityValue;
			for(int i=0; i<depth-1; i++){
				sb.append(" ");
			}
			sb.append(this.pluralityValue+"\n");
		}
		else if(sameClass(dataset)){
			String[] first = dataset.get(0);
			this.pluralityValue = first[first.length-1];
			for(int i=0; i<depth-1; i++){
				sb.append(" ");
			}
			sb.append(this.pluralityValue+"\n");
		}
		else{
			int indexOfImportantAttribute = importance(dataset);
			if(indexOfImportantAttribute == -1){
				this.pluralityValue = finalResolution(dataset);
				for(int i=0; i<depth-1; i++){
					sb.append(" ");
				}
				sb.append(this.pluralityValue+"\n");
			}
			else{
			System.out.println("important value: "+indexOfImportantAttribute);
			HashMap<String, Integer> attributeMap = countAttribute(indexOfImportantAttribute, dataset);
			for(String atr : attributeMap.keySet()){
				List<String[]> split = makeSplit(atr, indexOfImportantAttribute, dataset);
				if(!split.isEmpty()){
					DTLearning newChild = new DTLearning(dataset, numOfAttributes-1, this);
					this.children.add(newChild);
				}
				classify(split, numOfAttributes-1);
			}
			}
		}
	}
	/**
	 * returns a dataset without the splitting attribute
	 * returns and empty list if no row with that attribute found
	 */
	List<String[]> makeSplit(String value, int index, List<String[]> dataset){
		List<String[]> resultList = new ArrayList<String[]>();
		for(String[] row : dataset){
			if(row[index].equals(value)){
				String[] modifiedRow = removeAtr(row, index);
				resultList.add(modifiedRow);
			}
		}
		return resultList;
	}
	/**
	 * removes the given attribute from a String array
	 */
	String[] removeAtr(String[] row, int index){
		String[] newrow = new String[row.length-1];
		for(int i=0; i<row.length; i++){
			if(i<index){
				newrow[i] = row[i];
			}
			if(i>index){
				newrow[i-1] = row[i];
			}
		}
		return newrow;
	}
	/**
	 * returns Map of unique values for the given index in the dataset
	 * Map of size 2 for boolean
	 */
	HashMap<String, Integer> countAttribute(int index, List<String[]>dataset){
		HashMap<String, Integer> attributeMap = new HashMap<String, Integer>();
		for(String[] row : dataset){
			if(!attributeMap.containsKey(row[index])){
				attributeMap.put(row[index], 0);
			}
			attributeMap.put(row[index], attributeMap.get(row[index])+1);
		}
		return attributeMap;
	}
	
	private String finalResolution(List<String[]> dataset){
		int count;
		boolean found;
		List<String> names = new ArrayList<String>(0);
		List<Integer> counts = new ArrayList<Integer>(0);
		for(String[] select : dataset){
			count = 0;
			found = false;
			for(String sel : names){
				if(sel.equals(select[0])){
					counts.set(count, counts.get(count)+1);
					found=true;
				}
				count++;
			}
			if(found==false){
				names.add(select[0]);
				counts.add(1);
			}
		}
		int max = 0;
		count=0;
		int loc = 0;
		boolean multi = false;
		for(int temp : counts){
			if(temp==max){
				multi = true;
			}
			if(temp>max){
				max = temp;
				multi = false;
				loc = count;
			}
			count++;
		}
		return names.get(loc);
	}

	/**
	 * given a dataset and an attribute index, calculate the entropy value of splitting
	 * on that attribute
	 */

	private int importance(List<String[]> dataset){
		System.out.println("determining importance");
		int ret = 0;
		int attributes = dataset.get(0).length-1;
		System.out.println("Number of attributes = "+attributes);
		List<Double> Imps = new ArrayList<Double>(0);
		int i = 0;
		if(attributes!=0){
			while(i<attributes){
				Imps.add(informationGain(i, dataset));
				i++;
			}
			double max = Imps.get(0);
			int count = 0;
			for(double test : Imps){
				if(test>max){
					max = test;
					ret = count;
				}
				count++;
			}
		}else{
			ret = -1;
		}
		return ret;
	}

	private double informationGain(int attribute, List<String[]> dataset){
		//System.out.println("gaining info");
		double result = 0;
		List<Integer> results = new ArrayList<Integer>(0);
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
				sorted.get(0).add(select);           // add our string to the stored set by similar attribute
			}else{                       //the attribute holder is not empty so see if our attribute is already logged
				contanined=false;
				count = 0;
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
			sum = sum + ((hold/(double)total)*entropy(sel));
		}
		result = result - sum;
		System.out.println("result is: "+result);
		return result;
	}


	public double entropy(List<String[]> dataset){
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
				count = 0;
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
	//	}

	/**
	 * returns true if the given list is all of the same class,
	 * assumes target classification is the final column
	 */
	boolean sameClass(List<String[]> dataset){
		String[] first = dataset.get(0);
		int lastindex = first.length;
		String target = first[lastindex-1];
		for(String[] row : dataset){
			if(row[lastindex-1]!= target)
				return false;
		}
		return true;
	}
	/**
	 * returns string representation of the current node
	 * best run on the root
	 */
	public String toString(){
		return sb.toString();
	}
}
