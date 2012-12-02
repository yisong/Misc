package main;

import test.SubsetSumTest;

/*
 * special case of Subset Problem
 * return true iff there exists a subset of k elements
 * whose sum is equal to the given number
 */

public class KSubsetSum {

	public static int[] numbers = SubsetSumTest.numbers;
	public static int positive = 0;
	public static int negative = 0;
	public static boolean[][][] result;
	
	public static boolean compute(int num, int e){
		
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] < 0) {
				negative += numbers[i];
			} else {
				positive += numbers[i];
			}
		}
		
		int n = positive - negative + 1;
		int m = numbers.length;
		result = new boolean[n][m][e + 1];
		
		for (int i = 0; i < m; i ++) {
			result[-1 * negative][i][0] = true;
		}
		
		for (int j = 0; j < m; j ++) {
			for (int k = 0; k <= e; k++) {
				for (int i = negative; i <= positive; i ++) {
					result[i - negative][j][k] = getResult(i - numbers[j], j - 1, k - 1) || getResult(i, j - 1, k);
				}
			}
		}
		
		return result[num - negative][m - 1][e];
		
	}
	
	private static boolean getResult(int i, int j, int k){
		return (i < negative || i > positive || j < 0 || k < 0) ? (i == 0 && k == 0) : result[i - negative][j][k];
	}
}
