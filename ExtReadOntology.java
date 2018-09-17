package kempot.domparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ExtReadOntology {
	
	public Multimap<String,String> networkmap = ArrayListMultimap.create();
	public static HashSet<String> partsFound = new HashSet<String>();
	public int nodecounter;
	public static Multimap<String, ExtRules> ruleListObj = ArrayListMultimap.create();
	public static HashMap<String, String> rulePartListObj = new HashMap<String, String>();
	public float Probs;
	public Rules rules;
	
	private List<String> values = null;
	private File fXmlFile = new File("CaseAnthrax.xml");
	private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	private Document doc = dBuilder.parse(fXmlFile);
		
	private XPath xPath =  XPathFactory.newInstance().newXPath();
	
	private String rulenamesEx, rulebodyAttEx, ruleheadEx, rulealterEx, ruleprobEx, rulebodyAttEx2, ruleObjpropEx, ruleObjpropEx2, ruleNodeNameEx, ruleNodeNameEx2, rulepriorityEx; //all eXpressions
	private String keyrule, ruleAtt, rulealter, ruleID, ruleIDprob, ruleObjprop, ruleObjprop2, ruleAtt2, ruleNodeName, ruleNodeName2, ruleStatus, rulepriority;
	private String stateExpression, node2Expression, riskfactor1, riskfactor2;
	
	
	
	public ExtReadOntology() throws ParserConfigurationException, SAXException, IOException{
		try {   		
			
		
			stateExpression = "/rdf-RDF/owl-NamedIndividual/@rdf-about"; //states
			NodeList stateList = (NodeList) xPath.compile(stateExpression).evaluate(doc, XPathConstants.NODESET);
			
			for (int i = 0; i < stateList.getLength(); i++) {
				//System.out.println(stateList.getLength());
				String roughstates = stateList.item(i).getFirstChild().getNodeValue();
				//System.out.println(roughstates);
				
				node2Expression = "/rdf-RDF/owl-NamedIndividual[@rdf-about=\""+roughstates+"\"]/rdf-type/@rdf-resource"; //nestednodes
				NodeList nodeList2 = (NodeList) xPath.compile(node2Expression).evaluate(doc, XPathConstants.NODESET);
				
				String[] nodes2 = null;
				String states[]=roughstates.split("#");
				
				
					for(int j=0; j < nodeList2.getLength(); j++){
						//System.out.println(nodeList2.getLength());
						String roughnodes = nodeList2.item(j).getFirstChild().getNodeValue();
						//System.out.println(roughnodes);
						
						nodes2=roughnodes.split("#");
						//System.out.println(states[1]);
						//System.out.println(nodes2[1]);
						networkmap.put(nodes2[1], states[1]);
					}
				
			}
			
//			// Display content network
//			for (String key : networkmap.keySet()) {
//			     values = (List<String>) networkmap.get(key);
//			     //System.out.println(key+": "+values);
//			}
			
			execRule();
			
			Iterator<String> iterHashSet = partsFound.iterator();
			
			//creating new map for rules that contains riskfactor in the confounding rules: Status=Parts
			while(iterHashSet.hasNext()){
				ArrayList<String> kids = new ArrayList<String>();
				ArrayList<String> parent = new ArrayList<String>();
				
				String rf = iterHashSet.next();
				//System.out.println("inside con and a value is: "+rf);
				
				Collection<ExtRules> splitting = ruleListObj.get(rf);
				Iterator itr = splitting.iterator();
				
				while(itr.hasNext()) {
					ExtRules rul = (ExtRules) itr.next();
					String status = rul.getRuleStatus();
					String name = rul.getRuleName();
					
					
					if(status.equals("Solo")){
						kids.add(name);
					}
					
					if(status.equals("Confounding")){
						parent.add(name);
					}
									
					for(int k=0; k<kids.size(); k++){
						for(int j=0; j<parent.size(); j++){
							//System.out.println(kids.get(k)+"#"+parent.get(j));
							saveRulePartsRepresentation(kids.get(k), parent.get(j), rf);
						}
					}
					
				}
				
				
			}
		
		} catch (Exception e) {
		e.printStackTrace();
	    }
	}
		
	public void execRule(){
		try{
			//taking the name of rules
			rulenamesEx = "/rdf-RDF/rdf-Description/rdfs-label"; 
			NodeList rulenamesList = (NodeList) xPath.compile(rulenamesEx).evaluate(doc, XPathConstants.NODESET);
			int numOfRules = rulenamesList.getLength();
			
			rulepriorityEx = "/rdf-RDF/rdf-Description/rdfs-comment"; 
			NodeList rulepriorityList = (NodeList) xPath.compile(rulepriorityEx).evaluate(doc, XPathConstants.NODESET);
			
			for (int i = 0; i < numOfRules ; i++) {
				keyrule = rulenamesList.item(i).getFirstChild().getNodeValue();
				//System.out.println(keyrule);	
				
				String temprulePrio[] = rulepriorityList.item(i).getFirstChild().getNodeValue().split("- ");
				rulepriority = temprulePrio[1];
			
				rulebodyAttEx = "/rdf-RDF/rdf-Description[rdfs-label=\""+keyrule+"\"]/swrl-body/rdf-Description/rdf-rest/rdf-Description/rdf-first/rdf-Description/swrl-argument2/@rdf-resource"; //Male, Winter, Adults
				ruleObjpropEx = "/rdf-RDF/rdf-Description[rdfs-label=\""+keyrule+"\"]/swrl-body/rdf-Description/rdf-rest/rdf-Description/rdf-first/rdf-Description/swrl-propertyPredicate/@rdf-resource"; //hasMutation, hasHabits
				
				rulebodyAttEx2 = "/rdf-RDF/rdf-Description[rdfs-label=\""+keyrule+"\"]/swrl-body/rdf-Description/rdf-rest/rdf-Description/rdf-rest/rdf-Description/rdf-first/rdf-Description/swrl-argument2/@rdf-resource"; //Male, Winter, Adults
				ruleObjpropEx2 = "/rdf-RDF/rdf-Description[rdfs-label=\""+keyrule+"\"]/swrl-body/rdf-Description/rdf-rest/rdf-Description/rdf-rest/rdf-Description/rdf-first/rdf-Description/swrl-propertyPredicate/@rdf-resource"; //hasMutation, hasHabits
				
				NodeList rulebodyAttList = (NodeList) xPath.compile(rulebodyAttEx).evaluate(doc, XPathConstants.NODESET);
				NodeList ruleObjpropList = (NodeList) xPath.compile(ruleObjpropEx).evaluate(doc, XPathConstants.NODESET);
				
				NodeList rulebodyAttList2 = (NodeList) xPath.compile(rulebodyAttEx2).evaluate(doc, XPathConstants.NODESET);
				NodeList ruleObjpropList2 = (NodeList) xPath.compile(ruleObjpropEx2).evaluate(doc, XPathConstants.NODESET);
				
			    String tempruleAtt[]=rulebodyAttList.item(0).getFirstChild().getNodeValue().split("#");
			    ruleAtt = tempruleAtt[1];			    
			    ruleNodeName2="Empty";
			    
			    if(rulebodyAttList2.item(0)==null || ruleObjpropList2.item(0)==null){
			    	ruleAtt2="Empty";
			    }
			    
			    else{
			    	String tempruleAtt2[] = rulebodyAttList2.item(0).getFirstChild().getNodeValue().split("#");
			    	ruleAtt2 = tempruleAtt2[1];
			    }
			    	    
			    ruleNodeNameEx = "/rdf-RDF/owl-ObjectProperty[@rdf-about=\""+ruleObjpropList.item(0).getFirstChild().getNodeValue()+"\"]/rdfs-range/@rdf-resource";
			    NodeList ruleNodeNameList = (NodeList) xPath.compile(ruleNodeNameEx).evaluate(doc, XPathConstants.NODESET);
			    //System.out.println(ruleNodeNameEx);

			    String tempruleNodeName[]=ruleNodeNameList.item(0).getFirstChild().getNodeValue().split("#");
				ruleNodeName = tempruleNodeName[1];
				
				System.out.println(ruleNodeName+ruleAtt);
				
				if(!ruleAtt2.equals("Empty")){
					ruleNodeNameEx2 = "/rdf-RDF/owl-ObjectProperty[@rdf-about=\""+ruleObjpropList2.item(0).getFirstChild().getNodeValue()+"\"]/rdfs-range/@rdf-resource";
				    NodeList ruleNodeNameList2 = (NodeList) xPath.compile(ruleNodeNameEx2).evaluate(doc, XPathConstants.NODESET);
				    
				    String tempruleNodeName2[]=ruleNodeNameList2.item(0).getFirstChild().getNodeValue().split("#");
					ruleNodeName2 = tempruleNodeName2[1];
				}
			        
				riskfactor1 = ruleNodeName+ruleAtt;
				riskfactor2 = ruleNodeName2+ruleAtt2;
				
				ruleStatus = "Solo";
				
				//filling in the rule status
				if(!riskfactor1.equals("EmptyEmpty") && !riskfactor2.equals("EmptyEmpty")){
					ruleStatus = "Confounding";
					partsFound.add(riskfactor1);
					partsFound.add(riskfactor2);
					
				}

			    rulealterEx = "/rdf-RDF/rdf-Description/swrl-head/.//swrl-propertyPredicate/@rdf-resource"; //Alter, Set, Estimate
				ruleheadEx = "/rdf-RDF/rdf-Description/swrl-head/.//swrl-argument1/@rdf-resource"; //Disease
				ruleprobEx = "/rdf-RDF/rdf-Description/swrl-head/.//swrl-argument2"; //Probability
				

				NodeList rulealterList = (NodeList) xPath.compile(rulealterEx).evaluate(doc, XPathConstants.NODESET);
				NodeList ruleheadList = (NodeList) xPath.compile(ruleheadEx).evaluate(doc, XPathConstants.NODESET);
				NodeList ruleprobList = (NodeList) xPath.compile(ruleprobEx).evaluate(doc, XPathConstants.NODESET);
				
		    
				//System.out.println("parts: "+partsFound);
				
				String temprulealter[]=rulealterList.item(i).getFirstChild().getNodeValue().split("#");
				rulealter = temprulealter[1];
				
				ruleIDprob = ruleprobList.item(i).getFirstChild().getNodeValue();
				
				//to handle incomplete information about OR: [Low, Medium, High], or [n-fold]
				if(rulealter.equals("estimateRisk")){
					OrdinalQuantifier ord = new OrdinalQuantifier(ruleIDprob);
					ruleIDprob = ord.quantifiedProbStr;
					rulealter = "alterRisk";
				}
				else if(rulealter.equals("reduceRisk") || rulealter.equals("addRisk")){
					PercentageQuantifier perc = new PercentageQuantifier (rulealter, ruleIDprob); 
					ruleIDprob = perc.quantifiedProbStr;
					rulealter = "alterRisk";
				}
				else if(rulealter.equals("setPathogen")){
					OrdinalQuantifier ord = new OrdinalQuantifier(ruleIDprob);
					ruleIDprob = ord.quantifiedProbDStr;
					rulealter = "alterRisk";
				}
				
				String temprulehead[]=ruleheadList.item(i).getFirstChild().getNodeValue().split("#");
				ruleID = temprulehead[1];   
				
				    
				saveRuleIntermediateRepresentation();
				
			}
		
			
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	  }

	private void saveRulePartsRepresentation(String rulename, String ruleConfounding, String riskfactorPart) {
		// TODO Auto-generated method stub
		rulePartListObj.put(rulename+"#"+ruleConfounding, riskfactorPart);
		//System.out.println(rulename+"#"+ruleConfounding+" "+riskfactorPart);
	}

	private void saveRuleIntermediateRepresentation() {
		// TODO Auto-generated method stub
		
		
		ruleListObj.put(riskfactor1, new ExtRules(keyrule, riskfactor1, riskfactor2, rulealter, ruleID, ruleIDprob, ruleStatus, rulepriority));
		
		//for avoiding EmptyEmpty becomes key
		if(!ruleNodeName2.equals("Empty") || !ruleAtt2.equals("Empty")){
			ruleListObj.put(riskfactor2, new ExtRules(keyrule, riskfactor2, riskfactor1, rulealter, ruleID, ruleIDprob, ruleStatus, rulepriority));
			//System.out.println(riskfactor2+" "+keyrule+" "+riskfactor1+" "+rulealter+" "+ruleID+" "+ruleIDprob+" "+ruleStatus+" "+rulepriority);
		}
		
		System.out.println(riskfactor1+" "+keyrule+" "+riskfactor2+" "+rulealter+" "+ruleID+" "+ruleIDprob+" "+ruleStatus+" "+rulepriority);
		
	}
}
