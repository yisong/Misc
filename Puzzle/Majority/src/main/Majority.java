package main;

/*
 * find the majority number in a sequence
 */

public class Majority {
	
	public static int findMajority(int[] a){
		return a[findMajorityHelper(a, 0, a.length)];
	}
	
	private static int findMajorityHelper(int[] a, int h, int t){
		if (t - h == 1){
			return h;
		}
		
		int mid = (h + t)/2;
		int leftMajority = findMajorityHelper(a, h, mid);
		if (leftMajority >= 0 && checkMajority(a, h, t, leftMajority)) {
			return leftMajority;
		}
		
		int rightMajority = findMajorityHelper(a, mid, t);
		if (rightMajority >= 0 && checkMajority(a, h, t, rightMajority)) {
			return rightMajority;
		}
		
		return -1;
		
	}
	
	private static boolean checkMajority(int[] a, int h, int t, int tar){
		int count = 0;
		for (int i = h; i < t; i++) {
			if (a[i] == a[tar]) {
				count ++;
			}
		}
		
		return count > (t-h)/2;
	}

}
