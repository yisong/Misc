package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Silhouette {

	public static List<Pair> silhouette(Triple[] input){
		return reform(compute(input, 0, input.length));
	}
	
	private static List<Pair> compute(Triple[] a, int h, int t){
		int n = t - h;
		if (n == 0) {
			return Arrays.asList(new Pair[]{new Pair(0, 0)});
		} else if (n == 1) {
			return Arrays.asList(new Pair[]{new Pair(0, 0), new Pair(a[h].left, a[h].height), new Pair(a[h].right, 0)});
		}
		
		int mid = (t + h) / 2;
		List<Pair> leftArray = compute(a, h, mid);
		List<Pair> rightArray = compute(a, mid, t);
		return merge(leftArray, rightArray);
	}
	
	private static List<Pair> merge(List<Pair> a, List<Pair> b){
		List<Pair> result = new ArrayList<Pair>();
		result.add(new Pair(0, 0));
		int n = a.size();
		int m = b.size();
		int ha = 0;
		int hb = 0;
		int i = 1;
		int j = 1;
		
		while (i < n || j < m) {
			if (i < n && j < m && a.get(i).x == b.get(j).x) {
				ha = a.get(i).y;
				hb = b.get(j).y;
				result.add(new Pair(a.get(i).x, Math.max(ha, hb)));
				i ++;
				j ++;
			} else if (j == m || (i < n && a.get(i).x < b.get(j).x)) {
				ha = a.get(i).y;
				result.add(new Pair(a.get(i).x, Math.max(ha, hb)));
				i ++;
			} else {
				hb = b.get(j).y;
				result.add(new Pair(b.get(j).x, Math.max(ha, hb)));
				j ++;
			}
		}
		
		return result;
	}
	
	private static List<Pair> reform (List<Pair> r) {
		ArrayList<Pair> result = new ArrayList<Pair>();
		result.add(new Pair(0, 0));
		for (int i = 1; i < r.size(); i++) {
			if (r.get(i).y != r.get(i - 1).y) {
				result.add(new Pair(r.get(i).x, result.get(result.size() - 1).y));
				result.add(r.get(i));
			}
		}
		result.add(new Pair(Integer.MAX_VALUE, 0));
		return result;
	}
}
