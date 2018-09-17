package kempot.domparser;

import java.util.ArrayList;
import java.util.List;

public class ImpossibleCombinations {
	
	public String[][] pairs = {
//	        {"a1to5", "Elementary"},
//	        {"a1to5", "HighSchool"},
//	        {"a1to5", "University"},
//	        {"Elementary", "a1to5"},
//	        {"HighSchool", "a1to5"},
//	        {"University", "a1to5"},
//	        
	        {"Male", "Pregnant"},
	        {"Pregnant", "Male"},
//	        {"Nurse", "Children"},
//	        {"Nurse", "Infant"},
//	        
//	        {"a1to5", "Smoker"},
//	        {"a1to5", "Drinker"},
//	        {"Smoker", "a1to5"},
//	        {"Drinker", "a1to5"},
//	        
//	        {"a6to14", "Smoker"},
//	        {"a6to14", "Drinker"},
//	        {"Smoker", "a6to14"},
//	        {"Drinker", "a6to14"},

	    };
	private int addingLocationIndex = 0;
	
	NetConverter count;
	
	public ImpossibleCombinations(){
		
	}

    /**
     * generate pairs
     */
    public boolean checkValidPermutation(ArrayList<String> permutation) {
        // for each impossible pair in all pairs
        for(String[] impossiblePair : pairs) {
            // set currently matched items between impossible pair 
            // and row (or permutation we are checking) to 0, nothing found
            int matchedItems = 0;
            // iterate over each item, and if it is present in the row
            // or permutation, increment the matched item count
            for(String item : impossiblePair) {
                if (permutation.contains(item)) {
                    matchedItems += 1;
                }
            }

            if (matchedItems == impossiblePair.length) {
                return false;
            }
        }
        // no impossible conditions were found, therefore, return true
        return true;
    }
    /** END **/
	
}
