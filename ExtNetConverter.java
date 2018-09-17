package kempot.domparser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.common.collect.Multimap;

import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Streamer;
import norsys.neticaEx.aliases.Node;

public class ExtNetConverter {
	
	public static ArrayList<String> queue = new ArrayList<String>();
	//public static ArrayList<String> confoundingList = new ArrayList<String>();
	public static int counterImpPairs = 0;
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		long startTime = System.currentTimeMillis();
		//ReadXMLFile network = new ReadXMLFile();
		ExtReadOntology network = new ExtReadOntology();
		Multimap<String,String> map = network.networkmap;
		List<Integer> statenums = new ArrayList<Integer>();
		
		int jailbreakCounter=0;
		
		try {
			System.out.println ("\nWelcome to Netica-J !\n");
	
	    	Node.setConstructorClass ("norsys.neticaEx.aliases.Node");
	    	new Environ (null);
			
			Net   net          = new Net();
	    	net.setName("CaseAnthrax");
	    	
	    	Node  disease   = new Node ("Anthrax",     "AtRisk, NotAtRisk",    net);
	    	Node tempID = null;
	    	Node  tempnode = null;
	    	
	    	int numOfnodes = 0;
	    	
            ImpossibleCombinations check = new ImpossibleCombinations();
            
        	float atrisk=0;
        	ExtCPTconstruction setcpt;
        	GetMarginalProb setMarginal;
	    	
	    	for (String key : map.keySet()) {
	    		 String statenames = "";
	    		 String statecols = "";
	    		 
	    		 //ignoring creating Infectious Diseases
	    		 if(key.equals("InfectiousDiseases")){
	    			 continue;
	    		 }
	    		 else{
				     List<String> values = (List<String>) map.get(key);
				     int statenum = values.size();
				     statenums.add(statenum);
				     
				     //constructing state names per node
				     for (String value : values){
				    	 statenames = statenames+value+",";
				     }
				     
				     //erasing the last comma of generated state names
				     statecols = statenames.substring(0, (statenames.length()-1));
				     tempnode = new Node (key, statecols, net);
				     
				     //setMarginal = new GetMarginalProb("Crohn.xls", key);
				     
				     //tempnode.setCPTable(setMarginal.MargProb);
	    		 }
	    		 
	    		 //adding queue feature of multimap which make sure that keys are ordered as inserted
	    		 queue.add(key);
	    		 //System.out.println(queue.size());
	    		 numOfnodes++;
	    		 disease.addLink(tempnode);
	    		 
			}

            /** CHANGED BITS
             * Harshvardhan Pandit
             */
            
            // this are the values in the first column
            // construct a list for each value and store them in data
            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            List<String> list = (List<String>) map.get(queue.get(0));
            for (int i=0; i<statenums.get(0); i++) {
                ArrayList<String> item = new ArrayList<String>();
                item.add(list.get(i));
                
                data.add(item);
            }

            // iterate over the rest of columns
            // and add items to each list
            // n is the number of columns, which go from 0 to 12
            // since we handled column0, n goes from 1 to 12
            try{
            	//PrintWriter writer = new PrintWriter("log1.txt");
            
//          ImpossibleCombinations check = new ImpossibleCombinations();
//        	float atrisk=0;
//        	CPTconstruction setcpt;
        	
            for (int n=1; n<=(numOfnodes-1); n++) {
            
                // newdata is a temporary variable to hold data
                // which will be assigned to data AFTER the loop ends
                // This is to keep existing data unchanged until the loop finishes
                ArrayList<ArrayList<String>> newdata = new ArrayList<ArrayList<String>>();
                list = (List<String>) map.get(queue.get(n));
                // try--> for (i=0; i<list.size(); i++) {
                for (int j=0; j<data.size(); j++) {
                	for (int i=0; i<statenums.get(n); i++) {
                    
                        // create a new list as a copy of an item in data
                        ArrayList<String> item = new ArrayList<String>(data.get(j));
                        // add the item from current column to list
                        item.add(list.get(i)); 
                        newdata.add(item);
                        if (n == (numOfnodes-1)) {
                        	
                        	String tempStemp = String.join(",", item);
                        	//String checkingtempStemp = String.join(",", item);
                        	
                            /** Harshvardhan Pandit
                             */

                            // impossibleConditions: List of List of Strings
                            // containing permutations of every impossible conditions
                        	boolean isValid = check.checkValidPermutation(item);
                            if (isValid) {
                                setcpt = new ExtCPTconstruction(tempStemp);
                                atrisk = setcpt.jprob;
                                jailbreakCounter++;
                            } 
                            
                            else 
                                atrisk = 0;
                            
                            //writer.println(tempStemp+": "+atrisk/100);
                            
                            //System.out.println(tempStemp+": "+atrisk/100);
                            //tuberculosis.setCPTable(tempStemp, 0.03, (1-0.03));
                            //System.out.println(atrisk/100);
                            //System.out.println(1-(atrisk/100));
                            disease.setCPTable(tempStemp, atrisk/100, (1-(atrisk/100)));
                       // isValid -> true if valid; false otherwise
                        }
                    }
                }
                // store newdata as data
                data = newdata;
            }
            } catch(IOException e) {
            
            }
            
            //for(int i ){
            
            //}
            
            /** END OF CHANGE **/
			
			System.out.println("Finish writing "+data.size()+" lines of CPT");
			System.out.println("Contain "+jailbreakCounter+" lines of possible combinations and "+(data.size()-jailbreakCounter)+
					" impossible combinations");
			
			Streamer stream = new Streamer ("CaseAnthrax.dne");
	    	net.write(stream);
	    	
		} catch (NeticaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		
		System.out.println("Network and CPT have been created!!");
		
	}

}
