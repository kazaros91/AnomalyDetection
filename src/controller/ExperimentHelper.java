package controller;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import detection.data.DetectionData;
import detection.data.DetectionResult;
import methods.VariableLengthMethod;
import view.PlotXY;


public class ExperimentHelper {

	// get Decision Values
	public static List<List<Double>> getDecisionValues(VariableLengthMethod<?> method, DetectionData detectionData) {
		List<List<Double>> DD = new ArrayList<List<Double>>();
		DetectionResult result = method.detect( detectionData.getFalsePositiveData() );
		DD.add(result.getDecisionValues());
		for ( int i = 0; i < detectionData.getTruePositiveData().size() - 1; ++i) {
			result = method.detect( detectionData.getTruePositiveData(i) );
			DD.add(result.getDecisionValues());
		}
		
		return DD;
	}
	
	public static void plotDecisionValues(String testDir, int windowSize, String plotName, int trainingId, List<List<Double>> series) {
		final String outputDirectory = testDir + "/decision values/";
		Utils.makeDir(outputDirectory);
			
		PlotXY view = new PlotXY(plotName + " (trainingId = " + trainingId + ")", "index", "Decision Values", outputDirectory);   //  appName, plotName, Xname, Yname, outputDirectory;
		String seriesName = "normal user: userId = " + trainingId;
		view.add( series.get(0), seriesName );
		System.out.println("Decision Values(" + 0 + ") = "  + series.get(0));
		for ( int i = 1; i < series.size(); ++i ) {
			int id = (i < trainingId)? (i) : (i + 1);   //  get the relative user id
			seriesName = "anomalous user: userId = " + id;
			view.add(series.get(i), seriesName);
			System.out.println("Decision Values(" + i + ") = "  + series.get(i));
		}
		view.draw();
	}
	
	// get ROCValues
	public static Pair<List<Double>, List<Double>> getROCValues(VariableLengthMethod<?> method, List<String> falseData, List<String> trueData, String methodName) {
			List<Double> xData = new ArrayList<Double>();
			List<Double> yData = new ArrayList<Double>();
			
//			System.out.println(methodName + "\nbegin roc: ");
			for ( double threshold = 0.0; threshold < 1.0; threshold = threshold + 0.001 ) {
				DetectionResult falsePositive = method.detect(falseData, threshold);
				DetectionResult truePositive = method.detect(trueData, threshold);
			
				xData.add( falsePositive.getPositivesRate() );
				yData.add( truePositive.getPositivesRate() );
				
				// uncomment to print FPR and TPR
//				double FPR = falsePositive.getPositivesRate();
//				double TPR = truePositive.getPositivesRate();
//				if ( !(TPR == 1 && FPR == 1) && (TPR - FPR) > 0.2 )
//				{
//					System.out.println( "FPR = " + FPR + ", TPR = " + TPR + ", threshold = " + threshold);
//				}
			}
//			System.out.println("end roc\n");
		
		return new Pair<List<Double>, List<Double>>(xData, yData);
	}
	
	// plot ROCCurves
	public static void plotROCCurves(String testDir, int windowSize, String plotName, String [] seriesNames, Pair<List<Double>, List<Double>>... series) {
		final String outputDirectory = testDir + "/roc/";
		Utils.makeDir(outputDirectory);
			
		PlotXY view = new PlotXY(plotName, "FPR", "TPR", outputDirectory);   //  appName, plotName, Xname, Yname, outputDirectory;
		for ( int i = 0; i < series.length; ++i ) {
			view.add( series[i].getValue0(),  series[i].getValue1(), seriesNames[i]);
		}
		view.draw();
	}
	
}
