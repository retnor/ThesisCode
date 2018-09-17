package kempot.domparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import norsys.netica.*;

public class GetBeliefs {
	public static List<String> inputtedBeliefs = new ArrayList<String>();
	
	public static void main(String args[]) throws NeticaException{
		double prob = 0.0;
		
//		This is for Anthrax
		
		
//		inputtedBeliefs.add("Equatorial");	//Hemisphere	
//		inputtedBeliefs.add("Forest");		//Natural
//		inputtedBeliefs.add("Sunny");		//Weather
//		inputtedBeliefs.add("Jobless");	//Occupation
//		inputtedBeliefs.add("Influenza");	//PreExistingIllness
//		inputtedBeliefs.add("Farms");		//ManMade
//		inputtedBeliefs.add("Rain");		//Season
//		inputtedBeliefs.add("Rash");		//Symptom
		
		inputtedBeliefs.add("Pregnant");	//DevelopmentStage
		inputtedBeliefs.add("Vegetarian");		//Eating
		inputtedBeliefs.add("Anthrax");		//Eating
		inputtedBeliefs.add("US");	//Country
		inputtedBeliefs.add("Female");		//Gender
		inputtedBeliefs.add("Smoker");		//Smoking
		
			
//		This is for Crohn's Disease
		
//		inputtedBeliefs.add("NotSpecified");	//Weather
//		inputtedBeliefs.add("Parents");	//Relationship
//		inputtedBeliefs.add("One");		//Genetic
//		inputtedBeliefs.add("UK");		//Country
//		inputtedBeliefs.add("NotSpecified");		//Season
//		inputtedBeliefs.add("Unknown");	//Smoking
		
//		This is for Tuberculosis	
		
//		inputtedBeliefs.add("HIV");	//PreExistingIllness
//		inputtedBeliefs.add("Elementary");	//Education
//		inputtedBeliefs.add("BCG");		//Vaccination
//		inputtedBeliefs.add("Private");		//Living Habit
//		inputtedBeliefs.add("Dry");	//Weather
//		inputtedBeliefs.add("Drinker");	//Alcohol
//		inputtedBeliefs.add("Indonesia");		//Country
//		inputtedBeliefs.add("Female");	//Gender
//		inputtedBeliefs.add("a15to24");	//Age
//		inputtedBeliefs.add("NonSmoker");	//Smoking
		
//		This is for Dengue
		
		
		
		prob = calculate();
		
		System.out.println(prob*100);
	}

	public static double calculate() throws NeticaException{
		Environ env = new Environ (null);
		
		Net net = new Net (new Streamer ("CaseAnthrax.dne"));
		
		Node disease	= net.getNode("Anthrax");
		
		//Read in the net
		NodeList nodes = net.getNodes();
		int numNodes = nodes.size();

		for(int i=0; i<(numNodes-1); i++){
			Node temporary = nodes.getNode(i);			
			temporary.finding().clear();
			temporary.finding().enterState(inputtedBeliefs.get(i));
		}
		
		net.compile();
		
		return disease.getBelief("AtRisk");
	}
}
