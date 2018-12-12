package data;

import java.util.ArrayList;
import java.util.List;

public class ListString {

	public static String getString(List<String> list, int beginIndex, int endIndex) {
		StringBuilder s = new StringBuilder();
		for ( int i = beginIndex; i < endIndex; ++i ) {
			s.append( list.get(i) );
			
		}
		return s.toString();
	}
	
	public static List<String> getList(String s) {
		ArrayList<String> list = new ArrayList<String>();
		for ( int i = 0; i < s.length(); ++i ) {
			list.add( s.substring(i, i + 1) );
		}
		
		return list;
	}
	
//	uncomment for testing
//	public static void test() {
//		List<String> s = ListString.getList("123124521514324");
//		System.out.println(s);
//		
//		System.out.println( ListString.getString(s, 4, 7) );
//	}
//	
//	public static void main(String [] args) {
//		test();
//	}
	
}