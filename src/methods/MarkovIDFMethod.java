package methods;

import model.Profile;


public class MarkovIDFMethod extends MarkovMethod {

	public MarkovIDFMethod() {
		super();
	}
	
	@Override
	public void filterProfile() {
		Profile.calculateIDFValues(5);   //  in total there are 5 users
		// removing short sequences with low IDF values
        removeLowRecognnizablePatterns();
	}
	
	private void removeLowRecognnizablePatterns() {
		for ( int i = 0; i < LGS.size(); ++i ) {
			String shortSequence =  LGS.get(i).getString();
			if ( Profile.getIDF(shortSequence) < thresholdIdf ) {
				LGS.remove(i);
			}
		}
	}
		
}
