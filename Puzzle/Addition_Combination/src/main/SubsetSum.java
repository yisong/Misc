package main;

import test.SubsetSumTest;

/*
 * return true iff there exists a subset of the given array
 * whose sum is equal to the given number
 * 
 * if domain is positive integer
 * then it is equivalent to UnorderedNonduplicateAdditionCombination
 * 
 * here is the solution for all integers
 */

public class SubsetSum {

	public static int[] numbers = SubsetSumTest.numbers;
	public static int positive = 0;
	public static int negative = 0;
	public static boolean[][] result;
	
	public static boolean compute(int num){
		
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] < 0) {
				negative += numbers[i];
			} else {
				positive += numbers[i];
			}
		}
		
		int n = positive - negative + 1;
		int m = numbers.length;
		result = new boolean[n][m];
		
		for (int i = 0; i < m; i ++) {
			result[-1 * negative][i] = true;
		}
		
		for (int j = 0; j < m; j ++) {
			for (int i = negative; i <= positive; i ++) {
				result[i - negative][j] = getResult(i - numbers[j], j - 1) || getResult(i, j - 1);
			}
		}
		
		return result[num - negative][m - 1];
		
	}
	
	private static boolean getResult(int i, int j){
		return (i < negative || i > positive || j < 0) ? (i == 0) : result[i - negative][j];
	}
}
