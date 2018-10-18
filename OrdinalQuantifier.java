package kempot.domparser;

import java.util.Random;

public class OrdinalQuantifier {
	
	public float quantifiedProb = 0.0F;
	public double quantifiedProbD;
	public float max = 0.0F;
	public float min = 0.0F;
	public String quantifiedProbStr = "";
	public String quantifiedProbDStr = "";
	public float central = 0.0F;
	
	public OrdinalQuantifier(String ordinal){
		
		if(ordinal.equals("Low") || ordinal.equals("Medium") || ordinal.equals("High")){
			OrdQuantifier(ordinal);
			//System.out.println(ord+", "+max+", "+min);
		}
		
		else if(ordinal.equals("InActive") || ordinal.equals("LessActive") || ordinal.equals("Active") || ordinal.equals("MoreActive")){
			PathogenQuantifier(ordinal);
			//System.out.println(ordinal+", "+central);
		}
		
		else{
			nfoldQuantifier(ordinal);
			//System.out.println(ordinal+", "+max+", "+min);
		}
		
		quantifiedProbD = getProb(ordinal, central);
		quantifiedProb = getProb(ordinal, max, min);
		quantifiedProbStr = ""+quantifiedProb;
		quantifiedProbDStr = ""+quantifiedProbD;
		
	}

	private double getProb(String ordinal, float central) {
		// TODO Auto-generated method stub
		double SD = 0.25;
		
		//Note: each run will have different random value.
		Random r = new Random();
		double randomD = r.nextGaussian()*SD + central;
		
		if(randomD<0)
			randomD = 0;
		
		return randomD;
	}

	private void PathogenQuantifier(String ord) {
		// TODO Auto-generated method stub
		if(ord.equals("InActive")){
			central = 0.15F;
		}
		else if(ord.equals("LessActive")){
			central = 0.7F;
		}
		else if(ord.equals("Active")){
			central = 1.5F;
		}
		else if(ord.equals("MoreActive")){
			central = 1.85F;
		}
	}

	private void nfoldQuantifier(String nfold) {
		// TODO Auto-generated method stub
		String takeN[] = nfold.split("-");
		float maxmin = 0.25F;
		//System.out.println(takeN[0]);
		float n = Float.parseFloat(takeN[0]);
		
		min = n-maxmin;
		max = n+maxmin;
	}

	private void OrdQuantifier(String ord) {
		// TODO Auto-generated method stub
		
		if(ord.equals("Low")){
			min = 0.5F;
			max = 0.25F;
		}
		else if(ord.equals("Medium")){
			min = 0.75F;
			max = 1.25F;
		}
		else if(ord.equals("High")){
			min = 1.5F;
			max = 1.75F;
		}
	}
	
	private float getProb(String ord, float max, float min) {
		// TODO Auto-generated method stub
		//Note: each run will have different random value.
		Random r = new Random();
		float random = min + r.nextFloat() * (max - min);
		
		return random;
	}

}
