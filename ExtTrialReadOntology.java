package kempot.domparser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ExtTrialReadOntology {
	
	public static Multimap<String,String> networkmap = ArrayListMultimap.create();
	public int nodecounter;
	public static Multimap<String, ExtRules> ruleListObj = ArrayListMultimap.create();
	public float Probs;
	public Rules rules;
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		try {   
			List<String> values = null;
			File fXmlFile = new File("Crohn.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			XPath xPath =  XPathFactory.newInstance().newXPath();
			
			String rulenamesEx, rulebodyAttEx, ruleheadEx, rulealterEx, ruleprobEx, rulebodyAttEx2, ruleObjpropEx, ruleObjpropEx2, ruleNodeNameEx, ruleNodeNameEx2; //all eXpressions
			String keyrule, ruleAtt, rulealter, ruleID, ruleIDprob, ruleObjprop, ruleObjprop2, ruleAtt2, ruleNodeName, ruleNodeName2;
			
			//taking the name of rules
			rulenamesEx = "/rdf-RDF/rdf-Description/rdfs-label"; 
			NodeList rulenamesList = (NodeList) xPath.compile(rulenamesEx).evaluate(doc, XPathConstants.NODESET);
			int numOfRules = rulenamesList.getLength();
			
			for (int i = 0; i < numOfRules ; i++) {
				keyrule = rulenamesList.item(i).getFirstChild().getNodeValue();
				//System.out.println(keyrule);			
			
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
				
				//System.out.println(ruleNodeName+ruleAtt);
				
				if(!ruleAtt2.equals("Empty")){
					ruleNodeNameEx2 = "/rdf-RDF/owl-ObjectProperty[@rdf-about=\""+ruleObjpropList2.item(0).getFirstChild().getNodeValue()+"\"]/rdfs-range/@rdf-resource";
				    NodeList ruleNodeNameList2 = (NodeList) xPath.compile(ruleNodeNameEx2).evaluate(doc, XPathConstants.NODESET);
				    
				    String tempruleNodeName2[]=ruleNodeNameList2.item(0).getFirstChild().getNodeValue().split("#");
					ruleNodeName2 = tempruleNodeName2[1];
				}
			        
			    rulealterEx = "/rdf-RDF/rdf-Description/swrl-head/.//swrl-propertyPredicate/@rdf-resource"; //Increase, Decrease, Set
				ruleheadEx = "/rdf-RDF/rdf-Description/swrl-head/.//swrl-argument1/@rdf-resource"; //Disease
				ruleprobEx = "/rdf-RDF/rdf-Description/swrl-head/.//swrl-argument2"; //Probability
				

				NodeList rulealterList = (NodeList) xPath.compile(rulealterEx).evaluate(doc, XPathConstants.NODESET);
				NodeList ruleheadList = (NodeList) xPath.compile(ruleheadEx).evaluate(doc, XPathConstants.NODESET);
				NodeList ruleprobList = (NodeList) xPath.compile(ruleprobEx).evaluate(doc, XPathConstants.NODESET);
				
		    
				String temprulealter[]=rulealterList.item(i).getFirstChild().getNodeValue().split("#");
				rulealter = temprulealter[1];
				String temprulehead[]=ruleheadList.item(i).getFirstChild().getNodeValue().split("#");
				ruleID = temprulehead[1];   
				ruleIDprob = ruleprobList.item(i).getFirstChild().getNodeValue();
				    
				ruleListObj.put(keyrule, new ExtRules(ruleNodeName+ruleAtt, ruleNodeName2+ruleAtt2, rulealter, ruleID, ruleIDprob)); 
				ruleListObj.put(keyrule, new ExtRules(ruleNodeName2+ruleAtt2, ruleNodeName+ruleAtt, rulealter, ruleID, ruleIDprob));
				
				System.out.println(keyrule+" "+ruleNodeName+ruleAtt+" "+ruleNodeName2+ruleAtt2+" "+rulealter+" "+ruleID+" "+ruleIDprob);
				System.out.println(keyrule+" "+ruleNodeName2+ruleAtt2+" "+ruleNodeName+ruleAtt+" "+rulealter+" "+ruleID+" "+ruleIDprob);
			}
			
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	  }
	}
