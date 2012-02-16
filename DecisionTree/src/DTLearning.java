import java.util.List;

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
			
		}
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
