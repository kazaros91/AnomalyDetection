package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import detection.data.DetectionData;
import pojo.UserBehavior;
import pojo.UserBehaviorBuilder;


public class DAO {
	
	private static boolean isRead = false;
	private static String input;
	
	public static int MIN_ID = 1;
	public static int MAX_ID = 5;
	
	public DAO() {}
 
    
//    public List<String> getActionsByRelativeUserId(int relativeUserId) {
//	    	if (relativeUserId < DAO.MIN_ID || DAO.MAX_ID > 5 )
//	    		System.out.println("Invalid user Id");
//    	
//	    final int USER_ID1 = 36233277;  
//		final int USER_ID2 = 130270245;
//		final int USER_ID3 = 65645933;  
//		final int USER_ID4 = 59511789;  
//		final int USER_ID5 = 73196588; 
//			
//		
//		switch ( relativeUserId ) {
//			case 1: { 
//				return getActionsFromCSV(USER_ID1);
//			}
//			case 2: { 
//				return getActionsFromCSV(USER_ID2);
//			}
//			case 3: { 
//				return getActionsFromCSV(USER_ID3);
//			}
//			case 4: { 
//				return getActionsFromCSV(USER_ID4);
//			}
//			case 5: { 
//				return getActionsFromCSV(USER_ID5);
//			}
//		}
//				
//		return null;
//    }	
	
//	////// NEW CODE FOR CSV
//	public List<String> getActionsFromCSVAll (int userId) {
//		List<UserBehavior> behavior = getUserBehaviorByUserIdCSV(userId);
//		
//		for ( int i=0; i < behavior.size(); ++i ) {
//			UserBehavior u = behavior.get(i);
//			System.out.println( "BEHAVIOR: " + u.getUserId() + " " +
//											 u.getBehaviorType() + " " + 
//											 u.getDateTime() );
//		}
//		List<String> actions = new ArrayList<String>();
////		StringBuilder s = new StringBuilder();
//		for (int i = 0; i < behavior.size(); ++i) {
//		     String currentAction = Integer.toString( behavior.get(i).getBehaviorType() );   //  convert current action to String 
//		     actions.add(currentAction);
////		     s.append( currentAction );
//		}
//
////		System.out.println(" HHH " + actions.subList(0, 5).toString() );
////		return s.toString();
//		return actions;
//	}

	public List<String> getActionsFromCSV(int userId) {
		List<String> actions = new ArrayList<String>();
		
		final String currentDirectory = System.getProperty("user.dir");
		final String inputFilename = currentDirectory + "/cleaned_dataset1/behavior" + userId + ".csv";
		
		BufferedReader input;
		
		try { 
			input = new BufferedReader(new FileReader(new File(inputFilename)));
			
			String line = "";
			String[] st = null;
			
			input.readLine();   //  first line is omitted since it contains fields of the data
			while ( ( line = input.readLine() ) != null ) {
				st = line.replace("\"","").split(",");
				String action = st[0];
				actions.add(action);
			}
			input.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}     
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		}
		
		return actions;   //  note that the actions are ordered chronologically in the .csv file
	}
	
	private List<UserBehavior> getUserBehaviorByUserIdCSV(int userId) {
		List<UserBehavior> behavior = new ArrayList<UserBehavior>();
		
		final String currentDirectory = System.getProperty("user.dir");
		final String inputFilename = currentDirectory + "/tianchi_mobile_recommend_train_user.csv";
		
		BufferedReader input;
		
		try { 
			input = new BufferedReader(new FileReader(new File(inputFilename)));
			
			String line = "";
			String[] st = null;
			int i = 0;
			final int MAX_NUMBER = 15000;
			input.readLine();   //  first line is omitted since it contains fields of the data
			while ( ( line = input.readLine() ) != null && i < MAX_NUMBER ) {
				st = line.replace("\"","").split(",");
				System.out.println( st[0] + "   " + st[1] + "   " + st[2] + "   " + st[3] + "   " + st[4] + "   " +  st[5] );
				
				UserBehavior userBehavior = new UserBehaviorBuilder()
						.addUserId( (int) Integer.parseInt(st[0]) )
						.addItemId( (int) Integer.parseInt(st[1]) )
						.addBehaviorType( (int) Integer.parseInt(st[2]) )
						.addUserGeohash( st[3])
						.addItemCategory( (int) Integer.parseInt(st[4]) )
						.addTime(st[5]).get();
				
				if ( userBehavior.getUserId() == userId ) {
					++i;
					behavior.add(userBehavior);
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}     
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		}
		
		behavior.sort(UserBehavior.TimeComparator); //  sort chronologically, i.e by date as ascending
		return behavior;
	}
	
	private static void writeToCsv(List<String> behavior, int id) {
		final String currentDirectory = System.getProperty("user.dir");
		final String outputFilename = currentDirectory + "/cleaned_dataset1/behavior" + id + ".csv";
		Writer output;
		System.out.println(outputFilename);
		try { 
			output = new PrintWriter(new File(outputFilename));
			StringBuilder actions = new StringBuilder();
			for ( String b : behavior) {
//				actions.append(b + ',');
				actions.append(b);
				actions.append('\n');
			}
			System.out.println(actions.toString());
			output.write(actions.toString());
			output.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}     
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		}
	}
	
	public List<List<String>> getAllData() {
		List<List<String>> data = new ArrayList<List<String>>();
		for (int id = DAO.MIN_ID; id <= DAO.MAX_ID; ++id) {
			data.add(this.getActionsFromCSV(id));
		}	
		
		return data;
	}
	
	//  all of the following methods to get the data for training and detection stages
	public List<String> getTrainingData(int id) {
		 final int TRAINING_DATA_SIZE = 10000;
		 List<String> User = this.getActionsFromCSV(id);
		 List<String> trainingData = User.subList(0, TRAINING_DATA_SIZE);
		 
		 return trainingData;
	}
	
	public DetectionData getDetectionData(int id) {
		// should throw an exception 
		if (id < DAO.MIN_ID || DAO.MAX_ID > 5 )
    		 	System.out.println("Invalid user Id");
		 
		 List<String> User = this.getActionsFromCSV(id);
		 final int DETECTION_DATA_SIZE = 5000;
		 List<String> falsePositiveData = new ArrayList<String>();
		 falsePositiveData = User.subList( User.size() - DETECTION_DATA_SIZE, User.size() );
		 System.out.println("DetectionData: userId = " + id + ", User size = " +  User.size() );
		 
		 
		 List< List<String> > truePositiveData = new ArrayList< List<String> >();
		 for (int i = DAO.MIN_ID; i < id; ++i) {
			 List<String> Useri =  this.getActionsFromCSV(i);
			 truePositiveData.add( Useri.subList( Useri.size() - DETECTION_DATA_SIZE, Useri.size() ) );
		 }
		 for (int i = id + 1; i <= DAO.MAX_ID; ++i) {
			 List<String> Useri = this.getActionsFromCSV(i);
			truePositiveData.add( Useri.subList( Useri.size() - DETECTION_DATA_SIZE, Useri.size() ) );
		 }
		 
		 DetectionData data = new DetectionData(falsePositiveData, truePositiveData);
		 
		 return data;
	}

	
}
