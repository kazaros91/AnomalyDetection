package detection.data;

import java.util.List;


public class DetectionResult {
	List<Double> decisionValues;
	double negativesRate;
	double positivesRate;
	
	public DetectionResult() {}
	
	public DetectionResult(List<Double> decisionValues, Double negativesRate, Double positivesRate) {
		this.decisionValues = decisionValues;
		this.negativesRate = negativesRate;
		this.positivesRate = positivesRate;
	}
	
	public List<Double> getDecisionValues() {
		return decisionValues;
	}
	
	public double getNegativeRates() {
		return negativesRate;
	}
	
	public double getPositivesRate() {
		return positivesRate;
	}
	
}
