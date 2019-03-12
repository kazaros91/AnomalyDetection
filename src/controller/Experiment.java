package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javatuples.Pair;

import dao.DAO;
import detection.data.DetectionData;
import methods.MarkovF2Method;
import methods.MarkovMethod;


public class Experiment {
	
	private List<Integer> lengths;
	private Map<Integer, Integer> weights;
	private Map<Integer, Integer> weights2;
	private int windowSize;
	private double eta;
	private double thresholdDecision;
	
	
	public String defineParameters() {
		String dir = System.getProperty("user.dir") + "/Experiments4";  // defining the directory to store the results
		Utils.makeDir(dir);
		// BEGIN: defining the hyper parameters
		// BEGIN: hyper parameters written in Experiments1, Experiments2, Experiments3, Experiments4
		
		// hyper parameters in common for the training stage for all methods in common, by default this case is Experiement3
		int W = 5;
		lengths = new ArrayList<Integer>();
		for ( int k = 0; k < W; ++k )
			lengths.add(2*k+2);  // lengths == l

		weights = new HashMap<Integer, Integer>();
		for ( int k = 0; k < W; ++k )
			weights.put( lengths.get(k), 2*k+1 );  // weights == e
		weights2 = new HashMap<Integer, Integer>();
		for ( int k = 0; k < W; ++k )
			weights2.put( lengths.get(k), 2*k+1 );  // weights2 == e2
			
		// hyper parameters in common for the detection stage in for all methods
		windowSize = 300;
		
		// parameters for Markov Chain based methods' detection stage
		eta = 0.2;
		
		Utils.writeToFile(dir, lengths, weights, weights2, windowSize, eta);
		// END: hyper parameters written in Experiments1, Experiments2, Experiments3, Experiments4
		
		thresholdDecision = 0.79;   //  in applications it should be defined by cross validation
		// END: defining the hyper parameters
		
		return dir;
	}
	
	Experiment() {}
	
	
	public static void main(String [] args) {
		Experiment experiment = new Experiment();
		String dir = experiment.defineParameters();
		experiment.performExperiments(dir);
	}
			
	public void performExperiments(String dir) {
		// BEGIN: hyper parameters for Markov Chain based methods' training stage
//		int N = 4;
		for ( int N = 2; N <= 5; ++N ) {
			// END: hyper parameters for Markov Chain based methods' training stage
			String testDir = dir + "/TEST_N=" + N;
			Utils.makeDir(testDir);
			
			// BEGIN: MARKOV CHAIN BASED METHODS
			// Markov [4] method
			MarkovMethod markov = new MarkovMethod();
			markov.setTrainingParameters(lengths, N, weights, weights2);
			markov.setDetectionParameters(windowSize, eta, thresholdDecision);
			
			// MarkovN2 [4] method
			MarkovMethod markovN2 = new MarkovMethod();
			markovN2.setTrainingParameters(lengths, N*N, weights, weights2);
			markovN2.setDetectionParameters(windowSize, eta, thresholdDecision);
						
			// MarkovF2 method
			MarkovF2Method markovF2 = new MarkovF2Method();
			markovF2.setTrainingParameters(lengths, N, weights, weights2);
			markovF2.setDetectionParameters(windowSize, eta, thresholdDecision);
			// END: MARKOV CHAIN BASED METHODS
			
			// initializing of Datasource Object
			DAO dao = new DAO();
			// defining the user id to get his actions for the training stage
			for (int trainingId = 2; trainingId <= 4; ++trainingId )   //  trainingId == x in the paper
			{
//				int trainingId = 3;
				List<String> trainingSeq = dao.getTrainingData(trainingId);
				
				markov.train(trainingSeq);
				System.out.println();
				markovN2.train(trainingSeq);
				System.out.println();
				markovF2.train(trainingSeq);
				System.out.println();
				
				// defining detections stage's data
				DetectionData detectionData = dao.getDetectionData(trainingId);
				List<String> falsePositivesData = detectionData.getFalsePositiveData();
				
				String [] methodNames = {"Markov [4]", "MarkovN2 [4]", "MarkovF2"};
				List< List<Double> > DD0 = ExperimentHelper.getDecisionValues(markov, detectionData);
				List< List<Double> > DD1 = ExperimentHelper.getDecisionValues(markovN2, detectionData);
				List< List<Double> > DD2 = ExperimentHelper.getDecisionValues(markovF2, detectionData);
				
				ExperimentHelper.plotDecisionValues(testDir, windowSize, methodNames[0], trainingId, DD0);
				ExperimentHelper.plotDecisionValues(testDir, windowSize, methodNames[1], trainingId, DD1);
				ExperimentHelper.plotDecisionValues(testDir, windowSize, methodNames[2], trainingId, DD2);
				
				for 	( int id2 = 0; id2 < detectionData.getTruePositiveData().size() - 1; ++id2 ) 
				{
//					int id2 = 1;
					List<String> truePositivesData = detectionData.getTruePositiveData(id2);
					int detectionId = (id2 < trainingId - 1)? (id2 + 1) : (id2 + 2);   //  get relative user id
					final String idPair = "(trainingId = " + trainingId + ", detectionId = " + detectionId + ")";
					
					Pair<List<Double>, List<Double>> series1 = ExperimentHelper.getROCValues(markov, falsePositivesData, truePositivesData, methodNames[0]);
					Pair<List<Double>, List<Double>> series2 = ExperimentHelper.getROCValues(markovN2, falsePositivesData, truePositivesData, methodNames[1]);
					Pair<List<Double>, List<Double>> series3 = ExperimentHelper.getROCValues(markovF2, falsePositivesData, truePositivesData, methodNames[2]);
//				
					ExperimentHelper.plotROCCurves(testDir, windowSize, idPair, methodNames, series1, series2, series3);
				}
//			}

			}
		}
	}
		
	
}
