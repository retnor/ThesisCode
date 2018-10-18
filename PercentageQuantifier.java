package kempot.domparser;

public class PercentageQuantifier {
	
	public float quantifiedProb = 0.0F;
	public String quantifiedProbStr = "";
	
	public PercentageQuantifier(String operation, String percentage){
		String tempPerc[] = percentage.split("%");
		float perc = Float.parseFloat(tempPerc[0]);
		
		if(operation.equals("reduceRisk")){
			quantifiedProb = 1-(perc/100);
		}
		else{
			quantifiedProb = 1+(perc/100);
		}
		
		quantifiedProbStr = ""+quantifiedProb;
	}
}
