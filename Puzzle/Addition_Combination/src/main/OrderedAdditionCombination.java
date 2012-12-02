package main;

import java.util.HashMap;
import java.util.Map;

import test.AdditionCombinationTest;


/*
 * return all combination whose sum is the given number 
 * assume all the numbers are positive
 * 
 * allow duplicate
 */

public class OrderedAdditionCombination {
	
	public static int[] numbers = AdditionCombinationTest.numbers;

	public static int compute(int num){
		int n = numbers.length;
		int[] result = new int[num + 1];
		result[0] = 1;
		for(int i = 1; i <= num; i++) {
			for(int j = 0; j < n; j++) {
				result[i] += getResults(i - numbers[j], result);
			}
		}
		return result[num];
	}
	
	public static int getResults(int index, int[] a){
		return index < 0? 0: a[index];
	}
	
	/*
	 * another implementation
	 */
	
	public static int compute2(int num){
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(0, 1);
		return orderedAdditionCombination2Helper(num, map);
	}
	
	private static int orderedAdditionCombination2Helper(int num, Map<Integer, Integer> map){
		if(map.containsKey(num)){
			return map.get(num);
		}else if(num < 0 ){
			return 0;
		}else {
			int result = 0;
			for(int i = 0; i < numbers.length; i++){
				result += orderedAdditionCombination2Helper(num - numbers[i], map);
			}
			
			map.put(num, result);
			return result;
		}
	}
}
