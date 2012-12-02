package main;

import java.util.ArrayList;

public class SubString {

	public static ArrayList<Integer> isSubString(String s, String sub){
		SuffixTree t = new SuffixTree(s);
		return t.find(sub);
	}
}
