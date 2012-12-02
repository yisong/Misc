package main;

import java.util.ArrayList;

public class LongestIncreasingSubsequence {

	/*
	 * O(n^2), there exists a O(n) algorithm
	 */
	public static ArrayList<Integer> longestIncreasingSubsequence(int[] a){
		ArrayList<Integer>[] solutions = (ArrayList<Integer>[])new ArrayList[a.length];
		for (int i = 0; i < a.length; i++) {
			longestIncreasingSubsequenceHelper(a[i], i - 1, solutions);
		}
		
		int maxIndex = -1;
		int max = 0;
		
		for (int i = 0; i < a.length; i ++){
			if (max < solutions[i].size() ){
				max = solutions[i].size();
				maxIndex = i;
			}
		}
		
		return solutions[maxIndex];
		
	}
	
	private static void longestIncreasingSubsequenceHelper(int n, int length, ArrayList<Integer>[] solutions){

		int maxIndex = -1;
		int max = 0;
		for (int i = 0; i <= length ; i++){
			ArrayList<Integer> a = solutions[i];
			if(a.get(a.size() - 1) <= n && a.size() > max){
				maxIndex = i;
				max = a.size();
			}
		}
		ArrayList<Integer> b;
		
		if (maxIndex > -1){
			b = (ArrayList<Integer>) solutions[maxIndex].clone();
		} else {
			b = new ArrayList<Integer>();
		}
		
		b.add(n);
		solutions[length + 1] = b;
	}
	
	
}
