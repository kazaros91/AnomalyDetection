package rochelper;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;


public class ROCHelper {
	private int positive;
	private int negative;
	ArrayList<Integer> decisionValues;
	
	private List<Double> positiveRates;
	private List<Double> negativeRates;
	
	public ROCHelper() {
		positive = 0;
		negative = 0;
		
		positiveRates = new ArrayList<Double>();
	    negativeRates = new ArrayList<Double>();
	}
	
	public void add(boolean isPositive) {
		if (isPositive)
			++positive;
		else
			++negative;
		
		positiveRates.add( getPositiveRate() );
		negativeRates.add( getNegativeRate() );
	}
	
	public void add(ArrayList<Integer> decisionValues) {
		this.decisionValues = decisionValues;
	}
	
	public void addPositive() {
		++positive;
	}
	
	public void addNegative() {
		++negative;
	}
	
	public double getPositiveRate() { 
		return (double) positive / (double) (positive + negative);
	}
	
	public double getNegativeRate() { 
		return (double) negative / (double) (positive + negative);
	}
	
	public List<Double> getPositiveRates() { 
		return positiveRates;
	}
	
	public List<Double> getNegativeRates() { 
		return negativeRates;
	}
	
	public List< Pair<Double, Double> > getROCValues(List<Double> negativeRates) {
		List< Pair<Double, Double> > roc = new ArrayList< Pair<Double, Double> >();
		for (int i = 0; i < negativeRates.size(); ++i) { 
			roc.add( new Pair<Double, Double>( negativeRates.get(i), this.positiveRates.get(i) ) );
		}
		
		return roc;
	}
	
//	public double getTPR() {
//		return TP / (TP + FN); 
//	}
//	
//	public void getFNR() {
//		return FN / (FN + TP); 
//	}
//	
//	
//	public void getFPR() {
//		return FP / (FP + TN); 
//	}
//	
//	public double getTNR() {
//		return TN / (TN + FP); 
//	}

}
