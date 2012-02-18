import java.util.*;

/**
 * decision tree learning takes place here,
 * @author harry moreno
 *
 */
public class DTLearning{
	List<String[]> Dataset;
	DTLearning parent;
	List<DTLearning> children;
	String pluralityValue;
	int depth;
	StringBuilder sb;
	int split;
	String attr;
	List<Integer> checked;
	boolean terminal;

	DTLearning(List<String[]> dataset){
		this.parent = null;
		this.Dataset=dataset;
		this.children = new ArrayList<DTLearning>();
		this.depth = 1;
		this.sb = new StringBuilder();
		attr = null;
		this.checked = new ArrayList<Integer>(0);
		buildChildren();
	}

	DTLearning(List<String[]> dataset, DTLearning parent, String attribute, List<Integer> check){
		this.parent = parent;
		this.Dataset=dataset;
		this.attr = attribute;
		//System.out.println("datset is: "+Dataset.get(0).length);
		this.children = new ArrayList<DTLearning>();
		this.depth = parent.depth + 1;
		this.sb = parent.sb;
		this.checked = check;
//		System.out.println(checked.size());
		//System.out.println("created child!");
	}

	public boolean match(String[] input){
//		System.out.println("Let's look at a node");
		if(this.terminal==true){
//			System.out.println("ITS terminal! and its pluratlity is :" + this.pluralityValue + " And our final is : " + input[input.length-1]);
			return this.pluralityValue.equals(input[input.length-1]);
		}
		if(children.isEmpty()){
			return false;
		}
		String attrib = input[this.split];
		for(DTLearning chld : this.children){
			if(chld.attr.equals(attrib)){
				return chld.match(input);
			}
		}
		//System.out.println("here");
		return false;
	}

	public void buildChildren(){
		if(sameClass(this.Dataset)){
			this.terminal=true;
			this.pluralityValue = this.Dataset.get(0)[this.Dataset.get(0).length-1];
//			System.out.println("same data term");
//			for(String[] test : Dataset){
//				System.out.println(test[test.length-1]);
//			}
//			System.out.println("+ checked +");
//			System.out.println("PLURALITY VALUE : " + this.pluralityValue);
			return;
		}

		int indexOfImportantAttribute = importance(this.Dataset, this.checked);
		if(indexOfImportantAttribute == -1){
			System.out.println("important stop");
			this.pluralityValue = finalResolution(this.Dataset);
			this.terminal = true;
			return;

		}
		else{
			this.split=indexOfImportantAttribute;
			this.terminal=false;
			List<String> attributes = getAttributes(this.split, this.Dataset);
			for(String atr : attributes){
				List<String[]> split = makeSplit(atr, indexOfImportantAttribute, this.Dataset);
				if(!split.isEmpty()){
					//System.out.println(dataset.get(0).length);
					List<Integer> list = new ArrayList<Integer>(this.checked);
					
					list.add(indexOfImportantAttribute);
//					System.out.println("splitting" + atr + " : " + this.checked.size());
					DTLearning newChild = new DTLearning(new ArrayList<String[]>(split), this, atr, list);
					this.children.add(newChild);
				}
			}
//			System.out.println(this.children.size());
			
			for(DTLearning chld : this.children){
				chld.buildChildren();
			}

		}
	}


	private List<String> getAttributes(int index, List<String[]> dataset){
		List<String> output = new ArrayList<String>(0);
		for(String[] sel : dataset){
			if(!output.contains(sel[index])){
				output.add(sel[index]);
			}
		}
		return output;
	}

	/**
	 * returns a dataset without the splitting attribute
	 * returns and empty list if no row with that attribute found
	 */
	List<String[]> makeSplit(String value, int index, List<String[]> dataset){
		List<String[]> resultList = new ArrayList<String[]>();
		for(String[] row : dataset){
			if(row[index].equals(value)){
				//String[] modifiedRow = removeAtr(row, index);
				resultList.add(row);
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

	private int importance(List<String[]> dataset, List<Integer> checked){
		//		System.out.println("determining importance");
		int ret = 0;
		int attributes = dataset.get(0).length-1;
		//System.out.println("Number of attributes = "+attributes);
		List<Double> Imps = new ArrayList<Double>(0);
		List<Integer> Indexes = new ArrayList<Integer>(0);
		int i = 0;
		if(attributes!=checked.size()){
			while(i<attributes){
				if(!checked.contains(i)){
					Imps.add(informationGain(i, dataset));
					Indexes.add(i);
				}
				i++;
			}
			double max = Imps.get(0);
			int count = 0;
			for(double test : Imps){
				if(test>max){
					max = test;
					ret = Indexes.get(count);
				}
				count++;
			}
		}else{
			ret = -1;
		}
		//System.out.println("ret IS: " + ret);

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
			temp = select[attribute];                  //the attribute holder is not empty so see if our attribute is already logged
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
		result = 1;
		double sum = 0;
		double hold;
		for(List<String[]> sel : sorted){
			hold = sel.size();
			//System.out.println("entropy sel: "+entropy(sel));
			sum = sum + ((hold/(double)total)*entropy(sel));
		}
		result = result - sum;
		//System.out.println("result is: "+result);
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
		double I = 0.0;
		double frequency = 0.0;
		for(double k : results){
			frequency = k/(double)total;
			I = I - (frequency)*(Math.log(frequency)/Math.log(2));
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
//		System.out.println(target);
		for(String[] row : dataset){
			if(!row[lastindex-1].equals(target))
				return false;
		}
		return true;
	}
}
