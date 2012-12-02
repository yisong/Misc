package main;

import test.AdditionCombinationTest;

/*
 * return all combination whose sum is the given number 
 * assume all the numbers are positive
 * 
 * allow duplicate
 */

public class UnorderedAdditionCombination {
	
	public static int[] numbers = AdditionCombinationTest.numbers;
	
	public static int compute(int num){
		int n = numbers.length;
		int[][] results = new int[num + 1][n];
		for (int i = 0; i < n; i++) {
			results[0][i] = 1;
		}
		
		for(int i = 1; i <= num; i++) {
			for(int j = 0; j <n; j++) {
				results[i][j] = getResults(i - numbers[j], j, results) + getResults(i, j - 1, results);
			}
		}
		return results[num][n-1];
		
	}
	
	private static int getResults(int i, int j, int[][] results){
		return (i < 0 || j < 0)? 0 : results[i][j];
	}
}
