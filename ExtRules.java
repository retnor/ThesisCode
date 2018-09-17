package kempot.domparser;

public class ExtRules {
	private String rulenames;
	private String disease;
	private String ruleObjprop;
	private String rulealter;
	private String rulefactor1;
	private String rulefactor2;
	private float ruleProb;
	private String ruleStatus;
	private float rulePrio;
	
	ExtRules(){
		
	}
	
	ExtRules(String name, String riskfactor1, String riskfactor2, String alter, String dis, String prob, String stat, String prio){
		rulenames = name;
		disease = dis;
		rulefactor1 = riskfactor1;
		rulefactor2 = riskfactor2;
		rulealter = alter;
		ruleProb = Float.parseFloat(prob);
		ruleStatus = stat;
		rulePrio = Float.parseFloat(prio);
	}
	
	public String getRuleName(){
		return rulenames;
	}
	
	public String getRuleFactor1(){
		return rulefactor1;
	}
	
	public String getRuleFactor2(){
		return rulefactor2;
	}
	
	public String getRuleAlter(){
		return rulealter;
	}
	
	public String getRuleDisease(){
		return disease;
	}
	
	public float getProb(){
		return ruleProb;
	}
	
	public float getPrio(){
		return rulePrio;
	}
	
	public String getRuleStatus(){
		return ruleStatus;
	}
	
}
