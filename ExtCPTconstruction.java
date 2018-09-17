package kempot.domparser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.common.collect.Multimap;

public class ExtCPTconstruction {
	
	public static float jprob;
	public static String alteration;

	public ExtCPTconstruction(String input) throws ParserConfigurationException, SAXException, IOException{
		//PrintWriter writer = new PrintWriter("log1.txt");
		//writer.println("==========");
		ArrayList<String> foundList = new ArrayList<String>();
		HashMap<String, Float> ORList = new HashMap<String, Float>();
		HashMap<String, Boolean> condeleted = new HashMap<String, Boolean>();
		//boolean condel = false;
		//ReadXMLFile2 rules = new ReadXMLFile2();
		
		
		Multimap<String, ExtRules> ruleObjmap = ExtReadOntology.ruleListObj;
		
		
		//input: Equatorial,Female,Forest,Cold,Farmers,Influenza,Farms,Adults,Indonesia,Autumn,Nausea,EatingMeats
		
		//initial probability of a person contracting a disease
		jprob = 1;		
		String[] itemList = input.split(",");
		
		//setRiskflag will be true if the rule alteration is 'setRisk'
		boolean setPathogenflag = false;
		float tempSetRisk = 0;
		int i = 0;
		
		boolean confounding = false;
		
		for(String item : itemList){
			
			String nodename = ExtNetConverter.queue.get(i++);
			
			String tempCheck = nodename+item; 
			
			//checking whether each attribute within a state combination appears in rules or not 
			
			boolean result = ruleObjmap.containsKey(tempCheck);
			
			//writer.println(tempCheck+": "+result);
			
			
			String rulenames = "";
			String rulestatus = "";
			float ruleprob = 0.0F;
			float rulepriority = 0.0F;
			int con = 0;
			
			//get the conditional probs
			if(result){
				System.out.print(tempCheck+", ");
				Collection<ExtRules> colrul = ruleObjmap.get(tempCheck);
				Iterator itr = colrul.iterator();				
				
				while(itr.hasNext()) {
					ExtRules rul = (ExtRules) itr.next();
					rulenames = rul.getRuleName();
					rulestatus = rul.getRuleStatus();
					ruleprob = rul.getProb();
					alteration = rul.getRuleAlter();
					rulepriority = rul.getPrio();
					
					if(alteration.equals("alterRisk") && rulepriority==1){
					
						if(!ORList.containsKey(rulenames)){
							ORList.put(rulenames, ruleprob);
							//condeleted.put(rulenames, false);
							//System.out.println(rulenames+" has been added to ORList");
						}
						else{
							confounding = true;
							condeleted.put(rulenames, false);
							//System.out.println(rulenames+" has been added to condeleted");
						}
						
					//System.out.println("rulenames: "+rulenames+", rulestatus: "+rulestatus+", confounding: "+confounding);
					}
					
					else if(alteration.equals("setRisk")){
						setPathogenflag = true;
						tempSetRisk = rul.getProb();
					}
			    }	
			}
		}
		
		//removing riskfactors that appears when its complete-riskfactor rule appears
		if(confounding){
			//confounding = true;
			//ORList.put(rulenames, ruleprob);
			//System.out.println("inside con");
			
			//ExtReadOntology.rulePartListObj.get(0);
			Set<String> keyPartListcon = ExtReadOntology.rulePartListObj.keySet();
			Iterator<String> keyitercon = keyPartListcon.iterator();
			
			while(keyitercon.hasNext()){
				String rawkey[] = keyitercon.next().split("#");
				String keyPartcon = rawkey[0];
				//System.out.println("is there anyone here "+rawkey[1]);
				
				//if ORList contains the part of risk factor
				if(ORList.containsKey(keyPartcon)){
					ORList.remove(keyPartcon);
					//condeleted.put(rawkey[1], true);
					//System.out.println(keyPartcon+" has been removed from ORList 1st step");
				}
			}
		}
			
		//removing the confounding rule when the complete-riskfactor doesnt appear
		Set<String> keyPartList = ExtReadOntology.rulePartListObj.keySet();
		Iterator<String> keyiter = keyPartList.iterator();
		
		while(keyiter.hasNext()){
			String keyPart = keyiter.next();
			//String keyPart = rawkey[0];
			//System.out.println("keyPart keluar ga: "+keyPart);
			
			String riskfactor = ExtReadOntology.rulePartListObj.get(keyPart);	
			Collection <ExtRules> find = ExtReadOntology.ruleListObj.get(riskfactor);
			Iterator itr = find.iterator();
						
			while(itr.hasNext()) {
				ExtRules rul = (ExtRules) itr.next();
				String rname = rul.getRuleName();
				String status = rul.getRuleStatus();
								
				//System.out.println("rname: "+rname+", status: "+status);
				if(ORList.containsKey(rname) && status.equals("Confounding")){
					if(!condeleted.containsKey(rname)){
						ORList.remove(rname);
						//System.out.println(rname+" has been removed from ORList 2nd step");
					}
				}
			}
		}

		System.out.println(": ");		
		for (String name: ORList.keySet()){
            String key = name.toString();
            float value = ORList.get(name);  
            //writer.println("I am in ORList: "+key+", "+value);
            System.out.print(key+": "+value+", ");
            jprob = jprob * value;
		}
		
//		for (String name: condeleted.keySet()){
//            String key = name.toString();
//            String value = condeleted.get(name).toString();  
//            System.out.println("I am in condeleted: "+key+", "+value);  
//		}
 		
		//assigning the end result of jprob with prob value given in setRisk, only if there is Pathogen rule type found
		if(setPathogenflag){
			if(tempSetRisk==0)
				jprob = tempSetRisk;
			else{
				jprob = jprob*tempSetRisk;
			}
		}
		
		//checking that any probabilities would go between 0 and 1
		//this condition happens when a person owns all disease-increasing-risk attributes 
		//and the sum product of all probabilities in all rules are exceed than 1
		if(jprob/100 >= 1){
			jprob = 100;
			System.out.println("Some CPT values might exceed 1!!");
		}
		
		//this condition happens when there is more disease-reducing-risk attributes 
		//rather than disease-increasing-risk attributes owned by a person
		else if(jprob/100 < 0){
			jprob = 0;
			System.out.println("Some CPT values might below 0!!");
		}
		
		System.out.println("Total jprob: "+jprob);
		//System.out.println("");
	}
}
