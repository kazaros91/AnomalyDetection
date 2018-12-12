package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javatuples.Pair;

import dao.DAO;
import detection.data.DetectionData;
import idf.calculator.IDFCalculator;
import methods.MarkovF2IDFMethod;
import methods.MarkovF2Method;
import methods.MarkovIDFMethod;
import methods.MarkovMethod;


public class Experiment {
	
	public static void main(String [] args) {
		test();
	}
			
	public static void test() {
		// BEGIN defining the hyperparameters
		// BEGIN: HYPERPARAMETERS THOSE ARE MODIFIED IN Experiments1, Experiments2, Experiments3, Experiments4
		// parameters for the training stage for all methods in common
		int W = 5;
		List<Integer> lengths = new ArrayList<Integer>();
		for ( int i = 0; i < W; ++i )
			lengths.add(2*i+2);
		
		Map<Integer, Integer> weights = new HashMap<Integer, Integer>();
		for ( int i = 0; i < W; ++i )
			weights.put( lengths.get(i), 2*i+1 );
		Map<Integer, Integer> weights2 = new HashMap<Integer, Integer>();
		for ( int i = 0; i < W; ++i )
			weights2.put( lengths.get(i), 2*i+1 );
			
		// parameters for the detection stage in for all methods common
		int windowSize = 300;
		// END: OF HYPERPARAMETERS THOSE ARE MODIFIED IN Experiments1, Experiments2, Experiments3, Experiments4
		
		// parameters for Markov Chain based method's detection stage
		double eta = 0.2;
		
		double thresholdDecision = 0.7;   //  in applications it should be defined by cross validation
		
		//  parameters for IDF using methods'
		double idfThreshold = 2.5;
		
		// initializing of Datasource Object and defining the user id to get his actions for the training stage
		DAO dao = new DAO();
		IDFCalculator.calculateIDFValues(dao.getAllData(), lengths, weights);
		
		// parameters for Markov Chain based method's training stage
		int N = 4;
		for ( N = 2; N <= 5; ++N ) {
			// END of defining the hyperparameters
				
			// MARKOV CHAIN BASED METHODS
			// -----------------
			// Markov method [4]
			MarkovMethod markov = new MarkovMethod();
			markov.setTrainingParameters(lengths, N, weights, weights2, 0.0);   //  0.0 is default thresholdIDF
			markov.setDetectionParameters(windowSize, eta, thresholdDecision);
			
			// Markov method with IDF
		    MarkovIDFMethod markovIdf = new MarkovIDFMethod();
			markovIdf.setTrainingParameters(lengths, N, weights, weights2, idfThreshold);
			markovIdf.setDetectionParameters(windowSize, eta, thresholdDecision);
			
			// Markov method [4] N square
			MarkovMethod markovN2 = new MarkovMethod();
		    markovN2.setTrainingParameters(lengths, N*N, weights, weights2, 0.0);   //  0.0 is default thresholdIDF
		    markovN2.setDetectionParameters(windowSize, eta, thresholdDecision);
		    			
			// Markov method N square with IDF
			MarkovIDFMethod markovN2Idf = new MarkovIDFMethod();
			markovN2Idf.setTrainingParameters(lengths, N*N, weights, weights2, idfThreshold);
			markovN2Idf.setDetectionParameters(windowSize, eta, thresholdDecision);
			// -----------------
			
			// Markov method N square with IDF
			// -----------------
			MarkovF2Method markovF2 = new MarkovF2Method();
			markovF2.setTrainingParameters(lengths, N, weights, weights2, 0.0);   //  0.0 is default thresholdIDF
			markovF2.setDetectionParameters(windowSize, eta, thresholdDecision);
		
			// Markov method with IDF
			MarkovF2Method markovF2Idf = new MarkovF2IDFMethod();
			markovF2Idf.setTrainingParameters(lengths, N, weights, weights2, idfThreshold);
			markovF2Idf.setDetectionParameters(windowSize, eta, thresholdDecision);
			// -----------------
			
			for (int trainingId = 2; trainingId <= 4; ++trainingId ) 
			{
	//			int id = 3;
				List<String> trainingSeq = dao.getTrainingData(trainingId);

				markov.train(trainingSeq);
				System.out.println();
				markovIdf.train(trainingSeq);
				System.out.println();
				markovN2.train(trainingSeq);
				System.out.println();
				markovN2Idf.train(trainingSeq);
				System.out.println();
				markovF2.train(trainingSeq);
				System.out.println();
				markovF2Idf.train(trainingSeq);
				System.out.println();
				
				// defining detections stage's data
				DetectionData detectionData = dao.getDetectionData(trainingId);
				List<String> falsePositivesData = detectionData.getFalsePositiveData();
			
				String [] methodNames = {"IDF [3]", "Markov [4]", "MarkovIDF", "MarkovN2 [4]", "MarkovN2IDF", "MarkovF2", "MarkovF2IDF"};
	//			for ( int i = 0; i < names.length; ++i ) {
			
				List< List<Double> > DD1 = ExperimentHelper.getDecisionValues(markov, detectionData);
				List< List<Double> > DD2 = ExperimentHelper.getDecisionValues(markovIdf, detectionData);
				List< List<Double> > DD3 = ExperimentHelper.getDecisionValues(markovN2, detectionData);
				List< List<Double> > DD4 = ExperimentHelper.getDecisionValues(markovN2Idf, detectionData);
				List< List<Double> > DD5 = ExperimentHelper.getDecisionValues(markovF2, detectionData);
				List< List<Double> > DD6 = ExperimentHelper.getDecisionValues(markovF2Idf, detectionData);
				
				ExperimentHelper.plotDecisionValues(N, windowSize, methodNames[1], trainingId,  DD1);
				ExperimentHelper.plotDecisionValues(N, windowSize, methodNames[2], trainingId, DD2);
				ExperimentHelper.plotDecisionValues(N, windowSize, methodNames[3], trainingId, DD3);
				ExperimentHelper.plotDecisionValues(N, windowSize, methodNames[4], trainingId, DD4);
				ExperimentHelper.plotDecisionValues(N, windowSize, methodNames[5], trainingId, DD5);
				ExperimentHelper.plotDecisionValues(N, windowSize, methodNames[6], trainingId, DD6);
//				
				for 	( int id2 = 0; id2 < detectionData.getTruePositiveData().size() - 1; ++id2 ) 
				{
//					int id2 = 1;
					List<String> truePositivesData = detectionData.getTruePositiveData(id2);
					int detectionId = (id2 < trainingId - 1)? (id2 + 1) : (id2 + 2);   //  get relative user id
					final String idPair = "(training data's user id = " + trainingId + ", detection data's userId id = " + detectionId + ")";
					Pair<List<Double>, List<Double>> series1 = ExperimentHelper.getROCValues(markov, falsePositivesData, truePositivesData, methodNames[1]);
					Pair<List<Double>, List<Double>> series2 = ExperimentHelper.getROCValues(markovIdf, falsePositivesData, truePositivesData, methodNames[2]);
					Pair<List<Double>, List<Double>> series3 = ExperimentHelper.getROCValues(markovN2, falsePositivesData, truePositivesData, methodNames[3]);
					Pair<List<Double>, List<Double>> series4 = ExperimentHelper.getROCValues(markovN2Idf, falsePositivesData, truePositivesData, methodNames[4]);
					Pair<List<Double>, List<Double>> series5 = ExperimentHelper.getROCValues(markovF2, falsePositivesData, truePositivesData, methodNames[5]);
					Pair<List<Double>, List<Double>> series6 = ExperimentHelper.getROCValues(markovF2Idf, falsePositivesData, truePositivesData, methodNames[6]);
//				
					ExperimentHelper.plotROCCurves(N, windowSize, idPair, methodNames, series1, series2, series3, series4, series5, series6);
				}
//			}

			}
		}
	}
		
	
}
