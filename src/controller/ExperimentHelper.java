package controller;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import dao.DAO;
import detection.data.DetectionData;
import detection.data.DetectionResult;
import methods.VariableLengthMethod;
import view.PlotXY;


public class ExperimentHelper {

	// move to DAO_CSV
	// methods get the data for training and detection stage of the methods
	public static List< List<String> > getTrainingDataIDF(DAO dao, int id) {
		 final int TRAINING_DATA_SIZE = 10000; 
		 List< List<String> > trainingData = new ArrayList< List<String> >();
		 
		 List<String> User = dao.getActionsFromCSV(id);
		 trainingData.add( (List<String>) User.subList(0, TRAINING_DATA_SIZE) );
		 
		 for (int i = DAO.MIN_ID; i < id; ++i) {
			 List<String> Useri = dao.getActionsFromCSV(i);
			 trainingData.add(Useri);
		 }
		 for (int i = id + 1; i <= DAO.MAX_ID; ++i) {
			 List<String> Useri = dao.getActionsFromCSV(i);
			 trainingData.add(Useri);
		 }

		 return trainingData;
	}
	
	public static List<String> getTrainingDataNoIDF(DAO dao, int id) {
		 final int TRAINING_DATA_SIZE = 10000;
		 List<String> User = dao.getActionsFromCSV(id);
		 List<String> trainingData = (List<String>) User.subList(0, TRAINING_DATA_SIZE);
		 
		 return trainingData;
	}
	
	public static DetectionData getDetectionData(DAO dao, int id) {
		 List<String> User = dao.getActionsFromCSV(id);
		 final int DETECTION_DATA_SIZE = 5000;
		 List<String> falsePositiveData = new ArrayList<String>();
		 falsePositiveData = User.subList( User.size() - DETECTION_DATA_SIZE, User.size() );
		 
		 List< List<String> > truePositiveData = new ArrayList< List<String> >();
		 for (int i = DAO.MIN_ID; i < id; ++i) {
			 List<String> Useri =  dao.getActionsFromCSV(i);
			 truePositiveData.add( Useri.subList( Useri.size() - DETECTION_DATA_SIZE, Useri.size() ) );
		 }
		 for (int i = id + 1; i <= DAO.MAX_ID; ++i) {
			 List<String> Useri = dao.getActionsFromCSV(i);
			truePositiveData.add( Useri.subList( Useri.size() - DETECTION_DATA_SIZE, Useri.size() ) );
		 }
		 
		 DetectionData data = new DetectionData(falsePositiveData, truePositiveData);
		 
		 return data;
	}

	// implement as a default value in VariableLengthMethod
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
	
	public static void plotDecisionValues(int N, int windowSize, String plotName, int trainingId, List<List<Double>> series) {
		final String currentDirectory = System.getProperty("user.dir");
		final String outputDirectory = currentDirectory + "/Experiments/TEST_N=" + N // + experimentId
													   + "/decision values/";
		PlotXY view = new PlotXY(plotName + " (trainingId = " + trainingId + ")", "index", "Decision Values", outputDirectory);   //  appName, plotName, Xname, Yname, outputDirectory;
		
		String seriesName = "normal user: user id = " + trainingId;
		view.add( series.get(0), seriesName );
		System.out.println("Decision Values(" + 0 + ") = "  + series.get(0));
		for ( int i = 1; i < series.size(); ++i ) {
			int id = (i < trainingId)? (i) : (i + 1);   //  get the relative user id
			seriesName = "anomalous user: user id = " + id;
			view.add(series.get(i), seriesName);
			System.out.println("Decision Values(" + i + ") = "  + series.get(i));
		}
		view.draw();
	}
	
	// get ROCValues
	public static Pair<List<Double>, List<Double>> getROCValues(VariableLengthMethod<?> method, List<String> falseData, List<String> trueData, String methodName) {
			List<Double> xData = new ArrayList<Double>();
			List<Double> yData = new ArrayList<Double>();
			
			System.out.println(methodName + "\nbegin roc: ");
			for ( double threshold = 0.0; threshold < 1.0; threshold = threshold + 0.001 ) {
				DetectionResult falsePositive = method.detect(falseData, threshold);
				DetectionResult truePositive = method.detect(trueData, threshold);
			
				xData.add( falsePositive.getPositivesRate() );
				yData.add( truePositive.getPositivesRate() );
				
				double FPR = falsePositive.getPositivesRate();
				double TPR = truePositive.getPositivesRate();
				if ( !(TPR == 1 && FPR == 1) && (TPR - FPR) > 0.2 )
				{
					System.out.println( "FPR = " + FPR + ", TPR = " + TPR + ", threshold = " + threshold);
				}
			}
			System.out.println("end roc\n");
		
		return new Pair<List<Double>, List<Double>>(xData, yData);
	}
	
	// plot ROCCurves
	public static void plotROCCurves(int N, int windowSize, String plotName, String [] seriesNames, Pair<List<Double>, List<Double>>... series) {
		final String currentDirectory = System.getProperty("user.dir");
		final String outputDirectory = currentDirectory + "/Experiments/TEST_N=" + N // + experimentId 
													   + "/roc/";
		PlotXY view = new PlotXY(plotName, "FPR", "TPR", outputDirectory);   //  appName, plotName, Xname, Yname, outputDirectory;
		for ( int i = 0; i < series.length; ++i ) {
			view.add( series[i].getValue0(),  series[i].getValue1(), seriesNames[i+1]);
		}
		view.draw();
	}
	
}
