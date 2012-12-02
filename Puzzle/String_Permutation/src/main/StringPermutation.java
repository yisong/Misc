package main;

/*
 * all character in string are unique
 */

public class StringPermutation {

	public static void stringPermutation(String s){
		stringPermutationHelper(new char[s.length()], s.toCharArray(), 0);
	}
	
	private static void stringPermutationHelper(char[] chosen, char[] left, int count){
		if (count == chosen.length){
			System.out.println(String.valueOf(chosen));
		} else {
			for(int i = count; i < left.length; i++){
				char c = left[i];
				chosen[count] = c;
				swap(left, count, i);
				stringPermutationHelper(chosen, left, count + 1);
				swap(left, i, count);
			}
		}
	}
	
	private static void swap(char[] a , int n, int m){
		char c = a[n];
		a[n] = a[m];
		a[m] = c;
	}
	
}
