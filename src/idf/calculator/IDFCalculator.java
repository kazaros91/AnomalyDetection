package idf.calculator;


import java.util.List;
import java.util.Map;

import model.Profile;


public class IDFCalculator {
	
	public static void calculateIDFValues(List< List<String> > sequences, List<Integer> lengths,  Map<Integer, Integer> weights) {
		for ( int i = 0; i < sequences.size(); ++i ) {
			Profile profile = new Profile();
			profile.generateVariableLengthSequences( sequences.get(i), lengths, weights );   //  generating for all Profiles for the sake of calculating IDF values
		}
		
		Profile.calculateIDFValues( sequences.size() );
	}

}
